package scripts.gengarcooker.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;

public abstract class Methods {

    // Needs mouse to be hovering positionable
    public static boolean safeClick(String desiredUptext){
        System.out.println("---- The safeClick method began.");
        // Wait for mouse to hover positionable
        General.sleep(400,500);

        // fail safe for spam right click on option
        if (ChooseOption.isOpen()){
            ChooseOption.select("Cancel");
            return false;
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.isUptext(desiredUptext)){
            Mouse.click(3);

            if (Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return ChooseOption.isOpen();
                }
            }, General.random(400,500))){
                if (ChooseOption.isOptionValid(desiredUptext)){
                    ChooseOption.select(desiredUptext);
                    return true;
                }
            }
            // Didn't find right option or didn't right click properly.
            return false;
        } else {
            Mouse.click(1);
            return true;
        }
    }

    public static boolean waitToStop(int low, int high){
        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return !Player.isMoving();
            }
        }, General.random(low, high));
    }
}
