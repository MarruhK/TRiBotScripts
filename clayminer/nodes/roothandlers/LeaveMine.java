package scripts.clayminer.nodes.roothandlers;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.clayminer.data.Constants;
import scripts.clayminer.data.Vars;
import scripts.clayminer.framework.Node;
import scripts.clayminer.framework.Validators;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GClicking;

public class LeaveMine extends HandleRoots implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The LeaveMine Node has been Validated! Executing...");

       WebWalking.walkTo(Constants.INSIDE_MINE_ROOT_TILE);
       passRoots();
    }

    @Override
    public boolean validate()
    {
        return (Inventory.isFull() && Validators.isInMine());
    }
}
