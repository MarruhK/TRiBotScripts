package scripts.gengarlibrary;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api.Clicking;

import java.awt.*;
import java.util.HashMap;

public class WorldHop
{
    //cache so we can just pull the appropriate child id for a world instead of looking for it every time
    private static final HashMap<Integer, Integer> WORLD_CHILDREN_CACHE = new HashMap<>();

    //normal logout inter constants
    private static final int NORMAL_LOGOUT_MASTER = 182;
    private static final int WORLD_SWITCHER_CHILD = 3;

    //world hopper inter constants
    private static final int HOPPER_MASTER = 69;
    private static final int HOPPER_CLOSE_CHILD = 3;
    private static final int LOADING_TEXT_CHILD = 2;
    private static final int HOPPER_LIST_CHILD = 7;
    private static final int MIN_Y = 228;
    private static final int MAX_Y = 418;

    //Option dialogue constants, the interface that appears sometimes when you hop (i.e. dangerous worlds)
    private static final int DIALOGUE_MASTER = 219;
    private static final int DIALOGUE_CHILD = 0;
    private static final int DIALOGUE_OPTION_COMPONENT = 2;

    //misc. constants
    private static final long INTERFACE_SWITCH_TIMEOUT = 3500; //max wait time for world hopper inter to load
    private static final long INTERFACE_LOAD_TIMEOUT = 5000; //max wait time for world hopper inter to load
    private static int initialWorld;

    //public API methods
    public static boolean hop(int world)
    {
        System.out.println("hop:");

        initialWorld = world;

        if (world > 100)
            world %= 100;

        //open hopper interface if necessary
        if(GameTab.open(GameTab.TABS.LOGOUT) && (isHopperUp() || openHopper()))
        {
            try
            {
                final RSInterface WORLD_LIST = Interfaces.get(HOPPER_MASTER, HOPPER_LIST_CHILD);
                final Integer WORLD_CHILD = WORLD_CHILDREN_CACHE.get(world);    // get world value i thnk

                //load cache if necessary
                if((WORLD_CHILDREN_CACHE.isEmpty() || WORLD_CHILD == null) // HASHMAP
                        && WORLD_LIST != null)
                    loadCache(WORLD_LIST);

                final RSInterface TARGET_WORLD = WORLD_LIST.getChild(WORLD_CHILDREN_CACHE.get(world));

                //check if world is scrolled into view
                if(isWorldVisible(TARGET_WORLD) || scrollWorldIntoView(WORLD_LIST, TARGET_WORLD))
                {
                    return clickWorld(WORLD_LIST.getChild(WORLD_CHILDREN_CACHE.get(world)), world) //Can't use cached TARGET_WORLD due to it's pos changing on scroll
                            &&
                            Timing.waitCondition(isHopSuccesful(), 6000);
                }
            }
            catch(Exception e){}
        }

        return false;
    }

    //antiban methods
    public static boolean isHopperUp()
    {
        return Interfaces.get(HOPPER_MASTER) != null;
    }

    private static boolean openHopper()
    {
        System.out.println("openHopper:");
        final RSInterface WORLD_SWITCHER_BUTTON = Interfaces.get(NORMAL_LOGOUT_MASTER, WORLD_SWITCHER_CHILD);

        if(WORLD_SWITCHER_BUTTON != null && WORLD_SWITCHER_BUTTON.click())
        {
            System.out.println("openHopper: Successfully clicked the world hopping button.");
            return Timing.waitCondition(doesInterfaceExist(HOPPER_MASTER), INTERFACE_SWITCH_TIMEOUT);
            //&& Timing.waitCondition(FCConditions.interTextNotContains(HOPPER_MASTER, LOADING_TEXT_CHILD, "Loading..."), INTERFACE_LOAD_TIMEOUT);
            // ^ for laggy shit helpful for later.
        }
        return false;
    }

    public static boolean closeHopper()
    {
        final RSInterface CLOSE_BUTTON = Interfaces.get(HOPPER_MASTER, HOPPER_CLOSE_CHILD);

        if(CLOSE_BUTTON != null && Clicking.click(CLOSE_BUTTON))
            return Timing.waitCondition(doesInterfaceNotExist(HOPPER_MASTER), 1500);

        return false;
    }

    private static boolean isWorldVisible(RSInterface targetWorld)
    {
        Rectangle rect = targetWorld.getAbsoluteBounds();   // y axis changes based on where it is

        return rect.y > MIN_Y && rect.y < MAX_Y;
    }

    private static boolean scrollWorldIntoView(RSInterface worldList, RSInterface targetWorld)
    {
        final long START_TIME = Timing.currentTimeMillis();
        final long TIMEOUT = 7000;
        final Rectangle WORLD_LIST_BOUNDS = worldList.getAbsoluteBounds();

        Rectangle targetRectangle;

        do
        {
            //move mouse into world list interface if necessary
            if(!WORLD_LIST_BOUNDS.contains(Mouse.getPos()))
                Mouse.moveBox(WORLD_LIST_BOUNDS);

            //scroll in appropriate direction
            targetRectangle = targetWorld.getAbsoluteBounds();
            Mouse.scroll(targetRectangle.y < MIN_Y);
            if(Timing.timeFromMark(START_TIME) > TIMEOUT)
                return false;
            General.sleep(10, 40);
        }
        while(!isWorldVisible(targetWorld));

        General.sleep(70, 120);
        return true;
    }

    private static boolean clickWorld(RSInterface targetWorld, int worldNum)
    {
        if(Clicking.hover(targetWorld) && Timing.waitCondition(uptextContains("Switch" + ("" + worldNum)), 1000))
        {
            Mouse.click(1);

            // Check for that shitty hopping dialogue that may occur and clicks option 2.
            if(Timing.waitCondition(doesInterfaceExist(DIALOGUE_MASTER), 1500))
            {
                final RSInterface DIALOGUE = Interfaces.get(DIALOGUE_MASTER, DIALOGUE_CHILD);
                Clicking.click(DIALOGUE.getChild(DIALOGUE_OPTION_COMPONENT));
            }

            // idk what this is for.
            // return Timing.waitCondition(FCConditions.onWorldCondition(worldNum), 5000);
        }
        return false;
    }

    // Creates <key, value> shit where key is world and value is world component value.
    private static void loadCache(RSInterface worldList)
    {   // Interfaces.get(HOPPER_MASTER, HOPPER_LIST_CHILD) ^
        final int FIRST_WORLD_COMPONENT = 0;
        final int WORLD_NUMBER_OFFSET = 2;
        final int WORLD_OFFSET = 6;

        for (int i = FIRST_WORLD_COMPONENT; i < worldList.getChildren().length; i += WORLD_OFFSET)
        {
            RSInterface mainChild = worldList.getChild(i);
            RSInterface worldNumChild = worldList.getChild(i + WORLD_NUMBER_OFFSET);

            if(mainChild != null && worldNumChild != null)
                WORLD_CHILDREN_CACHE.put(Integer.parseInt(worldNumChild.getText()), i);
        }
    }


    // Static conditions

    /**
     * Checks if hopping dialogue occurs.
     *
     * @return
     */
    private static Condition doesInterfaceExist(int masterID)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(masterID) != null;
            }
        };
    }

    /**
     * Checks if hopping dialogue occurs.
     *
     * @return
     */
    private static Condition doesInterfaceNotExist(int masterID)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(masterID) == null;
            }
        };
    }

    private static Condition uptextContains(String uptext)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Game.isUptext(uptext);
            }
        };
    }

    private static Condition isHopSuccesful()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Game.getCurrentWorld() != initialWorld;
            }
        };
    }
}
