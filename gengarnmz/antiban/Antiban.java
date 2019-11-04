package scripts.gengarnmz.antiban;

import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.types.RSObject;

// The class is over-commented for my own sake. Einstein's open source script heavily influenced this class.
public class Antiban
{
    // Instance manipulation
    private Antiban () {}                                   // No instantiation outside of class
    private static Antiban antiban = new Antiban();         /* Apparently main thread is in a static context so this is
                                                               required since you cannot get non-static members from a
                                                               static context.*/
    public static Antiban get() { return antiban; }          // Basic encapsulation

    // Instantiating the Anti Ban Compliance UtilityPotion
    private ABCUtil abcInstance = new ABCUtil();

    // Used in action conditions
    int hpToEatAt = this.abcInstance.generateEatAtHP();
    int energyToRunAt = this.abcInstance.generateRunActivation();
    boolean shouldHover = false;
    boolean shouldOpenMenu = false;
    boolean shouldMoveToAnticipated = false;

    /* 1) Timed Actions  _______________________________________________________________________________________________
           When the player isn't particularly busy (i.e. idling while chopping tree), these timed actions should be
           performed.
    */

    /**
     *  Checks the internal timers if a timed action is required.
     */
    public void timedActions()
    {
        if (abcInstance.shouldLeaveGame())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldLeaveGame initiated.");
            abcInstance.leaveGame();
        }

