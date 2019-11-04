package scripts.tutisland.data;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants
{
    // TILES AND AREAS____________________________________________________________________________________________________________

    // DC + GS
    private static final RSTile T11_DCGS = new RSTile(3097, 3111, 0);
    private static final RSTile T12_DCGS = new RSTile(3087, 3101, 0);
    public static final RSArea AREA_DCGS = new RSArea(T11_DCGS, T12_DCGS);

    // DST
    private static final RSTile T11_DST = new RSTile(3097, 3087, 0);
    private static final RSTile T12_DST = new RSTile(3106, 3100, 0);
    public static final RSArea AREA_DST = new RSArea(T11_DST, T12_DST);

    // DCS
    private static final RSTile T11_DCS = new RSTile(3073, 3089, 0);
    private static final RSTile T12_DCS = new RSTile(3077, 3091, 0);
    public static final RSArea AREA1_DCS = new RSArea(T11_DCS, T12_DCS);

    private static final RSTile T21_DCS = new RSTile(3074, 3087, 0);
    private static final RSTile T22_DCS = new RSTile(3075, 3088, 0);
    public static final RSArea AREA2_DCS = new RSArea(T21_DCS, T22_DCS);

    private static final RSTile T31_DCS = new RSTile(3073, 3083, 0);
    private static final RSTile T32_DCS = new RSTile(3078, 3086, 0);
    public static final RSArea AREA3_DCS = new RSArea(T31_DCS, T32_DCS);

    private static final RSTile T41_DCS = new RSTile(3075, 3082, 0);
    private static final RSTile T42_DCS = new RSTile(3076, 3082, 0);
    public static final RSArea AREA4_DCS = new RSArea(T41_DCS, T42_DCS);

    private static final RSTile T51_DCS = new RSTile(3072, 3089, 0);
    private static final RSTile T52_DCS = new RSTile(3072, 3091, 0);
    public static final RSArea AREA5_DCS = new RSArea(T51_DCS, T52_DCS);

    // QGR
    private static final RSTile T11_QGR = new RSTile(3083, 3119, 0);
    private static final RSTile T12_QGR = new RSTile(3089, 3125, 0);
    public static final RSArea AREA1_QGR = new RSArea(T11_QGR, T12_QGR);

    private static final RSTile T21_QGR = new RSTile(3080, 3119, 0);
    private static final RSTile T22_QGR = new RSTile(3082, 3123, 0);
    public static final RSArea AREA2_QGR = new RSArea(T21_QGR, T22_QGR);

    // MA
    private static final RSTile T11_MA = new RSTile(3070, 9492, 0);
    private static final RSTile T12_MA = new RSTile(3094, 9525, 0);
    public static final RSArea AREA_MA = new RSArea(T11_MA, T12_MA);

    // CTR
    private static final RSTile T11_CTR_RAT_PIT = new RSTile(3105, 9511, 0);
    private static final RSTile T12_CTR_RAT_PIT = new RSTile(3094, 9525, 0);
    public static final RSArea A1_CTR_RAT_PIT = new RSArea(T11_CTR_RAT_PIT, T12_CTR_RAT_PIT);

    private static final RSTile T21_CTR_RAT_PIT = new RSTile(3109, 9514, 0);
    private static final RSTile T22_CTR_RAT_PIT = new RSTile(3107, 9522, 0);
    public static final RSArea A2_CTR_RAT_PIT = new RSArea(T21_CTR_RAT_PIT, T22_CTR_RAT_PIT);

    private static final RSTile T31_CTR_RAT_PIT = new RSTile(3110, 9518, 0);
    private static final RSTile T32_CTR_RAT_PIT = new RSTile(3110, 9519, 0);
    public static final RSArea A3_CTR_RAT_PIT = new RSArea(T31_CTR_RAT_PIT, T32_CTR_RAT_PIT);

    private static final RSTile T1_CTR = new RSTile(3095, 9492, 0);
    private static final RSTile T2_CTR = new RSTile(3124, 9532, 0);
    public static final RSArea AREA_CTR = new RSArea(T1_CTR, T2_CTR);

    // DB
    private static final RSTile T11_DB = new RSTile(3118, 3125,0);
    private static final RSTile T12_DB = new RSTile(3124, 3119,0);
    public static final RSArea AREA1_DB = new RSArea(T11_DB, T12_DB);

    private static final RSTile T21_DB = new RSTile(3125, 3125,0);
    private static final RSTile T22_DB = new RSTile(3129, 3123,0);
    public static final RSArea AREA2_DB = new RSArea(T21_DB, T22_DB);

    // DMS
    private static final RSTile T11_DMS = new RSTile(3128, 3103, 0);
    private static final RSTile T12_DMS = new RSTile(3120, 3110, 0);
    public static final RSArea AREA_DMS = new RSArea(T11_DMS, T12_DMS);

    // DWS
    private static final RSTile T11_DWS = new RSTile(3140, 3084, 0);
    private static final RSTile T12_DWS = new RSTile(3142, 3089, 0);
    public static final RSArea AREA1_DWS = new RSArea(T11_DWS, T12_DWS);

    private static final RSTile T21_DWS = new RSTile(3138, 3091, 0);
    private static final RSTile T22_DWS = new RSTile(3140, 3091, 0);
    public static final RSArea AREA2_DWS = new RSArea(T21_DWS, T22_DWS);

    private static final RSTile T31_DWS = new RSTile(3139, 3090, 0);
    private static final RSTile T32_DWS = new RSTile(3141, 3090, 0);
    public static final RSArea AREA3_DWS = new RSArea(T31_DWS, T32_DWS);

    private static final RSTile T41_DWS = new RSTile(3139, 3083, 0);
    private static final RSTile T42_DWS = new RSTile(3141, 3083, 0);
    public static final RSArea AREA4_DWS = new RSArea(T41_DWS, T42_DWS);

    private static final RSTile T51_DWS = new RSTile(3138, 3082, 0);
    private static final RSTile T52_DWS = new RSTile(3140, 3082, 0);
    public static final RSArea AREA5_DWS = new RSArea(T51_DWS, T52_DWS);

    // LUMBY
    public static final RSTile TILE_LUMBY = new RSTile(3233, 3230, 0);

    private static final RSTile T1_LUMBY_CASTLE = new RSTile(3220, 3221, 0);
    private static final RSTile T2_LUMBY_CASTLE = new RSTile(3224, 3216, 0);
    public static final RSArea AREA_LUMBY_CASTLE = new RSArea(T1_LUMBY_CASTLE, T2_LUMBY_CASTLE);

    public static final boolean isInStartingArea()
    {
        if (AREA_DCGS.contains(Player.getPosition()))
        {
            System.out.println("Constants -- isInStartingArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInSurvivalArea()
    {
        if (AREA_DST.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInSurvivalArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInChefArea()
    {
        if     (AREA1_DCS.contains(Player.getPosition()) ||
                AREA2_DCS.contains(Player.getPosition()) ||
                AREA3_DCS.contains(Player.getPosition()) ||
                AREA4_DCS.contains(Player.getPosition()) ||
                AREA5_DCS.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInChefArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInQuestGuideArea()
    {
        if     (AREA1_QGR.contains(Player.getPosition()) ||
                AREA2_QGR.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInQuestGuideArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInMiningArea()
    {
        if (AREA_MA.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInMiningArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInCombatTrainingArea()
    {
        if (AREA_CTR.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInCombatTrainingArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInBankingArea()
    {
        if     (AREA1_DB.contains(Player.getPosition()) ||
                AREA2_DB.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInBankingArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInMonkArea()
    {
        if (AREA_DMS.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInMonkArea: TRUE");
            return true;
        }
        return false;
    }

    public static final boolean isInWizardArea()
    {
        if     (AREA1_DWS.contains(Player.getPosition()) ||
                AREA2_DWS.contains(Player.getPosition()) ||
                AREA3_DWS.contains(Player.getPosition()) ||
                AREA4_DWS.contains(Player.getPosition()) ||
                AREA5_DWS.contains(Player.getPosition()))
        {
            System.out.println("_________________________________________________________________________________________");
            System.out.println("Constants -- isInWizardArea: TRUE");
            return true;
        }
        return false;
    }
}
