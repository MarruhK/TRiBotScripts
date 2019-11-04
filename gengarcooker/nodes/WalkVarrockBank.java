package scripts.gengarcooker.nodes;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.gengarcooker.data.Constants;
import scripts.gengarcooker.data.Methods;
import scripts.gengarcooker.data.Vars;
import scripts.gengarcooker.framework.Node;

public class WalkVarrockBank extends Node{


    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkVarrockBank Node has been Validated! Executing...");

        RSObject[] closedDoors = Objects.find(7, Constants.CLOSED_RANGE_DOOR_ID);
        RSObject closedDoor = null;

        for (RSObject door: closedDoors){
            if (door.getPosition() == new RSTile(3241, 3412, 0)){
                System.out.println("WalkVarrockBank: Proper door is closed and should be opened.");
                closedDoor = door;
            }
        }

        if (closedDoor != null){
            // Door is closed, open it.
            if (!Methods.safeClick("Open Door")){
                System.out.println("WalkVarrockBank: Failed to open door to leave. Returning...");
                return;
            }
        }

        if (WebWalking.walkTo(Constants.BANK_TILE)){
            Methods.waitToStop(20500, 21000);
        }

        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        // Not in bank and no raw food in inventory.
        return (Inventory.getCount(Vars.rawFoodID) == 0 &&
                !Banking.isInBank());
    }
}
