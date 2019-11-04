package scripts.clayminer.nodes;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSTile;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Node;
import scripts.clayminer.data.Vars;

public class BankOre implements Node {
    private final RSTile BANK_TILES = new RSTile(3093, 3245);


    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BankOre Node has been Validated! Executing...");

        Banking.openBank();
        Banking.depositAllExcept(Constants.PICKAXE_IDS);
        Banking.close();
    }

    @Override
    public boolean validate()
    {
        return (Inventory.isFull() && Banking.isInBank());
    }
}
