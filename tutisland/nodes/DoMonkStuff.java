package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.DPathNavigator;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;

public class DoMonkStuff implements Node
{
    private final int ID_NPC_MONK = 3319;






    @Override
    public void execute()
    {
        System.out.println("___________________________________________________________________________________________");
        System.out.println("DoMonkStuff Node has been initialized!");

        if (APIMethods.getChatText().contains("<col=0000ff>PrayerPotion</col><br>")                               ||
            APIMethods.getChatText().contains("Talk with Brother Brace and he'll tell you about prayers.")  ||
            APIMethods.getChatText().contains("<col=0000ff>Ignore list</col><br>"))
        {
            System.out.println("DoMonkStuff: Talk to the monk");
            APIMethods.clickNPC(NPCs.findNearest(ID_NPC_MONK), "Talk-to",3300);

            if (Timing.waitCondition(APIMethods.conditionIsInChatDialogue(), 2700))
                APIMethods.continueChat();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Your PrayerPotion menu</col><br>"))
        {
            System.out.println("DoMonkStuff: Open prayer tab");
            GameTab.open(GameTab.TABS.PRAYERS);
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Friends list</col><br>"))
        {
            System.out.println("DoMonkStuff: Open friend tab");
            GameTab.open(GameTab.TABS.FRIENDS);
        }
        else if (APIMethods.getChatText().contains("This is your friends list."))
        {
            System.out.println("DoMonkStuff: Open ignore tab");
            GameTab.open(GameTab.TABS.IGNORE);
        }
        General.sleep(500);
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_DMS.contains(Player.getPosition()) &&
               !APIMethods.getChatText().contains("Your final instructor");
    }
}
