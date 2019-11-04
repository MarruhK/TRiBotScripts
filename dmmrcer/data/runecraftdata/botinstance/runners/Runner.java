package scripts.dmmrcer.data.runecraftdata.botinstance.runners;

import org.tribot.api2007.Inventory;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.data.runecraftdata.Runes;
import scripts.dmmrcer.data.runecraftdata.botinstance.BotInstance;

public abstract class Runner extends BotInstance
{
    private int essenceTraded;

    public Runner()
    {
    }

    public int getEssenceTraded()
    {
        return essenceTraded;
    }

    public void incrementEssenceTrade(int inc)
    {
        essenceTraded += inc;
    }

    public abstract boolean shouldBank();
    public abstract boolean shouldWalkToRcer();

    public abstract void walkToBank();
    public abstract void walkToMule();
    public abstract void walkToRcer();
    public abstract void bank();
}
