package scripts.gengarnmz.data.potions.combat.superpotions;

import scripts.gengarnmz.data.potions.combat.CombatPotion;

public abstract class SuperPotion extends CombatPotion
{
    protected int getSuperPotionBoost(int combatLvl)
    {
        return (5 + (int) (0.15 * combatLvl));
    }

    public SuperPotion()
    {
        super();
    }
}
