package scripts.anglerfish.nodes;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import scripts.anglerfish.data.Constants;
import scripts.anglerfish.data.Vars;
import scripts.anglerfish.framework.Node;
import scripts.anglerfish.data.Methods;

public class WalkToAnglers extends Node {
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToAnglers Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.FISHING_TILE)){
            Methods.waitToStop(13500, 15000);
        }

        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        // Inventory contains fishing rod and bait, not full, and not near fishing tile.
        return (Vars.shouldExecute                                                      &&
                Inventory.find(Constants.BAIT_ID, Constants.FISHING_ROD_ID).length == 2 &&
                !Inventory.isFull()                                                     &&
                Player.getPosition().distanceTo(Constants.FISHING_TILE) < 70            &&
                Player.getPosition().distanceTo(Constants.FISHING_TILE) > 10);
    }
}
