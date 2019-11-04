package scripts.gengarcooker.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.gengarcooker.data.Constants;
import scripts.gengarcooker.data.Methods;
import scripts.gengarcooker.data.Vars;
import scripts.gengarcooker.framework.Node;

public class WalkVarrockRange extends Node{
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkVarrockRange Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.OUTSIDE_DOOR_RANGE_TILE)){
            Methods.waitToStop(20500, 21000);
        }

        RSObject[] closedDoor = Objects.find(4, Constants.CLOSED_RANGE_DOOR_ID);

        if (closedDoor.length > 0){
            // Door is closed, open it.
            System.out.println("WalkVarrockRange: Door is closed, opening it...");
            closedDoor[0].hover();
            if (!Methods.safeClick("Open Door")){
                System.out.println("WalkVarrockRange: Failed to open door to enter. Returning...");
                return;
            }
        }

        if (WebWalking.walkTo(Constants.RANGE_TILE)){
            Methods.waitToStop(2500, 3000);
        } else {
            System.out.println("WalkVarrockRange: Failed to enter the range house. Returning...");
        }
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        // Has raw food and is not in range house
        return  (Inventory.getCount(Vars.rawFoodID) > 0                                 &&
                (!(Constants.RANGE_AREA_1.contains(Player.getPosition())      ||
                        Constants.RANGE_AREA_2.contains(Player.getPosition()))));
    }
}
