package scripts.fishingtrawler.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {

    // ITEM IDS
    public static final int EMPTY_BAILING_BUCKET_ID = 583;
    public static final int FULL_BAILING_BUCKET_ID = 585;
    public static final int WATER_HOLE_ID = 2484;

    // OBJECT IDS
    public static final int GANGPLANK_ID = 4977;
    public static final int TRAWLER_NET_ID = 2483;
    // Fixed and broken nets have same ID
    public static final int TRAWLER_NET_ONE = 2481;
    public static final int TRAWLER_NET_TWO = 2482;
    public static final int TRAWLER_NET_LADDER_ID = 4060;

    // ANIMATION
    public static final int FILLING_HOLE_ANIMATION = 827;
    public static final int LADDER_CLIMB_ANIMATION = 828;

    // TILES
    public static final RSTile BANK_TILE = new RSTile(2661, 3162, 0);
    public static final RSTile TRAWLER_NET_TILE = new RSTile(2666, 3166, 0);
    public static final RSTile OUTSIDE_GANGPLANK_TILE = new RSTile(2676, 3170, 0);
    public static final RSTile BEFORE_GAME_BOAT_TILE = new RSTile(2672, 3170, 1);
    public static final RSTile TRAWLER_NET_LADDER_TILE = new RSTile(2013, 4826, 0);

    // BOAT TRAWLER TILES_______________________________________________________________________________________________
    // DRY AREA TILES
    public static final RSTile DRY_BOAT_BOT_RIGHT_TILE_1 = new RSTile(1889, 4824, 0);
    public static final RSTile DRY_BOAT_BOT_RIGHT_TILE_2 = new RSTile(1892, 4824, 0);
    public static final RSTile DRY_BOAT_BOT_LEFT_TILE_1 = new RSTile(1885, 4824, 0);
    public static final RSTile DRY_BOAT_BOT_LEFT_TILE_2 = new RSTile(1888, 4824, 0);

    public static final RSTile DRY_BOAT_BOT_RIGHT_HOLE_TILE_1 = new RSTile(1889, 4823, 0);
    public static final RSTile DRY_BOAT_BOT_RIGHT_HOLE_TILE_2 = new RSTile(1892, 4823, 0);
    public static final RSTile DRY_BOAT_BOT_LEFT_HOLE_TILE_1 = new RSTile(1885, 4823, 0);
    public static final RSTile DRY_BOAT_BOT_LEFT_HOLE_TILE_2 = new RSTile(1888, 4823, 0);

    public static final RSTile DRY_BOAT_TOP_RIGHT_TILE_1 = new RSTile(1889, 4826, 0);
    public static final RSTile DRY_BOAT_TOP_RIGHT_TILE_2 = new RSTile(1892, 4826, 0);
    public static final RSTile DRY_BOAT_TOP_LEFT_TILE_1 = new RSTile(1885, 4826, 0); // 2013 4826
    public static final RSTile DRY_BOAT_TOP_LEFT_TILE_2 = new RSTile(1888, 4826, 0);

    // FLOODED AREA TILES_______________________________________________________________________________________________
    public static final RSTile FLOODED_BOAT_BOT_RIGHT_TILE_1 = new RSTile(2017, 4824, 0);
    public static final RSTile FLOODED_BOAT_BOT_RIGHT_TILE_2 = new RSTile(2020, 4824, 0);
    public static final RSTile FLOODED_BOAT_BOT_LEFT_TILE_1 = new RSTile(2013, 4824, 0);
    public static final RSTile FLOODED_BOAT_BOT_LEFT_TILE_2 = new RSTile(2016, 4824, 0);

    public static final RSTile FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_1 = new RSTile(2017, 4823, 0);
    public static final RSTile FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_2 = new RSTile(2020, 4823, 0);
    public static final RSTile FLOODED_BOAT_BOT_LEFT_HOLE_TILE_1 = new RSTile(2013, 4823, 0);
    public static final RSTile FLOODED_BOAT_BOT_LEFT_HOLE_TILE_2 = new RSTile(2016, 4823, 0);

    public static final RSTile FLOODED_BOAT_TOP_RIGHT_TILE_1 = new RSTile(2017, 4826, 0);
    public static final RSTile FLOODED_BOAT_TOP_RIGHT_TILE_2 = new RSTile(2020, 4826, 0);
    public static final RSTile FLOODED_BOAT_TOP_LEFT_TILE_1 = new RSTile(2013, 4826, 0);
    public static final RSTile FLOODED_BOAT_TOP_LEFT_TILE_2 = new RSTile(2016, 4826, 0);

    // AREAS____________________________________________________________________________________________________________
    // DUOS
    // #1 will be denoted as ONE and # 2 TWO
    public static final RSArea ONE_DRY_AREA = new RSArea(DRY_BOAT_TOP_LEFT_TILE_1, DRY_BOAT_BOT_LEFT_TILE_2);
    public static final RSArea ONE_FLOODED_AREA = new RSArea(FLOODED_BOAT_TOP_LEFT_TILE_1, FLOODED_BOAT_BOT_LEFT_TILE_2);
    public static final RSArea ONE_DRY_HOLE_AREA = new RSArea(DRY_BOAT_BOT_LEFT_HOLE_TILE_1, DRY_BOAT_BOT_LEFT_HOLE_TILE_2);
    public static final RSArea ONE_FLOODED_HOLE_AREA = new RSArea(FLOODED_BOAT_BOT_LEFT_HOLE_TILE_1, FLOODED_BOAT_BOT_LEFT_HOLE_TILE_2);

    public static final RSArea TWO_DRY_AREA = new RSArea(DRY_BOAT_TOP_RIGHT_TILE_1, DRY_BOAT_BOT_RIGHT_TILE_2);
    public static final RSArea TWO_FLOODED_AREA = new RSArea(FLOODED_BOAT_TOP_RIGHT_TILE_1, FLOODED_BOAT_BOT_RIGHT_TILE_2);
    public static final RSArea TWO_DRY_HOLE_AREA = new RSArea(DRY_BOAT_BOT_RIGHT_HOLE_TILE_1, DRY_BOAT_BOT_RIGHT_HOLE_TILE_2);
    public static final RSArea TWO_FLOODED_HOLE_AREA = new RSArea(FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_1, FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_2);







    public static final RSArea FLOODED_TOP_AREA = new RSArea(FLOODED_BOAT_TOP_LEFT_TILE_1, FLOODED_BOAT_TOP_RIGHT_TILE_2);
    public static final RSArea FLOODED_BOT_AREA = new RSArea(FLOODED_BOAT_BOT_LEFT_TILE_1, FLOODED_BOAT_BOT_RIGHT_TILE_2);
    public static final RSArea FLOODED_BOT_HOLE_AREA = new RSArea(FLOODED_BOAT_BOT_LEFT_HOLE_TILE_1, FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_2);

    public static final RSArea DRY_TOP_AREA = new RSArea(DRY_BOAT_TOP_LEFT_TILE_1, DRY_BOAT_TOP_RIGHT_TILE_2);
    public static final RSArea DRY_BOT_AREA = new RSArea(DRY_BOAT_BOT_LEFT_TILE_1, DRY_BOAT_BOT_RIGHT_TILE_2);
    public static final RSArea DRY_BOT_HOLE_AREA = new RSArea(DRY_BOAT_BOT_LEFT_HOLE_TILE_1, DRY_BOAT_BOT_RIGHT_HOLE_TILE_2);

    public static final RSArea[] DUO_DRY_BOAT_AREAS = {DRY_TOP_AREA, DRY_BOT_AREA};
    public static final RSArea[] DUO_FLOODED_BOAT_AREAS = {FLOODED_TOP_AREA, FLOODED_BOT_AREA};

    // DRY AREAS
    public static final RSArea DRY_TOP_RIGHT_AREA = new RSArea(DRY_BOAT_TOP_RIGHT_TILE_1, DRY_BOAT_TOP_RIGHT_TILE_2);
    public static final RSArea DRY_TOP_LEFT_AREA = new RSArea(DRY_BOAT_TOP_LEFT_TILE_1, DRY_BOAT_TOP_LEFT_TILE_2);
    public static final RSArea DRY_BOT_RIGHT_AREA = new RSArea(DRY_BOAT_BOT_RIGHT_TILE_1, DRY_BOAT_BOT_RIGHT_TILE_2);
    public static final RSArea DRY_BOT_LEFT_AREA = new RSArea(DRY_BOAT_BOT_LEFT_TILE_1, DRY_BOAT_BOT_LEFT_TILE_2);

    // DRY HOLE AREA
    public static final RSArea DRY_BOT_RIGHT_HOLE_AREA = new RSArea(DRY_BOAT_BOT_RIGHT_HOLE_TILE_1, DRY_BOAT_BOT_RIGHT_HOLE_TILE_2);
    public static final RSArea DRY_BOT_LEFT_HOLE_AREA = new RSArea(DRY_BOAT_BOT_LEFT_HOLE_TILE_1, DRY_BOAT_BOT_LEFT_HOLE_TILE_2);

    // FLOODED AREAS
    public static final RSArea FLOODED_TOP_RIGHT_AREA = new RSArea(FLOODED_BOAT_TOP_RIGHT_TILE_1, FLOODED_BOAT_TOP_RIGHT_TILE_2);
    public static final RSArea FLOODED_TOP_LEFT_AREA = new RSArea(FLOODED_BOAT_TOP_LEFT_TILE_1, FLOODED_BOAT_TOP_LEFT_TILE_2);
    public static final RSArea FLOODED_BOT_RIGHT_AREA = new RSArea(FLOODED_BOAT_BOT_RIGHT_TILE_1, FLOODED_BOAT_BOT_RIGHT_TILE_2);
    public static final RSArea FLOODED_BOT_LEFT_AREA = new RSArea(FLOODED_BOAT_BOT_LEFT_TILE_1, FLOODED_BOAT_BOT_LEFT_TILE_2);

    // FLOODED HOLE AREA
    public static final RSArea FLOODED_BOT_RIGHT_HOLE_AREA = new RSArea(FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_1, FLOODED_BOAT_BOT_RIGHT_HOLE_TILE_2);
    public static final RSArea FLOODED_BOT_LEFT_HOLE_AREA = new RSArea(FLOODED_BOAT_BOT_LEFT_HOLE_TILE_1, FLOODED_BOAT_BOT_LEFT_HOLE_TILE_2);

    public static final RSArea DRY_BOAT_AREA = new RSArea(new RSTile(1885, 4826, 0),
                                                     new RSTile(1892, 4824, 0));
    public static final RSArea FLOODED_BOAT_AREA = new RSArea(new RSTile(2013, 4826, 0),
                                                         new RSTile(2020, 4824, 0));

    public static final RSArea[] DRY_BOAT_AREAS = {DRY_TOP_LEFT_AREA, DRY_TOP_RIGHT_AREA, DRY_BOT_LEFT_AREA, DRY_BOT_RIGHT_AREA};
    public static final RSArea[] FLOODED_BOAT_AREAS = {FLOODED_TOP_LEFT_AREA, FLOODED_TOP_RIGHT_AREA, FLOODED_BOT_LEFT_AREA, FLOODED_BOT_RIGHT_AREA};

    // INTERFACE ID
    public static final int TRAWLER_INFO_BAR_INTERFACE_INDEX_ID = 366;
    public static final int TRAWLER_NET_STATUS_CHILD_ID = 26;
    // Child 28, text: "Time left: x mins, 1 min, Under 1 min
    public static final int TRAWLER_TIME_STATUS_CHILD_ID = 28;

    // WATER LEVEL SHIT
    // CHILD 25
    // child 3-22 are small segments of water level, each covering 5% range.
    // 8355839 is text coloour of water level bar. 0 if not filled
    public static final int TRAWLER_WATER_LEVEL_BAR_TEXT_COLOUR = 8355839;
    public static final int TRAWLER_WATER_LEVEL_BAR_5_PERCENT = 3;
    public static final int TRAWLER_WATER_LEVEL_BAR_10_PERCENT = 4;
    public static final int TRAWLER_WATER_LEVEL_BAR_15_PERCENT = 5;
    public static final int TRAWLER_WATER_LEVEL_BAR_20_PERCENT = 6;
    public static final int TRAWLER_WATER_LEVEL_BAR_25_PERCENT = 7;
    public static final int TRAWLER_WATER_LEVEL_BAR_30_PERCENT = 8;
    public static final int TRAWLER_WATER_LEVEL_BAR_35_PERCENT = 9;
    public static final int TRAWLER_WATER_LEVEL_BAR_40_PERCENT = 10;
    public static final int TRAWLER_WATER_LEVEL_BAR_45_PERCENT = 11;
    public static final int TRAWLER_WATER_LEVEL_BAR_50_PERCENT = 12;
    public static final int TRAWLER_WATER_LEVEL_BAR_55_PERCENT = 13;
    public static final int TRAWLER_WATER_LEVEL_BAR_60_PERCENT = 14;
    public static final int TRAWLER_WATER_LEVEL_BAR_65_PERCENT = 15;
    public static final int TRAWLER_WATER_LEVEL_BAR_70_PERCENT = 16;
    public static final int TRAWLER_WATER_LEVEL_BAR_75_PERCENT = 17;
    public static final int TRAWLER_WATER_LEVEL_BAR_80_PERCENT = 18;
    public static final int TRAWLER_WATER_LEVEL_BAR_85_PERCENT = 19;
    public static final int TRAWLER_WATER_LEVEL_BAR_90_PERCENT = 20;
    public static final int TRAWLER_WATER_LEVEL_BAR_95_PERCENT = 21;
    public static final int TRAWLER_WATER_LEVEL_BAR_100_PERCENT = 22;




}
