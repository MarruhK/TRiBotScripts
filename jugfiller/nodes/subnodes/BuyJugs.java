package scripts.jugfiller.nodes.subnodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;

import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.gengarlibrary.GClicking;
import scripts.jugfiller.data.Constants;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;
import scripts.jugfiller.framework.Validators;

public class BuyJugs extends Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BuyJugs subnode has been Validated! Executing...");

        // Trade shop keep or assistant and buy the shit
        RSNPC shopKeeper = Entities.find(NpcEntity::new)
                                .nameContains("Shop")
                                .actionsEquals("Trade")
                                .getFirstResult();

        if (shopKeeper != null)
        {
            do
            {
                General.sleep(350);
                GClicking.clickNpc(shopKeeper, "Trade");
            } while(!Interfaces.isInterfaceValid(300));
        }

        RSInterface emptyJugPacks = Entities.find(InterfaceEntity::new)
                                        .componentNameContains("Empty jug pack")
                                        .getFirstResult();

        if (emptyJugPacks == null || emptyJugPacks.getComponentStack() == 0)
        {
            System.out.println("execute: Cannot find empty jugs in shop.");
            closeInterface();
            return;
        }

        do
        {
            General.sleep(125);
            emptyJugPacks.click("Buy 5");
            waitForJugsToPurchase();
        } while (Inventory.find(Constants.PACKED_JUGS_ID).length <= 0);

        if (!closeInterface())
        {
            return;
        }

        // Unpack all the jugs you got.
        RSItem[] jugsToUnpack = Inventory.find(Constants.PACKED_JUGS_ID);

        if (jugsToUnpack.length <= 0)
        {
            System.out.println("execute: Unable to find jugs in inventory :(.");
            return;
        }

        for (RSItem jug : jugsToUnpack)
        {
            General.sleep(250, 370);
            jug.click();
        }

        Vars.isOutOfJugs = false;
    }

    private boolean closeInterface()
    {
        RSInterface closeInterface = Entities.find(InterfaceEntity::new)
                                        .actionEquals("Close")
                                        .getFirstResult();

        if (closeInterface == null)
        {
            System.out.println("closeInterface: Failed to close interface, perhaps gen store is not even open?");
            return false;
        }

        do
        {
            General.sleep(150);
            closeInterface.click();
        } while(Interfaces.isInterfaceValid(300));

        return true;
    }

    private boolean waitForJugsToPurchase()
    {
        return Timing.waitCondition(()->
        {
            General.sleep(150);
            return Inventory.find("Empty jug pack").length > 0;
        }, General.random(1350, 1500));
    }

    @Override
    public boolean validate()
    {
        return Vars.isOutOfJugs &&
               Validators.isInGeneralStore();
    }
}
