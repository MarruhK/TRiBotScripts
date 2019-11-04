package scripts.dmmrcer.nodes.runner;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSPlayer;
import scripts.dmmrcer.data.Constants;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.PlayerEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.trading.GTrade;
import scripts.gengarlibrary.trading.GTradeItem;

public class Trade implements Node
{
    // Trade rcer
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The Trade Node has been Validated! Executing...");

        RSPlayer player = Entities.find(PlayerEntity::new).nameEquals(Vars.rcerName).getFirstResult();

        if (player == null)
        {
            System.out.println("execute: Mule is not nearby, will wait and try again.");
            Antiban.get().timedActions();
            return;
        }

        final int essenceCount = Inventory.getCount(Constants.PURE_ESSENCE_ID);
        GTradeItem[] offerItems = {new GTradeItem(Constants.PURE_ESSENCE_ID, 0)};
        GTradeItem[] acceptItems = {new GTradeItem(Vars.rune.getId(), 0)};

        if (GTrade.tradePlayer(Vars.rcerName, true, offerItems, acceptItems))
        {
            System.out.println("execute: Successfully traded rcer and gave essence.");
            Vars.runner.incrementEssenceTrade(essenceCount);
        }

        Timing.waitCondition(()->
        {
            General.sleep(155, 266);
            return Inventory.find(Constants.PURE_ESSENCE_ID).length == 0;
        }, General.random(3200, 4100));
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
