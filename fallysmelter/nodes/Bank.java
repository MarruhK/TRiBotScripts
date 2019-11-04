package scripts.fallysmelter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import scripts.fallysmelter.data.Data;
import scripts.fallysmelter.data.Vars;
import scripts.fallysmelter.framework.Node;

public class Bank extends Node
{
    private static final RSTile TILE_BANK = new RSTile(2946, 3369, 0);

    @Override
    public void execute()
    {
        General.println("Bank ---------------------------------------------------------------");

        boolean isRingEquipped = Equipment.isEquipped(Data.ID_RING_FORGE);

        if (!isRingEquipped && isBankTrulyOpen())
        {
            // Deposit All if items in inventory
            if (Inventory.getAll().length > 0)
            {
                while (Inventory.getAll().length > 0)
                {
                    General.sleep(100);
                    Banking.depositAll();
                }
            }

            General.println("Bank: Withdrawing ring of forgining...");
            // Banking interface has opened. Get the ring of forging, if it exists.
            withdrawItems(1, Data.ID_RING_FORGE);

            // Ring has been withdrawn, cloose interface
            closeBank();

            // Wear ring
            RSItem[] rings = Inventory.find(Data.ID_RING_FORGE);

            if (rings.length > 0)
            {
                rings[0].click();
                General.sleep(1000);
            }
            else
            {
                General.println("Bank: Unable to find and equip ring.");
            }
        }

        if (Equipment.isEquipped(Data.ID_RING_FORGE) && isBankTrulyOpen())
        {
            // Deposit All if items in inventory
            if (Inventory.getAll().length > 0)
            {
                while (Inventory.getAll().length > 0)
                {
                    General.sleep(100);
                    Banking.depositAll();
                }
            }

            withdrawItems(28, Data.ID_IRON_ORE);
            closeBank();
        }
    }

    private boolean withdrawItems(int count, int id)
    {
        if(Banking.withdraw(count, id))
        {
            return true;
        }

        General.println("Bank: withdrawItems: Failed to withdraw specified item, double checking if any is in bank...");
        General.sleep(105, 250);

        if (Banking.find(id).length <= 0)
        {
            General.println("Bank: withdrawItems: Item does not exist!, END SCRIPT.");
            Vars.shouldExecute = false;
        }
        // item was not withdrawn
        return false;
    }

    private boolean isBankTrulyOpen()
    {
        if(Banking.isBankScreenOpen() || Banking.openBank())
        {
            // Wait for interface to open, return if it doesn't open.
            if (Timing.waitCondition(() ->
            {
                General.sleep(100);
                return Banking.isBankScreenOpen();
            }, 1500))
            {
                // Banking screen failed to open.
                General.println("Bank: isBankTrulyOpen: Banking interface opened.");
                return true;
            }
        }
        General.println("Bank: isBankTrulyOpen: Banking interface failed to open.");
        return false;
    }

    private void closeBank()
    {
        Banking.close();
        Timing.waitCondition(()->
        {
            General.sleep(100);
            return !Banking.isBankScreenOpen();
        }, 1500);
        General.sleep(1200);
    }

    @Override
    public boolean validate()
    {
        return Player.getPosition().distanceTo(TILE_BANK) <= 5 && Inventory.getCount(Data.ID_IRON_ORE) <= 0;
    }
}
