package scripts.dmmrcer.data;

import scripts.dmmrcer.data.runecraftdata.altars.Altar;
import scripts.dmmrcer.data.runecraftdata.Rune;
import scripts.dmmrcer.data.runecraftdata.botinstance.rcer.Rcer;
import scripts.dmmrcer.data.runecraftdata.botinstance.runners.Runner;
import scripts.gengarlibrary.Antiban;

public class Vars
{
    private Vars(){}

    public static boolean shouldExecute = false;

    public static String muleName;
    public static String rcerName;

    public static Rune rune;
    public static Altar altar;
    public static Runner runner;
    public static Rcer rcer;

    public static boolean isRunnerComingInAltar = false;
    public static boolean isRcerBanking = false;
}
