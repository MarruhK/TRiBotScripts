package scripts.fallysmelter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.fallysmelter.data.Data;
import scripts.fallysmelter.framework.Node;
import scripts.fallysmelter.utility.Antiban;

public class Smelt extends Node
{
    private static final RSTile TILE_FURNACE = new RSTile(2975, 3369, 0);

    @Override
    public void execute()
    {
        General.println("Smelt ----------------------------------------------------------");
        int furnaceId = 24009;

        RSObject[] furnaces = Objects.find(5, furnaceId);

        if (furnaces.length > 0)
        {
            furnaces[0].click();

            if (Timing.waitCondition(()->
            {
                General.sleep(100);
                return Interfaces.get(270) != null;
            }, 3500))
            {
                // Click smelt all iron ores
                if (Interfaces.get(270, 12).getChild(9) != null && !Interfaces.get(270, 12).getChild(9).getText().contains("<col"))
                {
                    // Click all
                    Interfaces.get(270, 12).getChild(9).click();
                }

                // Click iron
                if (Interfaces.get(270, 16) == null)
                    return;

                Interfaces.get(270, 16).click();

                if (Timing.waitCondition(()->
                {
                    General.sleep(100);
                    return Player.getAnimation() == 899;
                }, 3500))
                {

                    while (Inventory.getCount(Data.ID_IRON_ORE) > 0)
                    {
                        if (Timing.waitCondition(()->
                        {
                            General.sleep(100);
                            return Player.getAnimation() == 899;
                        }, 2500))
                        {
                            if (!Equipment.isEquipped(Data.ID_RING_FORGE))
                            {
                                // Ring ran out of charges, leave area.
                                WebWalking.walkTo(TILE_FURNACE);
                                return;
                            }
                            Antiban.get().timedActions();
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean validate()
    {
        return Player.getPosition().distanceTo(TILE_FURNACE) <= 5 &&
                Inventory.getCount(Data.ID_IRON_ORE) > 0 &&
                Equipment.isEquipped(Data.ID_RING_FORGE);
    }
}
