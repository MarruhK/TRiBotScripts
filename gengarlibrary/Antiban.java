package scripts.gengarlibrary;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

/**
 * The class is over-commented for my own sake. Einstein's open source script heavily influenced this class.
 *
 * Each script should use one instance of ABCUtil, unless the script switches RuneScape acc﻿ounts during runtime﻿.
 * Since each ABCUtil instance is tied to a single character profile for an account, the script must create a new
 * ABCUtil instance if a new RuneScape account starts to be used throughout the runtime of the script.
  */
public class Antiban
{
    // Instance manipulation
    private Antiban ()
    {
        hpToEatAt = this.abcInstance.generateEatAtHP();
        energyToRunAt = this.abcInstance.generateRunActivation();
        shouldHover = false;
        shouldOpenMenu = false;
        shouldMoveToAnticipated = false;

        // Ensures ABC use for API methods (i.e. Banking.bank())
        General.useAntiBanCompliance(true);
    }

    private static Antiban antiban = new Antiban();
    public static Antiban get() {return antiban; }

    // Instantiating the Anti Ban Compliance utility
    private ABCUtil abcInstance = new ABCUtil();

    // Used in action conditions
    private int hpToEatAt;
    private int energyToRunAt;
    boolean shouldHover;
    boolean shouldOpenMenu;
    boolean shouldMoveToAnticipated;


    // OLD STUFF
    /*// Anti Ban Compliance Utility
    private ABCUtil abcInstance;

    // Action condition variables
    private int hpToEatAt;
    private int energyToRunAt;
    private boolean shouldHover;
    private boolean shouldOpenMenu;


    //Use this constructor when the script will only utilize one logged in account per session.
    public Antiban()
    {
        this(new ABCUtil());
    }
    *//**
     * abcInstance is passed because you must have a unique instance per account logged in.
     *
     * I have yet to make a script that utilizes this; however, I have kept this here just in case I will end up making one.
     *//*
    public Antiban(ABCUtil abcInstance)
    {
        this.abcInstance = abcInstance;

        hpToEatAt = this.abcInstance.generateEatAtHP();
        energyToRunAt = this.abcInstance.generateRunActivation();
        shouldHover = false;
        shouldOpenMenu = false;

        // Ensures ABC use for API methods (i.e. Banking.bank())
        General.useAntiBanCompliance(true);
    }*/




    /* 1) Timed Actions  _______________________________________________________________________________________________
           When the player isn't particularly busy (i.e. idling while chopping tree), these timed actions should be
           performed.
    */

    /**
     * Needed separately for generateTrackers
     */
    public void checkLeaveGame()
    {
        if (abcInstance.shouldLeaveGame())
        {
            System.out.println("[ANTIBAN] Timed Actions: leaveGame initiated.");
            abcInstance.leaveGame();
        }
    }

    public void checkTabs()
    {
        if (abcInstance.shouldCheckTabs())
        {
            System.out.println("[ANTIBAN] Timed Actions: checkTabs initiated.");
            abcInstance.checkTabs();
        }
    }

    public void checkXp()
    {
        if (abcInstance.shouldCheckXP())
        {
            System.out.println("[ANTIBAN] Timed Actions: checkXP initiated.");
            abcInstance.checkXP();
        }
    }

    public void checkExamineEntity()
    {
        if (abcInstance.shouldExamineEntity())
        {
            System.out.println("[ANTIBAN] Timed Actions: examineEntity initiated.");
            abcInstance.examineEntity();
        }
    }

    public void checkMoveMouse()
    {
        if (abcInstance.shouldMoveMouse())
        {
            System.out.println("[ANTIBAN] Timed Actions: moveMouse initiated.");
            abcInstance.moveMouse();
        }
    }

    public void checkPickupMouse()
    {
        if (abcInstance.shouldPickupMouse())
        {
            System.out.println("[ANTIBAN] Timed Actions: pickUpMouse initiated.");
            abcInstance.pickupMouse();
        }
    }

    public void checkRightClick()
    {
        if (abcInstance.shouldRightClick())
        {
            System.out.println("[ANTIBAN] Timed Actions: shouldRightClick initiated.");
            abcInstance.rightClick();
        }
    }

