package scripts.clayminer.framework;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.clayminer.data.Constants;

public class Validators
{
    public static boolean isInMine()
    {
        return (Constants.MINE_AREA_1.contains(Player.getPosition()) ||
                Constants.MINE_AREA_2.contains(Player.getPosition()) ||
                Constants.MINE_AREA_3.contains(Player.getPosition()) ||
                Constants.MINE_AREA_4.contains(Player.getPosition()) ||
                Constants.MINE_AREA_5.contains(Player.getPosition()) ||
                Constants.MINE_AREA_6.contains(Player.getPosition()));
    }

    public static boolean isInMiddleFloor()
    {
        return Player.getPosition().getPlane() == 0 && !isInBottomFloor();
    }

    public static boolean isInBottomFloor()
    {
        return (Math.abs(Player.getPosition().getX() - 2467) < 60 &&
                Math.abs(Player.getPosition().getY() - 9893) < 50);
    }
}
