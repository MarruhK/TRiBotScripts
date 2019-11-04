package scripts.dmmblastfurnace.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import java.util.HashMap;

public class Constants
{
    // Tiles
    public static final RSTile TILE_REFUEL_COKE = new RSTile(1950, 4963, 0);

    private static final RSTile TILE_BF_SE = new RSTile(1957, 4958, 0);
    private static final RSTile TILE_BF_NW = new RSTile(1935, 4974, 0);

    private static final RSTile TILE_GE_SE = new RSTile(3186, 3468, 0);
    private static final RSTile TILE_GE_NW = new RSTile(3139, 3519, 0);

    private static final RSTile TILE_KELD_SE = new RSTile(2950, 10157, 0);
    private static final RSTile TILE_KELD_NW = new RSTile(2905, 10199, 0);

    // Areas
    public static final RSArea AREA_BF = new RSArea(TILE_BF_NW, TILE_BF_SE);
    public static final RSArea AREA_GE = new RSArea(TILE_GE_NW, TILE_GE_SE);
    public static final RSArea AREA_KELD = new RSArea(TILE_KELD_NW, TILE_KELD_SE);


}
