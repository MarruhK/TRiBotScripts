package scripts.dmmrelekkafisher.nodes.Bank;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSNPC;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;

public class BankLumby extends Bank {
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BankLumby Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.LUMBY_BANK_TILE)){
            Methods.waitToStopMoving(5000, 5100);

            if (Player.getPosition().equals(Constants.LUMBY_BANK_TILE)){
                System.out.println("BankLumby: Retrieving items from the depo box...");
                int counter = 0;

                while (counter < 3){
                    if (getItems()){
                        // Tele to cammy.
                        System.out.println("BankLumby: Going to tele cammy now.");
                        Inventory.find(Constants.CAMMY_TELE_ID)[0].click();
                        General.sleep(6000);
                        break;
                    } else {
                        // Ran outta tabs or poons, log out.
                        System.out.println("BankLumby: Didn't get items from depo box. Counter: " + counter);
                        counter++;
                    }
                }

                if (counter >= 3){
                    Vars.shouldExecute = false;
                }
            }
        }
    }

    boolean getItems()
    {
        if (super.getItems())
        {
            // DEPO BOX IS OPEN NOW
            RSInterfaceMaster depoBoxInterface = Interfaces.get(230);
            RSInterfaceChild depoBoxChild = Interfaces.get(230, 5);
            boolean gotTab = false;
            boolean gotEquipment = false;

            for (int i = 0; i < 10; i++)
            {
                if (gotEquipment && gotTab)
                {
                    System.out.println("getItems: Succesfully got both the tab and harp from depo box!");
                    // Close bank depo interface and return true.
                    depoBoxInterface.getChild(4).getChild(13).click();
                    return true;
                }
                else if (gotEquipment)
                {
                    if (getCammyTab(depoBoxChild, i)) {
                        gotTab = true;
                    }
                }
                else if (gotTab)
                {
                    if (getEquipment(depoBoxChild, i))
                    {
                        gotEquipment = true;
                    }
                }
                else
                {
                    // you have nothing.
                    if (getCammyTab(depoBoxChild, i))
                    {
                        gotTab = true;
                    } else if (getEquipment(depoBoxChild, i)) {
                        gotEquipment = true;
                    }
                }
            }
        }
        return false;
    }

    private boolean getCammyTab(RSInterfaceChild depoBoxChild, int i)
    {
        if (depoBoxChild.getChild(i).getComponentName().equals("<col=ff981f>Camelot teleport"))
        {
            depoBoxChild.getChild(i).click();

            // Wait for it to show up in inventory.
            if (Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Inventory.find(Constants.CAMMY_TELE_ID).length > 0;
                }
            }, General.random(1200, 1300)))
            {
                System.out.print("getItems: Succesfully got the teletab out of depo box");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validate()
    {
        return Constants.LUMBY_BANK_FLOOR.contains(Player.getPosition());
    }
}
