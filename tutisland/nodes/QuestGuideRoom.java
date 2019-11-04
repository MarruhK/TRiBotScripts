package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;

public class QuestGuideRoom implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("QuestGuideRoom nodes has been initiated! Executing...");

        if (APIMethods.getChatText().contains("Talk with the Quest Guide") ||
            APIMethods.getChatText().contains("Your Quest Journal"))
        {
            System.out.println("QuestGuideRoom: Talk to the Quest Guide");
            talkToQuestGuide();
        }
        else if (APIMethods.getChatText().contains("Open the Quest Journal"))
        {
            System.out.println("QuestGuideRoom: Open quest tab");
            GameTab.open(GameTab.TABS.QUESTS);
        }
        else
        {
            System.out.println("QuestGuideRoom: ERROR");
        }
        General.sleep(150);
    }

    private void talkToQuestGuide()
    {
        final int questGuideID = 3312;

        APIMethods.clickNPC(NPCs.findNearest(questGuideID), "Talk-to", 3200);
        APIMethods.continueChat();
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA1_QGR.contains(Player.getPosition()) || Constants.AREA2_QGR.contains(Player.getPosition());
    }
}
