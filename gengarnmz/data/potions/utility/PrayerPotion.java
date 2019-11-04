package scripts.gengarnmz.data.potions.utility;

import org.tribot.api2007.Skills;
import scripts.gengarlibrary.algorithms.NormalDistribution;

public class PrayerPotion extends UtilityPotion
{
    public PrayerPotion()
    {
        super();
        oneDoseId = 143;
        twoDoseId = 141;
        threeDoseId = 139;
        fourDoseId = 2434;

        boostAmount = getBoostAmount();
        setValueToDrinkAt();
    }

    @Override
    public boolean shouldDrinkPotion()
    {
        if (Skills.getCurrentLevel(Skills.SKILLS.PRAYER) <= valueToDrinkAt)
        {
            System.out.println("PrayerPotion - shouldDrinkPotion: Going to take a sip of the potion.");
            return true;
        }

        return false;
    }

    @Override
    protected int getBoostAmount()
    {
        return 7 + (Skills.SKILLS.PRAYER.getActualLevel() / 4);
    }

    @Override
    protected void setValueToDrinkAt()
    {
        if (rangeOfLvls == null)
        {
            cacheRangeOfLvls();
        }

        int min = (int) rangeOfLvls[0];
        int max = (int) rangeOfLvls[rangeOfLvls.length - 1];

        valueToDrinkAt = NormalDistribution.getGeneratedRandomNormalValue(rangeOfLvls, min, max);
        System.out.println("PrayerPotion - setValueToDrinkAt: New value to drink at: " + valueToDrinkAt);
    }

    private void cacheRangeOfLvls()
    {
        int maxPrayerLvlToPotAt = Skills.SKILLS.PRAYER.getActualLevel() - boostAmount;

        // Create range of prayer levels.
        rangeOfLvls = new double[maxPrayerLvlToPotAt];

        for (int i = 0; i < rangeOfLvls.length; i++)
        {
            rangeOfLvls[i] = i+1;
        }
    }

    @Override
    public String toString()
    {
        return "Prayer Potion";
    }
}
