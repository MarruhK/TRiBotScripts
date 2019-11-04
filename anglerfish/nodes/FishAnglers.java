package scripts.anglerfish.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSNPC;

import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.anglerfish.data.Methods;
import scripts.anglerfish.data.Vars;
import scripts.anglerfish.framework.Node;
import scripts.anglerfish.utilities.Antiban;
import scripts.anglerfish.data.Constants;

import java.awt.*;


public class FishAnglers extends Node
{
    // ANIMATIONS
    private static final int ANIM_FISHING = 623;

    // Fishing statistics; used in ABC2
    private long averageFishingWaitTime = 60000;
    private long totalFishingWaitTime;
    private long totalFishingInstances;
    private RSNPC nextTarget = null;

    private void updateFishingStatistics(long waitTime)
    {
        totalFishingWaitTime += waitTime;
        averageFishingWaitTime = totalFishingWaitTime / ++totalFishingInstances;

        System.out.println("updateFishingStatistics: Average wait = " + averageFishingWaitTime);
    }

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The FishAnglers Node has been Validated! Executing...");

        if(isBaitSufficient()) {
            // Check if fishing spot near you.
            RSNPC[] fishingSpots = NPCs.findNearest(Constants.FISHING_NPC_ID);

            // Not fishing
            if (Player.getAnimation() != ANIM_FISHING) {
                // Ensure nextTarget holds a valid value, if not, assign it
                if (nextTarget == null) {
                    nextTarget = fishingSpots[0];
                }

                // Ensure menu isn't open for next entity, if it is, click it via options.
                if (ChooseOption.isOpen()) {
                    ChooseOption.select("Bait Fishing spot");
                } else {
                    // If not, check if it's on screen
                    if (!nextTarget.isOnScreen()) {
                        Camera.turnToTile(nextTarget.getPosition());
                    }
                    // Ensure uptext is fishing, if not right click it and fish.
                    nextTarget.click();
                }

                // Prior to idle scripts.gengarlibrary.Antiban procedures
                nextTarget = Antiban.get().getNextTarget(constructNextFishingSpots(fishingSpots));
                Antiban.get().setHoverAndMenuBoolValues();
                Antiban.get().generateTrackers((int) averageFishingWaitTime);

                if (!waitToStartAnimating()) {
                    System.out.println("Did not animate fishing (i.e. begin to fish).");
                    return;
                }

                long startingFishTime = System.currentTimeMillis();
                if (isFishing()) {
                    System.out.println("Began fishing. Waiting and now doing antiban");
                    while (isFishing()) {
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

    private boolean isBaitSufficient(){
        // Check if you have baits.
        if (Inventory.find(Constants.BAIT_ID).length == 0)
        {
            System.out.println("No more bait to fish. Logging out.");
            Vars.shouldExecute = false;
            return false;
        }
        return true;
    }

    private boolean waitToStartAnimating()
    {
        Methods.waitToStop(1500, 1700);

        // Wait for actual animation
        return Timing.waitCondition(() ->
        {
            General.sleep(100);
            return Player.getAnimation() == ANIM_FISHING;
        }, General.random(4200, 4560));
    }

    private RSNPC[] constructNextFishingSpots(RSNPC[] fishingSpots)
    {
        if (fishingSpots.length <= 1)
            return null;

        RSNPC[] newFishingSpots = new RSNPC[fishingSpots.length-1];

        for (int i = 1; i < fishingSpots.length; i++)
        {
            newFishingSpots[i-1] = fishingSpots[i];
        }

        return newFishingSpots;
    }

    private boolean isFishing()
    {
        return Player.getAnimation() == ANIM_FISHING;
    }

    @Override
    public boolean validate() {

        return (Vars.shouldExecute                                                      &&
                Inventory.find(Constants.BAIT_ID, Constants.FISHING_ROD_ID).length == 2 &&
                !Inventory.isFull()                                                     &&
                Player.getPosition().distanceTo(Constants.FISHING_TILE) <= 10);
    }
}
