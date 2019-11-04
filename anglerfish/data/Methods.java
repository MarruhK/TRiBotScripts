package scripts.anglerfish.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;

public abstract class Methods {

    public static  void rightClick(String desiredUptext, Positionable target){

        // Move mouse next to positio
        // Before clicking pump, ensure nothing is in the way (i.e. Guard in front of pump)
        if (!Game.isUptext(desiredUptext)){
            Mouse.click(3);
            if (ChooseOption.isOpen()){
                ChooseOption.select(desiredUptext);
            }
        } else {
            // Left click
            Mouse.click(1);
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
