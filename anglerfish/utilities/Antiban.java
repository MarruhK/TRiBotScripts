package scripts.anglerfish.utilities;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

import java.util.ArrayList;

// The class is over-commented for my own sake. Einstein's open source script heavily influenced this class.
public class Antiban {

    // Instance manipulation
    private Antiban () {}                                   // No instantiation outside of class
    private static Antiban antiban = new Antiban();         /* Apparently main thread is in a static context so this is
                                                               required since you cannot get non-static members from a
                                                               static context.*/
    public static Antiban get() {return antiban; }          // Basic encapsulation

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
    public void timedActions() {
        if (abcInstance.shouldCheckTabs()){
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldCheckTabs initiated.");
            abcInstance.checkTabs();

        }

        if (abcInstance.shouldCheckXP()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldCheckXP initiated.");
            abcInstance.checkXP();
        }

        if (abcInstance.shouldExamineEntity()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldExamineEntity initiated.");
            abcInstance.examineEntity();
        }

        if (abcInstance.shouldMoveMouse()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldMoveMouse initiated.");
            abcInstance.moveMouse();
        }

        if (abcInstance.shouldPickupMouse()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldPickUpMouse initiated.");
            abcInstance.pickupMouse();
        }

        if (abcInstance.shouldRightClick()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldRightClick initiated.");
            abcInstance.rightClick();
        }

        if (abcInstance.shouldRotateCamera()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldRotateCamera initiated.");
            abcInstance.rotateCamera();
        }

        if (abcInstance.shouldLeaveGame()) {
            General.println("scripts.gengarlibrary.Antiban - Timed Actions: shouldLeaveGame initiated.");
            abcInstance.leaveGame();
        }
    }

    /* 2) Preferences __________________________________________________________________________________________________
            Based on the character's profile, these are the preferred way in handling certain types of actions. These
            preferred ways are not constant and must be generated whenever said action(s) are going to be done.

            The following methods have these preferences embedded within them in the TriBot API so we do not need
            to worry about handling preferences:
                - Banking.openBank
                - WebWalking
                - GameTab.open

            Within the ABCUtil class, the selectNextTarget method selects an element in a list of positionables that
            should be the next thing to click. The value returned should be stored. Once the next entity is interacted
            with, the entire process should be repeated again to determine the next positionable entity.
    */

