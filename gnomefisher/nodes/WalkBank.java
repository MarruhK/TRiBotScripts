package scripts.gnomefisher.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gnomefisher.data.Constants;
import scripts.gnomefisher.data.Methods;
import scripts.gnomefisher.framework.Node;


public class WalkBank extends Node{
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkVarrockBank Node has been Validated! Executing...");

        if (DaxWalker.walkTo(Constants.BANK_DOWN_STAIRS_TILE)){
            if(Methods.waitToStopMoving(40000, 41000)){

                RSObject[] stairs = Objects.find(5, Constants.BOTTOM_LADDER_ID);

                if (stairs.length > 0){
                    stairs[0].hover();
                    Methods.safeClick("Climb-up Staircase");
                }
            }
        }

        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (Inventory.isFull() &&
                !Constants.BANK_AREA.contains(Player.getPosition()));
    }
}
