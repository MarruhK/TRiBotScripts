package scripts.dmmrelekkafisher.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public abstract class Constants
{
    // FISHING SPOT NAME
    public static final String SHARK_FISHING_SPOT_NAME = "Harpoon Fishing spot";
    public static final String LOBSTER_FISHING_SPOT_NAME = "Cage Fishing spot";
    public static final String SHRIMP_FISHING_SPOT_NAME = "Small Net Fishing spot";

    // ITEMS
    public static final int RAW_SHARK_ID = 383;
    public static final int RAW_LOBSTER_ID = 377;
    public static final int RAW_SHRIMP_ID = 317;
    public static final int RAW_ANCHOVY_ID = 321;
    public static final int HARPOON_ID = 311;
    public static final int LOBSTER_POT_ID = 301;
    public static final int FISHING_NET_ID = 303;
    public static final int CAMMY_TELE_ID = 8010;

    // ANIMATION
    public static final int FISHING_SHARKS_ANIMATION = 618;
    public static final int FISHING_LOBSTERS_ANIMATION = 619;
    public static final int FISHING_SHRIMPS_ANIMATION = 621;

    // TILES
    public static final RSTile MULE_TILE = new RSTile(2636, 3669, 0);
    public static final RSTile SHARK_TILE = new RSTile(2647, 3709, 0);
    public static final RSTile LOBSTER_TILE = new RSTile(2641, 3695, 0);
    public static final RSTile SHRIMP_TILE = new RSTile(2631, 3690, 0);

    // DEATH RETURN
    public static final RSTile DEATH_TILE_1 = new RSTile(3219, 3216,0); // LUMBY
    public static final RSTile DEATH_TILE_2 = new RSTile(3224, 3221,0); // LUMBY

    public static final RSTile CAMMY_DEATH_TILE_1 = new RSTile(2752, 3481,0);
    public static final RSTile CAMMY_DEATH_TILE_2 = new RSTile(2761, 3476,0);

    public static final RSTile DOWN_STAIRS_TILE = new RSTile(3206, 3209,0);
    public static final RSTile MID_STAIRS_TILE = new RSTile(3205, 3209,1);
    public static final RSTile UP_STAIRS_TILE = new RSTile(3205, 3209,2);

    public static final RSTile LUMBY_BANK_TILE = new RSTile(3208, 3220,2);
    public static final RSTile CAMMY_BANK_TILE = new RSTile(2728, 3493,0);

    public static final RSTile CAMMY_BANK_TILE_AREA_1 = new RSTile(2721, 3493, 0);
    public static final RSTile CAMMY_BANK_TILE_AREA_2 = new RSTile(2730, 3490, 0);

    // AREA
    public static final RSArea DEATH_AREA = new RSArea(DEATH_TILE_1, DEATH_TILE_2);
    public static final RSArea CAMMY_DEATH_AREA = new RSArea(CAMMY_DEATH_TILE_1, CAMMY_DEATH_TILE_2);

    public static final RSArea LUMBY_AREA = new RSArea( new RSTile(3201, 3204, 0),
                                                        new RSTile(3225, 3229, 0));
    public static final RSArea LUMBY_2ND_FLOOR = new RSArea(new RSTile(3216, 3229, 1), MID_STAIRS_TILE);
    public static final RSArea LUMBY_BANK_FLOOR = new RSArea(new RSTile(3216, 3229, 2), UP_STAIRS_TILE);
    public static final RSArea CAMMY_BANK_AREA = new RSArea(CAMMY_BANK_TILE_AREA_1, CAMMY_BANK_TILE_AREA_2);

    // NPC
    public static final int DEPO_NPC_ID = 6520;
    public static final int CAMMY_DEPO_NPC_ID = 6560;
    public static final int LOBSTER_NPC_ID = 3914;
    public static final int SHARK_NPC_ID = 3915;
    public static final int SHRIMP_NPC_ID = 3913;

    // OBJECTS
    public static final int DOWN_LADDER_ID = 16671;
    public static final int MID_LADDER_ID = 16672;
    public static final int TOP_LADDER_ID = 16673;
}
