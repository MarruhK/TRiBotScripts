package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

import java.util.Random;


public class StartDreamDominic extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("StartDreamDominic initiated... Executing...");

        RSNPC dominic = Entities.find(NpcEntity::new)
                            .nameEquals("Dominic Onion")
                            .getFirstResult();

        // Null check + Fail to start dream
        if (dominic == null || !GClicking.clickNpc(dominic, "Dream") || !selectCustomizableRumbleHard())
        {
            System.out.println("StartDreamDominic: Error when attempting to start a dream via dominic");
        }
    }

    private boolean selectCustomizableRumbleHard()
    {
        // Wait for chat interface to come and see if u can select the shit
        if (Timing.waitCondition(GBooleanSuppliers.isNpcOptionValid(), 3000))
        {
            if (NPCChat.selectOption("Previous: Customisable Rumble (hard)", true))
            {
                return initializeCustomDream();
            }
            else if (NPCChat.selectOption("Rumble", true))
            {
                return NPCChat.selectOption("Customisable - hard", true) && initializeCustomDream();
            }

            System.out.println("selectCustomizableRumbleHard: Unable to find NPC select option");
        }

        System.out.println("selectCustomizableRumbleHard: Failed to open NPC option dialogue.");
        return false;
    }

    private boolean continueChat()
    {
        do
        {
            NPCChat.clickContinue(true);
            General.sleep(300, 450);
        }
        while (NPCChat.getClickContinueInterface() != null);

        return Timing.waitCondition(GBooleanSuppliers.isNpcContinueNull(), General.random(300, 500));
    }

    private boolean initializeCustomDream()
    {
        return continueChat() && NPCChat.selectOption("Yes", true) && continueChat();
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldStartDreamDominic();
    }
}
