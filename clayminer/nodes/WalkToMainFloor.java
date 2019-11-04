package scripts.clayminer.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Node;
import scripts.clayminer.framework.Validators;
import scripts.dax_api.api_lib.DaxWalker;

public class WalkToMainFloor implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToMainFloor Node has been Validated! Executing...");

        DaxWalker.walkTo(Constants.MAIN_LEVEL_TILE);
    }

    @Override
    public boolean validate()
    {

        return !Validators.isInMiddleFloor() && !Validators.isInBottomFloor() && !Inventory.isFull();
    }
}
