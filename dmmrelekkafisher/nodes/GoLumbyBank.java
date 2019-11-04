package scripts.dmmrelekkafisher.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.framework.Node;

public class GoLumbyBank implements Node {
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The GoLumbyBank Node has been Validated! Executing...");

        if (Player.getPosition().getPlane() == 0){
            if (WebWalking.walkTo(Constants.DOWN_STAIRS_TILE)){
                Methods.waitToStopMoving(13000, 13500);
                climbStairs(Objects.find(4, Constants.DOWN_LADDER_ID), 1);
            }
        } else if (Player.getPosition().getPlane() == 1){
            climbStairs(Objects.find(4, Constants.MID_LADDER_ID), 2);
        }
        General.sleep(1500);
    }

    private boolean climbStairs(RSObject[] ladder, int planeToGoTo){
        if (ladder.length > 0){
            ladder[0].hover();
            Methods.safeClick("Climb-up Staircase");
            return Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Player.getPosition().getPlane() == planeToGoTo;
                }
            }, General.random(2000, 2100));
        }
        return false;
    }

    @Override
    public boolean validate() {
        return (Constants.LUMBY_AREA.contains(Player.getPosition()) ||
                Constants.LUMBY_2ND_FLOOR.contains(Player.getPosition()));
    }
}
