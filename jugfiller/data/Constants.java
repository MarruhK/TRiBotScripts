package scripts.jugfiller.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public abstract class Constants {

    // Inventory items ID
    public static final int EMPTY_JUG_ID = 1935;
    public static final int WATER_JUG_ID = 1937;
    public static final int COINS_ID = 995;
    public static final int PACKED_JUGS_ID = 20742;

    // Interactive objects ID
    public static final int WATER_PUMP_ID = 24004;
    public static final int OPEN_GEN_STORE_DOOR_ID = 24055;
    public static final int CLOSED_GEN_STORE_DOOR_ID = 24056;

    // Animation
    public static final int WATER_FILLING_ANIM_ID = 832;

    // NPC IDs
    public static final int SHOP_KEEPER_ID = 512;
    public static final int SHOP_ASSISTANT_ID = 513;

    // Tiles and Areas
    public static final RSTile PUMP_TILE = new RSTile(2949, 3382, 0);
    public static final RSTile BANK_TILE = new RSTile(2944, 3372, 0);
    public static final RSTile GEN_STORE_DOOR_TILE = new RSTile(2958, 3384, 0);

    public static final RSArea BANK_AREA_1 = new RSArea(new RSTile(2943, 3373, 0),
                                                        new RSTile(2947, 3368, 0));
    public static final RSArea BANK_AREA_2 = new RSArea(new RSTile(2948, 3368, 0),
                                                        new RSTile(2949, 3368, 0));
    public static final RSArea GEN_STORE_AREA_1 = new RSArea(new RSTile(2957, 3387, 0),
                                                             new RSTile(2959, 3385, 0));
    public static final RSArea GEN_STORE_AREA_2 = new RSArea(new RSTile(2960, 3390, 0),
                                                             new RSTile(2953, 3388, 0));



    public static final int MAX_UNPACKED_JUG_PRICE = 203;
    public static final int AVERAGE_FILLING_TIME = 18500;


}
