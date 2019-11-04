package scripts.gnomefisher.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import scripts.gnomefisher.data.Constants;
import scripts.gnomefisher.framework.Node;
import scripts.gnomefisher.utilities.Antiban;
import scripts.gnomefisher.data.Methods;

import java.util.ArrayList;

public class FlyFish extends Node{

    // Fishing statistics; used in ABC2
    private long lastFishingWaitTime;
    private long averageFishingWaitTime = 60000; // 45 secs
    private long totalFishingWaitTime = 0;
    private long totalFishingInstances = 0;

    private RSNPC nextTarget = null;


    private void updateFishingStatistics(long waitTime){
        lastFishingWaitTime = waitTime;
        totalFishingWaitTime += lastFishingWaitTime;
        totalFishingInstances++;
        averageFishingWaitTime = totalFishingWaitTime / totalFishingInstances;

        General.println("Average Fishing Time is: " + averageFishingWaitTime);
    }

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The FlyFish Node has been Validated! Executing...");

        // Gets an array of valid fishing spots (fishing spots within the safe zone)
        RSNPC[] arrFishingSpots = constructValidFishingSpots(NPCs.findNearest(Constants.FISHING_SPOT_ID));

        if(isFishingSpotNear(arrFishingSpots) && Player.getAnimation() == -1 && Methods.isFeathersSufficient()){
            // Ensure nextTarget holds a valid value
            if (nextTarget == null){
                nextTarget = arrFishingSpots[0];
                System.out.print("nextTarget null. Initializing value, which is now tile: " + nextTarget.getPosition());
            }

            // Ensure menu isn't open for next entity, if it is, click it via options.
            if (ChooseOption.isOpen()){
                if (ChooseOption.isOptionValid("Lure Rod Fishing spot")){
                    ChooseOption.select("Lure Rod Fishing spot");
                } else {
                    System.out.println("Canceling text");
                    ChooseOption.select("Cancel");
                    return;
                }
            } else {
                // If so, check if it's on screen
                if (!nextTarget.isOnScreen()){
                    Camera.turnToTile(nextTarget.getPosition());
                }
                if(isValidFishingSpotFound(nextTarget)){
                    nextTarget.hover();
                    System.out.print("Position of nextTarget we clicking is: " + nextTarget.getPosition());
                    Methods.safeClick("Lure Rod Fishing spot");
                } else {
                    System.out.print("not a valid fishing spot for some reason still?");
                    nextTarget = null;
                    return;
                }
            }

            // Ensures that there is a valid fishing spot near you, if not it walks to base position.
            if(isValidFishingSpotFound(nextTarget)){

                // Prior to idle scripts.gengarlibrary.Antiban procedures
                nextTarget = Antiban.get().getNextTarget(arrFishingSpots);
                System.out.print("nextTarget tile has been initalized to: " + nextTarget.getPosition());
                Antiban.get().setHoverAndMenuBoolValues();
                Antiban.get().generateTrackers((int)averageFishingWaitTime);

                if (!waitToStartAnimating()){
                    System.out.println("Did not animate fishing (i.e. begin to fish).");
                    return;
                }

                long startingFishTime = System.currentTimeMillis();
                if (isFishing()){
                    System.out.println("Began fishing. Waiting and now doing antiban");
                    while (isFishing()){
                        General.sleep(200);
                        Antiban.get().timedActions();
                        Antiban.get().executeShouldHoverAndMenu(nextTarget);
                    }
                    updateFishingStatistics(System.currentTimeMillis() - startingFishTime);
                    Antiban.get().sleepReactionTime((int) averageFishingWaitTime);
                }
            }
        }
        System.out.println("_________________________________________________________________________________________");
    }

    // Converts the given array into an array that is valid (i..e within the safe zone).
    private RSNPC[] constructValidFishingSpots (RSNPC[] initialFishingSpots){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        ArrayList<RSNPC> fishingSpots = new ArrayList<RSNPC>();

        // Ensure that the fishing spot is "valid" in the sense that it's in safe area.
        for (RSNPC fishingSpot: initialFishingSpots){
            System.out.println("Checking this spot to see if valid: " + fishingSpot.getPosition());
            for (RSTile tile: Constants.FISHING_TILES){
                // If tiles are identical, then it is a valid fishing spot.
                if (fishingSpot.getPosition().equals(tile)){
                    // Valid fishing spot, use this for future stuff.
                    fishingSpots.add(fishingSpot);
                }
            }
        }

        RSNPC[] arrFishingSpots = new RSNPC[fishingSpots.size()];

        System.out.println("=====================================");
        System.out.println("These spots are valid: ");
        for (int i = 0; i < fishingSpots.size(); i++){
            System.out.println(fishingSpots.get(i).getPosition());
            arrFishingSpots[i] = fishingSpots.get(i);
        }
        System.out.println("=====================================");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return arrFishingSpots;
    }

    // Check if you are near a valid fishing spot.
    private boolean isValidFishingSpotFound(RSNPC theFishingSpot) {
        // Didn't find a valid fish tile if false.
        for (RSTile tile: Constants.FISHING_TILES) {
            // If tiles are identical, then it is a valid fishing spot.
            if (theFishingSpot.getPosition().equals(tile)) {
                // Valid fishing spot, use this for future stuff.
                System.out.println("isValidFishingSpot:        Found a valid fishing spot, returning true");
                return true;
            }
        }
        System.out.println("isValidFishingSpot:        Failed to find a valid fishing spot, returning false");
        isFishingSpotNear(null);
        return false;
    }

    // Walks to a base position if you're not near a fishing spot.
    private boolean isFishingSpotNear(RSNPC[] fishingSpots){
        if (fishingSpots == null || fishingSpots.length == 0){
            System.out.println("isFishingSpotnear:        No fishing spot nearby. Walking to base position.");
            // Walk to base spot where there should be a bunch of fishing spots

            WebWalking.walkTo(Constants.BASE_FISHING_TILE);
            Methods.waitToStopMoving(1500, 1700);

            // Leave nodes. It will come back to this ndoe and re-do shit
            return false;
        }
        System.out.println("isFishingSpotnear:        Found a fishing spot, returning true");
        return true;
    }

    private boolean waitToStartAnimating(){
        Methods.waitToStopMoving(1500, 1700);

        // Wait for actual animation
        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Player.getAnimation() == Constants.FISHING_ANIM;
            }
        }, General.random(4200, 4560));
    }

    private boolean isFishing(){
        return Player.getAnimation() == Constants.FISHING_ANIM;
    }

    @Override
    public boolean validate() {
        return (!Inventory.isFull()                                                 &&
                Constants.BASE_FISHING_TILE.distanceTo(Player.getPosition()) <= 10  &&
                Player.getPosition().getPlane() == 0);
    }
}