    public void checkRotateCamera()
    {
        if (abcInstance.shouldRotateCamera())
        {
            System.out.println("[ANTIBAN] Timed Actions: shouldRotateCamera initiated.");
            abcInstance.rotateCamera();
        }
    }

    /**
     *  Checks the internal timers if a timed action is required.
     *
     *  Use this when you are essentially afk waiting for a task to complete (i.e. woodcutting, filling jugs, fighting)
     */
    public void timedActions()
    {
        checkLeaveGame();
        checkTabs();
        checkXp();
        checkExamineEntity();
        checkMoveMouse();
        checkPickupMouse();
        checkRightClick();
        checkRotateCamera();
    }

    /* 2) Preferences __________________________________________________________________________________________________
            Based on the character's profile, these are the preferred way in handling certain types of actions. These
            preferred ways are not constant and must be generated whenever said action(s) are going to be done.

            The following methods have these preferences embedded within them in the TriBot API so we do not need
            to worry about handling preferences; HOWEVER, you must invoke General.useAntiBanCompliance(true) in order
            for this to be the case (constructor does this for you here):
                - Banking.openBank
                - WebWalking - Dax DaxWalker too
                - GameTab.open
    */

    /**
     * Obtains the next positionable you should select, from an array of positionables.
     *
     * Ensure the elements in the array does not include the current entity we will/are interacting with.
     *
     * This should be called right after you begin interacting with a new positionable (i.e. when you click on another
     * rock and begin mining it). After calling, cache the value returned, as the output of this method isn't consistent.
     * Once you interact with the cached value, you must recall this method again to get the new next target.
     *
     * The only time where you should call this method again is if a new positionable has spawned since you last called
     * this method AND it is closer than any other positionable.
     *
     * Note: This should never be sent an empty array or null since if you have no readily available positionables, then
     * the script should call moveToAnticipated
     *
     * @param targets An array of positionables containing the targets
     * @return The next Positionable entity to click if targets is not empty. Null if empty
     */
    public Positionable getNextTarget(Positionable[] targets)
    {
        if (targets.length == 0)
        {
            System.out.println("[ANTIBAN] getNextTarger: ERROR, YOU SHOULD'VE SENT THIS TO moveToAnticipated!!!!");
            return null;
        }

        return abcInstance.selectNextTarget(targets);
    }

    /* 3) Action Conditions ____________________________________________________________________________________________
            ABCUtil generates conditions for certain actions. The generated values should be stored in "global
            variables" so taht we aren't constantly calling them. Once the action is completed, using the generated
            condition values, this same value will be regenerated and assigned the new value.

            Some classes in the API already implement these action conditions within them. These include:
                - WebWalking:   generateRunActivation
    */

    /**
     * Determines if the bot should eat food.
     *
     * If we are in desperate need of eating, then we can do so, ignoring the output of this method. But that's only for
     * dire circumstances.
     *
     * @param currentHPPercentage Player's current HP percentage
     * @return True if we should eat food, false otherwise
     */
    public boolean shouldEatFood (int currentHPPercentage)
    {
        if (currentHPPercentage <= this.hpToEatAt)
        {
            // Below generates the integer value of a percent (Ex: 55% is 55)
            this.hpToEatAt = this.abcInstance.generateEatAtHP();
            return true;
        }

        return false;
    }


    /**
     * Determines if the player should run. Not needed usually since WebWalking implements this already.
     */
    public void checkActivateRunAndExecute()
    {
        if (!Game.isRunOn() && Game.getRunEnergy() >= this.energyToRunAt)
        {
            Options.setRunEnabled(true);
            this.energyToRunAt = this.abcInstance.generateRunActivation();
        }
    }

    /**
     * Determines if the bot should move to the next anticipated entity spawn.
     *
     * This should only be used when no entities are available and we are awaiting one to spawn (i.e. no nearby rocks).
     * If there are available entities, then we ignore this action.
     *
     * @return True if we should, false otherwise
     */
    public boolean shouldMoveToAnticipated()
    {
        return abcInstance.shouldMoveToAnticipated();
    }

