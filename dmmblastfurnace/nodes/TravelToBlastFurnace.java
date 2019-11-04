package scripts.dmmblastfurnace.nodes;

import org.tribot.api2007.Player;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmblastfurnace.data.Constants;
import scripts.dmmblastfurnace.framework.Node;


public class TravelToBlastFurnace extends Node
{
    @Override
    public void execute()
    {
        DaxWalker.walkTo(Constants.AREA_BF.getRandomTile());
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_KELD.contains(Player.getPosition());
    }
}
