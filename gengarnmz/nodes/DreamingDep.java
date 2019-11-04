/*
package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.algorithms.NormalDistribution;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;
import scripts.gengarnmz.antiban.scripts.gengarlibrary.Antiban;

public class Dreaming extends Node
{
    private double[] rangeOfPrayerLvls;
    private int prayerLvlToPot;
    private int combatLvlToPot;

    private RSTile centerTile;

    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Dreaming initiated... Executing...");

        do
        {
            General.sleep(150);
            walkToCenter();
        } while (!isInCenter());

        activateProtectionPrayer();
        initializePotionLevels();

        while (Validators.isDreaming())
        {
            General.sleep(100);

            if (shouldSuicide())
            {
                suicide();
            }

            checkRepotConditions();
            scripts.gengarlibrary.Antiban.get().timedActions();
        }

        // null so next dream we can get center tile
        centerTile = null;
    }

    private boolean shouldSuicide()
    {
        return (Inventory.find(Vars.combatPotion.getIds()).length <= 0 && Vars.getCurrentLevel() == Vars.skill.getActualLevel()) ||
               (Vars.isRanging && (Vars.outOfScales || Vars.outOfDarts));
    }

    private void suicide()
    {
        Prayer.disable(Prayer.PRAYERS.PROTECT_FROM_MELEE);
    }

    private void walkToCenter()
    {
        // Run like 15 steps north and like 3 west
        if (centerTile == null)
        {
            RSTile currentTile = Player.getPosition();
            centerTile = new RSTile(currentTile.getX() - 3, currentTile.getY() + 15, currentTile.getPlane());
        }

        if (WebWalking.walkTo(centerTile) &&
            Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(8200, 9000)))
        {
            System.out.println("walkToCenter: Sucessfully walked to center tile");
        }
    }

    private boolean isInCenter()
    {
        // Better method for this rather than assuming first interaction in dream is correct, read TB thread i made for responses
        return Player.getPosition().distanceTo(centerTile) <= 4;
    }

    private void activateProtectionPrayer()
    {
        Prayer.enable(Prayer.PRAYERS.PROTECT_FROM_MELEE);
        General.sleep(250, 475);
    }

    private void initializePotionLevels()
    {
        // Initialize prayer
        final int restoreAmountOfPrayPot = 7 + (Skills.SKILLS.PRAYER.getActualLevel() / 4);
        final int maxPrayerLvlToPot = Skills.SKILLS.PRAYER.getActualLevel() - restoreAmountOfPrayPot;
        System.out.println("initializePotionLevels: Amount of prayer restored by prayer pot: " + restoreAmountOfPrayPot);
        System.out.println("initializePotionLevels: Maximum level bot should drink prayer pot: " + maxPrayerLvlToPot);

        // Create range of prayer levels.
        double[] prayLvls = new double[maxPrayerLvlToPot];

        for (int i = 0; i < prayLvls.length; i++)
        {
            prayLvls[i] = i+1;
        }

        rangeOfPrayerLvls = prayLvls;
        resetPrayerLvlToPot();
        resetCombatLvlToPot();
    }

    private void resetPrayerLvlToPot()
    {
        int min = (int) rangeOfPrayerLvls[0];
        int max = (int) rangeOfPrayerLvls[rangeOfPrayerLvls.length - 1];
        System.out.println("resetPrayerLvlToPot: min: " + min);
        System.out.println("resetPrayerLvlToPot: max: " + max);

        prayerLvlToPot = NormalDistribution.getGeneratedRandomNormalValue(rangeOfPrayerLvls, min, max);
        System.out.println("resetPrayerLvlToPot: New prayer point to restore at: " + prayerLvlToPot);
    }

    private void resetCombatLvlToPot()
    {
        combatLvlToPot = Vars.isRanging ? getRangingPotionBoost() : getSuperPotionBoost(Skills.SKILLS.STRENGTH.getActualLevel());
        System.out.println("resetCombatLvlToPot: New combat lvl to repot is: " + this.combatLvlToPot);
    }

    // Add attack training
    private int getSuperPotionBoost(int combatLvl)
    {
        int potBoost = (5 + (int) (0.15 * combatLvl));
        double[] data = {combatLvl + potBoost - 7, combatLvl + potBoost - 6, combatLvl + potBoost - 5, combatLvl + potBoost - 4};
        return NormalDistribution.getGeneratedRandomNormalValue(data, combatLvl + potBoost - 7, combatLvl + potBoost - 4);
    }

    private int getRangingPotionBoost()
    {
        int rangedLvl = Skills.SKILLS.RANGED.getActualLevel();

        if (Inventory.find(Constants.ID_SUPER_RANGING_POTIONS).length > 0)
            return getSuperPotionBoost(rangedLvl);

        int potBoost;

        if (Skills.SKILLS.RANGED.getActualLevel() >= 90)
        {
            potBoost = 13;
        }
        else if (Skills.SKILLS.RANGED.getActualLevel() >= 80)
        {
            potBoost = 12;
        }
        else
        {
            potBoost = 11;
        }

        double[] data = {rangedLvl + potBoost - 7, rangedLvl + potBoost - 6, rangedLvl + potBoost - 5, rangedLvl + potBoost - 4};
        return NormalDistribution.getGeneratedRandomNormalValue(data, rangedLvl + potBoost - 7, rangedLvl + potBoost - 4);
    }

    private void checkRepotConditions()
    {
        if (Validators.isDreaming())
        {
            if (shouldDrinkCombatPotion())
            {
                drinkCombatPotion();
            }

            if (shouldDrinkPrayerPotion())
            {
                drinkPrayerPotion();
            }
        }
    }

    private boolean shouldDrinkPrayerPotion()
    {
        return  Inventory.find(Constants.ID_PRAYER_POTIONS).length > 0 &&
                Skills.SKILLS.PRAYER.getCurrentLevel() <= prayerLvlToPot;
    }

    private void drinkPrayerPotion()
    {
        System.out.println("drinkPrayerPotion: Going to drink potion, if any exist.");
        RSItem[] prayerPotions = Inventory.find(Constants.ID_PRAYER_POTIONS);

        if (prayerPotions.length > 0)
        {
            int currentLvl = Skills.getCurrentLevel(Skills.SKILLS.PRAYER);
            prayerPotions[0].click();

            Timing.waitCondition(GBooleanSuppliers.isPotionSipped(currentLvl, Skills.SKILLS.PRAYER), General.random(1250, 1300));

            // Placed here because player's only realize this occured when re-potting
            ensureIsInCombat();
            resetPrayerLvlToPot();
        }
    }

    private boolean shouldDrinkCombatPotion()
    {
        return  Inventory.find(Constants.ID_COMBAT_POTIONS).length > 0 &&
                (Vars.isRanging && Skills.SKILLS.RANGED.getCurrentLevel() <= combatLvlToPot ||
                !Vars.isRanging && Skills.SKILLS.STRENGTH.getCurrentLevel() <= combatLvlToPot);
    }

    private void drinkCombatPotion()
    {
        System.out.println("drinkCombatPotion: Going to drink potion, if any exist.");
        RSItem[] combatPotions = Inventory.find(Constants.ID_COMBAT_POTIONS);

        if (combatPotions.length > 0)
        {
            int currentLvl = Skills.getCurrentLevel(Vars.skill);
            combatPotions[0].click();

            Timing.waitCondition(GBooleanSuppliers.isPotionSipped(currentLvl, Vars.skill), General.random(1250, 1300));

            // Placed here because player's only realize this occured when re-potting
            ensureIsInCombat();
            resetCombatLvlToPot();
        }
    }

    */
/**
     * Player at times may trap himself in between black demons while not getting attacked. This resets agro.
     *//*

    private void ensureIsInCombat()
    {
        if (!Player.getRSPlayer().isInCombat())
        {
            System.out.println("ensureIsInCombat: Player is not in combat, fixing...");
            RSNPC[] mobs = NPCs.find("Black Demon");

            if (mobs.length > 0)
            {
                // Walk under him
                RSNPC mob = mobs[0];

                if (WebWalking.walkTo(mob.getPosition()))
                {
                    mob.click();
                }
            }
        }
    }

    @Override
    public boolean validate()
    {
        return Validators.isDreaming();
    }
}
*/
