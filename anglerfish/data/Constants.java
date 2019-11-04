package scripts.anglerfish.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public abstract class Constants
{

    // IDS
    public static final int FISHING_ROD_ID = 307;
    public static final int ANGLERFISH_ID = 13439;
    public static final int NOTED_ANGLERFISH_ID = 13440;
    public static final int BAIT_ID = 13431;
    public static final int FISHING_NPC_ID = 6825;
    public static final int[] TELETAB_ID = {8007, 8008};

    // TILES
    public static final RSTile BANK_TILE = new RSTile(1804, 3788, 0);
    public static final RSTile FISHING_TILE = new RSTile(1827, 3772, 0);
    public static final RSTile LUMBY1_TILE = new RSTile(3219, 3222, 0);
    public static final RSTile LUMBY2_TILE = new RSTile(3224, 3216, 0);
    public static final RSTile VARROCK1_TILE = new RSTile(3221, 3421, 0);
    public static final RSTile VARROCK2_TILE = new RSTile(3206, 3434, 0);

    // RSAREA
    public static final RSArea LUMBRIDGE = new RSArea(LUMBY1_TILE, LUMBY2_TILE);
    public static final RSArea VARROCK = new RSArea(VARROCK1_TILE, VARROCK2_TILE);
}
