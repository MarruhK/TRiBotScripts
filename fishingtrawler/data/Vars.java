package scripts.fishingtrawler.data;

import org.tribot.api2007.types.RSArea;

public abstract class Vars {
    // initialize on onStart.
    public static boolean isNetFixer = false;
    public static RSArea dryTrawlerArea;
    public static RSArea drySouthHoleTrawlerArea;
    public static RSArea floodedTrawlerArea;
    public static RSArea floodedSouthHoleTrawlerArea;
    public static int indexOfBuckets = 0;
    public static boolean shouldExecute = true;

}
