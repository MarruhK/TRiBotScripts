package scripts.dmmrelekkafisher.data;

import org.tribot.api2007.types.RSTile;
import scripts.dmmrelekkafisher.framework.Node;

public abstract class Vars
{
    public static boolean shouldExecute;
    public static String muleName;

    // Fishing-spot specific variables
    public static String fishingSpotName;
    public static String equipmentName;
    public static int fishingSpotNPCID;
    public static int[] fishID = new int[2];
    public static int fishingEquipmentID;
    public static int fishingAnimation;
    public static int depoBoxNPCID;             // must intiialize based on death locaiton
    public static RSTile fishingTile;

    // Spawn related
    public static Node Bank;
    public static Node walkToBank;

    // Concurrency Variable
    public static boolean isPkerDetected = false;   // Irrelevant atm.
}
