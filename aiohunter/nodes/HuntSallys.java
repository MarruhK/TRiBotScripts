package scripts.aiohunter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.gengarlibrary.ACamera;
import scripts.aiohunter.data.Constants;
import scripts.aiohunter.data.Methods;
import scripts.aiohunter.data.Vars;
import scripts.aiohunter.framework.Node;
import scripts.aiohunter.utilities.Antiban;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;

public class HuntSallys extends Node
{
    // Constants
    private static final int ID_TREE_EMPTY = 8990;
    private static final int ID_TREE_SET = 8989;
    private static final int ID_TREE_CHECK = 8986;

    private static final int ANIMATION_SET_TRAP = 5215;
    private static final int ANIMATION_CHECK_TRAP= 5207;

    private static final String UPTEXT_SET_TRAP = "Set-trap";
    private static final String UPTEXT_CHECK_TRAP = "Check";

    private static final RSTile TILE_AREA_1 = new RSTile(2451, 3224, 0);
    private static final RSTile TILE_AREA_2 = new RSTile(2446, 3229, 0);
    private static final RSArea AREA_HUNTING = new RSArea(TILE_AREA_1, TILE_AREA_2);

    // Hunting statistics; used in ABC2
    private long lastHuntingWaitTime;
    private long averageHuntingWaitTime = 3000;
    private long totalHuntingWaitTime = 0;
    private long totalHuntingInstances = 0;

    public HuntSallys(ACamera aCamera)
    {
        super(aCamera);
    }

    private void updateHuntingStatistics(long waitTime)
    {
        lastHuntingWaitTime = waitTime;
        totalHuntingWaitTime += lastHuntingWaitTime;
        totalHuntingInstances++;
        averageHuntingWaitTime = totalHuntingWaitTime / totalHuntingInstances;

        General.println("Average Wait Time is: " + averageHuntingWaitTime);
    }

    @Override
    public void execute()
    {
        System.out.println("____________________________________________________________________________________________________________________________________________________");
        System.out.println("The HuntSallys Node has been Validated! Executing...");

        if (!AREA_HUNTING.contains(Player.getPosition()))
        {
            DaxWalker.walkTo(Constants.TILE_RESET);
            Timing.waitCondition(conditionWaitToStopMoving(), 5500);
        }
        else if (isNetsSet())
        {
            // Nets are set so now we are about to AFK.
            doAntiban();
        }
        else  if (isEquipmentOnGround() && lootEquipment())
        {
            setTrap();
        }
        else if (isSallyCaught() && !isEquipmentOnGround())
        {
            catchSally();
        }
        else if (isNetNotSet() && !isEquipmentOnGround())
        {
            setTrap();
        }
    }

