package scripts.gengarnmz.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

import java.util.HashMap;

public class Constants
{
    private Constants(){}

    public static final String[] VALID_MOBS = {"Me", "King Roald", "Count Draynor", "Tree spirit", "Khazard Warlord", "Bouncer", "Black Demon", "The Kendal"};
    public static final String[] INVALID_MOBS =
            {"Witch's experiment", "Treus Dayth", "Trapped Soul",
            "The Untouchable", "The Inadequacy", "The Everlasting",
            "Tanglefoot", "Slagilith", "Skeleton Hellhound",
            "Sand Snake", "Nezikchened", "Nazastarool", "Moss giant",
            "Karamel", "Kamil", "Jungle Demon", "Ice Troll King",
            "Glod", "Giant scarab", "Giant Roc", "Gelatinnoth Mother",
            "Flambeed", "Fareed", "Evil Chicken", "Elvarg",
            "Dessous", "Dessourt", "Damis", "Dagannoth mother",
            "Dad", "Culinaromancer", "Corsair Traitor",
            "Corrupt Lizardman", "Chronozon", "Black Knight Titan",
            "Barrelchest", "Arrg", "Agrith-Naar", "Agrith-Na-Na"};


    // Items____________________________________________________________________________________________________________
    public static final int ZULRAH_SCALES_ID = 12934;
    public static final int MITHRIL_DARTS_ID = 809;
    public static final int COINS_ID = 995;

    // Weapons__________________________________________________________________________________________________________
    public static final int[] BLOWPIPE_IDS = {12926, 12924};
    public static final int[] BLUDGEON_IDS = {13263};
    public static final int[] ABBY_DAGGER_IDS = {13265};
    public static final int[] DSCIM_IDS = {4587};

    // Potions__________________________________________________________________________________________________________
    public static final int ID_PRAYER_POTION_4 = 2434;
    private static final int ID_PRAYER_POTION_3 = 139;
    private static final int ID_PRAYER_POTION_2 = 141;
    private static final int ID_PRAYER_POTION_1 = 143;
    public static final int[] ID_PRAYER_POTIONS = {ID_PRAYER_POTION_4, ID_PRAYER_POTION_3, ID_PRAYER_POTION_2, ID_PRAYER_POTION_1};

    public static final int ID_ABSORBSION_POTION_4 = 11734;
    private static final int ID_ABSORBSION_POTION_3 = 11735;
    private static final int ID_ABSORBSION_POTION_2 = 11736;
    private static final int ID_ABSORBSION_POTION_1 = 11737;
    public static final int[] ID_ABSORBSION_POTIONS = {ID_ABSORBSION_POTION_4, ID_ABSORBSION_POTION_3, ID_ABSORBSION_POTION_2, ID_ABSORBSION_POTION_1};

    private static final int ID_RANGING_POTION_4 = 2444;
    private static final int ID_RANGING_POTION_3 = 169;
    private static final int ID_RANGING_POTION_2 = 171;
    private static final int ID_RANGING_POTION_1 = 173;
    public static final int[] ID_RANGING_POTIONS = {ID_RANGING_POTION_4, ID_RANGING_POTION_3, ID_RANGING_POTION_2, ID_RANGING_POTION_1};

    private static final int ID_SUPER_COMBAT_POTION_4 = 12695;
    private static final int ID_SUPER_COMBAT_POTION_3 = 12697;
    private static final int ID_SUPER_COMBAT_POTION_2 = 12699;
    private static final int ID_SUPER_COMBAT_POTION_1 = 12701;
    public static final int[] ID_SUPER_COMBAT_POTIONS = {ID_SUPER_COMBAT_POTION_4, ID_SUPER_COMBAT_POTION_3, ID_SUPER_COMBAT_POTION_2, ID_SUPER_COMBAT_POTION_1};

    public static final int ID_SUPER_RANGING_POTION_4 = 11722;
    public static final int ID_SUPER_RANGING_POTION_3 = 11723;
    public static final int ID_SUPER_RANGING_POTION_2 = 11724;
    public static final int ID_SUPER_RANGING_POTION_1 = 11725;
    public static final int[] ID_SUPER_RANGING_POTIONS = {ID_SUPER_RANGING_POTION_4, ID_SUPER_RANGING_POTION_3, ID_SUPER_RANGING_POTION_2, ID_SUPER_RANGING_POTION_1};

    public static final int ID_OVERLOAD_4 = 11730;
    public static final int ID_OVERLOAD_3 = 11731;
    public static final int ID_OVERLOAD_2 = 11732;
    public static final int ID_OVERLOAD_1 = 11733;
    public static final int[] ID_OVERLOADS = {ID_OVERLOAD_4, ID_OVERLOAD_3, ID_OVERLOAD_2, ID_OVERLOAD_1};

