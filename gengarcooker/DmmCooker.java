package scripts.gengarcooker;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.gengarcooker.data.Constants;
import scripts.gengarcooker.data.Vars;
import scripts.gengarcooker.framework.Node;
import scripts.gengarcooker.nodes.BankFood;
import scripts.gengarcooker.nodes.CookFish;
import scripts.gengarcooker.nodes.WalkVarrockBank;
import scripts.gengarcooker.nodes.WalkVarrockRange;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "DMM Cooker",
        category =      "Cooking",
        description =   "Cooks food at Varrock East range.")
public class DmmCooker extends Script implements Starting, Ending, Painting{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.COOKING);
    private int startXP = Skills.getXP(Skills.SKILLS.COOKING);
    private int cookedFish = 0;
    private int gainedXP = 0;
    private int gainedLVL = 0;
    private long timeRan = 0;

    @Override
    public void onStart() {
        General.useAntiBanCompliance(true);

        new GUI();

        while (!Vars.isInitialized){
            // Wait for user to finish using GUI
            General.sleep(100);
        }
    }

    @Override
    public void run() {

        if (Vars.location.equals("Hosidius")){
            Collections.addAll(nodes, Constants.NODES[1]);
        }

        loop(250, 500);
    }

    private void loop(int min, int max) {
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



    // _________________________________________________________________________________________________________________

    public void onEnd() {
        General.println("                                                                                        ");
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        General.println("TheGengar's *DMMCooker* has completed.");
        General.println("Runtime: " + timeRan);
        General.println("Food cooked: " + cookedFish);
        General.println("Cooking XP gained: " + gainedXP);
        General.println("Cooking LVLs gained: " + gainedLVL);
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("                                                                                        ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("TheGengar's *DMMCooker* has completed.");
        System.out.println("Runtime: " + Timing.msToString(timeRan));
        System.out.println("Food cooked: " + cookedFish);
        System.out.println("Cooking XP gained: " + gainedXP);
        System.out.println("Cooking LVLs gained: " + gainedLVL);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.out.println("Didn't find paint image.");
            return null;
        }
    }

    @Override
    public void onPaint(Graphics g) {
        // Paint image
        Image img = getImage("https://i.imgur.com/0aQy9Ce.png");
        Graphics2D gg = (Graphics2D)g;
        gg.drawImage(img, 0, 339, null);

        // Text data
        timeRan = System.currentTimeMillis() - startTime;
        int currentLvl = Skills.getActualLevel(Skills.SKILLS.COOKING);
        gainedLVL = currentLvl - startLvl;

        gainedXP = Skills.getXP(Skills.SKILLS.COOKING) - startXP;
        long xpPerHour = (long) (gainedXP * 3600000d / timeRan);
        cookedFish = gainedXP / Vars.expOfFood;                // Make database for fish cooking exp rates. 700 trout 900 slamon
        long foodPerHour = (cookedFish * 3600000 / timeRan);

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Food Cooked: " + cookedFish, 200, 390);
        g.drawString("Gained Cooking XP: " + gainedXP + " (Levels: " + gainedLVL + ")", 200, 410);

        g.drawString("Cooking XP/H: " + xpPerHour, 200, 450);
        g.drawString("Food Cooked/H : " + foodPerHour, 200, 470);
    }
}
