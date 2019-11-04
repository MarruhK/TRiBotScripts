package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;


public class CombatTrainingRoom implements Node
{
    private RSTile TILE_INSTRUCTOR = new RSTile(3106, 9508, 0);

    private final int ID_NPC_INSTRUCTOR = 3307;
    private final int ID_NPC_RAT = 3313;

    private final int ID_BRONZE_DAGGER = 1205;
    private final int ID_SHORTBOW = 841;
    private final int ID_ARROWS = 882;
    private final int ID_GATE = 9720;

    private final int INTERFACE_MASTER_WORN = 387;
    private final int INTERFACE_CHILD_WORN_OPEN = 18;
    private final int INTERFACE_CHILD_WORN_CLOSE = 4;



    @Override
    public void execute()
    {
        System.out.println("___________________________________________________________________________________________");
        System.out.println("CombatTrainingRoom Node has been initialized!");

        if (APIMethods.getChatText().contains("<col=0000ff>combat</col><br>")                               ||
           (APIMethods.getChatText().contains("You're now holding your dagger.") && closeWornInterface())   ||
           (APIMethods.getChatText().contains("<col=0000ff>Well done, you've made your first kill!</col><br>")))
        {
            if (exitRatArea())
            {
                System.out.println("CombatTrainingRoom: Talk to instructor");
                APIMethods.clickNPCConsiseUptext(NPCs.find(ID_NPC_INSTRUCTOR), "Talk-to combat Instructor  (level-146)", 6000, -1);
                APIMethods.continueChat();
            }
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Wielding weapons</col><br>"))
        {
            System.out.println("CombatTrainingRoom: open equipment tab");
            openEquipmentTab();
        }
        else if (APIMethods.getChatText().contains("Left click your dagger to ") ||
                 APIMethods.getChatText().contains("This is your worn inventory"))
        {
            System.out.println("CombatTrainingRoom: Wield dagger");
            wieldDagger();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Unequipping items</col><br>") &&
                 closeWornInterface())
        {
            System.out.println("CombatTrainingRoom: Wield sword");
            wieldSwordAndShield();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>combat interface</col><br>"))
        {
            System.out.println("CombatTrainingRoom: Open combat tab");
            GameTab.open(GameTab.TABS.COMBAT);
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Attacking</col><br>") ||
                 APIMethods.getChatText().contains("This is your combat interface."))
        {
            if (!isInRatArea())
            {
                System.out.println("CombatTrainingRoom: Go inside rat area");
                goInRatArea();
            }

            System.out.println("CombatTrainingRoom: Kill rat");
            killRat();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Well done, you've made your first kill!</col><br>") &&
            isInRatArea())
        {
            System.out.println("CombatTrainingRoom: Leave rat pit");
            exitRatArea();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Rat ranging</col><br>") &&
                 equipBowAndArrows())
        {
            System.out.println("CombatTrainingRoom: Range rat");
            rangeRat();
        }
        else
        {
            System.out.println("CombatTrainingRoom: ERROR");
        }
        General.sleep(200);
    }



    private boolean closeWornInterface()
    {
        int master = 84;
        int child = 4;

        RSInterfaceMaster wornInterface = Interfaces.get(master);

        if (wornInterface != null)
            wornInterface.getChild(child).click();

        return Timing.waitCondition(APIMethods.conditionIsInterfaceNotValid(master), 1200);
    }

    private void rangeRat()
    {
        RSNPC[] rats = NPCs.findNearest(ID_NPC_RAT);

        if (rats.length == 0)
            return;

        final int rangeAnimation = 426;

        for (RSNPC rat : rats)
        {
            if (!rat.isOnScreen())
                Camera.turnToTile(rat.getPosition());

            if (rat.isOnScreen())
            {
                if (APIMethods.clickNPCConsiseUptext(rats, "Attack Giant rat  (level-3)", 1700, rangeAnimation))
                {
                    System.out.println("CombatTrainingRoom: Successfully began to attack giant rat.");
                    if (Timing.waitCondition(APIMethods.conditionIsChatTextEqualTo("<col=0000ff>Moving on</col><br>"), 22000))
                    {
                        System.out.println("CombatTrainingRoom: Successfully killed giant rat.");
                        return;
                    }
                }
            }
        }
    }

    private boolean equipBowAndArrows()
    {
        RSItem[] bow = Inventory.find(ID_SHORTBOW);
        RSItem[] arrow = Inventory.find(ID_ARROWS);

        if (Inventory.find(ID_SHORTBOW).length > 0)
            bow[0].click();

        if (Inventory.find(ID_ARROWS).length > 0)
            arrow[0].click();

        return (Inventory.find(ID_SHORTBOW).length == 0 && Inventory.find(ID_ARROWS).length == 0);
    }

    private boolean isInRatArea()
    {
        return (Constants.A1_CTR_RAT_PIT.contains(Player.getPosition())   ||
                Constants.A2_CTR_RAT_PIT.contains(Player.getPosition())   ||
                Constants.A3_CTR_RAT_PIT.contains(Player.getPosition()));
    }

    private boolean exitRatArea()
    {
        System.out.println("CombatTrainingRoom -- exitRatArea: Leaving rat area.");

        if (!isInRatArea())
            return true;

        return DaxWalker.walkTo(TILE_INSTRUCTOR);
    }

    private boolean goInRatArea()
    {
        System.out.println("CombatTrainingRoom -- exitRatArea: Entering rat area.");

        if (isInRatArea())
            return true;

        return DaxWalker.walkTo(Constants.A3_CTR_RAT_PIT.getRandomTile());
    }

    private void killRat()
    {
        RSNPC[] rats = NPCs.findNearest(8, ID_NPC_RAT);

        if (rats.length == 0)
            return;

        rats[0].click();

        // wait for combat to finish
        Timing.waitCondition(APIMethods.conditionIsChatTextEqualTo("Well done, you've made your first kill!"), 17000);
    }

    private void wieldSwordAndShield()
    {
        GameTab.open(GameTab.TABS.INVENTORY);

        if (!Timing.waitCondition(APIMethods.conditionIsGameTabOpen(GameTab.TABS.INVENTORY), 1000))
            return;

        final int bronzeSwordID = 1277;
        final int shieldID = 1171;

        RSItem[] sword = Inventory.find(bronzeSwordID);
        RSItem[] shield = Inventory.find(shieldID);

        if (sword.length == 0 || shield.length == 0)

        return;
        shield[0].click();
        sword[0].click();
    }

    private boolean openEquipmentTab()
    {
        GameTab.open(GameTab.TABS.EQUIPMENT);

        return Timing.waitCondition(APIMethods.conditionIsGameTabOpen(GameTab.TABS.EQUIPMENT), 1200);
    }

    private void wieldDagger()
    {
        final int masterActualWornableInterface = 84;

        Interfaces.get(INTERFACE_MASTER_WORN, INTERFACE_CHILD_WORN_OPEN).click();

        if (Timing.waitCondition(APIMethods.conditionIsInterfaceValid(masterActualWornableInterface), 1200))
        {
            RSItem[] dagger = Inventory.find(ID_BRONZE_DAGGER);

            if (dagger.length == 0)
                return;

            dagger[0].click();
        }
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_CTR.contains(Player.getPosition());
    }
}
