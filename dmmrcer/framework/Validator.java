package scripts.dmmrcer.framework;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import scripts.dmmrcer.data.Constants;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.nodes.rcer.*;
import scripts.dmmrcer.nodes.runner.Bank;
import scripts.dmmrcer.nodes.runner.Trade;
import scripts.dmmrcer.nodes.runner.WalkToBank;
import scripts.dmmrcer.nodes.runner.WalkToRcer;

public class Validator
{
    // Rcer
    private Node reset;
    private Node craftRunes;
    private Node enterRuins;
    private Node getToRuins;
    private Node waitForMule;

    // Runner
    private Node bank;
    private Node trade;
    private Node walkToBank;
    private Node walkToRcer;

    public Validator()
    {
        if (Vars.rcerName == null)
        {
            reset = new Reset();
            craftRunes = new CraftRunes();
            enterRuins = new EnterRuins();
            getToRuins = new GetToRuins();
            waitForMule = new WaitForMule();
        }
        else
        {
            bank = new Bank();
            trade = new Trade();
            walkToBank = new WalkToBank();
            walkToRcer = new WalkToRcer();
        }
    }

    public void execute()
    {
        if (Vars.rcerName == null)
        {
            executeRcer();
        }
        else
        {
            executeRunner();
        }
    }

    private void executeRcer()
    {
        while (Vars.shouldExecute)
        {
            General.sleep(125, 250);

            if (Inventory.find(Constants.PURE_ESSENCE_ID).length > 0)
            {
                if (isInAltar())
                {
                    craftRunes.execute();
                }
                else if (Player.getPosition().distanceTo(Vars.altar.getRuinsTile()) <= 9)
                {
                    enterRuins.execute();
                }
                else
                {
                    getToRuins.execute();
                }
            }
            else if (Vars.rcer.shouldWaitForMule())
            {
                waitForMule.execute();
            }
            else
            {
                // Have a check to see if u died and act appropriately, perhaps have an extra thread that shud be used insetad
                reset.execute();
            }
        }
    }

    private void executeRunner()
    {
        while (Vars.shouldExecute)
        {
            General.sleep(125, 250);

            if (Inventory.find(Constants.PURE_ESSENCE_ID).length > 0)
            {
                if (Vars.runner.shouldWalkToRcer())
                {
                    walkToRcer.execute();
                }
                else if (Vars.runner.shouldBank())
                {
                    walkToBank.execute();
                }
                else
                {
                    trade.execute();
                }
            }
            else if (Vars.runner.shouldBank())
            {
                bank.execute();
            }
            else
            {
                walkToBank.execute();
            }
        }
    }

    public boolean isInAltar()
    {
        return Player.getPosition().distanceTo(Vars.altar.getAltarTile()) <= 15;
    }
}
