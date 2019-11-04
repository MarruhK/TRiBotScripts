package scripts.piscfavour.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Player;

public class Methods {




    public boolean stopWalking(int low, int high){
        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Player.isMoving();
            }
        }, General.random(low, high));
    }

}
