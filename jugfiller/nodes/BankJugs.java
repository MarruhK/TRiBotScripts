package scripts.jugfiller.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.gengarlibrary.GBanking;
import scripts.jugfiller.data.Constants;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;
import scripts.jugfiller.framework.Validators;

public class BankJugs extends Node{

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BankJugs Node has been Validated! Executing...");

        if (!GBanking.openBank())
        {
            System.out.println("execute: Failed to open bank..");
            Banking.close();
            return;
        }

        if (Inventory.getAll().length > 0 && !GBanking.depositAll())
        {
            System.out.println("execute: Failed to deposit items, returning..");
            Banking.close();
            return;
        }


        RSItem[] jugs = Banking.find(Constants.EMPTY_JUG_ID);

        if (jugs.length == 0)
        {
            System.out.println("execute: Out of Jugs, going to buy them.");
            Vars.isOutOfJugs = true;

            RSItem[] coins = Banking.find(Constants.COINS_ID);

            if (coins.length == 0)
            {
                System.out.println("execute: Out of coins, ending...");
                Vars.isOutOfCoins = true;
                return;
            }

            do
            {
                Banking.withdraw(0, Constants.COINS_ID);
                Timing.waitCondition(()->
                {
                    General.sleep(100);
                    return Inventory.find(Constants.COINS_ID).length > 0;
                }, General.random(750, 800));
            } while (Inventory.find(Constants.COINS_ID).length <= 0);

            Banking.close();
            return;
        }

        // Get amount of jugs left to fill
        GBanking.withdrawItem(Constants.EMPTY_JUG_ID);
        Banking.close();
    }

    @Override
    public boolean validate()
    {
        return !Vars.isOutOfCoins  &&
                Validators.isInBank()   &&
                Inventory.find(Constants.EMPTY_JUG_ID).length <= 0;
    }
}
