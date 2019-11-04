package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;

public class DoWizardStuff implements Node
{
    private final int ID_NPC_WIZARD = 3309;

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("DoWizardStuff nodes has been initiated! Executing...");

        if (APIMethods.getChatText().contains("<col=0000ff>Your final instructor!</col><br>") ||
            APIMethods.getChatText().contains("This is your spells list."))
        {
            APIMethods.clickNPC(NPCs.find(ID_NPC_WIZARD), "Talk-to", 2500);

            if (Timing.waitCondition(APIMethods.conditionIsInChatDialogue(), 2500))
                APIMethods.continueChat();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Open up your final menu</col><br>"))
        {
            GameTab.open(GameTab.TABS.MAGIC);
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Cast Wind Strike at a chicken.</col><br>") &&
            GameTab.open(GameTab.TABS.MAGIC))
        {
            strikeChicken();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>You have almost completed the tutorial!</col><br>"))
        {
            leaveIsland();
        }
    }

    private void leaveIsland()
    {
        APIMethods.clickNPC(NPCs.find(ID_NPC_WIZARD), "Talk-to", 2500);

        while (NPCChat.getMessage() != null || NPCChat.getOptions() != null)
        {
            if (NPCChat.getOptions() == null)
            {
                APIMethods.continueChat();
            } else if (NPCChat.getOptions().length == 2)
            {
                NPCChat.selectOption(NPCChat.getOptions()[0], true);
            } else
            {
                NPCChat.selectOption(NPCChat.getOptions()[1], true);
            }
            General.sleep(250);
        }
    }

    private void strikeChicken()
    {
        if (!Game.isUptext("Cast Wind Strike ->"))
            Magic.selectSpell("Wind Strike");

        final int chickenID = 3316;

        RSNPC[] chickens = NPCs.findNearest(chickenID);

        if (chickens.length == 0)
            return;

        final int spellAnimation = 711;

        APIMethods.clickNPCConsiseUptext(chickens, "Cast Wind Strike -> Chicken  (level-3)", 1700, spellAnimation);
    }

    @Override
    public boolean validate()
    {
        return  Constants.AREA1_DWS.contains(Player.getPosition()) ||
                Constants.AREA2_DWS.contains(Player.getPosition()) ||
                Constants.AREA3_DWS.contains(Player.getPosition()) ||
                Constants.AREA4_DWS.contains(Player.getPosition()) ||
                Constants.AREA5_DWS.contains(Player.getPosition());
    }
}