    private void doAntiban()
    {
        System.out.println("doAntiban:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        // Prior to idle antiban procedure
        long startingHuntingTime = System.currentTimeMillis();
        Antiban.get().generateTrackers((int) averageHuntingWaitTime);

        while (isNetsSet())
        {
            System.out.print("doAntiBan: Commencing idle procedures.");
            General.sleep(200);
            Antiban.get().timedActions();
        }

        updateHuntingStatistics(System.currentTimeMillis() - startingHuntingTime);
        Antiban.get().sleepReactionTime((int) averageHuntingWaitTime);
    }

    private boolean lootEquipment()
    {
        System.out.println("lootEquipment:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        RSGroundItem[] items = GroundItems.find(Constants.ID_NET, Constants.ID_ROPE);

        if (items.length > 0)
        {
            RSTile hoverTile = items[0].getPosition();

            // Gets specifically the items on the current tile, not any other traps that may have fallen.
            while (GroundItems.getAt(hoverTile).length > 0)
            {
                General.sleep(General.random(75, 130));

                RSGroundItem[] groundItems = GroundItems.getAt(hoverTile);

                if (groundItems.length <= 0 || groundItems == null){
                    System.out.println("lootEquipment~~ Ground item seems to have disapeared what?");
                    return false;
                }

                RSGroundItem groundItem = groundItems[0];
                int groundItemID = groundItem.getID();
                int currentAmount = Inventory.getCount(groundItemID);

                // Shits on the ground, loot it.
                if (AccurateMouse.click(groundItem, "Take"))
                {
                    this.aCamera.turnToTile(groundItem.getPosition());

                    if (Timing.waitCondition(conditionWaitToStopMoving(), getWalkTime(hoverTile)) &&
                        Timing.waitCondition(conditionIsItemLooted(groundItemID, currentAmount), 1500))
                    {
                        System.out.println("lootEquipment: Finished looting the equipment!");
                        return true;
                    }
                }
            }
        }
        System.out.println("lootEquipment~~ Failed to loot items");
        return false;
    }

    private void catchSally()
    {
        System.out.println("catchSally:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        RSObject[] sallyTrees = Objects.findNearest(8, ID_TREE_CHECK);

        if (sallyTrees.length > 0 && sallyTrees != null)
        {
            int initialCount = getSalamanderCount();
            int timeout = 25000;
            long currentTime = Timing.currentTimeMillis();
            RSObject sallyTree = sallyTrees[0];

            while (getSalamanderCount() == initialCount)
            {
                General.sleep(General.random(75, 130));

                if (Timing.currentTimeMillis() - currentTime > timeout)
                {
                    System.out.println("catchSally: Failed to click tree,");
                    return;
                }

                if (Methods.clickObject(sallyTree, UPTEXT_CHECK_TRAP, ANIMATION_CHECK_TRAP))
                    Timing.waitCondition(conditionIsItemLooted(Constants.ID_RED_SALLY, initialCount), 2500);
            }

            System.out.println("catchSally: Salamander caught, resetting the trap.");
            setTrap();
        }
    }

    private void setTrap()
    {
        System.out.println("setTrap:~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        RSObject[] sallyTrees = Objects.findNearest(5, ID_TREE_EMPTY);

        if (sallyTrees.length > 0 && sallyTrees != null)
        {
            int initialCount = Inventory.find(Constants.ID_ROPE).length;

            if (initialCount <= 0 || Inventory.find(Constants.ID_NET).length <= 0)
            {
                System.out.println("setTrap: Appears like you have no equipment for hunter, ending...");

                if (isEquipmentOnGround())
                {
                    lootEquipment();
                }
                else
                {
                    Vars.shouldExecute = false;
                }
                return;
            }

            int timeout = 25000;
            long currentTime = Timing.currentTimeMillis();
            RSObject sallyTree = sallyTrees[0];
            RSTile sallyTreePosition = sallyTree.getPosition();

            // Should not throw null as we already know tree exists on that tile. It can either be set or empty...
            while (Objects.getAt(sallyTreePosition)[0].getID() == ID_TREE_EMPTY)
            {
                General.sleep(General.random(75, 130));

                if (Timing.currentTimeMillis() - currentTime > timeout)
                {
                    System.out.println("setTrap: Failed to click tree,");
                    return;
                }

                if (Methods.clickObject(sallyTree, UPTEXT_SET_TRAP, ANIMATION_SET_TRAP))
                    General.sleep(250);
            }

            if (Timing.waitCondition(conditionIsTreeSet(sallyTreePosition), 2000))
            {
                System.out.println("setTrap: Trap has been set");
            }
            else
            {
                System.out.println("setTrap: Trap failed to set.... ID: " + Objects.getAt(sallyTreePosition)[0].getID());
            }
        }
    }

    private int getWalkTime(RSTile itemTile)
    {
        int tilesAway = itemTile.distanceTo(Player.getPosition());

        if (tilesAway <= 0)
            tilesAway = 0;

        return tilesAway * 1000 / 3;
    }

    private boolean isSallyCaught()
    {
        return Objects.findNearest(8, ID_TREE_CHECK).length > 0;
    }

    private int getSalamanderCount()
    {
        return Inventory.find(Constants.ID_RED_SALLY).length;
    }

    // Checks to see if equipment are on the ground
    public static boolean isEquipmentOnGround()
    {
        return GroundItems.findNearest(Constants.ID_NET, Constants.ID_ROPE).length > 0;
    }

    private boolean isNetNotSet()
    {
        return Objects.findNearest(5, ID_TREE_EMPTY).length > 0;
    }

    // Checks to see if all nets are set and ready to go.
    private boolean isNetsSet()
    {
        return Objects.findNearest(10, ID_TREE_SET).length == 3;
    }

    @Override
    public boolean validate() {
        return getSalamanderCount() < 16;
    }


    // Conditions _____________________________

    private Condition conditionWaitToStopMoving()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return !Player.isMoving();
            }
        };
    }

    private Condition conditionIsItemLooted(int itemId, int initialAmount)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Inventory.find(itemId).length > initialAmount;
            }
        };
    }

    private Condition conditionIsAnimation(int animation)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Player.getAnimation() == animation;
            }
        };
    }

    private Condition conditionIsTreeSet(RSTile tile)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Objects.getAt(tile)[0].getID() == ID_TREE_SET;
            }
        };
    }
}
