package scripts.dmmrcer.data.runecraftdata.botinstance;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.dmmrcer.data.Vars;
import scripts.gengarlibrary.GClicking;

public abstract class BotInstance
{
    public Boolean isUsingTalisman;

    public BotInstance()
    {
        // Cache as you shouldn't be losing this (unless you die so maybe i do need to fix this)
        isUsingTalisman = Inventory.find(Vars.altar.getTalismanId()).length > 0;
    }

    public boolean enterRuins()
    {
        RSObject[] ruins = Objects.find(10, Vars.altar.getRuinsId());

        if (ruins.length == 0)
        {
            System.out.println("execute: Unable to find Mysterious ruins.");
            return false;
        }

        String uptext;

        if (isUsingTalisman)
        {
            RSItem[] talisman = Inventory.find(Vars.altar.getTalismanId());

            if (talisman.length == 0)
            {
                System.out.println("enterRuins: You don't have a talisman, ending script");
                Vars.shouldExecute = false;
            }

            talisman[0].click();

            uptext = "Use "+ Vars.altar.getAltarName().substring(0, Vars.altar.getAltarName().indexOf(" ") + 1) + "talisman -> Mysterious ruins";
            System.out.println(uptext);
        }
        else
        {
            uptext = "Enter";
        }

        if (GClicking.clickObject(ruins[0], uptext))
        {
            return Timing.waitCondition(()->
            {
                General.sleep(100);
                return Player.getPosition().distanceTo(Vars.altar.getAltarTile()) <= 15;
            }, General.random(3150, 3200));
        }

        System.out.println("enterRuins: Failed to click on ruins");
        return false;
    }
}
