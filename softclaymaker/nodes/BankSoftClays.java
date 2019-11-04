package scripts.softclaymaker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.softclaymaker.data.Constants;
import scripts.softclaymaker.data.Variables;
import scripts.softclaymaker.framework.Node;

public class BankSoftClays extends Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BankSoftClays node has been Validated! Executing...");

        // Soft clays are made deposit everything
        Banking.openBank();
        General.sleep(250, 650);
        Banking.depositAll();
        General.sleep(250, 650);

        // Check if any water jugs or clays are left.
        if (Banking.find(Constants.WATER_JUG_ID).length == 0 || Banking.find(Constants.HARD_CLAY_ID).length == 0){
            // Out of water jugs or clay
            System.out.println("execute: Ran out of jugs or water.");
            Variables.shouldExecute = false;
            return;
        }

        Banking.withdraw(14, Constants.WATER_JUG_ID);
        Timing.waitCondition(GBooleanSuppliers.isItemWithdrawn(Constants.WATER_JUG_ID), General.random(750, 900));

        Banking.withdraw(14, Constants.HARD_CLAY_ID);
        Timing.waitCondition(GBooleanSuppliers.isItemWithdrawn(Constants.WATER_JUG_ID), General.random(750, 900));

        Banking.close();
    }

    @Override
    public boolean validate()
    {
        return Inventory.getCount(Constants.HARD_CLAY_ID) == 0 ||
               Inventory.getCount(Constants.WATER_JUG_ID) == 0;
    }
}
