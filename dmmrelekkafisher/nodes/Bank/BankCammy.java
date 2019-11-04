package scripts.dmmrelekkafisher.nodes.Bank;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSNPC;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;

public class BankCammy extends Bank {
    @Override
    public void execute()
    {
        System.out.println("Retrieving items from the depo box...");
        int counter = 0;

        while (counter < 3)
        {
            if (getItems())
            {
                // Tele to cammy.
                System.out.println("BankCammy: Going to Relekka now.");
                break;
            }
            else
            {
                // Ran outta tabs or poons, log out.
                System.out.println("BankCammy: Didn't get items from depo box. Counter: " + counter);
                counter++;
            }
        }

        if (counter >= 3)
        {
            Vars.shouldExecute = false;
        }
    }

    boolean getItems()
    {
       if (super.getItems())
       {
            // DEPO BOX IS OPEN NOW.
            RSInterfaceMaster depoBoxInterface = Interfaces.get(230);
            RSInterfaceChild depoBoxChild = Interfaces.get(230, 5);
            boolean gotEquipment = false;

            for (int i = 0; i < 10; i++)
            {
                if (gotEquipment)
                {
                    System.out.println("getItems: Succesfully got the harp from depo box!");
                    // Close bank depo interface and return true.
                    depoBoxInterface.getChild(4).getChild(13).click();
                    return true;
                }
                else
                    {
                    // You do not have harp
                    if (getEquipment(depoBoxChild, i))
                    {
                        gotEquipment = true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean validate()
    {
        return Constants.CAMMY_BANK_AREA.contains(Player.getPosition());
    }
}
