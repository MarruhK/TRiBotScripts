package scripts.fallysmelter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.fallysmelter.data.Data;
import scripts.fallysmelter.framework.Node;

public class WalkToBank extends Node
{
    @Override
    public void execute()
    {
        General.println("WalkBank");
        DaxWalker.walkTo(Data.TILE_BANK);
    }

    @Override
    public boolean validate()
    {
        return Player.getPosition().distanceTo(Data.TILE_BANK) > 5 && Inventory.getCount(Data.ID_IRON_ORE) <= 0;
    }
}
