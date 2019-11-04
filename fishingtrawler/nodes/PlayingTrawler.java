package scripts.fishingtrawler.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.fishingtrawler.data.Constants;
import scripts.fishingtrawler.data.Methods;
import scripts.fishingtrawler.data.Vars;
import scripts.fishingtrawler.framework.Node;

import java.util.ArrayList;


public class PlayingTrawler extends Node {


    RSInterface trawlerInterface = Interfaces.get(366);
    // game uptext "Fill Leak"
    // Climb-up Ship's ladder
    // Climb-down Ship's laddehttps://www.youtube.com/watch?v=P6gJj8ZXOYor
    // Inspect Trawler net

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The PlayingTrawler Node has been Validated! Executing...");
        System.out.println("Walking to designated area.");
        General.println("Vars.isNetFixer = " + Vars.isNetFixer);

        // Depending on if flooded or not, walking tiles change.
        if (isBailingNeeded()){
            WebWalking.walkTo(Vars.floodedTrawlerArea.getRandomTile());
        } else {
            WebWalking.walkTo(Vars.dryTrawlerArea.getRandomTile());
        }


        while (true) {
            General.sleep(120);

            if (Vars.isNetFixer) {
                // Fix the net based on conditions
                if (isTimeEnding()) {
                    System.out.print("TIME IS ENDING FIX THE NET IF BROKEN!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    if (isNetBroken()){
                        fixNet();
                    } else {
                        doNoNetFixTrawler();
                    }
                } else {
                    doNoNetFixTrawler();
                }
            }  else if (isTimeEnding()) {
                soloNetFix();
            } else {
                doNoNetFixTrawler();
            }
        }
    }

    //__________________________________________________________________________________________________________________

    private void soloNetFix(){
        System.out.println("- soloNetFix method has began!");

        RSObject[] holes = Objects.find(8, Constants.WATER_HOLE_ID);

        if (holes == null || holes.length == 0) {
            if (isBailingNeeded()) {
                bailWater();
            }
            return;
        } else {
            Methods.clickObject(holes[0], Constants.FILLING_HOLE_ANIMATION, "Fill leak");
        }
    }

    //__________________________________________________________________________________________________________________

    private void doNoNetFixTrawler(){
        System.out.println("- donNoNetFixTrawler method has began!");
        // Ensure nextTarget holds a valid value, if not, assign it

        RSObject[] areaHoles = getLeakingHoles();

        if (areaHoles == null || areaHoles.length == 0) {
            if (isBailingNeeded()) {
                bailWater();
            }
            return;
        } else {
            Methods.clickObject(areaHoles[0], Constants.FILLING_HOLE_ANIMATION, "Fill leak");
        }
    }

    //__________________________________________________________________________________________________________________
    private boolean isTimeEarly() {
        // First two minutes.
        return (isTrawlerTime("Time left: 12 mins") || isTrawlerTime("Time left: 11 mins"));
    }

    private boolean isTimeMiddle(){
        // from 3-10 minutes
        return (!(isTimeEarly() || isTimeEnding()));
    }

    private boolean isTimeEnding(){
        // Last two minutes
        return (isTrawlerTime("Time left: Under 1 min"));
    }

    private boolean isTrawlerTime(String time){
        return trawlerInterface.getChild(Constants.TRAWLER_TIME_STATUS_CHILD_ID).getText().equals(time);
    }
    //__________________________________________________________________________________________________________________

    private boolean isNetBroken() {
        if (Interfaces.get(Constants.TRAWLER_INFO_BAR_INTERFACE_INDEX_ID, Constants.TRAWLER_NET_STATUS_CHILD_ID)
                .getText().equals("Net: <col=ff0000>Ripped!</col>")){
            System.out.println("isNetBroken is true.");
            return true;
        }
        System.out.println("isNetBroken is false.");
        System.out.println("Text value is: " + Interfaces.get(Constants.TRAWLER_INFO_BAR_INTERFACE_INDEX_ID, Constants.TRAWLER_NET_STATUS_CHILD_ID)
                .getText());
        return false;
    }

    private void fixNet(){
        // Climb-up Ship's ladder
        // Climb-down Ship's ladder
        // Inspect Trawler net
        System.out.println("Net is ripped and conditions are met. Going to fix net now.");

        // Locate ladder and climb.
        RSObject[] ladders = Objects.find(8, Constants.TRAWLER_NET_LADDER_ID);
        if (ladders.length > 0 && ladders != null) {
            System.out.println("-- climbing ladder to go to net");
            Methods.clickObject(ladders[0],Constants.LADDER_CLIMB_ANIMATION, "Climb-up Ship's ladder");
        }

        inspectTrawlerNet();

        // Go back downstairs.

        RSObject[] upstairsLadder = Objects.find(8, 4139);

        if (upstairsLadder.length > 0 && upstairsLadder != null){
            System.out.println("-- climbing ladder to go to ship base");
            Methods.clickObject(upstairsLadder[0], Constants.LADDER_CLIMB_ANIMATION, "Climb-down Ship's ladder");
        }

    }

    private void inspectTrawlerNet(){
        RSObject[] nets = Objects.find(6, Constants.TRAWLER_NET_ONE);

        if (nets.length > 0 && nets != null){
            do{
                System.out.println("-- fixing the net");
                Camera.turnToTile(nets[0].getPosition());
                Methods.clickObject(nets[0], Constants.FILLING_HOLE_ANIMATION,"Inspect Trawler net");
            } while (isNetBroken());
        }
    }

    //__________________________________________________________________________________________________________________
    // Returns the nearest hole that is within the player's designated area. Null if none exist.
    private RSObject[] getLeakingHoles() {
        System.out.println("-- getLeakingHoles method began.");
        RSObject[] holes = Objects.find(8, Constants.WATER_HOLE_ID);

        if (holes.length == 0) {
            System.out.println("-- getLeakingHoles DID NOT FIND ANYTHING AND RETURNED NULL");
            return null;
        }

        ArrayList<RSObject> holesInArea = new ArrayList<RSObject>();

        // maybe if initially dry, ensure flooded doesn' occur.
        for (RSObject hole: holes){
            RSTile holePosition = hole.getPosition();

            // add if statement to change trawler area based on if baliing is needed
            if (isBailingNeeded()){
                // flooded so need flooded area tiles

                // if below certain y position then use hole area, else normal area.
                if (hole.getPosition().getY() < 4826) {
                    if (isInArea(Vars.floodedSouthHoleTrawlerArea, holePosition)){
                        holesInArea.add(hole);
                    }
                } else {
                    if (isInArea(Vars.floodedTrawlerArea, holePosition)){
                        holesInArea.add(hole);
                    }
                }
            } else {
                // Not flooded so use dry area tiles
                // if below certain y position then use hole area, else normal area.
                if (hole.getPosition().getY() < 4826) {
                    if (isInArea(Vars.drySouthHoleTrawlerArea, holePosition)){
                        holesInArea.add(hole);
                    }
                } else {
                    if (isInArea(Vars.dryTrawlerArea, holePosition)){
                        holesInArea.add(hole);
                    }
                }
            }
        }

        RSObject[] allLeakingHoles = new RSObject[holesInArea.size()];

        for (int i = 0; i < holesInArea.size(); i++){
            allLeakingHoles[i] = holesInArea.get(i);
        }

        if (allLeakingHoles.length > 0){
            System.out.println("-- Returning array of leaking holes to fix.");
        } else {
            System.out.println("-- Returning an empty array of leaking holes.");
        }
        return allLeakingHoles;
    }



    private boolean isInArea(RSArea area, RSTile holePosition){
        if (area.contains(holePosition)) {
            // The hole is in the RSArea the character is designated for. Fix.
            return true;
        } else {
            return false;
        }
    }

    //__________________________________________________________________________________________________________________
    private boolean isBailingNeeded(){
        if (Constants.FLOODED_BOAT_AREA.contains(Player.getPosition())){
            return true;
        }
        return false;
    }

    // Bails water out of ship if needed. Will stop immediately if water in ship is removed.
    private void bailWater(){
        Inventory.getAll()[Vars.indexOfBuckets].click();
        Inventory.getAll()[Vars.indexOfBuckets].click();
        Inventory.getAll()[Vars.indexOfBuckets].click();  // clicks last bucket.
    }

    private boolean isWaterLevelXPercent(int waterLevelPercent) {
        return (trawlerInterface.getChild(waterLevelPercent).getTextColour()
                == Constants.TRAWLER_WATER_LEVEL_BAR_TEXT_COLOUR);
    }
    //__________________________________________________________________________________________________________________


    @Override
    public boolean validate() {
        return true;
    }
}
