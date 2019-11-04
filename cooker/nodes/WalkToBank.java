package scripts.cooker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.cooker.data.Vars;
import scripts.cooker.framework.Node;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;

public class WalkToBank implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToBank Node has been Validated! Executing...");

        DaxWalker.walkTo(Vars.bankArea.getRandomTile());
        Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(13500, 15000));
    }

    @Override
    public boolean validate()
    {
        return !Vars.bankArea.contains(Player.getPosition()) && Inventory.find(Vars.rawFoodId).length <= 0;
    }
}
