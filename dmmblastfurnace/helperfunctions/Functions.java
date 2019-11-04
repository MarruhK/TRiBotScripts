package scripts.dmmblastfurnace.helperfunctions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;

public class Functions
{
    public static boolean clickObject(int animation, String action, int... id)
    {
        RSObject[] objects = Objects.find(15, id);

        if (objects.length > 0 && objects != null)
        {
            RSObject object = objects[0];

            AccurateMouse.click(object, action);
        }

        if (Timing.waitCondition(conditionIsAnimation(animation), General.random(4900, 5500)))
        {
            General.sleep(1500, 2000);
            return true;
        }
        return false;
    }

    // Conditions
    private static Condition conditionIsAnimation(int animation)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);

                return  Player.getAnimation() == animation;
            }
        };
    }
}
