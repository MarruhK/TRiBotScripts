package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;


public class MiningArea implements Node
{
    private final RSTile TILE_INSTRUCTOR = new RSTile(3081, 9503, 0);

    private final int ID_NPC_INSTRUCTOR = 3311;

    private final int ID_OBJECT_TIN_ORE = 10080;
    private final int ID_OBJECT_COPPER_ORE = 10079;

    private final int ID_PICKAXE = 1265;
    private final int ID_TIN_ORE = 438;
    private final int ID_COPPER_ORE = 436;
    private final int ID_HAMMER = 2347;
    private final int ID_BRONZE_BAR = 2349;

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("MiningArea nodes has been initiated! Executing...");

        // Remove one of the uptext that involves first walking to the instructor.
        if (APIMethods.getChatText().contains("<col=0000ff>Mining and Smithing</col><br>")          ||
            APIMethods.getChatText().contains("mine them. He'll even give you the required tools.") ||
            APIMethods.getChatText().contains("You've made a bronze bar!"))
        {
            System.out.println("MiningArea: Talk to instructor.");
            if (removeWeirdTutIslandChatDialogue() && APIMethods.clickNPC(NPCs.find(ID_NPC_INSTRUCTOR), "Talk-to",  6500))
            {
                APIMethods.continueChat();
                System.out.println("MiningArea: test");
            }

        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Prospecting</col><br>") ||
                 APIMethods.getChatText().contains("It's copper.<br><br>") ||
                 APIMethods.getChatText().contains("It's tin.<br><br>"))
        {
            System.out.println("MiningArea: Prospect rock.");
            prospectOres();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Mining</col><br>") &&
            Inventory.find(ID_PICKAXE).length > 0)
        {
            System.out.println("MiningArea: Mine rocks");
            mineOres();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Smelting</col><br>") &&
            Inventory.find(ID_COPPER_ORE).length > 0                            &&
            Inventory.find(ID_TIN_ORE).length > 0)
        {
            System.out.println("MiningArea: Smelt ores");
            smeltOres();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Smithing a dagger</col><br>") &&
            Inventory.find(ID_HAMMER).length > 0                                              &&
            Inventory.find(ID_BRONZE_BAR).length > 0)
        {
            System.out.println("MiningArea: Make daggers");
            makeDagger();
        }
        else
        {
            System.out.println("MiningArea: ERROR");
        }
        General.sleep(150);
    }

    private boolean removeWeirdTutIslandChatDialogue()
    {
        final int master = 162;
        final int child = 37;

        RSInterface weirdChatDialogue = Interfaces.get(master, child);

        if (!weirdChatDialogue.isHidden())
            weirdChatDialogue.click();

        return weirdChatDialogue.isHidden();
    }

    private void prospectOres()
    {
        while (!APIMethods.getChatText().contains("Talk to the Mining Instructor"))
        {
            if (APIMethods.getChatText().contains("It's copper.<br><br>"))
            {
                if (!APIMethods.clickObject(Objects.find(10, ID_OBJECT_TIN_ORE), "Prospect Rocks",-1))
                    DaxWalker.walkTo(new RSTile(3078, 9503, 0));
            }
            else if (APIMethods.getChatText().contains("It's tin.<br><br>"))
            {
                if (!APIMethods.clickObject(Objects.find(10, ID_OBJECT_COPPER_ORE), "Prospect Rocks",-1))
                    DaxWalker.walkTo(new RSTile(3082, 9501, 0));


            }
            else
            {
                RSObject[] rocks = getRocks();

                if (!APIMethods.clickObject(rocks, "Prospect Rocks",-1))
                    DaxWalker.walkTo(new RSTile(3078, 9503, 0));
            }
            General.sleep(3500);
        }
    }

    private void mineOres()
    {
        RSObject[] ores = getRocks();

        if (ores == null)
            return;

        for (RSObject ore : ores)
        {
            if (!ore.isOnScreen())
                DaxWalker.walkTo(ore.getPosition());

            ore.click();
            Timing.waitCondition(APIMethods.conditionIsAnimation(625), 4500);
            Timing.waitCondition(APIMethods.conditionIsAnimationNot(625), 4500);
        }
    }

    private void makeDagger()
    {
        final int anvilID = 2097;

        APIMethods.clickObject(Objects.findNearest(8, anvilID), "Smith Anvil", -1);

        final int master = 312;
        final int child = 2;
        final int component = 2;
        final int smithingAnimation = 898;

        if (Timing.waitCondition(APIMethods.conditionIsInterfaceValid(master), General.random(4500, 4600)))
        {
            Interfaces.get(master, child).getChild(component).click();
            Timing.waitCondition(APIMethods.conditionIsAnimation(smithingAnimation), 1200);
            Timing.waitCondition(APIMethods.conditionIsAnimation(-1), 3700);
        }
    }

    private void smeltOres()
    {
        final RSTile furnaceTile = new RSTile(3079, 9498, 0);

        DaxWalker.walkTo(furnaceTile);

        // No null check we already konw it exists.
        RSItem[] randomOre = Inventory.find(Math.random() > 0.5 ? ID_COPPER_ORE : ID_TIN_ORE);
        doSmelting(randomOre[0], randomOre[0].getID() == ID_TIN_ORE ? "Use Tin ore -> Furnace" : "Use Copper ore -> Furnace");
        Timing.waitCondition(APIMethods.conditionIsAnimation(-1), 5000);
    }

    private boolean doSmelting(RSItem ore, String uptext)
    {
        ore.click();

        final int furnaceID = 24012;
        final int smeltingAnimation = 3423;
        RSObject[] furnace = Objects.find(4, furnaceID);

        if (furnace.length == 0)
            return false;

        return APIMethods.clickObject(furnace, uptext, smeltingAnimation);
    }

    private RSObject[] getRocks()
    {
        RSObject[] tinOres = Objects.findNearest(9, ID_OBJECT_TIN_ORE);
        RSObject[] copperOres = Objects.findNearest(9, ID_OBJECT_COPPER_ORE);

        if (tinOres.length == 0 || copperOres.length == 0)
            return null;

        RSObject tinOre = tinOres[0];
        RSObject copperOre = copperOres[0];

        return (Player.getPosition().distanceTo(tinOre.getPosition()) < Player.getPosition().distanceTo(copperOre.getPosition())
                ? new RSObject[]{tinOre, copperOre} : new RSObject[]{copperOre, tinOre});
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_MA.contains(Player.getPosition());
    }
}
