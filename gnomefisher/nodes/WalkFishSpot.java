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


public class WalkFishSpot extends Node{
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkFishSpot Node has been Validated! Executing...");

        if (DaxWalker.walkTo(Constants.BASE_FISHING_TILE)){
            Methods.waitToStopMoving(47000, 48000);
        }
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (!Inventory.isFull()                                                 &&
                Constants.BASE_FISHING_TILE.distanceTo(Player.getPosition()) > 10);
    }
}
