package scripts.gengarnmz.data.potions.combat.superpotions;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import scripts.gengarnmz.data.potions.NmzPotion;

public class SuperRangingPotion extends SuperPotion implements NmzPotion
{
    public SuperRangingPotion()
    {
        super();
        oneDoseId = 11725;
        twoDoseId = 11724;
        threeDoseId = 11723;
        fourDoseId = 11722;

        skill = Skills.SKILLS.RANGED;
        boostAmount = getBoostAmount();
        setValueToDrinkAt();
    }

    @Override
    public int getDoses()
    {
        final int dosesOneDose =     Inventory.getCount(oneDoseId);
        final int dosesTwoDose =     Inventory.getCount(twoDoseId) * 2;
        final int dosesThreeDose =   Inventory.getCount(threeDoseId) * 3;
        final int dosesFourDose =    Inventory.getCount(fourDoseId) * 4;

        return dosesOneDose + dosesTwoDose + dosesThreeDose + dosesFourDose;
    }

    @Override
    public int getCost()
    {
        return 250;
    }

    @Override
    public boolean shouldDrinkPotion()
    {
        if (Skills.getCurrentLevel(Skills.SKILLS.RANGED) <= valueToDrinkAt)
        {
            System.out.println("SuperRangingPotion - shouldDrinkPotion: Going to take a sip of the potion.");
            return true;
        }

        return false;
    }

    @Override
    protected int getBoostAmount()
    {
        return getSuperPotionBoost(Skills.getActualLevel(Skills.SKILLS.RANGED));
    }

    @Override
    public String toString()
    {
        return "Super Ranging Potion";
    }
}
