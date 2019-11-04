package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarnmz.antiban.Antiban;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.data.potions.combat.CombatPotion;
import scripts.gengarnmz.data.potions.combat.superpotions.SuperRangingPotion;
import scripts.gengarnmz.data.potions.utility.PrayerPotion;
import scripts.gengarnmz.data.potions.utility.UtilityPotion;
import scripts.gengarnmz.data.weapons.Blowpipe;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class Dreaming extends Node
{
    private RSTile centerTile;
    private CombatPotion combatPotion = new SuperRangingPotion();
    private UtilityPotion utilityPotion = new PrayerPotion();


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

        while (Validators.isDreaming())
        {
            General.sleep(100);

            if (shouldSuicide())
            {
                suicide();
            }

            checkRepotConditions();
            Antiban.get().timedActions();
        }

        // null so next dream we can get center tile
        centerTile = null;
    }

    private boolean shouldSuicide()
    {
        return (Inventory.find(Vars.combatPotion.getIds()).length <= 0 && Vars.weapon.getSkill().getCurrentLevel() == Vars.weapon.getSkill().getActualLevel()) ||
               (Vars.weapon instanceof Blowpipe && (Vars.outOfScales || Vars.outOfDarts));
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
            System.out.println("walkToCenter: Successfully walked to center tile");
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

    private void checkRepotConditions()
    {
        if (combatPotion.shouldDrinkPotion())
        {
            combatPotion.drinkPotion();
            ensureIsInCombat();
        }

        if (utilityPotion.shouldDrinkPotion())
        {
            utilityPotion.drinkPotion();
            ensureIsInCombat();
        }
    }

    /**
     * Player at times may trap himself in between black demons while not getting attacked. This resets agro.
     */
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

    //TODO Add rapid heal and dwarven rock cake report

    @Override
    public boolean validate()
    {
        return Validators.isDreaming();
    }
}

