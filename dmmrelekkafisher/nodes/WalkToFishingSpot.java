package scripts.dmmrelekkafisher.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;

public class WalkToFishingSpot implements Node {
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToFishingSpot Node has been Validated! Executing...");

        if (WebWalking.walkTo(Vars.fishingTile))
        {
            Methods.waitToStopMoving(30000, 31000);
        }
    }

    @Override
    public boolean validate()
    {
        return  (Inventory.find(Vars.fishID).length < 15                &&
                 !Vars.isPkerDetected                                   &&
                 !Inventory.isFull()                                    &&
                 Player.getPosition().distanceTo(Vars.fishingTile) > 4);
    }
}
