package scripts.gnomefisher.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.gnomefisher.data.Constants;
import scripts.gnomefisher.data.Methods;
import scripts.gnomefisher.framework.Node;

public class LeaveBank extends Node {
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The LeaveBank Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.BANK_UP_STAIRS_TILE)){
            if(Methods.waitToStopMoving(7000, 7100)){

                RSObject[] stairs = Objects.find(5, Constants.TOP_LADDER_ID);

                if (stairs.length > 0){
                    stairs[0].hover();
                    Methods.safeClick("Climb-down Staircase");
                }
            }
        }
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (!Inventory.isFull() &&
                Player.getPosition().getPlane() == 1);
    }
}
