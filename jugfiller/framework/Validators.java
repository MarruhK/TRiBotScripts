package scripts.jugfiller.framework;

import org.tribot.api2007.Player;
import scripts.jugfiller.data.Constants;

public class Validators
{
    public static boolean isInBank()
    {
        return Constants.BANK_AREA_1.contains(Player.getPosition()) ||
               Constants.BANK_AREA_2.contains(Player.getPosition());
    }

    public static boolean isInGeneralStore()
    {
        return Constants.GEN_STORE_AREA_1.contains(Player.getPosition()) ||
               Constants.GEN_STORE_AREA_2.contains(Player.getPosition());
    }
}
