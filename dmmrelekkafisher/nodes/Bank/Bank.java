package scripts.dmmrelekkafisher.nodes.Bank;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSNPC;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;

public abstract class Bank implements Node
{
    // Opens the deposit box
    boolean getItems()
    {
        RSNPC[] wizard = NPCs.find(Vars.depoBoxNPCID);

        if (wizard.length > 0)
        {
            wizard[0].hover();
            Methods.safeClick("Deposit-box Financial wizard");

            General.sleep(4000);

            RSInterfaceMaster depoBoxInterface = Interfaces.get(230);
            if (depoBoxInterface != null)
            {
                System.out.println("Bank: Opened deposit box. Returning...");
                return true;
            }
        }
        return false;
    }

    boolean getEquipment (RSInterfaceChild depoBoxChild, int i)
    {
        if (depoBoxChild.getChild(i).getComponentName().equals("<col=ff981f>" + Vars.equipmentName))
        {
            depoBoxChild.getChild(i).click();

            // Wait for it to show up in inventory.
            if (Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Inventory.find(Vars.fishingEquipmentID).length > 0;
                }
            }, General.random(1200, 1300)))
            {
                System.out.print("getEquipment: Succesfully got the fishing equipment out of depo box");
                return true;
            }
        }
        return false;
    }
}