    public static final int[] ID_COMBAT_POTIONS =  {ID_RANGING_POTION_4, ID_RANGING_POTION_3,
                                                    ID_RANGING_POTION_2, ID_RANGING_POTION_1,
                                                    ID_SUPER_COMBAT_POTION_4, ID_SUPER_COMBAT_POTION_3,
                                                    ID_SUPER_COMBAT_POTION_2, ID_SUPER_COMBAT_POTION_1,
                                                    ID_SUPER_RANGING_POTION_4, ID_SUPER_RANGING_POTION_3,
                                                    ID_SUPER_RANGING_POTION_2, ID_SUPER_RANGING_POTION_1,
                                                    ID_OVERLOAD_4, ID_OVERLOAD_3, ID_OVERLOAD_2, ID_OVERLOAD_1};

    // Tiles____________________________________________________________________________________________________________
    private static final RSTile TILE_BANK_1 = new RSTile(2609, 3097, 0);
    private static final RSTile TILE_BANK_2 = new RSTile(2613, 3088, 0);
    public static final RSArea AREA_BANK = new RSArea(TILE_BANK_1, TILE_BANK_2);

    private static final RSTile TILE_NMZ_1 = new RSTile(2601, 3118, 0);
    private static final RSTile TILE_NMZ_2 = new RSTile(2611, 3112, 0);
    public static final RSArea AREA_NMZ = new RSArea(TILE_NMZ_1, TILE_NMZ_2);

    private static final RSTile TILE_BARREL_2 = new RSTile(2606, 3113, 0);
    public static final RSArea AREA_BARRLES = new RSArea(TILE_NMZ_1, TILE_BARREL_2);


    /*

    // Mappings_________________________________________________________________________________________________________
    public static final HashMap<int[], String> COMBAT_POTION_MAPPINGS = new HashMap<>(4);
    public static final HashMap<int[], String> WEAPON_STRING_MAPPING = new HashMap<>();
    public static final HashMap<int[], HashMap<Integer, AttackStyles>> WEAPON_MAPPING = new HashMap<>();

    public static void initializeMappings()
    {
        initializePotionMapping();
        initializeWeaponStyles();
        initializerWeaponMappings();
    }

    private static void initializerWeaponMappings()
    {
        WEAPON_STRING_MAPPING.put(BLOWPIPE_IDS, "Blowpipe");
        WEAPON_STRING_MAPPING.put(BLUDGEON_IDS, "Bludgeon");
        WEAPON_STRING_MAPPING.put(ABBY_DAGGER_IDS, "Abyssal Dagger");
        WEAPON_STRING_MAPPING.put(DSCIM_IDS, "D Scim");
    }

    private static void initializePotionMapping()
    {
        COMBAT_POTION_MAPPINGS.put(ID_RANGING_POTIONS, "RangingPotion Potions");
        COMBAT_POTION_MAPPINGS.put(ID_SUPER_RANGING_POTIONS, "Super RangingPotion Potions");
        COMBAT_POTION_MAPPINGS.put(ID_SUPER_COMBAT_POTIONS, "Super combat Potions");
        COMBAT_POTION_MAPPINGS.put(ID_OVERLOADS, "Overloads");
    }

    private static void initializeWeaponStyles()
    {
        initializeWeaponStyles(BLOWPIPE_IDS, AttackStyles.ACCURATE_RANGE, AttackStyles.RANGE, AttackStyles.NONE, AttackStyles.LONGRANGE);
        initializeWeaponStyles(BLUDGEON_IDS, AttackStyles.STRENGTH, AttackStyles.STRENGTH, AttackStyles.NONE, AttackStyles.STRENGTH);
        initializeWeaponStyles(ABBY_DAGGER_IDS, AttackStyles.ATTACK, AttackStyles.STRENGTH, AttackStyles.CONTROLLED, AttackStyles.DEFENCE);
        initializeWeaponStyles(DSCIM_IDS, AttackStyles.ATTACK, AttackStyles.STRENGTH, AttackStyles.CONTROLLED, AttackStyles.DEFENCE);
    }

    private static void initializeWeaponStyles(int[] weaponIds, AttackStyles... styles)
    {
        HashMap<Integer, AttackStyles> stylesHashMap = new HashMap<>();

        for (int i = 0; i < styles.length; i++)
        {
            stylesHashMap.put(i, styles[i]);
        }

        WEAPON_MAPPING.put(weaponIds, stylesHashMap);
    }*/
}
