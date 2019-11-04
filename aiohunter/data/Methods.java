package scripts.aiohunter.data;


import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;

public abstract class Methods
{
    /**
     * A safe way to ensure that you click an object.
     *
     * When normally clicking, other {@code Positionables} may obstruct your path, making you click on them instead. If
     * such case occurs, it would simply do a right click and click the uptext option.
     *
     * @param desiredUptext The option that you want to select
     * @return true if successful click, false otherwise
     */
    public static boolean safeClick(String desiredUptext)
    {
        // Wait for mouse to hover positionable
        General.sleep(400,500);

        // fail safe for spam right click on option
        if (ChooseOption.isOpen())
        {
            ChooseOption.select("Cancel");
            return false;
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.isUptext(desiredUptext))
        {
            Mouse.click(3);

            if (Timing.waitCondition(new Condition()
            {
                @Override
                public boolean active()
                {
                    General.sleep(100);
                    return ChooseOption.isOpen();
                }
            }, General.random(400,500)))
            {
                if (ChooseOption.isOptionValid(desiredUptext))
                {
                    ChooseOption.select(desiredUptext);
                    return true;
                }
            }
            // Didn't find right option or didn't right click properly.
            return false;
        }
        else
        {
            Mouse.click(1);
            return true;
        }
    }

    /**
     * With supplied parameters, ensures that the object will be clicked.
     *
     * Does all the null checking, camera movement, player movement to ensure the above goal is met. If no Animation
     * is to occur, then it is up to you to define the sleeps after clicking said object.
     *
     * @param object            An RSObject
     * @param desiredUpText     String value representing the uptext when mouse hovers the npc
     * @param animation         Desired animation when interacting with object. -1 if no animation to occur.
     * @return                  True if click was successful, false otherwise.
     */
    public static boolean clickObject(RSObject object, String desiredUpText, int animation)
    {
        System.out.println("clickObject: Initiated");
        int tilesAway = object.getPosition().distanceTo(Player.getPosition());
        int extraTime = tilesAway * 1000 / 3;

        if (!object.isOnScreen())
            Vars.cam.turnToTile(object.getPosition());

        if (!object.isOnScreen())
        {
            System.out.println("clickObject: Object is too far, walking and ret false.");
            DaxWalker.walkTo(Constants.TILE_RESET);
            Timing.waitCondition(conditionWaitToStopMoving(), General.random(3000 + extraTime, 3200 + extraTime));
            return false;
        }

        if (AccurateMouse.click(object, desiredUpText))
        {
            Vars.cam.turnToTile(object.getPosition());
            System.out.println("clickObject: Clicked tree, now waiting for animation.");

            if (Timing.waitCondition(conditionIsAnimation(animation), General.random(3000 + extraTime, 3200 + extraTime)))
            {
                System.out.println("clickObject: Successfully animated.");
                return true;
            }
        }
        System.out.println("clickObject: Failed to animate.");
        return false;
    }

    public static boolean safeClickEquipment(String desiredUptext)
    {
        // Wait for mouse to hover positionable
        General.sleep(400,500);

        // fail safe for spam right click on option
        if (ChooseOption.isOpen())
        {
            if (ChooseOption.isOptionValid(desiredUptext))
            {
                ChooseOption.select(desiredUptext);
                return true;
            }
            else
            {
                ChooseOption.select("Cancel");
                return false;
            }
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.isUptext(desiredUptext))
        {
            Mouse.click(3);

            if (Timing.waitCondition(new Condition()
            {
                @Override
                public boolean active()
                {
                    General.sleep(100);
                    return ChooseOption.isOpen();
                }
            }, General.random(400,500)))
            {
                if (ChooseOption.isOptionValid(desiredUptext))
                {
                    ChooseOption.select(desiredUptext);
                    return true;
                }
            }
            // Didn't find right option or didn't right click properly.
            return false;
        }
        else
        {
            Mouse.click(1);
            return true;
        }
    }

    // CONDITIONS ______________________________________________________________________________________________________
    public static Condition conditionIsAnimation(int animation)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Player.getAnimation() == animation;
            }
        };
    }

    public static Condition conditionWaitToStopMoving()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return !Player.isMoving();
            }
        };
    }
}
