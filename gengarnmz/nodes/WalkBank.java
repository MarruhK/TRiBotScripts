package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;


public class WalkBank extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("WalkBank initiated... Executing...");

        DaxWalker.walkTo(Constants.AREA_BANK.getRandomTile());

        Timing.waitCondition(GBooleanSuppliers.isInArea(Constants.AREA_BANK), General.random(10000, 13500));
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldWalkBank();
    }
}
