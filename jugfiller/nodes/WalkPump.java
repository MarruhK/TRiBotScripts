package scripts.jugfiller.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.jugfiller.data.Constants;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;

public class WalkPump extends Node
{

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkPump Node has been Validated! Executing...");

        if (!DaxWalker.walkTo(Constants.PUMP_TILE) || !Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(7100, 7700)))
        {
            System.out.println("execute: Failed to walk to the water pump");
        }
    }

    @Override
    public boolean validate()
    {
        return !Vars.isOutOfCoins                          &&
               Inventory.getCount(Constants.EMPTY_JUG_ID) > 0   &&
               Player.getPosition().distanceTo(Constants.PUMP_TILE) > 6;
    }
}
