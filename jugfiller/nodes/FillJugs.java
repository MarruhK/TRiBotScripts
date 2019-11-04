package scripts.jugfiller.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.jugfiller.data.Constants;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;

public class FillJugs extends Node
{
    // Filling statistics; used in ABC2
    private long averageFillingWaitTime = 18000;
    private long totalFillingWaitTime;
    private long totalFillingInstances;

    private void updateFillingStatistics(long waitTime)
    {
        totalFillingWaitTime += waitTime;
        averageFillingWaitTime = totalFillingWaitTime / ++totalFillingInstances;

        System.out.println("updateFillingStatistics: Average wait = " + averageFillingWaitTime);
    }
    
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The FillJugs Node has been Validated! Executing...");

        // Player is near the pump, begin filling jugs with water.
        long startTime = Timing.currentTimeMillis();

        // No need to null check since already done in validate
        int initialEmptyJugs = Inventory.find(Constants.EMPTY_JUG_ID).length;

        if (isFillingCommenced())
        {
            Antiban.get().generateTrackers((int)averageFillingWaitTime);

            while (isFilling())
            {
                General.sleep(250, 300);

                // Paint jug stats algo
                int tempEmptyJugs = Inventory.find(Constants.EMPTY_JUG_ID).length;

                if (tempEmptyJugs < initialEmptyJugs)
                {
                    Vars.jugsFilled = Vars.jugsFilled + (initialEmptyJugs - tempEmptyJugs);
                    initialEmptyJugs = tempEmptyJugs;
                }

                Antiban.get().timedActions();
            }

            updateFillingStatistics(Timing.currentTimeMillis() - startTime);
            Antiban.get().sleepReactionTime((int)averageFillingWaitTime);
        }
    }

    // Uses jugs on the waterpump. Return value based on if it is done correctly.
    private boolean isFillingCommenced()
    {
        RSObject pump = Entities.find(ObjectEntity::new)
                            .nameEquals("Waterpump")
                            .getFirstResult();

        if (pump == null)
        {
            System.out.println("isFillingCommenced: Unable to find water pump.");
            return false;
        }

        // Pump exists and is on the screen. Click an empty jug and then click the pump.
        if (!Game.isUptext("Use Jug ->"))
        {
            Inventory.find(Constants.EMPTY_JUG_ID)[0].click();
        }

        if (!GClicking.clickObject(pump, "Use Jug -> Waterpump", Constants.WATER_FILLING_ANIM_ID))
        {
            System.out.println("isFillingCommenced: Unable to use the jug on the water pump.");
            return false;
        }

        return true;
    }

    private boolean isFilling()
    {
        return Timing.waitCondition(GBooleanSuppliers.waitForAnimation(Constants.WATER_FILLING_ANIM_ID), General.random(600, 700));
    }

    @Override
    public boolean validate()
    {
        return !Vars.isOutOfCoins                                      &&
                Player.getPosition().distanceTo(Constants.PUMP_TILE) <= 5   &&
                Inventory.getCount(Constants.EMPTY_JUG_ID) > 0;
    }
}
