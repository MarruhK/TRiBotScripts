package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class DepositCoffer extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("DepositCoffer initiated... Executing...");

        RSItem[] coins = Inventory.find(Constants.COINS_ID);
        RSObject coffer = Entities.find(ObjectEntity::new)
                            .nameContains("Dominic's coffer")
                            .getFirstResult();

        if (coffer != null && coins.length > 0 && GClicking.clickObject(coffer))
        {
            RSInterface cofferDialogue = Entities.find(InterfaceEntity::new)
                                            .actionEquals("Continue")
                                            .getFirstResult();

            if (cofferDialogue == null)
            {
                System.out.println("execute: cofferDialogue is null.");
                return;
            }

            if (cofferDialogue.click() && !Timing.waitCondition(()->
            {
                General.sleep(125, 200);
                return Interfaces.isInterfaceSubstantiated(cofferDialogue);
            }, General.random(1250, 1333)))
            {
                System.out.println("execute: cofferDialogue is not substantiated");
                return;
            }

            Timing.waitCondition(GBooleanSuppliers.isNpcOptionValid(), General.random(2150, 2500));
            NPCChat.selectOption("Deposit money.", true);

            Timing.waitCondition(GBooleanSuppliers.isNpcOptionNotValid(), General.random(2150, 2500));
            Keyboard.typeSend("" + coins[0].getStack());

            Timing.waitCondition(GBooleanSuppliers.isNpcOptionValid(), General.random(2150, 2500));
            NPCChat.selectOption("Cancel", true);

            Timing.waitCondition(GBooleanSuppliers.isNpcOptionNotValid(), General.random(2150, 2500));
            Vars.isCofferEmpty = false;
        }
    }

    @Override
    public boolean validate()
    {
        return Validators.shouldDepositCoffer();
    }
}
