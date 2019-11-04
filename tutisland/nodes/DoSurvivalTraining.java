package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;


import java.awt.*;
import java.util.Arrays;

public class DoSurvivalTraining implements Node
{
    private final int ID_OBJECT_TREE = 9730;
    private final int ID_CHOPPED_TREE = 1342;
    private final int ID_LOG = 2511;
    private final int ID_TINDERBOX = 590;
    private final int ID_LIT_FIRE = 26185;
    private final int ID_RAW_SHRIMP = 2514;
    private final int ID_NPC_SHRIMP = 3317;

    private final int ANIMATION_CHOPPING = 879;
    private final int ANIMATION_LIGHTING = 733;
    private final int ANIMATION_FISHING = 621;
    private final int ANIMATION_COOKING = 897;
    private final int ANIMATION_NONE = -1;

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("DoSurvivalTraining nodes has been initiated! Executing...");

        // TALK to the survival expert.
        if (APIMethods.getChatText().contains("<col=0000ff>Moving around</col><br>") ||
            APIMethods.getChatText().contains("<col=0000ff>Your skill stats</col><br>"))
        {
            System.out.println("DoSurvivalTraining: Speaking to survival expert.");
            speakToExpert();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Cut down a tree<br>"))
        {
            System.out.println("DoSurvivalTraining: Going to chop tree");
            chopTree();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Making a fire</col><br>"))
        {
            System.out.println("DoSurvivalTraining: Going to light log on fire");
            lightLog();
        }
        else if (APIMethods.getChatText().contains("You gained some experience.<br>"))
        {
            System.out.println("DoSurvivalTraining: Open stat tab");
            openTabs();
        }
        else if (APIMethods.getChatText().contains("<col=0000ff>Catch some shrimp</col><br>"))
        {
            System.out.println("DoSurvivalTraining: Going to fish for shrimps");
            fishShrimps();
        }
        else if (isTimeToCook())
        {
            if (!isFireNearby())
            {
                System.out.println("DoSurvivalTraining: Fire not nearby, going to make one.");
                chopTree();
                lightLog();
            }
            System.out.println("DoSurvivalTraining: Going to cook the shrimps.");
            cookShrimps();
        }
        else
        {
            System.out.println("DoSurvivalTraining: No if statement have been executed...");
        }
        General.sleep(200, 350);
    }

    private boolean isFireNearby()
    {
        return Objects.find(6, ID_LIT_FIRE).length > 0;
    }

    private boolean isTimeToCook()
    {
        return (APIMethods.getChatText().contains("<col=0000ff>Cooking your shrimp</col><br>") && Inventory.find(ID_RAW_SHRIMP).length >= 2 ||
               (APIMethods.getChatText().contains("<col=0000ff>Burning your shrimp</col><br>") && Inventory.find(ID_RAW_SHRIMP).length >= 1));
    }

    private void speakToExpert()
    {
        RSNPC[] expert = NPCs.find(3306);

        if (expert.length == 0)
            return;

        expert[0].click();

        if (Timing.waitCondition(new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return  Interfaces.get(231, 2) != null &&
                        Interfaces.get(231, 2).getText().equals("Survival Expert");
            }
        }, General.random(1400, 1500)))
        {
            APIMethods.continueChat();
            GameTab.open(GameTab.TABS.INVENTORY);
        }
    }

    private void openTabs()
    {
        GameTab.open(GameTab.TABS.STATS);
        General.sleep(250);
    }

    private boolean fishShrimps()
    {
        // Need two shrimps in inventory
        RSNPC[] fishingSpots = NPCs.findNearest(ID_NPC_SHRIMP);

        if (fishingSpots.length == 0)
            return false;

        do
        {
            APIMethods.clickNPC(fishingSpots, "Net", 5500);
            Timing.waitCondition(APIMethods.conditionIsAnimation(ANIMATION_FISHING), 1200);
            Timing.waitCondition(APIMethods.conditionIsAnimationNot(ANIMATION_FISHING), 6000);
        } while (Inventory.find(ID_RAW_SHRIMP).length < 2);

        return false;
    }

    private boolean cookShrimps()
    {
        RSObject[] fires = Objects.find(5, ID_LIT_FIRE);

        if (fires.length > 0)
        {
            RSItem[] shrimps = Inventory.find(ID_RAW_SHRIMP);

            for (RSItem shrimp : shrimps)
            {
                // Click on shrimp,
                Mouse.moveBox(shrimp.getArea());
                Mouse.click(1);

                // click on fire, if it still exists, need to wrap in try/catch
                try
                {
                    if (Timing.waitCondition(APIMethods.conditionIsUptext("Use Raw shrimps ->"), 500) &&
                        useShrimpOnFire(fires))
                    {
                        // Failed one of the conditions above
                        System.out.println("DoSurvivalTraining: Successfully cooked the shrimp");
                    }
                }
                catch (Exception ex)
                {
                    System.out.println("DoSurvivalTraining: The fire no longer exists.");
                    return false;
                }
            }
        }
        return false;
    }

    private boolean useShrimpOnFire(RSObject[] fires)
    {
        if (APIMethods.clickObject(fires, "Use Raw shrimps -> Fire", ANIMATION_COOKING))
        {
            Timing.waitCondition(APIMethods.conditionIsAnimation(-1), 4000);
            return true;
        }
        return false;
    }

    private boolean chopTree()
    {
        System.out.println("DoSurvivalTraining: Going to chop tree for log");
        RSObject[] trees = Objects.find(4, ID_OBJECT_TREE);

        if (trees.length == 0)
            return false;

        RSObject tree = trees[0];

        tree.click();

        if (Timing.waitCondition(APIMethods.conditionIsAnimation(ANIMATION_CHOPPING), 2000))
        {
            // Currently chopping wait
            Timing.waitCondition(APIMethods.conditionIsAnimation(ANIMATION_NONE), 6000);
        }

        boolean isLogObtained = Inventory.find(ID_LOG).length <= 0;

        if (isLogObtained)
        {
            System.out.println("DoSurvivalTraining: Successfully chopped log");
        }
        else
        {
            System.out.println("DoSurvivalTraining: Failed to chop a log");
        }

        return isLogObtained;
    }

    private boolean lightLog()
    {
        // First make sure no fire is under you, fking aids.
        RSObject[] fires = Objects.find(2, ID_LIT_FIRE);

        if (fires.length > 0)
        {
             Object[] fireUnderMe = Arrays.stream(fires)
                                        .filter(rsObject -> Player.getPosition().equals(rsObject.getPosition()))
                                        .toArray();

             if (fireUnderMe.length > 0)
             {
                 System.out.println("DoSurvivalTraining: Currently under a fire, getting out of the way.");
                 DaxWalker.walkTo(Constants.AREA_DST.getRandomTile());
                 return false;
             }
        }

        RSItem[] tinderBox = Inventory.find(ID_TINDERBOX);
        RSItem[] logs = Inventory.find(ID_LOG);

        if (tinderBox.length == 0 || logs.length == 0)
            return false;

        Rectangle tinderBoxArea = tinderBox[0].getArea();
        Rectangle logArea = logs[0].getArea();

        // Move mouse and click on tinderbox
        Mouse.moveBox(tinderBoxArea);
        Mouse.click(1);

        // if uptext is use tinderbox on ->
        if (Timing.waitCondition(APIMethods.conditionIsUptext("Use Tinderbox ->"), 600))
        {
            Mouse.moveBox(logArea);

            if (Timing.waitCondition(APIMethods.conditionIsUptext("Use Tinderbox -> Logs"), 600))
                Mouse.click(1);
        }

        if (Timing.waitCondition(APIMethods.conditionIsAnimation(ANIMATION_LIGHTING), 6500) &&
            Timing.waitCondition(APIMethods.conditionIsAnimation(-1), 2500))
        {
            General.sleep(1200);
            return true;
        }
        return false;
    }




    @Override
    public boolean validate()
    {
        return Constants.AREA_DST.contains(Player.getPosition());
    }
}
