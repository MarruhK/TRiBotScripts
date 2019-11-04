package scripts.dmmrelekkafisher.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;

public class WalkToMule implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToMule Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.MULE_TILE))
        {
            Methods.waitToStopMoving(30000, 31000);
        }
    }

    @Override
    public boolean validate()
    {
        return (Inventory.find(Vars.fishingEquipmentID).length > 0  &&
                Inventory.isFull()                                  &&
                Player.getPosition().distanceTo(Constants.MULE_TILE) > 10);
    }
}
