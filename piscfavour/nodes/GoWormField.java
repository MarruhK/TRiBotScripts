package scripts.piscfavour.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import scripts.piscfavour.data.Constants;
import scripts.piscfavour.framework.Node;

public class GoWormField extends Node {

    @Override
    public void execute() {
        // Have at least one bucket in inventory and are far from the worm field
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The GoWormField Node has been Validated! Executing...");

        WebWalking.walkTo(Constants.WORM_FIELD_TILE);
        // Methods.waitToStopMoving(5500, 6000);
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (Inventory.find(Constants.BUCKETS_ID).length > 20 &&
                Player.getPosition().distanceTo(Constants.WORM_FIELD_TILE) > 7);
    }
}
