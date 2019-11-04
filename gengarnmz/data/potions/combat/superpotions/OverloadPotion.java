package scripts.gengarnmz.data.potions.combat.superpotions;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import scripts.gengarnmz.data.potions.NmzPotion;

public class OverloadPotion extends SuperPotion implements NmzPotion
{
    public OverloadPotion()
    {
        super();
        oneDoseId = 11733;
        twoDoseId = 11732;
        threeDoseId = 11731;
        fourDoseId = 11730;

        skill = Skills.SKILLS.STRENGTH;
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
        return 1500;
    }

    @Override
    public boolean shouldDrinkPotion()
    {
        // TODO Generate a sleep so we wait to drink another overload via abc
        if (Skills.getCurrentLevel(Skills.SKILLS.STRENGTH) <= valueToDrinkAt)
        {
            System.out.println("OverloadPotion - shouldDrinkPotion: Going to take a sip of the potion.");
            return true;
        }

        return false;
    }

    @Override
    protected int getBoostAmount()
    {
        return getSuperPotionBoost(Skills.getActualLevel(Skills.SKILLS.STRENGTH));
    }

    @Override
    public String toString()
    {
        return "Overload Potion";
    }
}
