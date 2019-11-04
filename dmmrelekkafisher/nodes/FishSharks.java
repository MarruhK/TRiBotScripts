package scripts.dmmrelekkafisher.nodes;

import org.tribot.api.General;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;
import scripts.dmmrelekkafisher.utility.Antiban;

public class FishSharks implements Node
{
    // Fishing statistics; used in ABC2
    private long lastFishingWaitTime;
    private long averageFishingWaitTime = 120000;
    private long totalFishingWaitTime = 0;
    private long totalFishingInstances = 0;

    private RSNPC nextTarget = null;

    private void updateFishingStatistics(long waitTime)
    {
        lastFishingWaitTime = waitTime;
        totalFishingWaitTime += lastFishingWaitTime;
        totalFishingInstances++;
        averageFishingWaitTime = totalFishingWaitTime / totalFishingInstances;

        General.println("Average Fishing Time is: " + averageFishingWaitTime);
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The FishSharks Node has been Validated! Executing...");

        RSNPC[] arrFishingSpots = NPCs.findNearest(Vars.fishingSpotNPCID);

        // Not fishing and valid spot nearby.
        if (arrFishingSpots.length > 0)
        {
            // Ensure nextTarget holds a valid value
            if (nextTarget == null)
            {
                nextTarget = arrFishingSpots[0];
                System.out.print("FishSharks: nextTarget null. Initializing value, which is now tile: " + nextTarget.getPosition());
            }

            // Ensure menu isn't open for next entity, if it is, click it via options.
            if (ChooseOption.isOpen())
            {
                if (ChooseOption.isOptionValid(Vars.fishingSpotName))
                {
                    ChooseOption.select(Vars.fishingSpotName);
                } else {
                    System.out.println("Canceling text");
                    ChooseOption.select("Cancel");
                    return;
                }
            }
            else
            {
                // If so, check if it's on screen
                if (!nextTarget.isOnScreen())
                {
                    Camera.turnToTile(nextTarget.getPosition());
                }
                Methods.clickNPC(nextTarget, Vars.fishingAnimation , Vars.fishingSpotName);
            }

            // Prior to idle scripts.gengarlibrary.Antiban procedures
            nextTarget = Antiban.get().getNextTarget(arrFishingSpots);

            System.out.print("FishSharks: nextTarget tile has been initalized to: " + nextTarget.getPosition());
            Antiban.get().setHoverAndMenuBoolValues();
            Antiban.get().generateTrackers((int)averageFishingWaitTime);

            long startingFishTime = System.currentTimeMillis();

            if (isFishing())
            {
                System.out.println("FishSharks: Began fishing. Waiting and now doing antiban");
                while (isFishing())
                {
                    General.sleep(200);

                    Antiban.get().timedActions();
                    Antiban.get().executeShouldHoverAndMenu(nextTarget);
                }
                updateFishingStatistics(System.currentTimeMillis() - startingFishTime);
                Antiban.get().sleepReactionTime((int) averageFishingWaitTime);      // if too long, change sleep.
            }
        }
    }

    private boolean isFishing(){
        return Player.getAnimation() == Vars.fishingAnimation;
    }

    @Override
    public boolean validate()
    {
        return  (Inventory.find(Vars.fishingEquipmentID).length > 0 &&
                 !Vars.isPkerDetected                               &&
                 !Inventory.isFull()                                &&
                 Player.getPosition().distanceTo(Vars.fishingTile) <= 4);
    }
}
