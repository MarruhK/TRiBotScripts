
package scripts.gengarlibrary;

public class DMMWorldHop{

}
/*
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api.GClicking;

import java.awt.*;
import java.util.HashMap;

public class DMMWorldHop
{
    //cache so we can just pull the appropriate child id for a world instead of looking for it every time
    private static final HashMap<Integer, Integer> WORLD_CHILDREN_CACHE = new HashMap<>();

    //normal logout inter constants
    private static final int NORMAL_LOGOUT_MASTER = 182;
    private static final int WORLD_SWITCHER_CHILD = 1;  // should be 3

    //world hopper interface constants
    private static final int HOPPER_MASTER = 69;
    private static final int HOPPER_CLOSE_CHILD = 3;
    private static final int LOADING_TEXT_CHILD = 2;
    private static final int HOPPER_LIST_CHILD = 7;
    private static final int MIN_Y = 228;
    private static final int MAX_Y = 418;

    //option dialogue constants
    private static final int DIALOGUE_MASTER = 219;
    private static final int DIALOGUE_CHILD = 0;
    private static final int DIALOGUE_OPTION_COMPONENT = 2;

    //misc. constants
    private static final long INTERFACE_SWITCH_TIMEOUT = 3500; //max wait time for world hopper inter to load
    private static final long INTERFACE_LOAD_TIMEOUT = 5000; //max wait time for world hopper inter to load

    public static boolean hop(int dmmWorld)
    {
        // DMM worlds are in the format of 10X

        if (GameTab.open(GameTab.TABS.LOGOUT) && isHopperUp() || openHopper())
        {
            try
            {
                final RSInterface WORLD_LIST = Interfaces.get(HOPPER_MASTER, HOPPER_LIST_CHILD);
                final Integer WORLD_CHILD = WORLD_CHILDREN_CACHE.get(dmmWorld);

                //load cache if necessary
                if((WORLD_CHILDREN_CACHE.isEmpty() || WORLD_CHILD == null) && WORLD_LIST != null)
                    loadCache(WORLD_LIST);

                final RSInterface TARGET_WORLD = WORLD_LIST.getChild(WORLD_CHILDREN_CACHE.get(dmmWorld));

                //check if world is scrolled into view
                if(isWorldVisible(TARGET_WORLD) || scrollWorldIntoView(WORLD_LIST, TARGET_WORLD))
                {
                    // after u click the world u need to go through the dialogue.
                    if (clickWorld(WORLD_LIST.getChild(WORLD_CHILDREN_CACHE.get(dmmWorld)), dmmWorld)) //Can't use cached TARGET_WORLD due to it's pos changing on scroll
                    {

                    }
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

    */
/**
     * The logout option is selected; however, the world switcher isn't. This method opens the world switcher assuming
     * the first statement is true.
     *
     * @return {@code True} if successful {@code False} otherwise.
     *//*

    private static boolean openHopper()
    {
        final RSInterface WORLD_SWITCHER_BUTTON = Interfaces.get(NORMAL_LOGOUT_MASTER, WORLD_SWITCHER_CHILD);

        if(WORLD_SWITCHER_BUTTON != null && WORLD_SWITCHER_BUTTON.click())
        {
            return  (
                        Timing.waitCondition(new Condition()
                                             {
                                                 @Override
                                                 public boolean active()
                                                 {
                                                     General.sleep(100);
                                                     return Interfaces.get(HOPPER_MASTER) != null;
                                                 }
                                             }, General.random(1800, 1900)
                                            )
                    );
        }


        return false;
    }

    private static boolean isWorldVisible(RSInterface targetWorld)
    {
        Rectangle rect = targetWorld.getAbsoluteBounds();

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
            {
                Mouse.moveBox(WORLD_LIST_BOUNDS);
            }

            //scroll in appropriate direction
            targetRectangle = targetWorld.getAbsoluteBounds();
            Mouse.scroll(targetRectangle.y < MIN_Y);

            if(Timing.timeFromMark(START_TIME) > TIMEOUT)
            {
                return false;
            }
            General.sleep(10, 40);
        }
        while(!isWorldVisible(targetWorld));

        General.sleep(70, 120);
        return true;
    }

    private static boolean clickWorld(RSInterface targetWorld, int worldNum)
    {
        if(GClicking.hover(targetWorld)) // && Timing.waitCondition(FCConditions.uptextContains(""+worldNum), 1000))
        {
            Mouse.click(1);
            if(Timing.waitCondition(FCConditions.interfaceUp(DIALOGUE_MASTER), 1500))
            {
                final RSInterface DIALOGUE = Interfaces.get(DIALOGUE_MASTER, DIALOGUE_CHILD);
                GClicking.click(DIALOGUE.getChild(DIALOGUE_OPTION_COMPONENT));
            }

            return Timing.waitCondition(FCConditions.onWorldCondition(worldNum), 5000);
        }

        return false;
    }

    */
/**
     * Only concerned about DMM worlds, so this populates the hashmap with only DMM worlds.
     *
     * @param worldList big ass list containing all the relevant world hopper information.
     *                  This is Interface.get(169, 7), that big ass list.
     *//*

    private static void loadCache(RSInterface worldList)
    {
        final int FIRST_WORLD_COMPONENT = 0;
        final int WORLD_NUMBER_OFFSET = 2;
        final int WORLD_OFFSET = 6;

        // First 6 components (i.e. 0-5) represent information wtih regards to one world.
        //  0 is clickable area
        //  2 is actual world value in String format
        for(int i = FIRST_WORLD_COMPONENT; i < worldList.getChildren().length; i += WORLD_OFFSET)
        {
            RSInterface mainChild = worldList.getChild(i);
            RSInterface worldNumChild = worldList.getChild(i + WORLD_NUMBER_OFFSET);
            int worldNum = Integer.parseInt(worldNumChild.getText());

            // Populate hashmap with the DMM worlds.
            if((mainChild != null && worldNumChild != null) &&
               (worldNum > 100 && worldNum != 117))
            {
                WORLD_CHILDREN_CACHE.put(worldNum, i);
            }
        }
    }
}
*/
