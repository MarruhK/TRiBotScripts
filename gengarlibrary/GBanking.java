package scripts.gengarlibrary;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;

public class GBanking
{
    private static RSInterfaceMaster bankInterface = Interfaces.get(12);

    public static boolean openBank()
    {
        if (Banking.openBank())
        {
            return Timing.waitCondition(()->
            {
                General.sleep(125);
                return Banking.isBankScreenOpen();
            }, General.random(1850, 1950));
        }

        return false;
    }

    public static boolean depositAll()
    {
        Banking.depositAll();

        return Timing.waitCondition(()->
        {
           General.sleep(150);
           return Inventory.getAll().length <= 0;
        }, General.random(1250, 1300));
    }

    public static boolean depositAllExcept(int... ids)
    {
        Banking.depositAllExcept(ids);

        return Timing.waitCondition(()->
        {
            General.sleep(150);
            return Inventory.getAll().length <= ids.length;
        }, General.random(1250, 1300));
    }

    public static boolean closeBank()
    {
        if (Banking.close())
        {
            return Timing.waitCondition(()->
            {
                General.sleep(125);
                return !Banking.isBankScreenOpen();
            }, General.random(1850, 1950));
        }

        return false;
    }

    public static boolean withdrawItem(int id)
    {
        return withdrawItem(0, id);
    }

    public static boolean withdrawItem(int count, int id)
    {
        if (!Banking.isBankScreenOpen())
        {
            System.out.println("withdrawItem: Bank screen isn't even open?");
            return false;
        }

        int checkCount = (int) ((Math.random() * 5) + 3);

        // Continously check it in case false negative
        for (int i = 0; i < checkCount; i++)
        {
            Banking.withdraw(count, id);

            if (Timing.waitCondition(()->
            {
                General.sleep(100);
                return Inventory.find(id).length >= count;
            }, 800))
            {
                break;
            }
        }

        // Failed to withdraw item
        if (Inventory.find(id).length < count)
        {
            System.out.println("withdrawItem: Item does not exist");
            closeBank();
            return false;
        }

        return true;
    }

    public static boolean isBankNotedOn()
    {
        if (bankInterface.isValid())
        {
            if (Game.getSetting(115) == 1)
            {
                System.out.println("isBankNotedOn: Bank setting: Noted set ON currently");
                return true;
            }
        }
        System.out.println("isBankNotedOn: Bank setting: Noted set OFF currently.");
        return false;
    }

    public static boolean setBankNotedOn()
    {
        if (bankInterface.isValid())
        {
            // EdgeBanking is open so check if it noted is selected.
            if (!isBankNotedOn()){
                // Turn noted setting on.
                bankInterface.getChild(24).click();

                if(Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return isBankNotedOn();
                    }
                }, General.random(1000,1100))){
                    System.out.println("EdgeBanking noted setting is on now.");
                    return true;
                } else {
                    System.out.println("Failed to set on noted.");
                    return false;
                }
            }
        }
        System.out.println("GBanking window isn't even opened, leaving.");
        return false;
    }
}
