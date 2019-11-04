package scripts.jugfiller.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.jugfiller.data.Constants;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;
import scripts.jugfiller.framework.Validators;

public class WalkBank extends Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkBank Node has been Validated! Executing...");

        if (DaxWalker.walkTo(Constants.BANK_TILE))
        {
            Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(6100, 6700));
        }
    }

    @Override
    public boolean validate()
    {
        return  !Vars.isOutOfCoins                          &&
                Inventory.getCount(Constants.EMPTY_JUG_ID) == 0  &&
                !Validators.isInBank();
    }
}
