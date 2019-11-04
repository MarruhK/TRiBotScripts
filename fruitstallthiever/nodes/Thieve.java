package scripts.fruitstallthiever.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.gengarlibrary.GClicking;
import scripts.fruitstallthiever.framework.Node;
import scripts.gengaragility.utility.Antiban;

public class Thieve extends Node
{
    private static final int FULL_STALL_ID = 28823;
    private static final int EMPTY_STALL_ID = 27537;
    private static final int THIEVE_ANIMATION = 823;
    private static final int TIME = 1800;

    @Override
    public void execute()
    {
        RSObject[] stalls = Objects.find(3, FULL_STALL_ID);

        if (stalls.length > 0)
        {
            while (!Inventory.isFull())
            {
                if (Timing.waitCondition(()-> Game.isUptext("Steal-from Fruit Stall"), TIME + General.random(450, 700)))
                {
                    GClicking.clickObject(stalls[0],"Steal-from", THIEVE_ANIMATION);
                    // generate reaction time
                    int waitTime = Antiban.get().generateReactionTime(TIME);

                    Antiban.get().sleepReactionTime(waitTime);
                    Antiban.get().generateTrackers(waitTime, Player.getRSPlayer().isInCombat());
                    Antiban.get().timedActions();
                }
                else
                {
                    RSObject[] stalls2 = Objects.find(3, FULL_STALL_ID, EMPTY_STALL_ID);

                    if (stalls2.length > 0 && stalls2[0].hover())
                    {
                        Antiban.get().timedActions();
                    }
                }
            }
        }
    }

    @Override
    public boolean validate()
    {
        return !Inventory.isFull() && !Player.getRSPlayer().isInCombat();
    }
}
