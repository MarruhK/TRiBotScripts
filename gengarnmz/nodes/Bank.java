package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.data.potions.NmzPotion;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class Bank extends Node
{
    private boolean isUsingNmzCombatPot;
    private boolean isUsingNmzUtilityPot;

    public Bank()
    {
        this.isUsingNmzCombatPot = Vars.combatPotion instanceof NmzPotion;
        this.isUsingNmzUtilityPot = Vars.utilityPotion instanceof NmzPotion;
    }

    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Bank initiated... Executing...");

        if (openBank() && depositAll())
        {
            // Withdraw BP shit
            if (Vars.outOfScales || Vars.outOfDarts)
            {
                System.out.println("Bank: checking bank for zulrah scales / darts");

                if (Vars.outOfScales)
                {
                    withdrawItem(0, Constants.ZULRAH_SCALES_ID);
                }

                if (Vars.outOfDarts)
                {
                    withdrawItem(0, Constants.MITHRIL_DARTS_ID);
                }
            }

            // Handle empty coffer
            if (Vars.isCofferEmpty)
            {
                handleEmptyCoffer();
            }

            // Handle non-nmz potions
            if (!isUsingNmzCombatPot)
            {
                System.out.println("Bank: checking bank for combat potions...");
                withdrawItem(6, Vars.combatPotion.getIds()[0]);
            }

            if (!isUsingNmzUtilityPot)
            {
                System.out.println("Bank: checking bank for utility potions...");
                withdrawItem(22, Vars.utilityPotion.getIds()[0]);
            }

            Banking.close();
        }
    }

    private void handleEmptyCoffer()
    {
        final int coinsId = 995;
        RSItem[] coinsItems = Banking.find(coinsId);

        if (coinsItems.length > 0)
        {
            RSItem coinsItem = coinsItems[0];
            int coinsStack = coinsItem.getStack();

            if (coinsStack >= 26000)
            {
                int count = 26000 * (coinsStack / 26000);
                withdrawItem(count, coinsId);
                Banking.close();
                return;
            }
        }

        System.out.println("Bank: Coffer is empty and do not have enough gp for another NMZ session. Ending...");
        Vars.shouldExecute = false;
        Banking.close();
    }

    private boolean openBank()
    {
        Banking.openBank();

        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return Banking.isBankScreenOpen();
        }, 4000))
        {
            System.out.println("Bank: failed to open bank.");
            return false;
        }

        return true;
    }

    private boolean depositAll()
    {
        Banking.depositAll();

        if (!Timing.waitCondition(()->
        {
            General.sleep(100);
            return Inventory.getAll().length == 0;
        }, 4000))
        {
            System.out.println("Bank: failed to deposit all items.");
            return false;
        }

        return true;
    }

    private void withdrawItem(int count, int id)
    {
        RSItem[] items = Banking.find(id);

        if (items.length <= 0)
        {
            System.out.println("Bank: Out of either pots, scales or dart. Logging out.");
            Vars.shouldExecute = false;
            Banking.close();
            return;
        }

        Banking.withdraw(count, id);
        Timing.waitCondition(()->
        {
            General.sleep(100);
            return Inventory.find(id).length > 20;
        }, 4000);
    }

    @Override
    public boolean validate()
    {
        return  Validators.shouldBank();
    }
}
