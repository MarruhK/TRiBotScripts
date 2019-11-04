package scripts.clayminer.nodes.roothandlers;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Node;
import scripts.clayminer.framework.Validators;

public class EnterMine extends HandleRoots implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The EnterMine Node has been Validated! Executing...");

        if (!passRoots())
        {
            WebWalking.walkTo(Constants.OUTSIDE_MINE_ROOT_TILE);
        }
    }

    @Override
    public boolean validate() {
        return Validators.isInBottomFloor() && !Validators.isInMine() && !Inventory.isFull();
    }
}
