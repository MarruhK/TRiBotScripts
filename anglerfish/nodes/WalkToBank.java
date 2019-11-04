package scripts.anglerfish.nodes;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import scripts.anglerfish.data.Constants;
import scripts.anglerfish.data.Vars;
import scripts.anglerfish.framework.Node;
import scripts.anglerfish.data.Methods;

public class WalkToBank extends Node {

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToBank Node has been Validated! Executing...");

        if (WebWalking.walkTo(Constants.BANK_TILE)){
            Methods.waitToStop(13500, 15000);
        }

        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (Vars.shouldExecute &&
                Inventory.isFull() &&
                !Banking.isInBank());
    }
}
