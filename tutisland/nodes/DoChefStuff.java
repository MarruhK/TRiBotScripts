package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;


import java.util.HashMap;

public class DoChefStuff implements Node
{
    private int ID_NPC_CHEF = 3305;
    private int ID_FLOUR = 2516;
    private int ID_BUCKET_WATER = 1929;
    private int ID_DOUGH = 2307;

    @Override
    public void execute()
    {
        System.out.println("___________________________________________________________________________________________");
        System.out.println("DoChefStuff Node has been initialized!");

        if (APIMethods.getChatText().contains("<col=0000ff>Find your next instructor</col><br>"))
        {
            System.out.println("DoChefStuff: Talk to chef.");
            talkToChef();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Making dough</col><br>"))
        {
            System.out.println("DoChefStuff: cook bread");
            makeDough();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Cooking dough</col><br>") &&
                cookDough())
        {
            System.out.println("DoChefStuff: Open music tab.");
            clickMusicTab();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>The music player</col><br>"))
        {
            System.out.println("DoChefStuff: Leaving building.");
            leaveBuilding();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Emotes</col><br>"))
        {
            System.out.println("DoChefStuff: Do random emote.");
            doEmote();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Running</col><br>"))
        {
            System.out.println("DoChefStuff: Open Run");
            setRun();
        }
        else
        {
            System.out.println("DoChefStuff: ERROR NO VALID IFS");
        }
        General.sleep(150, 250);
    }


    private boolean setRun()
    {
        GameTab.open(GameTab.TABS.OPTIONS);

        if (Timing.waitCondition(APIMethods.conditionIsGameTabOpen(GameTab.TABS.OPTIONS), 1500))
        {
            final int INTERFACE_MASTER = 261;
            final int INTERFACE_CHILD = 77;

            Interfaces.get(INTERFACE_MASTER, INTERFACE_CHILD).click();
        }
        return false;
    }

    private void doEmote()
    {
        GameTab.open(GameTab.TABS.EMOTES);

        if (Timing.waitCondition(APIMethods.conditionIsGameTabOpen(GameTab.TABS.EMOTES), General.random(1100, 1200)))
        {
            final int INTERFACE_MASTER = 216;
            final int INTERFACE_CHILD = 1;
            final int INTERFACE_COMPONENT = (int) Math.random() * 20;

            Interfaces.get(INTERFACE_MASTER, INTERFACE_CHILD).getChild(INTERFACE_COMPONENT).click();
        }
    }

    private void leaveBuilding()
    {
        // FIX TILE
        RSTile exitTile = new RSTile(3073, 3090, 0);

        DaxWalker.walkTo(exitTile);
    }

    private void clickMusicTab()
    {
        GameTab.open(GameTab.TABS.MUSIC);
    }

    private boolean talkToChef()
    {
        RSNPC[] chef = NPCs.find(ID_NPC_CHEF);

        if (chef.length == 0)
            return false;

        chef[0].click();

        if (Timing.waitCondition(APIMethods.conditionIsInChatDialogue(), 2100))
        {
            APIMethods.continueChat();
            return true;
        }

        return false;
    }

    private boolean makeDough()
    {
        if (APIMethods.useTwoItemsWithEachother(Inventory.find(ID_FLOUR), Inventory.find(ID_BUCKET_WATER)))
        {
            General.sleep(350);
            return true;
        }
        return false;
    }

    private boolean cookDough()
    {
        final int breadID = 2309;

        if (Inventory.find(breadID).length > 0)
            return true;

        final int rangeID = 9736;
        final int cookingAnimation = 896;
        RSItem[] dough = Inventory.find(ID_DOUGH);
        RSObject[] range = Objects.find(5, rangeID);

        if (dough.length == 0 || range.length == 0)
            return false;

        // Succesfully created the dough, use on the range.
        // was spam clicking don't spam click
        dough[0].click();
        APIMethods.clickObject(range, "Use Bread dough -> Range", cookingAnimation);

        return Timing.waitCondition(new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Inventory.find(breadID).length > 0;
            }
        }, 2600);
    }


    @Override
    public boolean validate()
    {
        return  Constants.AREA1_DCS.contains(Player.getPosition()) ||
                Constants.AREA2_DCS.contains(Player.getPosition()) ||
                Constants.AREA3_DCS.contains(Player.getPosition()) ||
                Constants.AREA4_DCS.contains(Player.getPosition()) ||
                Constants.AREA5_DCS.contains(Player.getPosition());
    }
}
