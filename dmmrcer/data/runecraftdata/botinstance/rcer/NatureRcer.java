package scripts.dmmrcer.data.runecraftdata.botinstance.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.data.runecraftdata.Runes;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;

public class NatureRcer extends Rcer
{
    @Override
    public boolean shouldReset()
    {
        return false;
    }

    @Override
    public boolean shouldWaitForMule()
    {
        return false;
    }

    @Override
    public void reset()
    {
        // Fix later this is not viable as u can't ahve a house in yanille






        final int houseTeleAnimation = 4069;
        final int magicTeleAnimation = 714;
        // Have relevant teleport methods here, only house tele here for now
        // RSItem houseTele = Entities.find(ItemEntity::new).nameEquals("Teleport to house").getFirstResult();

        RSItem airRunes = Entities.find(ItemEntity::new).nameEquals("Air rune").getFirstResult();
        RSItem lawRunes = Entities.find(ItemEntity::new).nameEquals("LawAltar rune").getFirstResult();
        RSItem earthRunes = Entities.find(ItemEntity::new).nameEquals("Earth rune").getFirstResult();

        if (airRunes == null || lawRunes == null || earthRunes == null)
        {
            System.out.println("teleport: Unable to teleport, as you do not have runes.");
            return;
        }

        RSInterface icon = fetchIcon(Interfaces.get(218), "Teleport to House");

        if(GameTab.TABS.MAGIC.open())
        {
            icon.click();
        }

        // Teleprot animatino
        Timing.waitCondition(GBooleanSuppliers.waitForAnimation(magicTeleAnimation), General.random(1850, 2100));
        Timing.waitCondition(() ->
        {
            General.sleep(100);
            return Entities.find(ObjectEntity::new).nameEquals("Portal").getResults().length > 0;
        }, General.random(2850, 3100));
    }

    @Override
    public void getToRuins()
    {
        if (!WebWalking.walkTo(Vars.altar.getRuinsTile()))
        {
            System.out.println("execute: Failed to webWalk to the ruins.");
            return;
        }

        Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(5300, 5700));
        // Maybe even wait for ruin to be nearby
    }

    private static RSInterface fetchIcon(RSInterface parent, String text)
    {
        //nullcheck
        if (parent == null) return null;

        //loop trough all childeren to find compatable match
        for (RSInterface child : parent.getChildren())
        {
            if (child.getText().contains(text) || child.getComponentName().contains(text))
            {
                return child;
            }
        }

        //if child not found, return null.
        return null;
    }
}
