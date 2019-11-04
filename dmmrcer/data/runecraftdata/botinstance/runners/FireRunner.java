package scripts.dmmrcer.data.runecraftdata.botinstance.runners;

import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dmmrcer.data.Constants;
import scripts.dmmrcer.data.Vars;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GBanking;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.teleports.DuelingRing;

public class FireRunner extends Runner
{
    private static final int TELE_TIME = 2400;

    private static final RSTile CATLESWARS_TILE_1 = new RSTile(2457, 3109, 0);
    private static final RSTile CATLESWARS_TILE_2 = new RSTile(2444, 3090, 0);
    private static final RSArea CASTLEWARS_AREA = new RSArea(CATLESWARS_TILE_1, CATLESWARS_TILE_2);

    private static final RSTile DUELARENA_TILE_1 = new RSTile(3302, 3261, 0);
    private static final RSTile DUELARENA_TILE_2 = new RSTile(3328, 3225, 0);
    private static final RSArea DUELARENA_AREA = new RSArea(DUELARENA_TILE_1, DUELARENA_TILE_2);

    @Override
    public boolean shouldBank()
    {
        // Validator already checks u have no essence
        RSObject bankChest = Entities.find(ObjectEntity::new).nameContains("Bank chest").getFirstResult();
        return bankChest != null;
    }

    @Override
    public boolean shouldWalkToRcer()
    {
        // Validator already checks if you have essence
        return Objects.find(10, Vars.altar.getAltarId()).length == 0 && DuelingRing.getCharges() > 0;
    }

    @Override
    public void walkToBank()
    {
        // Tele to castle wars and bank
        DuelingRing.teleport(DuelingRing.Teleports.CASTLWARS);

        Antiban.get().generateTrackers(TELE_TIME);
        Antiban.get().sleepReactionTime(TELE_TIME);
    }

    @Override
    public void walkToMule()
    {

    }

    @Override
    public void walkToRcer()
    {
        if (Vars.altar.getRuinsTile().distanceTo(Player.getPosition()) > 50 && !handleDuelRingTele())
        {
            System.out.println("walkToRcer: Failed to teleprot to DA");
            return;
        }

        Antiban.get().generateTrackers(TELE_TIME);
        Antiban.get().sleepReactionTime(TELE_TIME);

        System.out.println("walkToRcer: Walking to ruin tile at: " + Vars.altar.getRuinsTile().toString());

        if (!WebWalking.walkTo(Vars.altar.getRuinsTile()))
        {
            System.out.println("walkToRcer: Failed to walk to altar");
            return;
        }

        Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), 19000);

        if (enterRuins())
        {
            // Walk to altar
            WebWalking.walkTo(Vars.altar.getAltarTile());
            Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), 10000);
        }
    }

    @Override
    public void bank()
    {
        // Dueling ring must have at minimum two charges.
        boolean shouldWithdrawRing = false;

        if (DuelingRing.getCharges() < 2)
        {
            shouldWithdrawRing = true;
            // Unequip if equipped so we can bank it.
            if (Equipment.isEquipped(Constants.DUELING_RINGS_ID))
            {
                Equipment.remove(Equipment.SLOTS.RING);
                Timing.waitCondition(GBooleanSuppliers.isItemInInventory(Constants.DUELING_RINGS_ID), 1800);
            }
        }

        if (GBanking.openBank())
        {
            if (isUsingTalisman)
            {
                GBanking.depositAllExcept(Vars.altar.getTalismanId());
            }
            else
            {
                GBanking.depositAll();
            }

            if (!GBanking.withdrawItem(25, Constants.PURE_ESSENCE_ID))
            {
                System.out.println("bank: No pure essence found, returning false");
                Vars.shouldExecute = false;
                return;
            }

            if (shouldWithdrawRing && !GBanking.withdrawItem(1, Constants.DUELING_RINGS_ID[7]))
            {
                System.out.println("bank: No ring of duels found, returning false");
                Vars.shouldExecute = false;
                return;
            }

            GBanking.closeBank();
        }


    }

    private boolean handleDuelRingTele()
    {
        if (DuelingRing.getCharges() >= 2)
        {
            return DuelingRing.teleport(DuelingRing.Teleports.DUEL_ARENA);
        }
        else
        {
            if (DuelingRing.teleport(DuelingRing.Teleports.CASTLWARS))
            {
                System.out.println("handleDuelRingTele: teleporting to castlewards to get new ring.");
                bank();
            }

            return false;
        }
    }
}
