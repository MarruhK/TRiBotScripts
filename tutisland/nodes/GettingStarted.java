package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;

/**
 * The very first class that is ran, aside from DesignCharacter. Converses with Runescape Guide and leaves the room.
 */
public class GettingStarted implements Node
{
    private static final int NPC_RUNESCAPE_GUIDE = 3308;
    private static final int ID_DOOR = 9398;

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("GettingStarted nodes has been initiated! Executing...");

        if (APIMethods.getChatText().contains("Getting started"))
        {
            System.out.println("GettingStarted: Speak to Runescape Guide for first time.");
            doBeforeSettingsSelect();
        }
        else if (APIMethods.getChatText().contains("Player controls"))
        {
            if (APIMethods.getChatText().contains("Please click on the flashing spanner icon"))
            {
                System.out.println("GettingStarted: Open Settings Tab.");
                selectSetting();
            }
            else
            {
                System.out.println("GettingStarted: Just opened settings, talking to Runescape Guide again.");
                doAfterSettingsSelect();
            }
        }
        General.sleep(200, 350);
    }

    private static void doBeforeSettingsSelect()
    {
        if (clickRunescapeGuide())
        {
            continueChat();

            // Shouls have the option now regarding what u want to do
            if (Timing.waitCondition(new Condition()
                                      {
                                          @Override
                                          public boolean active()
                                          {
                                              General.sleep(100);
                                              return NPCChat.getOptions() != null;
                                          }
                                      }, 2500))
            {
                selectRandomOption();
                continueChat();
            }
        }
    }

    private static void selectSetting()
    {
        GameTab.open(GameTab.TABS.OPTIONS);
    }

    private static void doAfterSettingsSelect()
    {
        if (clickRunescapeGuide())
            continueChat();
    }

    private static boolean clickRunescapeGuide()
    {
        // Click runescape guide
        RSNPC[] runescapeGuides = NPCs.find(NPC_RUNESCAPE_GUIDE);

        if (runescapeGuides.length == 0)
            return false;

        RSNPC runescapeGuide = runescapeGuides[0];
        runescapeGuide.click(); // fix later to allow for mislcikcs.

        return !Timing.waitCondition(isClickToContinueValid(), 1300);
    }

    private static void continueChat()
    {
        do
        {
            NPCChat.clickContinue(true);
            General.sleep(300, 450);
        }
        while (NPCChat.getClickContinueInterface() != null);
        General.sleep(300, 350);
    }

    private static boolean selectRandomOption()
    {
        int optionToSelect = (int) Math.random() * 3;
        General.sleep(350, 600);

        return NPCChat.selectOption(NPCChat.getOptions()[optionToSelect], true);
    }

    // Conditions_______________________________________________________________________________________________________
    private static Condition isClickToContinueValid()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return NPCChat.getSelectOptionInterface() != null;
            }
        };
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_DCGS.contains(Player.getPosition()) && !APIMethods.isDesignInterfaceOpen();
    }
}
