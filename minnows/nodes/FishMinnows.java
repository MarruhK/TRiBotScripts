package scripts.minnows.nodes;

import org.tribot.api.General;
import org.tribot.api2007.*;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.minnows.data.Constants;
import scripts.minnows.data.Methods;
import scripts.minnows.data.Vars;
import scripts.minnows.framework.Node;
import scripts.minnows.utility.Antiban;

public class FishMinnows extends Node {

    // Fishing statistics; used in ABC2
    private long lastFishingWaitTime;
    private long averageFishingWaitTime = 12000;
    private long totalFishingWaitTime = 0;
    private long totalFishingInstances = 0;

    private RSNPC nextTarget = null;
    private RSTile nextTargetPosition = null;


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
        System.out.println("The FishMinnows Node has been Validated! Executing...");
        System.out.println("currentNPCID = " + Vars.currentNPCID);

        // Gets an array of valid fishing spots (fishing spots within the safe zone)

        if (!isValidNPCID()){
            RSNPC[] arrAllFishingSpots = (NPCs.findNearest(Constants.FISHING_SPOT_NPC_IDS[0],
                    Constants.FISHING_SPOT_NPC_IDS[1],
                    Constants.FISHING_SPOT_NPC_IDS[2],
                    Constants.FISHING_SPOT_NPC_IDS[3]));

            if (arrAllFishingSpots.length > 0){
                Vars.currentNPCID = arrAllFishingSpots[0].getID();
            } else {
                System.out.println("currentNPCID unable to initialize. Returning...");
                return;
            }
        }

        RSNPC[] arrFishingSpots = NPCs.findNearest(Vars.currentNPCID);

        // Not fishing and valid spot nearby.
        if (arrFishingSpots.length > 0){
            // Ensure nextTarget holds a valid value
            if (nextTarget == null){
                nextTarget = arrFishingSpots[0];
                System.out.print("nextTarget null. Initializing value, which is now tile: " + nextTarget.getPosition());
            }

            // Ensure menu isn't open for next entity, if it is, click it via options.
            if (ChooseOption.isOpen()){
                if (ChooseOption.isOptionValid(Constants.FISH_SPOT_NAME)){
                    ChooseOption.select(Constants.FISH_SPOT_NAME);
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
                Methods.clickNPC(nextTarget, Constants.FISHING_ANIMATION, Constants.FISH_SPOT_NAME);
            }

            // Prior to idle scripts.gengarlibrary.Antiban procedures
            nextTarget = Antiban.get().getNextTarget(arrFishingSpots);
            nextTargetPosition = nextTarget.getPosition();

            System.out.print("nextTarget tile has been initalized to: " + nextTarget.getPosition());
            Antiban.get().setHoverAndMenuBoolValues();
            Antiban.get().generateTrackers((int)averageFishingWaitTime);

            long startingFishTime = System.currentTimeMillis();

            if (isFishing()){
                System.out.println("Began fishing. Waiting and now doing antiban");
                while (isFishing()){
                    boolean temp = false;

                    General.sleep(200);
                    if (isLeapingFish()){
                        // Change fishing spots
                        nextTarget = getNewFishingSpot(nextTarget.getPosition());
                        System.out.println("Inside while - clickNPC call");
                        Methods.clickNPC(nextTarget, Constants.FISHING_ANIMATION, Constants.FISH_SPOT_NAME);
                    }
                    Antiban.get().timedActions();
                    Antiban.get().executeShouldHoverAndMenu(nextTarget);

                    if (temp){
                        break;
                    }
                }
                updateFishingStatistics(System.currentTimeMillis() - startingFishTime);
                Antiban.get().sleepReactionTime((int) averageFishingWaitTime);      // if too long, change sleep.
            }
        }
    }


    private RSNPC getNewFishingSpot(RSTile tile){

        // NPCS
        // ducks 2002 1839 1838, 7735 kylie minnow


        RSNPC[] newFishingSpots = NPCs.findNearest(Filters.NPCs.idNotEquals(2002, 1839, 1838, 7735, Vars.currentNPCID));

        if (newFishingSpots.length > 0){
            System.out.println("getNewFishingSpot: New currentNPCID assigned.");
            Vars.currentNPCID = newFishingSpots[0].getID();
            return newFishingSpots[0];
        }
        System.out.println("getNewFishingSpot: Could not find a new nextTarget, returning null.");
        Vars.currentNPCID = 0;
        return null;
    }

    private boolean isLeapingFish(){
        if (Interfaces.get(162, 46).getChild(0).getText().
                equals("A flying fish jumps up and eats some of your minnows!")){
            System.out.println("isLeapingFish: THE LEAPING FISH IS EATING YOUR MINNOWS!");
            return true;
        }
        return false;
    }

    private boolean isFishing(){
        return (Player.getAnimation() == Constants.FISHING_ANIMATION &&
                NPCs.findNearest(Filters.NPCs.tileEquals(nextTargetPosition)).length > 0);
    }

    private boolean isValidNPCID(){
        return (Vars.currentNPCID == Constants.FISHING_SPOT_NPC_IDS[0] ||
                Vars.currentNPCID == Constants.FISHING_SPOT_NPC_IDS[1] ||
                Vars.currentNPCID == Constants.FISHING_SPOT_NPC_IDS[2] ||
                Vars.currentNPCID == Constants.FISHING_SPOT_NPC_IDS[3]);
    }

    @Override
    public boolean validate() {

        return true;
    }
}
