package scripts.minnows;

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

import scripts.minnows.data.Constants;
import scripts.minnows.data.Vars;
import scripts.minnows.framework.Node;
import scripts.minnows.nodes.FishMinnows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.tribot.util.Util.getImage;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Minnows",
        category =      "Fishing",
        description =   "Fishes minnows. Has built in auto-pker detection and teles.")
public class Minnows extends Script implements Starting, Ending, Painting {

    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.FISHING);
    private int startXP = Skills.getXP(Skills.SKILLS.FISHING);
    private int fishCaught = 0;
    private int gainedXP = 0;
    private int gainedLVL = 0;
    private int initialMinnowAmount = 0;
    private long timeRan = 0;


    @Override
    public void onStart() {
        General.useAntiBanCompliance(true);

        RSItem[] inventoryMinnows = Inventory.find(Constants.MINNOW_ID);

        if (inventoryMinnows.length > 0){
            initialMinnowAmount = inventoryMinnows[0].getStack();
        }

        // Ensure you have small fishing net.
        if (Inventory.find(Constants.NET_ID).length == 0){
            Vars.shouldExecute = false;
            System.out.println("No fishing net. Cannot continue execution.");
            General.println("No fishing net. Cannot continue execution.");
        }
    }

    @Override
    public void run() {
        Collections.addAll(nodes,
                new FishMinnows());

        loop(250, 500);
    }

    public void loop(int min, int max){
        while (Vars.shouldExecute) {
            for (final Node node : nodes) {
                if (node.validate()) {
                    node.execute();
                    sleep(General.random(min, max));	//time in between executing nodes
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
        General.println("TheGengar's *Minnows* has completed.");
        General.println("Runtime: " + timeRan);
        General.println("Minnows Caught: " + fishCaught);
        General.println("Fishing XP gained: " + gainedXP);
        General.println("Fishing LVLs gained: " + gainedLVL);
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("                                                                                        ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("TheGengar's *Minnows* has completed.");
        System.out.println("Runtime: " + Timing.msToString(timeRan));
        System.out.println("Minnows Caught: " + fishCaught);
        System.out.println("Fishing XP gained: " + gainedXP);
        System.out.println("Fishing LVLs gained: " + gainedLVL);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onPaint(Graphics g) {
        // Paint image
        /*Image img = getImage("https://i.imgur.com/0aQy9Ce.png");
        Graphics2D gg = (Graphics2D)g;
        gg.drawImage(img, 0, 339, null);*/

        // Text data
        timeRan = System.currentTimeMillis() - startTime;
        int currentLvl = Skills.getActualLevel(Skills.SKILLS.FISHING);
        gainedLVL = currentLvl - startLvl;

        gainedXP = Skills.getXP(Skills.SKILLS.FISHING) - startXP;
        long xpPerHour = (long) (gainedXP * 3600000d / timeRan);
        long foodPerHour = (long) (fishCaught * 3600000d / timeRan);
        fishCaught = Inventory.find(Constants.MINNOW_ID)[0].getStack() - initialMinnowAmount;

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Minnows Caught: " + fishCaught, 200, 390);
        g.drawString("Gained Fishing XP: " + gainedXP + " (Levels: " + gainedLVL + ")", 200, 410);

        g.drawString("Fishing XP/H: " + xpPerHour, 200, 450);
        g.drawString("Minnows Caught/H : " + foodPerHour, 200, 470);
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
