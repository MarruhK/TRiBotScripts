package scripts.dmmrcer.data.runecraftdata.botinstance.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.data.runecraftdata.botinstance.BotInstance;
import scripts.gengarlibrary.GClicking;

public abstract class Rcer extends BotInstance
{
    public Rcer()
    {

    }

    public abstract void reset();
    public abstract void getToRuins();
    public abstract boolean shouldReset();
    public abstract boolean shouldWaitForMule();
}
