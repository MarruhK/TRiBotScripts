package scripts.dmmrcer.data.runecraftdata.altars;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.data.runecraftdata.Runes;
import scripts.gengarlibrary.GClicking;

public abstract class Altar
{
    private String altarName;
    private RSTile altarTile;
    private RSTile ruinsTile;
    private int altarId;
    private int ruinsId;
    private int tiaraId;
    private int talismanId;

    private Boolean isUsingTalisman;

    public Altar(Runes rune)
    {
        switch (rune)
        {
            case FIRE:
                this.altarTile = new RSTile(2583, 4840, 0);
                this.ruinsTile = new RSTile(3311, 3253, 0);
                this.altarId = 34764;
                this.ruinsId = 34817;
                this.tiaraId = 5537;
                this.talismanId = 1442;
                this.altarName = "Fire Altar";
                break;

            case WATER:
                break;

            case AIR:
                this.altarTile = new RSTile(2400, 4839, 0);
                this.ruinsTile = new RSTile(2987, 3294, 0);
                this.altarId = 2000;
                this.ruinsId = 2053;
                this.tiaraId = 5527;
                this.altarName = "Air Altar";
                break;

            case EARTH:
                break;

            case MIND:
                break;

            case BODY:
                break;

            case DEATH:
                break;

            case NATURE:
                this.altarTile = new RSTile(2400, 4839, 0);
                this.ruinsTile = new RSTile(2867, 3018, 0);
                this.altarId = 2000;
                this.ruinsId = 2053;
                this.tiaraId = 5541;
                this.altarName = "Nature Altar";
                break;

            case CHAOS:
                break;

            case LAW:
                this.altarTile = new RSTile(2464, 4830, 0);
                this.ruinsTile = new RSTile(2858, 3379, 0);
                this.altarId = 34767;
                this.ruinsId = 34820;
                this.tiaraId = -1;
                this.talismanId = 1458;
                this.altarName = "Law Altar";
                break;

            case COSMIC:
                break;

            case BLOOD:
                break;

            case SOUL:
                break;

            case WRATH:
                break;
        }
    }

    public RSTile getAltarTile()
    {
        return altarTile;
    }
    public RSTile getRuinsTile()
    {
        return ruinsTile;
    }
    public int getAltarId()
    {
        return altarId;
    }
    public int getRuinsId()
    {
        return ruinsId;
    }
    public int getTiaraId()
    {
        return tiaraId;
    }
    public int getTalismanId()
    {
        return talismanId;
    }
    public String getAltarName()
    {
        return altarName;
    }
}
