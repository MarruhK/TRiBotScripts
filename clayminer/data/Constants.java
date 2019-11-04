package scripts.clayminer.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants
{
    public static final int PASSING_ROOT_ANIMATION = 810;
    public static final int LADDER_CLIMB_ANIMATION = 828;
    public static final int MINING_ANIMATION = 624;

    public static final RSTile CLAY_TILE = new RSTile(2451, 9907,0);
    public static final RSTile INSIDE_MINE_ROOT_TILE = new RSTile(2467,9906,0);
    public static final RSTile OUTSIDE_MINE_ROOT_TILE = new RSTile(2467,9902,0);
    public static final RSTile MAIN_LEVEL_TILE = new RSTile(2465,3495,0);
    public static final RSTile BANK_TILE = new RSTile(2449, 3482, 1);
    public static final RSTile BOTTOM_LADDER_TILE = new RSTile(2464,9897, 0);

    public static final int[] PICKAXE_IDS = {1265, 1267, 1269, 12297, 1273, 1271, 1275};
    // Bronze -> Rune, including black pick

    public static final RSArea MINE_AREA_1 = new RSArea(new RSTile(2494, 9908, 0), new RSTile(2434, 9918, 0));  // COVERS MOST OF AREA
    public static final RSArea MINE_AREA_2 = new RSArea(new RSTile(2492, 9907, 0), new RSTile(2489, 9907, 0));  // SMALL LINE VERY RIGHT BOTTOM POCKET
    public static final RSArea MINE_AREA_3 = new RSArea(new RSTile(2468, 9905, 0), new RSTile(2434, 9907, 0));  // WEST OF ENTRANCE TO BOTTOM LEFT, RIGHT ABOVE SMALL POCK
    public static final RSArea MINE_AREA_4 = new RSArea(new RSTile(2476, 9907, 0), new RSTile(2469, 9904, 0));  // SMALL POCKET EAST OF ENTRANCE
    public static final RSArea MINE_AREA_5 = new RSArea(new RSTile(2461, 9904, 0), new RSTile(2434, 9899, 0));  // SAME AS 3, BUT A LITTLE LOWER Y AND AVOIDS OUTSIDE MINE
    public static final RSArea MINE_AREA_6 = new RSArea(new RSTile(2440, 9898, 0), new RSTile(2435, 9896, 0));  // SMALL POCKET BOTTOM LEFT
    public static final RSArea[] MINING_AREAS = {MINE_AREA_1, MINE_AREA_2, MINE_AREA_3, MINE_AREA_4, MINE_AREA_5, MINE_AREA_6};
}
