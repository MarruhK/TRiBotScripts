package scripts.cooker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.cooker.data.Vars;
import scripts.cooker.framework.Node;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.softclaymaker.data.Constants;
import scripts.softclaymaker.utilities.Antiban;

public class Cook implements Node
{
    // ACB2 statistic data
    private long totalWaitTime;
    private long averageWaitTime = 61500;
    private long cookingInstances = 0;

    private void updateStatistics(long previousTime)
    {
        totalWaitTime += previousTime;
        cookingInstances++;
        averageWaitTime = totalWaitTime / cookingInstances;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The Cook Node has been Validated! Executing...");

        RSObject range = Entities.find(ObjectEntity::new)
                            .nameEquals("Range")
                            .sortByDistance()
                            .getFirstResult();

        if (range == null)
        {
            System.out.println("execute: Failed to find the range...");
            return;
        }

        // CLick food in inventory and wait for uptext
        RSItem[] rawFoods = Inventory.find(Vars.rawFoodId);

        if (rawFoods.length == 0)
        {
            System.out.println("execute: Failed to find raw foods...");
            return;
        }

        final String rawFoodName = rawFoods[0].getDefinition().getName();

        // CLick the range via click object method and ensure proper uptext, no animation yet.
        GClicking.clickObject(range, "Cook Range");

        if (!Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(270), General.random(1300, 1500)))
        {
            System.out.println("execute: Failed to open cooking interface");
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

        RSInterface cookingInterface = Entities.find(InterfaceEntity::new)
                                            .actionEquals("Cook")
                                            .componentNameContains(rawFoodName)
                                            .getFirstResult();

        // Is null if alredy selected. Assume selected if null
        if (cookingInterface == null)
        {
            System.out.println("execute: ??? The cooking Interface is not visible, not sure what happen.");
            return;
        }

        cookingInterface.click();

        // Prior to idle scripts.gengarlibrary.Antiban procedures
        Antiban.get().generateTrackers((int) averageWaitTime);
        long startingTime = System.currentTimeMillis();     // Needed for stats which are needed for scripts.gengarlibrary.Antiban

        if (!Timing.waitCondition(() ->
        {
            General.sleep(100);
            return Inventory.find(Vars.rawFoodId).length <= 0;
        }, General.random(69000, 73000)))
        {
            System.out.println("Not cooking for some reason. Leaving nodes.");
            return;
        }

        // TODO have a better check to ensure you are cooking
        while (Inventory.find(Vars.rawFoodId).length > 0)
        {
            General.sleep(150);
            Antiban.get().timedActions();
        }

        updateStatistics(System.currentTimeMillis() - startingTime);
        Antiban.get().sleepReactionTime((int) averageWaitTime);
    }

    @Override
    public boolean validate()
    {
        // Near the range and inventory has raw food items
        return Inventory.find(Vars.rawFoodId).length > 0 && Vars.rangeArea.contains(Player.getPosition());
    }
}
