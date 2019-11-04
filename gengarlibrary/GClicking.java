package scripts.gengarlibrary;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Clickable07;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import scripts.gengarlibrary.initialsetup.CameraZoom;

import java.util.function.BooleanSupplier;

public class GClicking
{
    public static boolean clickPlayer(RSPlayer player, String... action)
    {
        return clickEntity(player, action);
    }

    public static boolean clickObject(RSObject object)
    {
        return clickEntity(object, object.getDefinition().getActions());
    }

    public static boolean clickObject(RSObject object, String desiredUpText)
    {
        return clickEntity(object, desiredUpText, -1);
    }

    public static boolean clickObject(RSObject object, int animation)
    {
        String[] actions = object.getDefinition().getActions();

        if (actions.length == 0)
            return false;

        return clickEntity(object, actions[0], animation);
    }

    public static boolean clickObject(RSObject object, String desiredUpText, int animation)
    {
        return clickEntity(object, desiredUpText, animation);
    }

    public static boolean clickNpc(RSNPC npc)
    {
        return clickEntity(npc, npc.getActions());
    }

    public static boolean clickNpc(RSNPC npc, String desiredUpText)
    {
        return clickEntity(npc, desiredUpText, -1);
    }

    private static <T extends Clickable07 & Positionable> boolean clickEntity(T clickableEntity, String[] actions)
    {
        if (actions.length == 0)
            return false;

        return clickEntity(clickableEntity, actions[0], -1);
    }

    /**
     * With supplied parameters, ensures that the entity will be clicked.
     *
     * Does all the null checking, camera movement, zoom out to ensure the above goal is met. If returns false, you
     * need to make the account walk to object and recall.
     *
     * @param clickableEntity   An entity that is clickable and has a position
     * @param uptext            String value representing the uptext when mouse hovers the npc
     * @param animation         Desired animation when interacting with object. -1 if no animation to occur.
     * @return                  True if click was successful, false otherwise.
     **/
    private static <T extends Clickable07 & Positionable> boolean clickEntity(T clickableEntity, String uptext, int animation)
    {
        if (!clickableEntity.isClickable() && !attemptAimCamera(clickableEntity))
        {
            System.out.println("clickEntity: Unable to click entity. It is too far away; resolve from caller.");
            return false;
        }

        clickableEntity.hover();
        Timing.waitUptext(uptext, General.random(650, 800));

        /*BooleanSupplier boolSupp = animation == -1 ? GBooleanSuppliers.waitToStopMoving() : GBooleanSuppliers.waitForAnimation(animation);*/

        // Split into two if statements for readability
        if (clickableEntity.getPosition().distanceTo(Player.getPosition()) <= 1)
        {
            if (!safeClick(uptext)  ||
                !Timing.waitCondition(GBooleanSuppliers.waitForAnimation(animation), General.random(1800, 1950)))
            {
                System.out.println("clickEntity: Failed to click entity (1)");
                return false;
            }
        }
        else if (animation == -1)
        {
            if (!(safeClick(uptext)                                                                     &&
                  Timing.waitCondition(GBooleanSuppliers.waitToStartMoving(), General.random(625, 800)) &&
                  Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(5200, 5400))))
            {
                System.out.println("clickEntity: Failed to click entity (2)");
                return false;
            }
        }
        else
        {
            if (!(safeClick(uptext) &&
                  Timing.waitCondition(GBooleanSuppliers.waitForAnimation(animation), General.random(5200, 5400))))
            {
                System.out.println("clickEntity: Failed to click entity (3)");
                return false;
            }
        }

        /*else if (!(safeClick(uptext)                                                                     &&
                   Timing.waitCondition(GBooleanSuppliers.waitToStartMoving(), General.random(625, 800)) &&
                   Timing.waitCondition(boolSupp, General.random(5200, 5400))))
        {
            System.out.println("clickEntity: Failed to click entity (2)");
            return false;
        }*/

        return true;
    }

    /**
     * WastedBro's implementation (with slight tweaks) that aims the camera at an entity.
     *
     * Attempts to do so 2-5 times. This is random to avoid patterns each attempt is different than the last.
     *
     * @param clickableEntity Any Type that implements the Clickable and Positionable interfaces (RSObject, RSNPC, etc)
     * @return Whether or not the target is in camera view
     */
    private static <T extends Clickable07 & Positionable> boolean attemptAimCamera(T clickableEntity)
    {
        if(clickableEntity == null)
            return false;

        int numberOfCameraAdjusts = 0;
        int maxCameraAdjusts = General.random(2, 5);

        while(!clickableEntity.isClickable() && numberOfCameraAdjusts <= maxCameraAdjusts)
        {
            if (numberOfCameraAdjusts == 0)
            {
                Camera.turnToTile(clickableEntity);
            }
            else if (numberOfCameraAdjusts == maxCameraAdjusts)
            {
                CameraZoom.zoomOut();
            }
            else
            {
                // If we fail to find by rotation, then lower the angle (to see further)
                int angle = Camera.getCameraAngle();
                Camera.setCameraAngle(angle - General.randomSD(5, angle, 30, 11));
            }

            numberOfCameraAdjusts++;
        }

        return clickableEntity.isClickable();
    }

    /**
     * A safe way to ensure that you click an object.
     *
     * When normally clicking, other {@code Positionables} may obstruct your path, making you click on them instead. If
     * such case occurs, it would simply do a right click and click the uptext option.
     *
     * @param desiredUptext The option that you want to select
     * @return true if successful click, false otherwise
     */
    private static boolean safeClick(String desiredUptext)
    {
        // fail safe for spam right click on option
        if (ChooseOption.isOpen())
        {
            ChooseOption.select("Cancel");
            System.out.println("safeClick: Option right click was open, cancelling options...");
            return false;
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.isUptext(desiredUptext))
        {
            Mouse.click(3);

            if (Timing.waitChooseOption(desiredUptext, 500))
            {
                ChooseOption.select(desiredUptext); // cant return this for w.e reason always false?
                return true;
            }
            else
            {
                ChooseOption.select("Cancel");
                return false;
            }
        }
        else
        {
            Mouse.click(1);
            return true;
        }
    }
}
