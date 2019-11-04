package scripts.dmmrcer.nodes.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.dmmrcer.framework.Validator;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;

public class CraftRunes implements Node
{
    private static final int RC_ANIMATION = 791;

    // Filling statistics; used in ABC2
    private long averageRcWaitTime = 2400;
    private long totalRcWaitTime;
    private long totalRcInstances;

    private void updateStats(long waitTime)
    {
        totalRcWaitTime += waitTime;
        averageRcWaitTime = totalRcWaitTime / ++totalRcInstances;

        System.out.println("updateFillingStatistics: Average wait = " + averageRcWaitTime);
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The CraftRunes Node has been Validated! Executing...");

        RSTile tile = Vars.altar.getAltarTile();
        int distance = tile.distanceTo(Player.getPosition());

        if (distance > 7)
        {
            WebWalking.walkTo(tile);
            Timing.waitCondition(GBooleanSuppliers.waitForAnimation(-1), General.random(2500, 3000));
        }

        // The altar has some fucked up actions and name, idk why
        RSObject[] altars = Objects.find(15, Vars.altar.getAltarId());

        if (altars.length == 0)
        {
            System.out.println("execute: Unable to find altar...");
            return;
        }

        RSObject altar = altars[0];
        long startTime = Timing.currentTimeMillis();

        if (!GClicking.clickObject(altar, RC_ANIMATION))
        {
            System.out.println("execute: Failed to click altar... Walking to it.");
            WebWalking.walkTo(altar.getPosition());
            return;
        }

        Antiban.get().generateTrackers((int)averageRcWaitTime);

        Timing.waitCondition(GBooleanSuppliers.waitForAnimation(-1), General.random(2500, 3000));

        updateStats(Timing.currentTimeMillis() - startTime);
        Antiban.get().sleepReactionTime((int)averageRcWaitTime);
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
