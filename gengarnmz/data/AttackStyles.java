package scripts.gengarnmz.data;

import org.tribot.api2007.Skills;

public enum AttackStyles
{
    ATTACK,
    STRENGTH,
    DEFENCE,
    CONTROLLED,
    ACCURATE_RANGE,
    RANGE,
    LONGRANGE,
    NONE;

    public static String styleToString(AttackStyles style)
    {
        switch (style)
        {
            case ATTACK:
                return "Attack";
            case STRENGTH:
                return "Strength";
            case DEFENCE:
                return "Defence";
            case CONTROLLED:
                return "Controlled";
            case ACCURATE_RANGE:
                return "Range";
            case RANGE:
                return "Range";
            case LONGRANGE:
                return "Long Range";
        }

        return "Null or Empty";
    }

    public static Skills.SKILLS styleToSkill(AttackStyles style)
    {
        switch (style)
        {
            case ATTACK:
                return Skills.SKILLS.ATTACK;
            case STRENGTH:
                return Skills.SKILLS.STRENGTH;
            case DEFENCE:
                return Skills.SKILLS.DEFENCE;
            case CONTROLLED:
                return Skills.SKILLS.STRENGTH;
            case ACCURATE_RANGE:
                return Skills.SKILLS.RANGED;
            case RANGE:
                return Skills.SKILLS.RANGED;
            case LONGRANGE:
                return Skills.SKILLS.RANGED;
        }

        return null;
    }
}