        if (abcInstance.shouldCheckTabs())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldCheckTabs initiated.");
            abcInstance.checkTabs();
        }

        if (abcInstance.shouldCheckXP())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldCheckXP initiated.");
            abcInstance.checkXP();
        }

        if (abcInstance.shouldExamineEntity())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldExamineEntity initiated.");
            abcInstance.examineEntity();
        }

        if (abcInstance.shouldMoveMouse())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldMoveMouse initiated.");
            abcInstance.moveMouse();
        }

        if (abcInstance.shouldPickupMouse())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldPickUpMouse initiated.");
            abcInstance.pickupMouse();
        }

        if (abcInstance.shouldRightClick())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldRightClick initiated.");
            abcInstance.rightClick();
        }

        if (abcInstance.shouldRotateCamera())
        {
            System.out.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldRotateCamera initiated.");
            abcInstance.rotateCamera();
        }
    }

    /* 2) Preferences __________________________________________________________________________________________________
            Based on the character's profile, these are the preferred way in handling certain types of actions. These
            preferred ways are not constant and must be generated whenever said action(s) are going to be done.

            The following methods have these preferences embedded within them in the TriBot API so we do not need
            to worry about handling preferences:
                - Banking.openBank
                - WebWalking - Dax DaxWalker too
                - GameTab.open
    */

    /**
     * Determines the next entity to click. Should be called and stored right when clicking an entity. Ensure the
     * elements in the array does not include the current entity we will/are interacting with. Once the next entity is
     * interacted with, the entire process should be repeated again to determine the next positionable entity.
     *
     * @param targets An array of positionables containing the targets
     * @return The next Positionable entity to click
     */
    public Positionable getNextTarget(Positionable[]targets)
    {
        return abcInstance.selectNextTarget(targets);
    }

    /* 3) Action Conditions ____________________________________________________________________________________________
            ABCUtil generates conditions for certain actions. The generated values should be stored in "global
            variables" so that when used, the value is persistent. Once the action is completed, using the generated
            condition values, this same value will be regenerated and assigned the new value.

            Some classes in the API already implement these action conditions within them. These include:
                - WebWalking:   generateRunActivation
                -*/

    /**
     * Determines if the bot should eat food.
     *
     * @param currentHPPercentage Player's current HP
     * @return True if we should eat food, false otherwise
     */
    public boolean eatFood (int currentHPPercentage)
    {
        boolean shouldEat = false;

        if (currentHPPercentage <= this.hpToEatAt)
        {
            shouldEat = true;
            this.hpToEatAt = this.abcInstance.generateEatAtHP();
        }
        return shouldEat;
    }


    /**
     * Determines if the player should run. Not needed usually since WebWalking implements this already.
     */
    public void shouldActivateRun()
    {
        if (!Game.isRunOn() && Game.getRunEnergy() >= this.energyToRunAt)
        {
            Options.setRunOn(true);
            this.energyToRunAt = this.abcInstance.generateRunActivation();
        }
    }

    /**
     * Determines if the bot should move to the next anticipated entity spawn.
     *
     * This should only be used when no entities are available and we are awaiting one to spawn (i.e. no nearby rocks).
     * If there are available entities, then we ignore this action.
     *
     * @param dist The distance to look for relevant entities
     * @param id The IDs of the resources to anticipate for.
     */
    public void executeShouldMoveToAnticipated(int dist, int...id)
    {
        if (Objects.findNearest(dist, id).length == 0 &&
                abcInstance.shouldMoveToAnticipated())
        {
            // Do script-specific shit here (example case below)

            // performReactionTimeWait(); -- Sleep for the generated reaction time
            // moveToAnticipated();       -- Move to the anticipated location
        }
        // Now we simply wait until a new target/resource spaw﻿ns
        // Do not keep checking if we should move to the anticipated location
        // Only perform this check once after each target/resource becomes unavailable
    }

    /*
    We should check this condition every 20-30 seconds, but only if we are not winning very many resources from other
    competitng players. This is vague because it depends on the script and individual preference.
     */

    /*// Here we start mining a rock
    long check_time = Timing.currentTimeMillis() + General.random(20000, 30000);
    while (isMining())
    {
        if (notWinningManyResources() && Timing.currentTimeMillis() >= check_time)
        { // Check if we should switch resources
            if (this.abc_util.shouldSwitchResources(getCompetitionCount())
            {
                switchResources(); //Switch resources
                return;
            }
            check_time = Timing.currentTimeMillis() + General.random(20000, 30000); // Generate a new check time
        }
    }*/

    /**
     * Call right after u clicK the entity. Decides if you should hover or open menu of next entity to engage with.
     *
     */
    public void setHoverAndMenuBoolValues()
    {
        this.shouldHover = abcInstance.shouldHover();
        this.shouldOpenMenu = abcInstance.shouldOpenMenu();
    }

    /**
     * Hovers or opens menu of the next target we will engage with. MUST BE CHANGED BASED ON SCRIPT (I.e paramater)
     *
     * @param nextTarget
     */
    public void executeShouldHoverAndMenu(RSObject nextTarget)
    {
        // Condition required by ABCUtil.
        if(Mouse.isInBounds() && this.shouldHover)
        {
            // System.out.println("scripts.gengarlibrary.Antiban - Action Conditions: executeShouldHoverAndMenu initiated. Hovering Entity.");
            nextTarget.hover();
            if (this.shouldOpenMenu)
            {
                System.out.println("scripts.gengarlibrary.Antiban - Action Conditions: executeShouldHoverAndMenu initiated. Opening Menu.");
                Mouse.click(3);
            }
        }
    }

    /* 4) Reaction Times ______________________________________________________________________________________________
            With activities that require some sort of variable time, a generated reaction time must be used rather than
            random sleeps. Generated reaction times should be used in activities that have a delay higher than one
            second (Like woodcutting and unlike alching).

            When creating reaction times, the script should specify four factors:
                1) Waiting Time:    Time spent variably waiting to complete action.
                2) Hovering:        If we are hovering over next entity.
                3) Menu Open:       If we have menu open for next entity.
                4) Under Attack:    Were we under attack now or while we were waiting. */

    /**
     * Generates the time that the player should be idling after completing an action that takes a variable amount of
     * time, longer than one second.
     *
     * This should be called right after the task that takes a variable amount of time is completed.
     *
     * @param waitingTime The variable time that it took to complete the action. This must be specified in the Node.
     * @return The time that should be spent sleeping
     */
    public int generateReactionTime(int waitingTime)
    {
        // Factors needed
        final boolean hovering = this.abcInstance.shouldHover();
        final boolean menu_open = this.abcInstance.shouldOpenMenu() && hovering;
        final ABCProperties props = this.abcInstance.getProperties();

        // Set properties
        props.setWaitingTime(waitingTime);
        props.setHovering(hovering);
        props.setMenuOpen(menu_open);
        props.setUnderAttack(Combat.isUnderAttack()); // || wasJustUnderAttack() inside the bracket
        props.setWaitingFixed(false);

        // Actual generation
        return this.abcInstance.generateReactionTime();
    }

    /**
     * Actually sleeps the amount specified by the {@code generateReactionTime} method
     *
     * @param waitingTime The variable time that it took to complete the action. This must be specified in the Node.
     */
    public void sleepReactionTime(int waitingTime)
    {
        try
        {
            int sleepTime = generateReactionTime(waitingTime);
            System.out.println("scripts.gengarlibrary.Antiban - Reaction Times: sleepReactionTime initiated. Sleeping for: " + sleepTime +
                    " milliseconds");
            this.abcInstance.sleep(sleepTime);
        }
        catch (final InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Generates variables that are essential in performing anti-ban, while the player is waiting for something. To be
     * more specific, it targets how often the mouse should leave the game area. When an entity is clicked which
     * requires the player to wait a variable amount of time, this method should be called right after.
     *
     * You should implement reaction times (and generate trackers off that reaction time) anytime you are performing
     * an action that will take a variable amount of time. You might find it is too slow, but this is how ABC2 works
     * for everything.
     *
     * Should be called after the action we are reacting too (i.e. after reacting to not chopping tree) and after the
     * sleep. After this, ABCUtil should leave game and leave game should be called. This can be done via calling
     * timedActions defined in this class.
     *
     * @param est_waiting The time it takes to do the action. Should be an average time that it takes, based of the
     *                    current run-time of the script.
     */
    public void generateTrackers(int est_waiting, boolean isUnderAttack)
    {
        // Setting the props
        final ABCProperties props = this.abcInstance.getProperties();
        props.setWaitingTime(est_waiting);
        props.setUnderAttack(false);
        props.setWaitingFixed(false);

        // Actual generation
        System.out.println("scripts.gengarlibrary.Antiban - Reaction Times: generateTrackers initiated.");
        this.abcInstance.generateTrackers(est_waiting);
    }
}
