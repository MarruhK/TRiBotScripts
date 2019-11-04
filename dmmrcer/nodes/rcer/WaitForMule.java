package scripts.dmmrcer.nodes.rcer;

import org.tribot.api.General;
import org.tribot.api2007.types.RSPlayer;
import scripts.dmmrcer.data.Constants;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.dmmrcer.framework.Validator;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.PlayerEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.trading.GTrade;
import scripts.gengarlibrary.trading.GTradeItem;

public class WaitForMule implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WaitForMule Node has been Validated! Executing...");

        RSPlayer player = Entities.find(PlayerEntity::new).nameEquals(Vars.muleName).getFirstResult();

        if (player == null)
        {
            System.out.println("execute: Mule is not nearby, will wait and try again.");
            Antiban.get().timedActions();
            return;
        }

        GTradeItem[] offerItems = {new GTradeItem(Vars.rune.getId(), 0)};
        GTradeItem[] acceptItems = {new GTradeItem(Constants.PURE_ESSENCE_ID, 0)};

        if (GTrade.tradePlayer(Vars.muleName, true, offerItems, acceptItems))
        {
            System.out.println("execute: Successfully traded mule and got essence.");
        }
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
