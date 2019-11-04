package scripts.dmmblastfurnace.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSGroundItem;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmblastfurnace.data.Constants;
import scripts.dmmblastfurnace.framework.Node;
import scripts.dmmblastfurnace.helperfunctions.Functions;
import scripts.dmmblastfurnace.utility.Antiban;

import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;

public class RefuelCoke extends Node
{
    private final int ANIMATION_COLLECT = 2441;
    private final int ANIMATION_REFUEL = 2442;

    private final int[] ID_STOVE = {9085, 9086, 9087};
    private final int ID_COKE_BOX = 9088;

    private final int ID_SPADE = 952;
    private final int ID_SPADE_COKE = 6448;

    @Override
    public void execute()
    {
        if (Interfaces.get(229, 1) != null)
        {
            // 15 seconds wait
            int timeout = 15000;
            long startingTime = Timing.currentTimeMillis();

            for (int i = 0; i <= 50; i++)
            {
                if (Timing.currentTimeMillis() - startingTime > timeout)
                    break;

                Antiban.get().timedActions();
                General.sleep(300);
            }
        }

        if (Inventory.find(ID_SPADE).length > 0)
        {
            Functions.clickObject(ANIMATION_COLLECT, "Collect", ID_COKE_BOX);
        }
        else if (Inventory.find(ID_SPADE_COKE).length > 0)
        {
            Functions.clickObject(ANIMATION_REFUEL,  "Refuel", ID_STOVE);
        }
        else
        {
            // Dont have a spade on you, loot.
            RSGroundItem[] spades = GroundItems.find(ID_SPADE);

            if (spades.length > 0 && spades != null)
            {
                AccurateMouse.click(spades[0], "Take");
                // Maybe add wait to ensure it is looted?
            }
            else
            {
                // walk to the tile where the coke shit is
                DaxWalker.walkTo(Constants.TILE_REFUEL_COKE);

                // maybe need to add a wait or some shit idk.
            }
        }

        // Add small sleep i guess here? test
    }

    @Override
    public boolean validate()
    {
        return   Constants.AREA_BF.contains(Player.getPosition()) &&
                (Inventory.find(ID_SPADE).length > 0 || Inventory.find(ID_SPADE_COKE).length > 0);
    }
}
