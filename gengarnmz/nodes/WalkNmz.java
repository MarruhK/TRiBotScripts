package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class WalkNmz extends Node
{
    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("WalkNmz initiated... Executing...");

        DaxWalker.walkTo(Constants.AREA_NMZ.getRandomTile());

        Timing.waitCondition(GBooleanSuppliers.isInArea(Constants.AREA_NMZ), General.random(10000, 13500));
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldWalkNmz();
    }
}