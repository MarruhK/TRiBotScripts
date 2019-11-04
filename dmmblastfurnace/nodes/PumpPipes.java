package scripts.dmmblastfurnace.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmblastfurnace.data.Constants;
import scripts.dmmblastfurnace.data.Vars;
import scripts.dmmblastfurnace.framework.Node;
import scripts.dmmblastfurnace.helperfunctions.Functions;
import scripts.dmmblastfurnace.utility.Antiban;


/*
    Data collected:

    For gauge to enter green zone: 		27, 25, 22, 22
    For gauge to enter mid green zone: 	30, 30, 25, 25
    For gauge to enter red zone: 		35, 34, 30, 30
    For gauge to enter mid red zone: 	40, 42, 37
    For gauge to enter very end red: 	51, 55

    Seems 35 seconds is ideal net time

    Rate of pressure increase to decrease ratio is 1:1.
*/

public class PumpPipes extends Node
{
    private static final RSTile TILE_PUMP = new RSTile(1952, 4961, 0);
    private static final int ID_BROKEN_PIPE_1 = 9117;
    private static final int ID_BROKEN_PIPE_2 = 9121;
    private static final int ID_PIPES = 9090;
    private static final int ANIMATION_OPERATE = 2432;

    private long idleStartTime = Timing.currentTimeMillis();
    private long pumpStartTime;
    private boolean isBreak = false;


    private void recalculateNetTime()
    {
        Vars.netTime = Vars.pumpTime <= Vars.idleTime ? resetTimes() : Vars.pumpTime - Vars.idleTime;
    }

    @Override
    public void execute()
    {
        // If animating and pipes are fine, then pump time increases, otherwise, decreases
        if (!arePipesBroken())
        {
            // Start pumping and track pump time
            pumpStartTime = doPump();
            Vars.idleTime = Vars.idleTime + (Timing.currentTimeMillis() - idleStartTime);
            recalculateNetTime();
            long initialPumpTime = Vars.pumpTime;

            while (isPumping() && !arePipesBroken())
            {
                General.sleep(100, 125);

                // Increase pumptime while you are pumping
                Vars.pumpTime = (Timing.currentTimeMillis() - pumpStartTime) + initialPumpTime; //resets puimptime even if positive
                recalculateNetTime();

                if (Vars.netTime >= 32500)
                {
                    // net time is too much, take break for a bit maybe 5 seconds
                    System.out.println("Overheat failsafe, clicking away.");

                    while (isPumping())
                    {
                        Objects.find(10, 9089)[0].click();
                        General.sleep(150);
                    }

                    // Stopped pumping
                    isBreak = true;
                    idleStartTime = Timing.currentTimeMillis();
                    General.sleep(5000);
                }
            }

            // Close temp gauge interface
            if (Interfaces.get(30) != null)
            {
                Interfaces.get(30, 4).click();
            }
        }
        else
        {
            // pipes are broken
            System.out.println("Pipes are broken.");
            if (!isBreak)
            {
                idleStartTime = Timing.currentTimeMillis();
            }
            else
            {
                isBreak = false;
            }


            // Not pumping properly
            while (arePipesBroken())
            {
                General.sleep(100, 125);

                // Increase idle time as you are not pumping
                if (arePipesBroken())
                    fixPipes();
            }
        }
    }








    /*private void oldCode()
    {
        if (Player.getAnimation() != ANIMATION_OPERATE)
        {
            // Click on the pump
            if (!Functions.clickObject(ANIMATION_OPERATE,  "Operate", ID_PIPES))
                DaxWalker.walkTo(TILE_PUMP);
        }

        int currentXP;
        long currentTime = Timing.currentTimeMillis();

        while (Player.getAnimation() == ANIMATION_OPERATE)
        {
            currentXP = Skills.getXP(Skills.SKILLS.STRENGTH);
            General.sleep(800); // 4 exp per second is exp rate

            if (currentXP != Skills.getXP(Skills.SKILLS.STRENGTH))
            {
                // Gaining exp
                pumpTime += Timing.currentTimeMillis() - currentTime;
            }
            else
            {
                idleTime += Timing.currentTimeMillis() - currentTime;
            }

            currentTime = Timing.currentTimeMillis();
            netTime = (pumpTime - idleTime);

            if (netTime > 29000)
            {
                // Might overheat, take a break 10 seconds
                System.out.println("Overheat failsafe, clicking away.");
                WebWalking.walkTo(TILE_PUMP);

                // 15 seconds wait
                int timeout = 10000;
                long startingTime = Timing.currentTimeMillis();

                for (int i = 0; i <= 33; i++)
                {
                    if (Timing.currentTimeMillis() - startingTime > timeout)
                        break;

                    scripts.gengarlibrary.Antiban.get().timedActions();
                    General.sleep(300);
                }

                idleTime += 13000;
            }
            else if (netTime < 0)
            {
                // idle time > pumping time so reset everything
                System.out.println("Resetting tmies.");
                resetTimes();
                netTime = 0;
            }

            System.out.println("PUMP TIME: " + pumpTime);
            System.out.println("IDLE TIME: " + idleTime);
            System.out.println("NET TIME: " + netTime);
        }
    }*/

    private void fixPipes()
    {
        int animation = 898;
        System.out.println("fixPipes(): Need to fix pipes.");

        while (Objects.find(15, ID_BROKEN_PIPE_1, ID_BROKEN_PIPE_2).length > 0)
        {
            Functions.clickObject(animation, "Repair", ID_BROKEN_PIPE_1, ID_BROKEN_PIPE_2);
        }
    }

    private long doPump()
    {
        // Ensure pump is clicked
        if (isPumping())
            return Timing.currentTimeMillis();

        while(!Functions.clickObject(ANIMATION_OPERATE,  "Operate", ID_PIPES))
        {
            DaxWalker.walkTo(TILE_PUMP);
            General.sleep(100, 125);
        }

        return Timing.currentTimeMillis();
    }

    private boolean isPumping()
    {
        return Player.getAnimation() == ANIMATION_OPERATE;
    }

    private boolean arePipesBroken()
    {
        return Objects.find(15, ID_BROKEN_PIPE_1, ID_BROKEN_PIPE_2).length > 0;
    }

    private long resetTimes()
    {
        Vars.pumpTime = 0;
        Vars.idleTime = 0;
        Vars.netTime = 0;
        idleStartTime = 0;

        return 0;
    }

    @Override
    public boolean validate()
    {
        return Constants.AREA_BF.contains(Player.getPosition());
    }
}
