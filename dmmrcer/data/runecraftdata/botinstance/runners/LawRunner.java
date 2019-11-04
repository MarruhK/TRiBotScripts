package scripts.dmmrcer.data.runecraftdata.botinstance.runners;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmrcer.data.Constants;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.data.runecraftdata.Runes;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.entityselector.finders.prefabs.PlayerEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GBanking;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;

public class LawRunner extends Runner

{
    private static final RSTile DRAYNOR_BANK_TILE = new RSTile(3092, 3243, 0);
    private static final RSTile ENTRANA_MONK_TILE = new RSTile(2835, 3336, 0);
    private static final RSTile PORT_SARIM_MONK_TILE = new RSTile(3047, 3235, 0);

    private static final RSTile ENTRANA_TILE_1 = new RSTile(2878, 3330, 0);
    private static final RSTile ENTRANA_TILE_2 = new RSTile(2800, 3390, 0);

    private static final RSArea ENTRANA_AREA = new RSArea(ENTRANA_TILE_1, ENTRANA_TILE_2);

    // Filling statistics; used in ABC2
    private long averageTravelTime = 11000;
    private long totalTravelWaitTime;
    private long totalTravelInstances;

    public LawRunner(){}

    private void updateFillingStatistics(long waitTime)
    {
        totalTravelWaitTime += waitTime;
        averageTravelTime = totalTravelWaitTime / ++totalTravelInstances;

        System.out.println("updateFillingStatistics: Average wait = " + averageTravelTime);
    }

    @Override
    public boolean shouldBank()
    {
        // is at bank
        return Banking.isInBank();
    }

    @Override
    public boolean shouldWalkToRcer()
    {
        // Not at the boat tiel
        return Player.getPosition().distanceTo(ENTRANA_MONK_TILE) > 5 || Player.getPosition().getPlane() == 1;
    }

    @Override
    public void walkToBank()
    {
        if (isInEntrana() && walkToAndWaitToStop(ENTRANA_MONK_TILE, 5000) && !travelMonk())
        {
            System.out.println("walkToRcer: Failed to handle preliminary walk to monk, returning");
            return;
        }

        walkToAndWaitToStop(DRAYNOR_BANK_TILE, 5000);
    }

    @Override
    public void walkToMule()
    {
        // Mule should be in PC bank useless atm
    }

    @Override
    public void walkToRcer()
    {
        if (!isInEntrana() && walkToAndWaitToStop(PORT_SARIM_MONK_TILE, 5000) && !travelMonk())
        {
            System.out.println("walkToRcer: Failed to handle preliminary walk to monk, returning");
            return;
        }

        if (Player.getPosition().getPlane() == 1)
            walkToAndWaitToStop(ENTRANA_MONK_TILE, 5000);
    }

    @Override
    public void bank()
    {
        if (GBanking.openBank() && Banking.isBankScreenOpen())
        {
            GBanking.depositAll();

            if (!GBanking.withdrawItem(25, Constants.PURE_ESSENCE_ID))
            {
                System.out.println("bank: No pure essence found, returning false");
                Vars.shouldExecute = false;
                return;
            }

            GBanking.closeBank();
        }
    }

    private boolean travelMonk()
    {
        RSNPC monk = Entities.find(NpcEntity::new).nameEquals("Monk of Entrana").getFirstResult();
        long startTime = Timing.currentTimeMillis();

        if (monk != null && GClicking.clickNpc(monk, "Take-boat"))
        {
            Antiban.get().generateTrackers((int)averageTravelTime);

            if (Timing.waitCondition(()->
            {
                General.sleep(250, 300);
                return Player.getPosition().getPlane() == 1;
            }, General.random(11600, 13030)))
            {
                updateFillingStatistics(Timing.currentTimeMillis() - startTime);
                Antiban.get().sleepReactionTime((int)averageTravelTime);

                return true;
            }
        }

        System.out.println("travelMonk: Failed to click the monk or find the monk.");
        return false;
    }

    private boolean walkToAndWaitToStop(RSTile tile, int timeout)
    {
        DaxWalker.walkTo(tile);
        return Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), timeout);
    }

    private boolean isInEntrana()
    {
        return ENTRANA_AREA.contains(Player.getPosition());
    }
}
