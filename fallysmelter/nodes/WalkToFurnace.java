package scripts.fallysmelter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.fallysmelter.data.Data;
import scripts.fallysmelter.framework.Node;

public class WalkToFurnace extends Node
{
    @Override
    public void execute()
    {
        General.println("WalkFurnace");
        DaxWalker.walkTo(Data.TILE_FURNACE);
    }

    @Override
    public boolean validate()
    {
        return Player.getPosition().distanceTo(Data.TILE_FURNACE) > 5 &&
                Inventory.getCount(Data.ID_IRON_ORE) > 0 &&
                Equipment.isEquipped(Data.ID_RING_FORGE);
    }
}