    /**
     * Moves to the tile.
     *
     * This should only be called after shouldMoveToAnticipated returns true AND after a sleep has been executed.     *
     *
     *      {@code performReactionTimeWait(); -- Sleep for the generated reaction time}
     *      {@code moveToAnticipated();       -- Move to the anticipated location}
     *
     * Now we simply wait until a new target/resource spaw﻿ns.
     *
     * Do not keep checking if we should move to the anticipated location. Only perform this check once after each
     * target/resource becomes unavailable
     *
     * @param tile RSTile to move to
     */
    public void moveToAnticipated(RSTile tile)
    {
        WebWalking.walkTo(tile);
        // Maybe add movement sleep here (i.e. wait to stop moving before continuing)
    }

    /**
     * Determines if we should switch resources, to one with less player competition, based on the current amount of
     * competing players. This will probably be done via hopping.
     *
     * We should check this condition every 20-30 seconds, but only if we are not winning very many resources from other
     * competitng players. This is vague because it depends on the script and individual preference.
     *
     * This is quite vague, as it depends entirely on the script itself. Below is an example:
     *
     * {@code
     *  long check_time = Timing.currentTimeMillis() + General.random(20000, 30000);
     *  while (isMining())
     *  {
     *      if (notWinningManyReso﻿ur﻿ce﻿s() && Timing.currentTimeMillis() >= check_time)
     *      {       // Check if we should switch resources
     *              if (this.abc_util.shouldSwitchResources﻿(getCompetitionCount())
     *              {
     *                  switchResources(); //Switch resources			return;
     *              }
     *                  // Generate a new check time
     *                  check_time = Timing.currentTimeMillis() + General.random(20000, 30000);
     *              }
     *          }
     *      }
     *  }
     * }
     *
     * @param competitionCount The amount of other players whom are competing for the same resource you are.
     * @return True if we should, false otherwise
     */
    public boolean shouldSwitchResources(int competitionCount)
    {
        return this.abcInstance.shouldSwitchResources(competitionCount);
    }

    /**
     * Call right after u click the entity. Decides if you should hover or open menu of next entity to engage with.
     *
     * We should use this method to determine if we should hover when we start interacting with each new target entity,
     * and we store the returned value. While interacting with our current target, if the stored value indicates we
     * should hover our next target, then we should do so. Note that we should only employ hovering when the mouse is
     * in the screen boundary.
     *
     */
    public void setHoverAndMenuBoolValues()
    {
        this.shouldHover = abcInstance.shouldHover();
        this.shouldOpenMenu = abcInstance.shouldOpenMenu();
    }

    /**
     * Hovers or opens menu of the next target we will engage with. MUST BE CHANGED BASED ON SCRIPT (i.e paramater)
     *
     * @param nextTarget The next target we will interact with
     */
    public void executeShouldHoverAndMenu(RSObject nextTarget)
    {
        // Condition required by ABCUtil.
        if(Mouse.isInBounds() && this.shouldHover)
        {
            nextTarget.hover();

            if (this.shouldOpenMenu)
            {
                Mouse.click(3);
            }
        }
    }

    /* 4) Reaction Times _______________________________________________________________________________________________
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
     * This should be called right after the task that takes a variable amount of time is completed, but because
     * sleepReactionTime already calls this method, there's no need to call it in your script.
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
        props.setUnderAttack(Combat.isUnderAttack());
        props.setWaitingFixed(false);   // Not supported in ABC2 yet

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
            System.out.print("[ANTIBAN] Reaction Times: Sleeping for: " + sleepTime + " milliseconds");
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
     * Should be called after we begin an action that take variable amount of time (i.e. click another tree).
     * After this, ABCUtil shouldLeaveGame and leaveGame should be called (timed actions does this).
     *
     * @param estimatedWaitingTime The time it takes to do the action. Should be an average time that it takes, based of
     *                             the current run-time of the script.
     */
    public void generateTrackers(int estimatedWaitingTime)
    {
        // Setting the props
        final ABCProperties props = this.abcInstance.getProperties();
        props.setWaitingTime(estimatedWaitingTime);
        props.setUnderAttack(Combat.isUnderAttack());
        props.setWaitingFixed(false); // Not supported in ABC2 yet

        // Actual generation
        System.out.println("[ANTIBAN] Reaction Times: generateTrackers generated.");
        this.abcInstance.generateTrackers();

        // Check if you should leave game
        checkLeaveGame();
    }
}
