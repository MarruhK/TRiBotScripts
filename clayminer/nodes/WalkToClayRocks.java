package scripts.clayminer.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Node;
import scripts.clayminer.data.Vars;
import scripts.clayminer.framework.Validators;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.WalkingCondition;
import scripts.gengarlibrary.GBooleanSuppliers;

public class WalkToClayRocks implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToClayRocks Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.CLAY_TILE))
        {
            Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(4200, 4350));
        }
    }

    @Override
    public boolean validate()
    {
        return  !Inventory.isFull()     &&
                 Validators.isInMine()  &&
                 Constants.CLAY_TILE.distanceTo(Player.getPosition()) > 6;
    }
}
