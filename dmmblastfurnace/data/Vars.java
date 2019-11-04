package scripts.dmmblastfurnace.data;

import scripts.dmmblastfurnace.framework.Node;

public abstract class Vars
{
    public static Node task;

    public static boolean shouldExecute = false;
    public static boolean safeCloseGUI = false;

    public static long pumpTime;
    public static long idleTime;
    public static long netTime;
}
