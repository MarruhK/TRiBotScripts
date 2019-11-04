package scripts.gengarcooker.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.gengarcooker.framework.Node;
import scripts.gengarcooker.nodes.BankFood;
import scripts.gengarcooker.nodes.CookFish;
import scripts.gengarcooker.nodes.WalkVarrockBank;
import scripts.gengarcooker.nodes.WalkVarrockRange;

public abstract class Constants {
    // GUI STUFF
    public static final String[] GAME_MODES = {" ", "07", "DMM", "SDMM"};
    public static final int[] GAME_MODE_MULTIPLIER = {0, 1, 5, 10};

    // FOOD DATA
    public static final String[] COOKABLE_FOODS = {" ", "Trout", "Salmon", "Tuna", "Lobster", "Swordfish", "Monkfish", "Sharks", "Angerfish"};
    public static final int[] COOKABLE_FOODS_EXP = {0, 70, 90, 100, 120, 140, 150, 210, 230};
    public static final int[] COOKABLE_FOODS_ID = {0, 335, 331, 359, 377, 371, 7944, 383, 13439};

    // LOCATION DATA
    public static final String[] LOCATIONS = {" ", "Hosidius", "Varrock East"};
    public static final Node[][] NODES = {null, {new BankFood(), new CookFish()}, {new BankFood(), new CookFish(), new WalkVarrockBank(), new WalkVarrockRange()}};
    public static final String[] RANGE_NAMES = {" ", "Clay oven", "Range"};
    public static final int[] RANGE_IDS = {0, 21302, 0};


    // ANIMATION
    public static final int COOKING_ANIMATION = 896;



    // ___________________________________________VARROCK EAST__________________________________________________________
    // OBJECTS
    public static final int CLOSED_RANGE_DOOR_ID = 11780;
    public static final int OPENED_RANGE_DOOR_ID = 11778;
    public static final int VARROCK_RANGE_ID = 7183;

    // TILES
    public static final RSTile OUTSIDE_DOOR_RANGE_TILE = new RSTile(3242, 3412, 0);
    public static final RSTile INSIDE_DOOR_RANGE_TILE = new RSTile(3241, 3412, 0);
    public static final RSTile RANGE_TILE = new RSTile(3238, 3410, 0);
    public static final RSTile BANK_TILE = new RSTile(3252, 3420, 0);

    // FOR RS AREA BELOW, INDICATING YOUR IN LITTLE HOUSE
    public static final RSTile NW_RANGE_TILE = new RSTile(3236, 3416, 0);
    public static final RSTile SE_RANGE_TILE = new RSTile(3240, 3409, 0);
    public static final RSTile SMALL_N_RANGE_TILE = new RSTile(3241, 3413, 0);
    public static final RSTile SMALL_S_RANGE_TILE = new RSTile(3241, 3412, 0);

    // AREAS
    public static final RSArea RANGE_AREA_1 = new RSArea(NW_RANGE_TILE, SE_RANGE_TILE);
    public static final RSArea RANGE_AREA_2 = new RSArea(SMALL_N_RANGE_TILE, SMALL_S_RANGE_TILE);


    // _________________________________________________________________________________________________________________



}
