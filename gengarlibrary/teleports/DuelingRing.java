package scripts.gengarlibrary.teleports;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.gengarlibrary.GBooleanSuppliers;

public class DuelingRing
{
    public enum Teleports
    {
        DUEL_ARENA,
        CASTLWARS,
        CLAN_WARS
    }

    private DuelingRing(){}

    private static final int[] RING_OF_DUELING_IDS = {2566, 2564, 2562, 2560, 2558, 2556, 2554, 2552};
    private static final int RING_TELE_ANIMATION = 714;

    public static int getCharges()
    {
        for (int i = 0; i < RING_OF_DUELING_IDS.length; i++)
        {
            if (Equipment.isEquipped(RING_OF_DUELING_IDS[i]))
            {
                return ++i;
            }

            if (Inventory.find(RING_OF_DUELING_IDS[i]).length > 0)
            {
                return ++i;
            }
        }

        return 0;
    }

    public static boolean teleport(Teleports option)
    {
        switch (option)
        {
            case DUEL_ARENA:
                return teleport("Duel Arena");

            case CASTLWARS:
                return teleport("Castle Wars");

            case CLAN_WARS:
                return teleport("Clan Wars");
        }

        System.out.println("teleport: Invalid option given.");
        return false;
    }

    private static boolean teleport(String option)
    {
        // equip ring if found in inventory.
        RSItem[] duelRing = Inventory.find(DuelingRing.getId());

        if (duelRing.length > 0)
        {
            duelRing[0].click();
        }

        Timing.waitCondition(()->
        {
            General.sleep(125, 240);
            return Equipment.find(RING_OF_DUELING_IDS).length > 0;
        }, 1900);

        // Equipment check
        RSItem[] ring = Equipment.find(RING_OF_DUELING_IDS);

        if (ring.length != 0)
        {
            return ring[0].click(option) && waitForTeleport() && GameTab.open(GameTab.TABS.INVENTORY);
        }

        // Check if a ring is in inventory - failsafe
        RSItem[] ringsInventory  = Inventory.find(RING_OF_DUELING_IDS);

        if (ringsInventory.length != 0)
        {
            RSItem ringInventory = ringsInventory[0];

            if (ringInventory.click("Rub") && waitForInterface(option))
            {
                RSInterface teleInterface = Entities.find(InterfaceEntity::new).textContains(option).getFirstResult();

                if (teleInterface != null)
                {
                    return teleInterface.click() && waitForTeleport();
                }
            }

            System.out.println("teleport: Failed to rub the dueling ring.");
            return false;
        }

        System.out.println("teleport: Unable to find the ring of dueling.");
        return false;
    }

    private static boolean waitForTeleport()
    {
        if (Timing.waitCondition(GBooleanSuppliers.waitForAnimation(RING_TELE_ANIMATION), 2450) &&
            Timing.waitCondition(GBooleanSuppliers.waitForAnimation(-1), 2450))
        {
            // Wait tick to ensure no buggy behaviour
            General.sleep(650, 730);
            return true;
        }

        return false;
    }

    private static boolean waitForInterface(String option)
    {
        return Timing.waitCondition(()->
        {
            General.sleep(120);
            return Entities.find(InterfaceEntity::new).textContains(option).getFirstResult() != null;
        }, 2500);
    }

    public static int[] getId()
    {
        return RING_OF_DUELING_IDS;
    }
}
