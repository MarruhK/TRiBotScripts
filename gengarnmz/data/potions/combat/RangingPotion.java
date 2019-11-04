package scripts.gengarnmz.data.potions.combat;

import org.tribot.api2007.Skills;
import scripts.gengarlibrary.algorithms.NormalDistribution;

public class RangingPotion extends CombatPotion
{
    public RangingPotion()
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
    public boolean shouldDrinkPotion()
    {
        if (Skills.getCurrentLevel(Skills.SKILLS.RANGED) <= valueToDrinkAt)
        {
            System.out.println("RangingPotion - shouldDrinkPotion: Going to take a sip of the potion.");
            return true;
        }

        return false;
    }

    @Override
    protected int getBoostAmount()
    {
        if (Skills.SKILLS.RANGED.getActualLevel() >= 90)
        {
            boostAmount = 13;
        }
        else if (Skills.SKILLS.RANGED.getActualLevel() >= 80)
        {
            boostAmount = 12;
        }
        else
        {
            boostAmount = 11;
        }

        int rangedLvl = Skills.getActualLevel(Skills.SKILLS.RANGED);
        int totalCombatLevel = rangedLvl + boostAmount;
        int low = totalCombatLevel - 7;
        int high = totalCombatLevel - 4;

        double[] data = {low, totalCombatLevel - 6, totalCombatLevel - 5, rangedLvl + high};
        return NormalDistribution.getGeneratedRandomNormalValue(data, low, high);
    }

    @Override
    public String toString()
    {
        return "Ranging Potion";
    }
}
