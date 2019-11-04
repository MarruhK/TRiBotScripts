package scripts.dmmblastfurnace.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmblastfurnace.data.Constants;
import scripts.dmmblastfurnace.framework.Node;

import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;

public class TravelToKeldagrim extends Node
{
    @Override
    public void execute()
    {
        final int ID_CART = 16168;
        final RSTile TILE_CART = new RSTile(3142, 3503, 0);


        DaxWalker.walkTo(TILE_CART);

        RSObject[] carts = Objects.find(4, ID_CART);

        if (carts.length > 0 && carts != null)
        {
            AccurateMouse.click(carts[0], "Travel");

            if (Timing.waitCondition(new Condition()
            {
                @Override
                public boolean active()
                {
                    General.sleep(100);
                    return Constants.AREA_KELD.contains(Player.getPosition());
                }
            }, 5000))
                System.out.println("arrived at keld");
        }
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_GE.contains(Player.getPosition());
    }
}
