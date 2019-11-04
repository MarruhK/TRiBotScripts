package scripts.relekkamule;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import scripts.relekkamule.data.Constants;
import scripts.relekkamule.data.Vars;
import scripts.relekkamule.utility.Antiban;
import sun.plugin2.message.Message;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.Collections;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Relekka Fish Mule",
        category =      "Muling",
        description =   "Mules fish in Relekka via Peer the seer.")
public class RelekkaMule extends Script implements MessageListening07, Painting
{
    // CONSTANTS
    public final int PEER_SEER_ID = 3895;

    // VARS
    private int fishMuled = 0;
    private boolean isInTrade = false;

    // PAINT
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private long timeRan = 0;

    @Override
    public void run()
    {
        new GUI();

        while (true)
        {
            sleep(100);
        }

        // loop(250, 500);
    }

    private void loop(int min, int max)
    {
        while (Vars.shouldExecute)
        {
            Antiban.get().timedActions();
            sleep(General.random(min, max));
        }

        // LOG OUT
        do
        {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(new Condition()
            {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Login.getLoginState() == Login.STATE.LOGINSCREEN;
                }
            }, General.random(10500, 10600));
        }
        while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }

    @Override
    public void tradeRequestReceived(String s) {
        System.out.print("tradeRequestReceived: Initiating trading sequence.");
        // Prevents spam trading
        if (Trading.getWindowState() == null)
        {
            RSPlayer theFisherToTrade = getFisherToTrade(s);

            // Nearby fishers are valid
            if (theFisherToTrade != null)
            {
                if (theFisherToTrade.getPosition().distanceTo(Player.getPosition()) <= 2)
                {
                    General.sleep(200);     // Prevent mislcick while fisher walks to trade
                    theFisherToTrade.click("Trade with " + theFisherToTrade.getName());

                    if(trade(theFisherToTrade.getName()))
                    {
                        do
                        {
                            fishMuled += giveItemsToPeer();
                        }
                        while (Inventory.getAll().length > 0);
                    }
                }
                else
                {
                    System.out.print("tradeRequestRecieved: The trading fisher is too far to trade. >1 tile.");
                }
            }
        }
    }

    private RSPlayer getFisherToTrade(String s)
    {
        RSPlayer[] nearbyPlayers = Players.find(Vars.fishers);
        RSPlayer theFisherToTrade = null;

        // Get the player that specifically traded you.
        for (RSPlayer fisher : nearbyPlayers)
        {
            if (s.equals(fisher.getName())){
                System.out.print("tradeRequestRecieved: Sucessfully determined that the currently trading Player is a valid bot.");
                theFisherToTrade = fisher;
                break;
            }
        }
        return theFisherToTrade;
    }

    private boolean trade(String theFisherToTrade)
    {
        System.out.println("trade: Method began.");

        // Check if player is even nearby, if not, ignore.
        RSPlayer[] trader = Players.find(theFisherToTrade);

        if (trader.length <= 0 || trader[0].getPosition().distanceTo(Player.getPosition()) > 1)
        {
            return false;
        }
        System.out.println("trade: Traded with fisher, waiting for response...");

        // Now must wait for the trade screen to pop-up (i.e. Trading begin).
        if (isTradeScreenOpen(Trading.WINDOW_STATE.FIRST_WINDOW))
        {
            if (isFirstTradeWindowSuccessful(theFisherToTrade))
            {
                // Wait for 2nd screen to open up.
                if (isTradeScreenOpen(Trading.WINDOW_STATE.SECOND_WINDOW))
                {
                    return isSecondTradeWindowSuccessful();
                }
                else
                {
                    System.out.println("trade: Failed to leave first trade window. Returning.");
                    Trading.close();
                    return false;
                }
            }
        }

        System.out.print("trade: Failed to open the trade screen...");
        return false;
    }

    // Waits 5~ seconds for trade screen to show up.
    private boolean isTradeScreenOpen(Trading.WINDOW_STATE state)
    {
        return Timing.waitCondition(new Condition()
        {
            @Override
            public boolean active() {
                General.sleep(1000);
                return Trading.getWindowState() == state;
            }
        }, General.random(5000,5100));
    }

    // Does first trading window procedure
    private boolean isFirstTradeWindowSuccessful(String theFisherToTrade)
    {
        System.out.println("inFirstTradeWindow: First trade window opened, offering fish.");

        // Wait to see fish in trade then accept.
        if (Timing.waitCondition(new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return Trading.getOfferedItems(true).length > 0;
            }
        }, General.random(10000, 10100)))
        {
            // Ensure it is the mule we traded, then press Accept.
            if (Trading.getOpponentName().equals(theFisherToTrade))
            {
                // horrible implementation to wait for anchovies
                // wait for him to accept.
                if (Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(200);
                        return Interfaces.get(335, 30).getText().equals("Other player has accepted.");
                    }
                }, General.random(11000, 11200)))
                {
                    System.out.println("inFirstTradeWindow: Accepted first trade window. Waiting for Second window.");
                    Trading.accept();
                    return true;
                }
                else
                {
                    System.out.println("inFirstTradeWindow: Other player did not accept trade...");
                }
            }
        }
        System.out.println("isFirstTradeWindowSuccessful: Failed to leave first trade window, closing...");
        Trading.close();
        return false;
    }

    // Does second trading window procedure
    private boolean isSecondTradeWindowSuccessful()
    {
        System.out.println("isSecondTradeWindowSucessful: On the seconds trade screen now.");

        // Once again, double check mule name and then accept.
        Trading.accept();
        System.out.println("isSecondTradeWindowSuccessful: Accepted second trade window.");

        // Make it wait till trade screen removed.
        if(isTradeScreenOpen(null))
        {
            System.out.println("isSecondTradeWindowSuccessful: Successfully muled.");
            return true;
        }

        System.out.println("isSecondTradeWindowSuccessful: Failed to mule. Leaving trade window.");
        Trading.close();
        return false;
    }
    private int giveItemsToPeer() {
        System.out.print("________________________________________________________________________________________");
        System.out.print("giveItemsToPeer: Method began.");

        // Now deposit the shit.
        RSItem[] fish = Inventory.find(Constants.RAW_LOBSTER_ID, Constants.RAW_SHARK_ID, Constants.RAW_SHRIMP_ID,
                                        Constants.RAW_ANCHOVY_ID);

        if (fish.length > 0)
        {
            int fishToMule = fish.length - 1;
            RSNPC[] peer = NPCs.findNearest(PEER_SEER_ID);

            if (peer.length > 0)
            {
                peer[0].hover();

                if (Vars.isDiaryDone)
                {
                    if (!safeClick("Deposit-items Peer the Seer"))
                    {
                        System.out.print("giveItemsToPeer: Failed to deposit the items. Returning...");
                        return 0;
                    }

                    if (Timing.waitCondition(new Condition()
                    {
                        @Override
                        public boolean active()
                        {
                            General.sleep(100);
                            return Interfaces.get(192) != null;
                        }
                    }, General.random(2500, 2600)))
                    {
                        System.out.print("giveItemsToPeer: Deposit box is opened, depositing items now.");
                        RSInterfaceMaster depoBox = Interfaces.get(192);

                        do
                        {
                            General.sleep(800);
                            depoBox.getChild(3).click();
                        }
                        while (Inventory.getAll().length > 0 &&
                               Interfaces.get(192) != null);

                        System.out.println("giveItemsToPeer: Deposited all items via deposit box.");

                        depoBox.getChild(1).getChild(11).click();       // Closes depobox
                        return fishToMule;
                    }
                    else
                    {
                        System.out.print("giveItemsToPeer: Deposit box is not open, returning...");
                        return 0;
                    }
                }
                else
                {
                    safeClick("Talk-to Peer the Seer");

                    // Wait for speaking.
                    if (Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            General.sleep(100);
                            return NPCChat.getClickContinueInterface() != null;
                        }
                    }, General.random(1500, 1600)))
                    {
                        // Have failsafe to ensure it actaully traded over.
                        seerDialogueSequencePreDiary();
                        return fishToMule;
                    }
                    else
                    {
                        System.out.print("giveItemsToPeer: FAILED TO CLICK, RESTARTING METHOD.");
                        giveItemsToPeer();
                    }
                    Keyboard.sendRelease(' ', 32);
                }
            }
        }
        System.out.print("giveItemsToPeer: Finished.");
        return 0;
    }

    private void seerDialogueSequencePreDiary()
    {
        System.out.print("giveItemsToPeer: FIRST BEGAN.");
        Keyboard.holdKey(' ', 32, new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return NPCChat.getOptions() != null;
            }});
        System.out.print("giveItemsToPeer: FIRST ENDED.");

        System.out.print("giveItemsToPeer: SECOND BEGAN.");
        Keyboard.holdKey('1', 49, new Condition()
        {
            @Override
            public boolean active()
            {
                General.sleep(100);
                return NPCChat.getClickContinueInterface() == null;
            }
        });
        System.out.print("giveItemsToPeer: SECOND ENDED.");

        System.out.print("giveItemsToPeer: THIRD BEGAN.");
        clickToContinue();
        clickToContinue();
        System.out.print("giveItemsToPeer: THIRD ENDED.");
    }

    private void clickToContinue(){
        // Waits for continue interface.
        if (Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return NPCChat.getClickContinueInterface() != null;
            }
        }, General.random(1000, 1100))){

            // Cont int exists, space.
            Keyboard.holdKey(' ', 32, new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return  NPCChat.getClickContinueInterface() == null;    // while this is false, continues to hold key. Stop when true.
                }
            });
        }
    }

    // Needs mouse to be hovering positionable
    public static boolean safeClick(String desiredUptext){
        // Wait for mouse to hover positionable
        General.sleep(400,500);

        // fail safe for spam right click on option
        if (ChooseOption.isOpen())
        {
            ChooseOption.select("Cancel");
            return false;
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.isUptext(desiredUptext))
        {
            Mouse.click(3);

            if (Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
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

    @Override
    public void onPaint(Graphics g)
    {
        // Text data
        timeRan = System.currentTimeMillis() - startTime;

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Fish caught: " + fishMuled, 200, 400);
    }

    @Override
    public void personalMessageReceived(String s, String s1) {

    }
    @Override
    public void playerMessageReceived(String s, String s1) {

    }
    @Override
    public void duelRequestReceived(String s, String s1) {

    }
    @Override
    public void serverMessageReceived(String s) {

    }
    @Override
    public void clanMessageReceived(String s, String s1) {

    }
}
