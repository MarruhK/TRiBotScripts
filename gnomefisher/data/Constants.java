package scripts.gnomefisher.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public abstract class Constants {

    public static final int FISHING_SPOT_ID = 1506;
    public static final int FLY_ROD_ID = 309;
    public static final int FEATHERS_ID = 314;
    public static final int FISHING_ANIM = 623;
    public static final int TROUT_ID = 335;
    public static final int SALMON_ID = 331;

    public static final int BOTTOM_LADDER_ID = 16675;
    public static final int TOP_LADDER_ID = 16677;


    public static final int INTERFACE_INDEX_OPTION_ID = 261;
    public static final int INTERFACE_CHILD_4_OPTIONS_ID = 1;
    public static final int INTERFACE_CHILD_SHIFT_CLICK_ID = 65;
    public static final int INTERFACE_COMPONENT_ACTION_CONTROLS_ID = 6;
    public static final int OPTIONS_NOT_SELECTED_ID = 761;


    public static final RSTile[] FISHING_TILES = {  new RSTile(2388, 3425, 0),
                                                    new RSTile(2389, 3423, 0),
                                                    new RSTile(2391, 3421, 0),
                                                    new RSTile(2394, 3418, 0)};


    public static final RSArea BANK_AREA = new RSArea(  new RSTile(2446, 3434, 1),
                                                        new RSTile(2445, 3415, 1));


    public static final RSTile BANK_DOWN_STAIRS_TILE = new RSTile(2443, 3415, 0);
    public static final RSTile BANK_UP_STAIRS_TILE = new RSTile(2445, 3416, 1);
    public static final RSTile ACTUAL_BANK_TILE = new RSTile(2445, 3424, 1);
    public static final RSTile BASE_FISHING_TILE = new RSTile(2392, 3423, 0);
}
