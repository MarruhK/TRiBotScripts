package scripts.cooker.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.cooker.data.Vars;
import scripts.cooker.framework.Node;
import scripts.gengarlibrary.GBanking;

public class Bank implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The Bank Node has been Validated! Executing...");
        GBanking.openBank();
        GBanking.depositAll();
        GBanking.withdrawItem(Vars.rawFoodId);
        GBanking.closeBank();
    }

    @Override
    public boolean validate()
    {
        return Vars.bankArea.contains(Player.getPosition()) && Inventory.find(Vars.rawFoodId).length <= 0;
    }
}
