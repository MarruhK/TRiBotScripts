package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import org.tribot.api2007.util.DPathNavigator;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;

public class DoBanking implements Node
{
    private final int INTERFACE_MASTER_BANK = 12;
    private final int INTERFACE_CHILD_BANK = 14;
    private final int INTERFACE_COMPONENT_BANK = 11;

    private final int INTERFACE_MASTER_POLL = 310;
    private final int INTERFACE_CHILD_POLL = 2;
    private final int INTERFACE_COMPONENT_POLL = 11;

    @Override
    public void execute()
    {
        System.out.println("___________________________________________________________________________________________");
        System.out.println("DoBanking Node has been initialized!");

        if (APIMethods.getChatText().contains("<col=0000ff>Banking</col><br>")  &&
            bank())
        {
            System.out.println("DoBanking: Talk to bankers");
            APIMethods.continueChat();
            talkToBanker();
        }
        else if (APIMethods.getChatText().contains("This is your bank box.") &&
                 closeInterface(INTERFACE_MASTER_BANK, INTERFACE_CHILD_BANK, INTERFACE_COMPONENT_BANK))
        {
            System.out.println("DoBanking: Click poll booth.");
            openPollBooth();
        }
        else if (APIMethods.getChatText().contains("This is a poll booth.<br>Polls are run")             &&
                 closeInterface(INTERFACE_MASTER_POLL, INTERFACE_CHILD_POLL, INTERFACE_COMPONENT_POLL))
        {
            System.out.println("DoBanking: Go to financial advisor.");
            enterFinancialAdvisorRoom(new RSTile(3126, 3124, 0));
        }
        else if (APIMethods.getChatText().contains("Financial advice"))
        {
            System.out.println("DoBanking: Speak to financial advisor.");
            speakToFinancialAdvisor();
        }
        else
        {
            System.out.println("DoBanking: ERROR");
        }
    }

    private void speakToFinancialAdvisor()
    {
        final int advisorID = 3310;

        RSNPC[] advisor = NPCs.findNearest(advisorID);

        if (advisor.length == 0)
            return;

        APIMethods.clickNPC(advisor, "Talk-to", 1200);

        if (Timing.waitCondition(APIMethods.conditionIsInChatDialogue(), 2500))
            APIMethods.continueChat();
    }

    private void enterFinancialAdvisorRoom(RSTile tile)
    {
        DaxWalker.walkTo(tile);
    }

    private void openPollBooth()
    {
        final int pollBoothID = 26815;

        APIMethods.clickObject(Objects.find(5, pollBoothID), "Use Poll booth",-1);

        final int continueChatDialogue = 229;

        if (Timing.waitCondition(APIMethods.conditionIsInterfaceValid(continueChatDialogue), 2700))
            APIMethods.continueChat();
    }

    private boolean closeInterface(int master, int child, int component)
    {
        RSInterfaceMaster bankInterface = Interfaces.get(master);

        if (bankInterface != null)
        {
            bankInterface.getChild(child).getChild(component).click();
        }
        return Timing.waitCondition(APIMethods.conditionIsInterfaceNotValid(master), 1600);
    }

    private void talkToBanker()
    {
        if (NPCChat.getOptions() == null)
        {
            System.out.println("DoBanking: Unable to get to the experience level chat option.");
            return;
        }

        NPCChat.selectOption(NPCChat.getOptions()[0], true);
    }

    private boolean bank()
    {
        final int bankID = 10083;

        RSObject[] bankers = Objects.find(3, bankID);

        if (bankers.length == 0)
            return false;

        bankers[0].click();

        return Timing.waitCondition(APIMethods.conditionIsInChatDialogue(), 1200);
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA1_DB.contains(Player.getPosition()) || Constants.AREA2_DB.contains(Player.getPosition());
    }
}
