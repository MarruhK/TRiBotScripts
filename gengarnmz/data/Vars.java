package scripts.gengarnmz.data;

import org.tribot.api2007.Skills;
import scripts.gengarnmz.data.potions.combat.CombatPotion;
import scripts.gengarnmz.data.potions.utility.UtilityPotion;
import scripts.gengarnmz.data.weapons.Weapon;

public abstract class Vars
{
    private Vars(){}

    public static boolean shouldExecute = false;

    // BSS Vars

    // Blowpipe Vars
    public static boolean outOfDarts = false;
    public static boolean outOfScales = false;
    public static boolean isFullDarts = false;
    public static boolean isFullScales = false;

    // Nmz Vars
    public static boolean isCofferEmpty = false;
    public static boolean isBarrelEmpty = false;    // Used for concurrently determining which barrel is empty.
    public static boolean isBarrelEmptyCombat = false;
    public static boolean isBarrelEmptyAbsorp = false;

    // GUI Vars
    public static CombatPotion combatPotion;
    public static UtilityPotion utilityPotion;
    public static Weapon weapon;
    public static Skills.SKILLS skill;
}
