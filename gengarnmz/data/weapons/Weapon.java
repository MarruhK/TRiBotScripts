package scripts.gengarnmz.data.weapons;

import org.tribot.api2007.Skills;
import scripts.gengarnmz.data.AttackStyles;

import java.util.HashMap;
import java.util.Set;

public abstract class Weapon
{
    protected int[] ids;
    protected String attackStyle;
    protected Skills.SKILLS skill;
    protected HashMap<String, Integer> combatStyleIndexMapping = new HashMap<>();

    public int getCombatStyleIndex(String style)
    {
        Integer indexStyle = combatStyleIndexMapping.get(style);

        if (indexStyle != null)
        {
            return indexStyle;
        }

        return -1;
    }

    public String getAttackStyle()
    {
        return attackStyle;
    }

    public Skills.SKILLS getSkill()
    {
        return this.skill;
    }

    public int[] getIds()
    {
        return ids;
    }

    public void setAttackStyle(String selectedStyle)
    {
        this.attackStyle = selectedStyle;
    }

    public void setSkill(Skills.SKILLS skill)
    {
        this.skill = skill;
    }
}