    /**
     * Determines the next entity to click. Should be called and stored right when clicking an entity. Ensure the
     * elements in the array does not include the current entity we will/are interacting with.
     *
     * @param targets An array of positionables containing the targets
     * @return The next Positionable entity to click
     */
    public RSNPC getNextTarget(RSNPC[] targets) {
        return (RSNPC) abcInstance.selectNextTarget(targets);
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
    public boolean eatFood (int currentHPPercentage){
        boolean shouldEat = false;

        if (currentHPPercentage <= this.hpToEatAt){
            shouldEat = true;
            this.hpToEatAt = this.abcInstance.generateEatAtHP();
        }
        return shouldEat;
    }


    /**
     * Determines if the player should run. Not needed usually since WebWalking implements this already.
     */
    public void shouldActivateRun(){
        if (!Game.isRunOn() && Game.getRunEnergy() >= this.energyToRunAt){
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
    public void setShouldMoveToAnticipated(int dist, int...id) {
        if (Objects.findNearest(dist, id).length == 0){
            this.shouldMoveToAnticipated = abcInstance.shouldMoveToAnticipated();
        }
    }

    // Moves to anticipated location if the above method determines it needs to be done.
    public void executeShouldMoveToAnticipated(){
        // ADD STUFF IN HERE BASED ON THE SCRIPT.
    }

    // RESOURCE SWITCHING. ANOTHER METHOD SHOULD BE ADDED HERE BUT IS SCRIPT SPECIFIC. ABCUtil#shouldSwitchResources

    /**
     * Call right after u clicK the entity. Decides if you should hover or open menu of next entity to engage with.
     *
     */
    public void setHoverAndMenuBoolValues(){
        this.shouldHover = abcInstance.shouldHover();
        this.shouldOpenMenu = abcInstance.shouldOpenMenu();
    }

    /**
     * Hovers or opens menu of the next target we will engage with. MUST BE CHANGED BASED ON SCRIPT (I.e paramater)
     *
     * @param nextTarget
     */
    public void executeShouldHoverAndMenu(RSNPC nextTarget){
        // Condition required by ABCUtil.
        if(Mouse.isInBounds() && this.shouldHover){
            General.println("scripts.gengarlibrary.Antiban - Action Conditions: executeShouldHoverAndMenu initiated. Hovering Entity.");
            nextTarget.hover();
            if (this.shouldOpenMenu){
                General.println("scripts.gengarlibrary.Antiban - Action Conditions: executeShouldHoverAndMenu initiated. Opening Menu.");
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
                4) Under Attack:    Were we under attack now or while we were waiting.
     */

    /**
     * Generates the time that the player should be idling after completing an action that takes a variable amount of
     * time, longer than one second.
     *
     * This should be called right after the task that takes a variable amount of time is completed.
     *
     * @param waitingTime The variable time that it took to complete the action. This must be specified in the Node.
     * @return The time that should be spent sleeping
     */
    public int generateReactionTime(int waitingTime){
        // Factors needed
        final boolean hovering = this.abcInstance.shouldHover();
        final boolean menu_open = this.abcInstance.shouldOpenMenu() && this.abcInstance.shouldHover();
        final ABCProperties props = this.abcInstance.getProperties();

        // Set properties
        props.setWaitingTime(waitingTime);
        props.setHovering(hovering);
        props.setMenuOpen(menu_open);
        props.setUnderAttack(Combat.isUnderAttack());   // || wasJustUnderAttack()
        props.setWaitingFixed(false);

        // Actual generation
        return this.abcInstance.generateReactionTime();
    }

    /**
     * Actually sleeps the amount specified by the {@code generateReactionTime} method
     *
     * @param waitingTime The variable time that it took to complete the action. This must be specified in the Node.
     */
    public void sleepReactionTime(int waitingTime){
        try {
            int sleepTime = generateReactionTime(waitingTime);
            General.println("scripts.gengarlibrary.Antiban - Reaction Times: sleepReactionTime initiated. Sleeping for: " + sleepTime +
                    " milliseconds");
            System.out.println("scripts.gengarlibrary.Antiban - Reaction Times: sleepReactionTime initiated. Sleeping for: " + sleepTime +
                    " milliseconds");
            this.abcInstance.sleep(sleepTime);
        } catch (final InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /*
        generateTrackers is required for ABCUtil's background thread. We give this method certain information so that
        it can manipulate said information so that it can generate variables that perform antiban relevant to having the
        mouse leave the game.

        You should implement reaction times (and generate trackers off that reaction time) anytime you are performing
        an action that will take a variable amount of time. You might find it is too slow, but this is how ABC2 works
        for everything.

        Common examples are chopping a tree, mining a rock or fighting an NPC.

        Requires two pieces of information"
            1) Estimated waiting:   Supply an average amount of time it takes to do task.
            2) Under Attack:        Are you under attack while doing this?
     */


    /**
     * Generates variables that are essential in performing anti-ban, while the player is waiting for something. To be
     * more specific, it targets how often the mouse should leave the game area. When an entity is clicked which
     * requires the player to wait a variable amount of time, this method should be called right after.
     *
     * @param est_waiting The time it takes to do the action. Should be an average time that it takes, based of the
     *                    current run-time of the script.
     */
    public void generateTrackers(int est_waiting) {
        // Setting the props
        final ABCProperties props = this.abcInstance.getProperties();
        props.setWaitingTime(est_waiting);
        props.setUnderAttack(false);
        props.setWaitingFixed(false);

        // Actual generation
        General.println("scripts.gengarlibrary.Antiban - Reaction Times: generateTrackers initiated.");
        this.abcInstance.generateTrackers();

        /* After this method, some "afk" handling method should be the very next thing in line (i.e. whileChopping
           method). This method should be called right after a reaction time has been generated and has slept through.
           Right after this, the entity should be clicked and the tracker should be generated.

            IMPORTANT NOTE: ABCUtil.shouldLeaveGame and ABCUtil.leaveGame should be called after this method is called.
                            NOT DOING SO MAKES THIS METHOD USELESS. */
    }
}
