package scripts.softclaymaker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;

import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.softclaymaker.data.Constants;
import scripts.softclaymaker.data.Variables;
import scripts.softclaymaker.framework.Node;
import scripts.softclaymaker.utilities.Antiban;

public class MakeClays extends Node
{
    // ACB2 statistic data
    private long totalWaitTime;
    private long averageWaitTime = 13500;
    private long softClayInstances = 0;
    private long lastWaitTime;

    private void updateStatistics(long previousTime)
    {
        lastWaitTime = previousTime;
        totalWaitTime += lastWaitTime;
        softClayInstances++;
        averageWaitTime = totalWaitTime / softClayInstances;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The MakeClays node has been Validated! Executing...");

        RSItem[] waterJugs = Inventory.find(Constants.WATER_JUG_ID);
        RSItem[] hardClays = Inventory.find(Constants.HARD_CLAY_ID);

        // Jugs and/or clay are not in the inventory.
        if (waterJugs.length == 0 || hardClays.length == 0)
        {
            System.out.println("execute: No jugs/clays in the inventory, returning...");
            return;
        }

        if (!waterJugs[waterJugs.length-1].click() || !Timing.waitCondition(GBooleanSuppliers.isUptext("Use Jug of water ->"), General.random(800, 1000)))
        {
            System.out.println("execute: Failed to click Jug of water, returning...");
            return;
        }

        if (!hardClays[0].click() || !Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(270), General.random(1300, 1500)))
        {
            System.out.println("execute: Failed to open soft-clay interface (i.e. interface where u make clays)");
            return;
        }

        RSInterface selectAllInterface = Entities.find(InterfaceEntity::new)
                                            .actionEquals("All")
                                            .getFirstResult();

        // Is null if alredy selected. Assume selected if null
        if (selectAllInterface != null)
        {
            selectAllInterface.click();
        }

        RSInterface makeClayInterface = Entities.find(InterfaceEntity::new)
                                            .actionEquals("Make")
                                            .componentNameContains("Soft clay")
                                            .getFirstResult();

        // Is null if alredy selected. Assume selected if null
        if (makeClayInterface == null)
        {
            System.out.println("execute: ??? The make clay button is not visible, not sure what happen.");
            return;
        }

        makeClayInterface.click();

        // Prior to idle scripts.gengarlibrary.Antiban procedures
        Antiban.get().generateTrackers((int)averageWaitTime);
        long startingTime = System.currentTimeMillis();     // Needed for stats which are needed for scripts.gengarlibrary.Antiban

        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return isSofteningClay();
        }, General.random(1500,1600)))
        {
            System.out.println("Not making soft clay for some reason. Leaving nodes.");
            return;
        }

        while (Inventory.find(Constants.HARD_CLAY_ID).length > 0 && Inventory.find(Constants.WATER_JUG_ID).length > 0)
        {
            General.sleep(150);
            Antiban.get().timedActions();
        }

        updateStatistics(System.currentTimeMillis() - startingTime);
        Antiban.get().sleepReactionTime((int) averageWaitTime);
    }

    private boolean isSofteningClay()
    {
        if (Inventory.getCount(Constants.HARD_CLAY_ID) > 0 && Inventory.getCount(Constants.WATER_JUG_ID) > 0)
        {
            // Softening clay has no animation so to check if we are softening clay, check to see if very first item in
            // inventory turns to an empty jug within 1.5s.
            return Timing.waitCondition(()->
            {
                    General.sleep(100);
                    return Inventory.getAll()[0].getID() == Constants.EMPTY_JUG_ID;
            }, General.random(1500, 1700));
        }

        return false;
    }

    @Override
    public boolean validate()
    {
        // Make sure no soft clays are in inventory
        return Inventory.getCount(Constants.HARD_CLAY_ID) > 0 &&
               Inventory.getCount(Constants.WATER_JUG_ID) > 0;
    }
}
