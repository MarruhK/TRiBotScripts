package scripts.gengarnmz.data.potions.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Skills;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.algorithms.NormalDistribution;
import scripts.gengarnmz.data.potions.Potion;

// TODO On skill level up, need to reset boostAmount
public abstract class CombatPotion extends Potion
{
    protected Skills.SKILLS skill;

    public CombatPotion()
    {
        super();
        this.regularDoseAmount = 24;
    }

    @Override
    protected void setValueToDrinkAt()
    {
        // TODO More efficient method of getting str level would be to cache it and have a listener for when you level to update cache
        int totalCombatLevel = Skills.getActualLevel(skill) + boostAmount;
        int low = totalCombatLevel - 7;
        int high = totalCombatLevel - 4;

        if (rangeOfLvls == null)
        {
            cacheRangeOfLvls(totalCombatLevel);
        }

        valueToDrinkAt = NormalDistribution.getGeneratedRandomNormalValue(rangeOfLvls, low, high);
        System.out.println("CombatPotion - setValueToDrinkAt: New value to drink at: " + valueToDrinkAt);
    }

    @Override
    public void drinkPotion()
    {
        final int currentLvl = Skills.getCurrentLevel(skill);
        super.drinkPotion();
        Timing.waitCondition(GBooleanSuppliers.isPotionSipped(currentLvl, skill), General.random(1250, 1400));
    }

    private void cacheRangeOfLvls(int totalCombatLevel)
    {
        rangeOfLvls = new double[4];
        int counter = 7;

        for (int i = 0; i < 4; i++)
        {
            rangeOfLvls[i] = totalCombatLevel - i - counter++;
        }
    }
}
