package scripts.gengaragility.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.*;
import scripts.gengarlibrary.ACamera;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;
import scripts.gengaragility.framework.Node;
import scripts.gengaragility.utility.Antiban;

public abstract class Obstacle extends Node
{
    // Constants
    private static final int ID_MARKS_OF_GRACE = 11849;

    // Vars
    private int foodId = getFoodId();
    private int obstacleCounter = 1;    // Used for updating time
    protected int averageWaitingTime;   // Give access to sub-c

    protected Obstacle(ACamera aCamera)
    {
        super(aCamera);
    }

    private void updateWaitTime(int timeWaited)
    {
        averageWaitingTime = (averageWaitingTime + timeWaited) / ++obstacleCounter;
        System.out.println("updateWaitTime: Average wait = " + averageWaitingTime);
    }

    protected boolean clickObstacle(String action, int obstacleId)
    {
        RSObject[] obstacles = Objects.find(15, obstacleId);

        if (obstacles.length > 0)
        {
            // Check if you should eat food.
            if (foodId > 0 && Antiban.get().eatFood(100 * Skills.SKILLS.HITPOINTS.getCurrentLevel() /
                                                                            Skills.SKILLS.HITPOINTS.getActualLevel() ))
                eatFood();

            // click object unless not on screen
            RSObject obstacle = obstacles[0];

            if (!obstacle.isOnScreen())
                this.aCamera.turnToTile(obstacle.getPosition());

            // Wait for it to be clickable
            if (Timing.waitCondition(()->
            {
                General.sleep(75, 100);
                return obstacle.isOnScreen();
            }, General.random(2500, 2800)))
            {
                // Track exp gain and time
                int initialExp = Skills.getXP(Skills.SKILLS.AGILITY);
                long currentTime = Timing.currentTimeMillis();

                AccurateMouse.click(obstacle, action);

                if (Timing.waitCondition(()->
                {
                    General.sleep(100);
                    return isExpGained(initialExp);
                }, General.random(9000, 9250)))
                {
                    // scripts.gengarlibrary.Antiban shit WRONG IMPLEMENTATION FIX
                    updateWaitTime((int) (Timing.currentTimeMillis() - currentTime));
                    int reactionTime = Antiban.get().generateReactionTime(averageWaitingTime);
                    Antiban.get().sleepReactionTime(reactionTime);
                    Antiban.get().generateTrackers(reactionTime,false);
                    Antiban.get().timedActions();

                    return true;
                }
                else
                {
                    System.out.println("clickObstacle: Failed to click the obstacle.");
                    return false;
                }
            }
            else
            {
                System.out.println("clickObstacle: Failed to turn to tile, exiting...");
                return false;
            }
        }

        System.out.println("clickObstacle: Obstacle not found");
        return false;
    }

    protected boolean executeShouldLootMarkOfGrace(RSArea area)
    {
        RSGroundItem[] marks = GroundItems.find(ID_MARKS_OF_GRACE);

        if (shouldLootMarkOfGrace() && marks.length > 0)
        {
            RSGroundItem mark = marks[0];
            RSTile markTile = mark.getPosition();

            if (!area.contains(mark.getPosition()))
            {
                System.out.println("executeShouldLootMarkOfGrace: Marks of grace is not in the current area.");
                return false;
            }

            if (!mark.isOnScreen())
                this.aCamera.turnToTile(markTile);

            // Wait for it to be clickable
            if (Timing.waitCondition(()->
            {
                General.sleep(75, 100);
                return mark.isOnScreen();
            }, General.random(2500, 2800)))
            {
                int initialAmountOfMarks = Inventory.getCount(ID_MARKS_OF_GRACE);
                AccurateMouse.click(mark, "Take");

                return Timing.waitCondition(()->
                {
                    General.sleep(100);
                    return Inventory.getCount(ID_MARKS_OF_GRACE) > initialAmountOfMarks;
                }, getWalkTime(markTile));
            }
            else
            {
                System.out.println("executeShouldLootMarkOfGrace: Failed to turn to tile, exiting...");
                return false;
            }
        }

        System.out.println("executeShouldLootMarkOfGrace: No marks can be found...");
        return false;
    }

    private int getWalkTime(RSTile itemTile)
    {
        int tilesAway = itemTile.distanceTo(Player.getPosition());

        if (tilesAway <= 0)
            tilesAway = 0;

        return tilesAway * 1000 / 3;
    }

    private void eatFood()
    {
        RSItem[] foods = Inventory.find(foodId);

        if (foods.length > 0)
            foods[0].click();
    }

    private int getFoodId()
    {
        RSItem[] foods = Inventory.find(Filters.Items.actionsContains("Eat"));

        if (foods.length > 0)
            return foods[0].getID();

        return -1;
    }

    private boolean isExpGained(int initialExp)
    {
        return Skills.getXP(Skills.SKILLS.AGILITY) > initialExp;
    }

    protected abstract boolean shouldLootMarkOfGrace();
}
