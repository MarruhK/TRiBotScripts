package scripts.piscfavour.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import scripts.piscfavour.data.Constants;
import scripts.piscfavour.framework.Node;

public class GoStore extends Node {

    @Override
    public void execute() {
        // No empty buckets in invent and at least one sand bucket along with being far from store
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The GoStore Node has been Validated! Executing...");

        WebWalking.walkTo(Constants.GEN_STORE_TILE);
        // Methods.waitToStopMoving(5500, 6000);
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (Inventory.find(Constants.BUCKETS_ID).length == 0        &&
                Inventory.find(Constants.BUCKETS_WORMS_ID).length > 0   &&
                Player.getPosition().distanceTo(Constants.GEN_STORE_TILE) > 7);
    }
}
