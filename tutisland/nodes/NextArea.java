package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.tutisland.data.Vars;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;


public class NextArea implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("NextArea nodes has been initiated! Executing...");

        if (Interfaces.get(263) == null && (Player.getPosition().distanceTo(Constants.TILE_LUMBY) <= 5))
        {
            System.out.println("NextArea: Walk to Lumbridge Castle");
            walkToArea(Constants.AREA_LUMBY_CASTLE.getRandomTile());
            Vars.shouldExecute = false;
        }
        else if (shouldGoToSurvivalTrainingArea())
        {
            System.out.println("NextArea: Walk to Survival Training Area");
            walkToArea(Constants.AREA_DST.getRandomTile());
        }
        else if (shouldGoToChefArea())
        {
            System.out.println("NextArea: Walk to Chef Building");
            RSTile chefBuildingTile = new RSTile(3075, 3084, 0);
            walkToArea(chefBuildingTile);
        }
        else if (shouldGoToAreaQGR())
        {
            System.out.println("NextArea: Walk to Quest room");
            walkToArea(Constants.AREA1_QGR.getRandomTile());
        }
        else if (shouldGoToMiningArea())
        {
            System.out.println("NextArea -- Walk to Mining Area.");
            RSTile miningTile = new RSTile(3081, 9504, 0);
            walkToArea(miningTile);
        }
        else if (shouldGoToCombatTrainingArea())
        {
            System.out.println("NextArea -- Walk to combat Training Room.");
            RSTile vanakkahTile = new RSTile(3106, 9509, 0);
            walkToArea(vanakkahTile);
        }
        else if (shouldGoToAreaBanking())
        {
            System.out.println("NextArea: Walk to Bank");
            walkToArea(Constants.AREA1_DB.getRandomTile());
        }
        else if (shouldGoToMonkArea())
        {
            System.out.println("NextArea: Walk to Monk.");
            walkToArea(Constants.AREA_DMS.getRandomTile());
        }
        else if (shouldGoToWizardArea())
        {
            System.out.println("NextArea: Walk to Wizard.");
            walkToArea(Constants.AREA1_DWS.getRandomTile());
        }
        else
        {
            System.out.println("NextArea: Error. No idea where Player is so logging out.");
            Vars.shouldExecute = false;
        }
    }

    private void walkToArea(RSTile tile)
    {
        DaxWalker.walkTo(tile);
    }

    private boolean shouldGoToSurvivalTrainingArea()
    {
        return  (APIMethods.getChatText().contains("Talk to the Survival Expert by the pond to continue the tutorial.") ||
                (APIMethods.getChatText().contains("<col=0000ff>Burning your shrimp</col><br>"))                        ||
                (APIMethods.getChatText().contains("<col=0000ff>Cooking your shrimp</col><br>"))                        ||
                (APIMethods.getChatText().contains("<col=0000ff>Moving around</col><br>"))                              ||
                (APIMethods.getChatText().contains("<col=0000ff>Your skill stats</col><br>"))                           ||
                (APIMethods.getChatText().contains("<col=0000ff>Cut down a tree<br>"))                                  ||
                (APIMethods.getChatText().contains("<col=0000ff>Making a fire</col><br>"))                              ||
                (APIMethods.getChatText().contains("You gained some experience.<br>"))                                  ||
                (shouldLeaveAreaDCGS())                                                                                 ||
                (APIMethods.getChatText().contains("<col=0000ff>Catch some shrimp</col><br>")));
    }

    private boolean shouldGoToChefArea()
    {
        return  (APIMethods.getChatText().contains("open it. Notice the mini-map in the top right; this shows")                         ||
                (APIMethods.getChatText().contains("<col=0000ff>Well done, you've just cooked your first RuneScape meal!</col><br>"))   ||
                (APIMethods.getChatText().contains("<col=0000ff>The music player</col><br>"))                                           ||
                (APIMethods.getChatText().contains("<col=0000ff>Cooking dough</col><br>"))                                              ||
                (APIMethods.getChatText().contains("<col=0000ff>Making dough</col><br>"))                                               ||
                (APIMethods.getChatText().contains("<col=0000ff>Find your next instructor</col><br>")));
    }

    private boolean shouldGoToAreaQGR()
    {
        return (APIMethods.getChatText().contains("You may notice that the number on the button goes down.")   ||
                (APIMethods.getChatText().contains("Talk with the Quest Guide"))                                ||
                (APIMethods.getChatText().contains("Your Quest Journal"))                                       ||
                (APIMethods.getChatText().contains("Open the Quest Journal"))                                   ||
                (APIMethods.getChatText().contains("<col=0000ff>Running</col><br>"))                            ||
                (APIMethods.getChatText().contains("<col=0000ff>Emotes</col><br>")));
    }

    private boolean shouldGoToMiningArea()
    {
        return   APIMethods.getChatText().contains("<col=0000ff>Mining and Smithing</col><br>")             ||
                (APIMethods.getChatText().contains("mine them. He'll even give you the required tools."))   ||
                (APIMethods.getChatText().contains("You've made a bronze bar!"))                            ||
                (APIMethods.getChatText().contains("<col=0000ff>Prospecting</col><br>"))                    ||
                (APIMethods.getChatText().contains("<col=0000ff>Mining</col><br>"))                         ||
                (APIMethods.getChatText().contains("<col=0000ff>Smelting</col><br>"))                       ||
                (shouldLeaveAreaQGR())                                                                      ||
                (APIMethods.getChatText().contains("<col=0000ff>Smithing a dagger</col><br>"));
    }

    private boolean shouldGoToCombatTrainingArea()
    {
        return  (APIMethods.getChatText().contains("<col=0000ff>combat</col><br>"))                                    ||
                (APIMethods.getChatText().contains("You're now holding your dagger."))                                 ||
                (APIMethods.getChatText().contains("<col=0000ff>Well done, you've made your first kill!</col><br>"))   ||
                (APIMethods.getChatText().contains("<col=0000ff>Wielding weapons</col><br>"))                          ||
                (APIMethods.getChatText().contains("Left click your dagger to "))                                      ||
                (APIMethods.getChatText().contains("This is your worn inventory"))                                     ||
                (APIMethods.getChatText().contains("<col=0000ff>Unequipping items</col><br>"))                         ||
                (APIMethods.getChatText().contains("<col=0000ff>combat interface</col><br>"))                          ||
                (APIMethods.getChatText().contains("<col=0000ff>Attacking</col><br>"))                                 ||
                (APIMethods.getChatText().contains("This is your combat interface."))                                  ||
                (APIMethods.getChatText().contains("<col=0000ff>Well done, you've made your first kill!</col><br>"))   ||
                (APIMethods.getChatText().contains("<col=0000ff>Rat ranging</col><br>"))                               ||
                (APIMethods.getChatText().contains("<col=0000ff>You've finished in this area</col><br>"));
    }

    private boolean shouldGoToAreaBanking()
    {
        return  (APIMethods.getChatText().contains("This is the Bank of RuneScape")             ||
                (APIMethods.getChatText().contains("This is your bank box."))                   ||
                (APIMethods.getChatText().contains("This is a poll booth.<br>Polls are run"))   ||
                (shouldLeaveAreaCTR())                                                          ||
                (APIMethods.getChatText().contains("Financial advice")));
    }

    private boolean shouldGoToMonkArea()
    {
        return  (shouldLeaveAreaDB())                                                                              ||
                (APIMethods.getChatText().contains("Follow the path to the chapel and enter it"))                  ||
                (APIMethods.getChatText().contains("<col=0000ff>PrayerPotion</col><br>") )                               ||
                (APIMethods.getChatText().contains("Talk with Brother Brace and he'll tell you about prayers."))   ||
                (APIMethods.getChatText().contains("<col=0000ff>Ignore list</col><br>"))                           ||
                (APIMethods.getChatText().contains("<col=0000ff>Your PrayerPotion menu</col><br>"))                      ||
                (APIMethods.getChatText().contains("<col=0000ff>Friends list</col><br>"))                          ||
                (APIMethods.getChatText().contains("This is your friends list."));
    }

    private boolean shouldGoToWizardArea()
    {
        return  (shouldLeaveAreaDMS())                                                                         ||
                (APIMethods.getChatText().contains("This is your spells list."))                               ||
                (APIMethods.getChatText().contains("<col=0000ff>Open up your final menu</col><br>"))           ||
                (APIMethods.getChatText().contains("<col=0000ff>Cast Wind Strike at a chicken.</col><br>"))    ||
                (APIMethods.getChatText().contains("<col=0000ff>You have almost completed the tutorial!</col><br>"));
    }

    private boolean shouldLeaveAreaDCGS()
    {
        if (APIMethods.getChatText().contains("<col=0000ff>Interacting with scenery</col><br>"))
        {
            System.out.println("NextArea -- shouldLeaveAreaDCGS: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaDST()
    {
        if (APIMethods.getChatText().contains("<col=0000ff>Well done, you've just cooked your first RuneScape meal!</col><br>"))
        {
            System.out.println("NextArea -- shouldLeaveAreaDST: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaDCS()
    {
        if (APIMethods.getChatText().contains("You may notice that the number on the button goes down."))
        {
            System.out.println("NextArea -- shouldLeaveAreaDCS: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaQGR()
    {
        // NEEDS TO BE FIXED.
        if (APIMethods.getChatText().contains("<col=0000ff>Moving on</col><br>"))
        {
            System.out.println("NextArea -- shouldLeaveAreaQGR: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaMA()
    {
        if (APIMethods.getChatText().contains("<col=0000ff>You've finished in this area</col><br>"))
        {
            System.out.println("NextArea -- shouldLeaveAreaMA: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaCTR()
    {
        if (APIMethods.getChatText().contains("To move on, click on the ladder shown. If you need to go over any") ||
            APIMethods.getChatText().contains("<col=0000ff>Banking</col><br>"))
        {
            System.out.println("NextArea -- shouldLeaveAreaCTR: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaDB()
    {
        if (APIMethods.getChatText().contains("Continue through the next door."))
        {
            System.out.println("NextArea -- shouldLeaveAreaDB: TRUE");
            return true;
        }
        return false;
    }

    private boolean shouldLeaveAreaDMS()
    {
        if (APIMethods.getChatText().contains("<col=0000ff>Your final instructor!<br>"))
        {
            System.out.println("NextArea -- shouldLeaveAreaDMS: TRUE");
            return true;
        }
        return false;
    }

    @Override
    public boolean validate()
    {
        // Not in designated nodes areas
        return    Vars.shouldExecute                                               &&
                ((Constants.isInStartingArea()      && shouldLeaveAreaDCGS())       ||
                 (Constants.isInSurvivalArea()      && shouldLeaveAreaDST())        ||
                 (Constants.isInChefArea()          && shouldLeaveAreaDCS())        ||
                 (Constants.isInQuestGuideArea()    && shouldLeaveAreaQGR())        ||
                 (Constants.isInMiningArea()        && shouldLeaveAreaMA())         ||
                 (Constants.isInCombatTrainingArea()&& shouldLeaveAreaCTR())        ||
                 (Constants.isInBankingArea()       && shouldLeaveAreaDB())         ||
                 (Constants.isInMonkArea()          && shouldLeaveAreaDMS())        ||
                  Constants.TILE_LUMBY.distanceTo(Player.getPosition()) < 2         ||
                  Constants.AREA_LUMBY_CASTLE.contains(Player.getPosition()));
    }
}
