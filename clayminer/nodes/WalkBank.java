package scripts.clayminer.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Node;
import scripts.clayminer.data.Vars;
import scripts.clayminer.framework.Validators;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;

public class WalkBank implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkBank Node has been Validated! Executing...");

        if (DaxWalker.walkTo(Constants.BANK_TILE))
        {
            Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(4200, 4350));
        }
    }

    @Override
    public boolean validate()
    {
        return (!Validators.isInBottomFloor()   &&
                !Banking.isInBank()             &&
                Inventory.isFull());
    }
}
