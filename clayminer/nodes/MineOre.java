package scripts.clayminer.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Validators;
import scripts.clayminer.framework.Node;
import scripts.clayminer.data.Vars;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GBooleanSuppliers;

public class MineOre implements Node
{
    private RSObject nextTarget;

    private static final int CLAY_ROCK_ID = 11362;

    // Mining statistics; used in ABC2
    private long averageMiningWaitTime = 1800;
    private long totalMiningWaitTime;
    private long totalMiningInstances = 0;

    private void updateMiningStatistics(long waitTime)
    {
        totalMiningWaitTime += waitTime;
        averageMiningWaitTime = totalMiningWaitTime / ++totalMiningInstances;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The MineOre Node has been Validated! Executing...");

        oldMineOre();

    }

    private void oldMineOre()
    {
        // Find nearby clay rocks to mine if next target holds no value.
        RSObject[] clayRocks = Entities.find(ObjectEntity::new)
                .nameEquals("Rocks")
                .sortByDistance()
                .getResults();

        // If rocks are found and you are not mining
        if (clayRocks.length > 0 && Player.getAnimation() == -1)
        {
            // Ensure nextTarget holds a valid value
            if (nextTarget == null)
            {
                nextTarget = clayRocks[0];
            }

            // Ensure menu isn't open for next entity, if it is, click it via options.
            if (ChooseOption.isOpen())
            {
                ChooseOption.select("Mine Rocks");
            }
            else
            {
                nextTarget.click();
            }

            // Prior to idle scripts.gengarlibrary.Antiban procedures
            nextTarget = (RSObject) Antiban.get().getNextTarget(clayRocks);     // Store the next rock to mine.
            Antiban.get().setHoverAndMenuBoolValues();
            Antiban.get().generateTrackers((int)averageMiningWaitTime);

            if (Timing.waitCondition(GBooleanSuppliers.waitForAnimation(Constants.MINING_ANIMATION), 3200))
            {
                final long startMiningTime = System.currentTimeMillis();

                while (isMining())
                {
                    General.sleep(150);

                    // Idling Antiban procedures
                    Antiban.get().executeShouldHoverAndMenu(nextTarget);
                    Antiban.get().timedActions();
                }

                Vars.minedOres++;

                // Mining is now complete. Update statistics and sleep the reaction time generated.
                updateMiningStatistics(System.currentTimeMillis() - startMiningTime);
                Antiban.get().sleepReactionTime((int) averageMiningWaitTime);
            }
        }
    }

    //__________________________________________________________________________________________________________________

    /**
     * Enable the bot to handle multiple bots near clay area.
     *
     */
    private void newMineOre()
    {
        RSObject[] clayRocks = Objects.find(10, CLAY_ROCK_ID);

        if (clayRocks.length == 0)
        {
            // move to anticipated

            General.println("No tree was found");
            Antiban.get().executeMoveToAnticipated();
            long startIdleTime = System.currentTimeMillis();

            // While no trees are available
            while(trees == null || trees.length == 0)
            {
                // Do the following:
                trees = Objects.findNearest(10, Vars.get().selectedTree); // check for trees

                if (!Player.isMoving())
                    Antiban.get().resolveTimedActions();
            }

            // A tree has just appeared: generate and sleep a reaction time
            long stopIdleTime = System.currentTimeMillis();
            Antiban.get().generateAndSleep((int)(stopIdleTime - startIdleTime));
        }
        else
        {

        }














    }

    private boolean isMining()
    {
        return (Player.getAnimation() == Constants.MINING_ANIMATION);
    }

    @Override
    public boolean validate()
    {
        return !Inventory.isFull() && Validators.isInMine() && Constants.CLAY_TILE.distanceTo(Player.getPosition()) <= 6;
    }
}
