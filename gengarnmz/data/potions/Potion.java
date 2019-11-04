package scripts.gengarnmz.data.potions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSItem;
import scripts.gengarlibrary.GBooleanSuppliers;

public abstract class Potion
{
    protected int regularDoseAmount;
    protected int valueToDrinkAt;
    protected int boostAmount;
    protected int oneDoseId;
    protected int twoDoseId;
    protected int threeDoseId;
    protected int fourDoseId;
    protected int[] ids;
    protected double[] rangeOfLvls;

    public Potion()
    {
        this.ids = new int[]{fourDoseId, threeDoseId, twoDoseId, oneDoseId};
    }

    public abstract boolean shouldDrinkPotion();
    protected abstract int getBoostAmount();
    protected abstract void setValueToDrinkAt();

    public void drinkPotion()
    {
        RSItem[] potions = Inventory.find(ids);

        if (potions.length <= 0)
        {
            System.out.println("drinkPotion: Unable to find potions in Inventory, returning...");
            return;
        }

        potions[0].click();
    }

    public int[] getIds()
    {
        return ids;
    }

    public int getRegularDoseAmount()
    {
        return regularDoseAmount;
    }
}
