package scripts.tutisland.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.ext.Doors;
import org.tribot.api2007.types.*;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker_engine.WalkingCondition;

import java.awt.*;

public abstract class APIMethods
{
    private static final int INTERFACE_MASTER_NPC_CHAT = 263;

    /**
     * If multiple doors are near destination tile, this may be buggy. If that is the case, use the overloaded method
     * that takes the object ID as well.
     *
     * @param finalTile
     * @param timeOut
     * @return
     */
    public static boolean walkToDoor(RSTile finalTile, long timeOut)
    {
        String[] doorNames = {"Door", "Gate"};
        long startingTime = Timing.currentTimeMillis();

        if (DaxWalker.walkTo(finalTile, () -> WalkingCondition.State.CONTINUE_WALKER))
        {
            // If you made it before timeout, click the door.
            while (Timing.currentTimeMillis() - startingTime < timeOut)
            {
                RSObject[] doors = Objects.find(5, doorNames);

                if (doors.length > 0 && doors != null)
                {
                    RSObject door = doors[0];
                    // immuatble bullshit might need diff class type????
                    String uptext = null;

                    // Get general uptext for all doors/gates
                    for (String action : door.getDefinition().getActions())
                    {
                        if (action.contains("Open"))
                        {
                            uptext = action + " " + door.getDefinition().getName();
                            System.out.println("APIMethods: Door uptext: " + uptext);
                            break;
                        }
                    }

                    if (uptext != null && clickObject(doors, uptext, -1))
                    {
                        // Doors.handledoor if above isn't good.
                        System.out.println("APIMethods -- walkToDoor: Succesfully walked through the door");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean sleepAfterWalk(RSTile tile, int timeout)
    {
        System.out.println("GengarAPI -- sleepAfterWalk: Walking to: " + tile.toString());

        if (Walking.walkTo(tile))
        {
            if (Timing.waitCondition(conditionWaitToStopMoving(), timeout) && Player.getPosition().equals(tile))
            {
                System.out.println("GengarAPI -- sleepAfterWalk: Supposedly walked");
                return true;
            }
        }
        System.out.println("GengarAPI -- sleepAfterWalk: Failed to walk");
        return false;
    }

    /**
     * With supplied parameters, ensures that the object will be clicked.
     *
     * Does all the null checking, camera movement, player movement to ensure the above goal is met. If no Animation
     * is to occur, then it is up to you to define the sleeps after clicking said object.
     *
     * @param objects           Array of RSObject, which can be empty, where the nearest will be interacted with.
     * @param desiredUpText     String value representing the uptext when mouse hovers the npc
     * @param animation         Desired animation when interacting with object. -1 if no animation to occur.
     * @return                  True if click was successful, false otherwise.
     */
    public static boolean clickObject(RSObject[] objects, String desiredUpText, int animation)
    {
        if (objects.length == 0)
            return false;

        RSObject object = objects[0];

        System.out.println("GengarAPI: The clickObject method began.");
        if (!object.isOnScreen())
            Camera.turnToTile(object.getPosition());

        if (!object.isOnScreen())
        {
            System.out.println("GengarAPI: clickObject: Object is too far to walk to, resolve from caller.");
            return false;
        }

        object.hover();

        if (safeClick(desiredUpText) &&
            Timing.waitCondition(conditionIsAnimation(animation), General.random(5000, 5100)))
        {
            System.out.println("GengarAPI: Successfully animated.");
            return true;
        }
        else
        {
            System.out.println("GengarAPI: Failed to animate.");
            return false;
        }
    }

    /**
     * With supplied parameters, ensures that the NPC will be clicked.
     *
     * Does all the null checking, camera movement, player movement to ensure the above goal is met.
     *
     * @param npcs              Array of RSNPC, which can be empty, where the nearest will be interacted with.
     * @param uptext            String value representing verb when hovering NPC (i.e. Talk-to ...)
     * @param walkingTimeout    Max time denoted for player to walk to npcTile.
     * @return                  True if NPC was clicked, false otherwise
     */
    public static boolean clickNPC(RSNPC[] npcs, String uptext, int walkingTimeout)
    {
        if (isNearNPC(npcs, walkingTimeout))
        {
            System.out.println("GengarAPI - clickNPC: Successfully clicked NPC.");
            RSNPC npc = npcs[0];

            if (npc.hover() &&
                    safeClick(uptext + " " + npc.getDefinition().getName()) &&
                    Timing.waitCondition(conditionIsInterfaceValid(INTERFACE_MASTER_NPC_CHAT), 2500))
            {
                System.out.println("GengarAPI - clickNPC: Successfully clicked NPC.");
                return true;
            }
            else
            {
                System.out.println("GengarAPI - clickNPC: Failed to click NPC.");
                return false;
            }
        }
        return false;
    }

    public static boolean isNearNPC(RSNPC npcs[], int walkingTimeout)
    {
        if (npcs.length == 0)
            return false;

        RSNPC npc = npcs[0];
        RSTile npcTile = npc.getPosition();

        // Possible for NPC to be found, but be extremely far away, walk to it if greater than 8 tiles away.
        if (!npc.isOnScreen())
            Camera.turnToTile(npc.getPosition());

        if (!npc.isOnScreen())
            DaxWalker.walkTo(npcTile);

        return true;
    }

    public static boolean clickNPCConsiseUptext(RSNPC[] npcs, String uptext, int walkingTimeout, int animation)
    {
        if (isNearNPC(npcs, walkingTimeout))
        {
            RSNPC npc = npcs[0];

            if (npc.hover()         &&
                safeClick(uptext)   &&
                Timing.waitCondition(conditionIsAnimation(animation), 1000))
            {
                System.out.println("GengarAPI: Successfully clicked NPC.");
                return true;
            }
            else
            {
                System.out.println("GengarAPI: Failed to click NPC.");
                return false;
            }
        }
        return false;
    }

    /**
     * While a hoverable is hovered, ensures that the desiredUptext option is clicked, whether by left or right click.
     *
     * @param desiredUptext String value representing the uptext when mouse hovers the hoverable
     * @return              True on successful click, false otherwise
     */
    public static boolean safeClick(String desiredUptext)
    {
        // Wait for mouse to hover positionable
        General.sleep(400,500);

        // fail safe for spam right click on option
        if (ChooseOption.isOpen())
        {
            ChooseOption.select("Cancel");
            return false;
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.getUptext().contains(desiredUptext))
        {
            Mouse.click(3);

            if (Timing.waitCondition(new Condition()
            {
                @Override
                public boolean active()
                {
                    General.sleep(100);
                    return ChooseOption.isOpen();
                }
            }, General.random(400,500)))
            {
                if (ChooseOption.isOptionValid(desiredUptext))
                {
                    ChooseOption.select(desiredUptext);
                    return true;
                }
            }
            // Didn't find right option or didn't right click properly.
            return false;
        }
        else
        {
            Mouse.click(1);
            return true;
        }
    }

    /**
     * Continues to click "click to continue" until no longer possible.
     */
    public static void continueChat()
    {
        do
        {
            NPCChat.clickContinue(true);
            General.sleep(300, 450);
        }
        while (NPCChat.getClickContinueInterface() != null);
        General.sleep(300, 350);
    }

    public static boolean useTwoItemsWithEachother(RSItem[] item1, RSItem[] item2)
    {
        if (item1.length == 0 || item2.length == 0)
            return false;

        Rectangle item1Area = item1[0].getArea();
        String item1Name = item1[0].getDefinition().getName();

        Rectangle item2Area = item2[0].getArea();
        String item2Name = item2[0].getDefinition().getName();

        // Move mouse and click on tinderbox
        Mouse.moveBox(item1Area);
        Mouse.click(1);

        if (Timing.waitCondition(APIMethods.conditionIsUptext("Use " + item1Name + " ->"), 600))
        {
            Mouse.moveBox(item2Area);

            if (Timing.waitCondition(APIMethods.conditionIsUptext(" " + item2Name), 600))
            {
                Mouse.click(1);
                return true;
            }
        }
        return false;
    }

    public static String getChatText()
    {
        int child = 1;
        int comp = 0;

        return Interfaces.get(INTERFACE_MASTER_NPC_CHAT, child).getChild(comp).getText();
    }

    public static boolean isDesignInterfaceOpen()
    {
        final int master = 269;
        return Interfaces.get(master) != null;
    }


    // CONDITIONS ______________________________________________________________________________________________________
    public static Condition conditionIsAnimation(int animation)
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

    public static Condition conditionIsAnimationNot(int animation)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Player.getAnimation() != animation;
            }
        };
    }

    public static Condition conditionIsUptext(String uptext)
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

    public static Condition conditionIsInChatDialogue()
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(231) != null || Interfaces.get(217) != null;
            }
        };
    }

    public static Condition conditionWaitToStopMoving()
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

    public static Condition conditionIsGameTabOpen(GameTab.TABS tab)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return GameTab.getOpen() == tab;
            }
        };
    }

    public static Condition conditionIsInterfaceValid(int masterIndex)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(masterIndex) != null;
            }
        };
    }

    public static Condition conditionIsInterfaceNotValid(int masterIndex)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Interfaces.get(masterIndex) == null;
            }
        };
    }

    public static Condition conditionIsChatTextEqualTo(String text)
    {
        return new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return getChatText().contains(text);
            }
        };
    }
}
