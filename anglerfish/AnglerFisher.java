package scripts.anglerfish;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.anglerfish.data.Constants;
import scripts.anglerfish.data.Vars;
import scripts.anglerfish.framework.Node;
import scripts.anglerfish.nodes.*;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Angler Fishing",
        category =      "Fishing",
        description =   "Fishes anglerfish.")
public class AnglerFisher extends Script implements Starting, Ending, Painting{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime;
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.FISHING);
    private int startXP = Skills.getXP(Skills.SKILLS.FISHING);
    private int initialBaitAmount;
    private int gainedFish = 0;
    private int gainedXP = 0;
    private int gainedLVL = 0;
    private long timeRan = 0;

        @Override
        public void onStart() {
            // Make a welcome message for starting script
            General.println("                                                                                        ");
            General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            General.println("TheGengar's *AnglerFisher* has commenced.");
            General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            System.out.println("                                                                                        ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("TheGengar's *AnglerFisher* has commenced.");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            // No GUI or anything else, so this is all that is needed atm.
            General.useAntiBanCompliance(true);
            startTime = System.currentTimeMillis();

            // Check if you have bait and/or rod
            RSItem[] baits = Inventory.find(Constants.BAIT_ID);
            if (baits.length > 0 && Inventory.find(Constants.FISHING_ROD_ID).length > 0){
               initialBaitAmount = baits[0].getStack();
            } else {
                Vars.shouldExecute = false;
            }
        }

        @Override
        public void run() {
            Collections.addAll(nodes,
                    new FishAnglers(),
                    new WalkToAnglers(),
                    new WalkToBank(),
                    new Bank(),
                    new CheckIfDead());

            // Pker detection thread. only do if you have tele tebs in inventory, otherwise redundant

            Runnable run = new PkerRunnable();
            Thread loopThread = new Thread(run);
            loopThread.start();

            loop(250, 500);
        }

        private void loop(int min, int max) {
            while (Vars.shouldExecute) {
                for (final Node node : nodes) {
                    if (node.validate()) {
                        node.execute();
                        General.sleep(General.random(min, max));	//time in between executing nodes
                    }
                }
            }

            do {
                System.out.println("Time to log out.");
                Login.logout();

                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return Login.getLoginState() == Login.STATE.LOGINSCREEN;
                    }
                }, General.random(10500, 10600));
            } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
        }

    @Override
    public void onEnd() {
        General.println("                                                                                        ");
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        General.println("TheGengar's *AnglerFisher* has completed.");
        General.println("Runtime: " + timeRan);
        General.println("Anglerfish caught: " + gainedFish);
        General.println("Fishing XP gained: " + gainedXP);
        General.println("Fishing LVLs gained: " + gainedLVL);
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("                                                                                        ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("TheGengar's *AnglerFisher* has completed.");
        System.out.println("Runtime: " + Timing.msToString(timeRan));
        System.out.println("Anglerfish caught: " + gainedFish);
        System.out.println("Fishing XP gained: " + gainedXP);
        System.out.println("Fishing LVLs gained: " + gainedLVL);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onPaint(Graphics g) {
        // Paint image
        Image img = getImage("https://i.imgur.com/0aQy9Ce.png");
        Graphics2D gg = (Graphics2D)g;
        gg.drawImage(img, 0, 339, null);

        // Text data
        timeRan = System.currentTimeMillis() - startTime;
        int currentLvl = Skills.getActualLevel(Skills.SKILLS.FISHING);
        gainedLVL = currentLvl - startLvl;

        gainedXP = Skills.getXP(Skills.SKILLS.FISHING) - startXP;
        long xpPerHour = (long) (gainedXP * 3600000d / timeRan);
        gainedFish = gainedXP / 600;
        long fishPerHour = (gainedFish * 3600000 / timeRan);

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Anglerfish caught: " + gainedFish, 200, 390);
        g.drawString("Gained Fishing XP: " + gainedXP + " (Levels: " + gainedLVL + ")", 200, 410);

        g.drawString("Fishing XP/H: " + xpPerHour, 200, 450);
        g.drawString("Fish Caught/H : " + fishPerHour, 200, 470);
    }

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.out.println("Didn't find paint image.");
            return null;
        }
    }
}