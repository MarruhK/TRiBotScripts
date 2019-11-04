package scripts.dmmrcer.data.runecraftdata.botinstance.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dmmrcer.data.Constants;
import scripts.dmmrcer.data.Vars;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;

public class LawRcer extends Rcer
{
    private static final RSTile BOAT_TILE = new RSTile(2835, 3336, 0);

    @Override
    public void reset()
    {
        if (isInAltar())
        {
            leavePortal();
        }

        // Walk to area
        DaxWalker.walkTo(BOAT_TILE);

        Timing.waitCondition(()->
        {
            General.sleep(250);
            return Player.getPosition().distanceTo(BOAT_TILE) <= 5;
        }, 5000);
    }

    @Override
    public void getToRuins()
    {
        // TODO Ensure player is in entrana

        System.out.println("getToRuins: Tile to walk to " + Vars.altar.getRuinsTile());

        if (!DaxWalker.walkTo(Vars.altar.getRuinsTile()))
        {
            System.out.println("getToRuins: Failed to walk to altar");
            return;
        }

        Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), 5000);
    }

    @Override
    public boolean shouldReset()
    {
        return false;
    }

    @Override
    public boolean shouldWaitForMule()
    {
        return Player.getPosition().distanceTo(BOAT_TILE) <= 5 && Inventory.find(Constants.PURE_ESSENCE_ID).length == 0;
    }

    private boolean isInAltar()
    {
        return Player.getPosition().distanceTo(Vars.altar.getAltarTile()) <= 20;
    }


    private boolean leavePortal()
    {
        RSObject portal = Entities.find(ObjectEntity::new).nameEquals("Portal").getFirstResult();

        if (portal != null && GClicking.clickObject(portal, "Use"))
        {
            return true;
        }

        System.out.println("leavePortal: Failed to leave the portal.");
        return false;
    }
}
