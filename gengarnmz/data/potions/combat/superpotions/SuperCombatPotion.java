package scripts.gengarnmz.data.potions.combat.superpotions;

import org.tribot.api2007.Skills;

public class SuperCombatPotion extends SuperPotion
{
    public SuperCombatPotion()
    {
        super();
        oneDoseId = 12701;
        twoDoseId = 12699;
        threeDoseId = 12697;
        fourDoseId = 12695;

        skill = Skills.SKILLS.STRENGTH;
        boostAmount = getBoostAmount();
        setValueToDrinkAt();
    }

    @Override
    public boolean shouldDrinkPotion()
    {
        if (Skills.getCurrentLevel(Skills.SKILLS.STRENGTH) <= valueToDrinkAt)
        {
            System.out.println("SuperCombatPotion - shouldDrinkPotion: Going to take a sip of the potion.");
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
        return "Super Combat Potion";
    }
}
