package scripts.dmmrelekkafisher.nodes;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;
import scripts.gnomefisher.data.Methods;

public class GoCammyBank implements Node {
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The GoCammyBank Node has been Validated! Executing...");


        if (WebWalking.walkTo(Constants.CAMMY_BANK_TILE)){
            System.out.println("GoCammyBank: Walking to the bank.");
            Methods.waitToStopMoving(19000, 19100);
        }
    }

    @Override
    public boolean validate() {
        return Inventory.find(Vars.fishingEquipmentID).length == 0 &&
                !Constants.CAMMY_BANK_AREA.contains(Player.getPosition());
    }
}
