package scripts.gnomefisher;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.gnomefisher.data.Constants;
import scripts.gnomefisher.data.Methods;
import scripts.gnomefisher.data.Variables;
import scripts.gnomefisher.framework.Node;
import scripts.gnomefisher.nodes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ScriptManifest(
        authors =       "TheGengar",
        name =          "Gnome Fisher",
        category =      "Fishing",
        description =   "Fly fishes in the gnome stronghold.")
public class GnomeFisher extends Script implements Starting, Painting, Ending {

    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.FISHING);
    private int startXP = Skills.getXP(Skills.SKILLS.FISHING);
    private int gainedFish = 0;
    private int gainedXP = 0;
    private int gainedLVL = 0;
    private long timeRan = 0;
    private int initialFeathers;

    @Override
    public void onStart() {
        General.useAntiBanCompliance(true);

        // Checks if shift-click is enabled, if not, enable it.
        RSInterfaceMaster optionInterface = Interfaces.get(Constants.INTERFACE_INDEX_OPTION_ID);
        if (isShiftClickNotActivated(optionInterface)) {
            activateShiftClick(optionInterface);
        }

        RSItem[] feathers = Inventory.find(Constants.FEATHERS_ID);
        RSItem[] flyRods = Inventory.find(Constants.FLY_ROD_ID);
        if (feathers.length == 0 || flyRods.length == 0){
            Variables.shouldExecute = false;
        } else {
            // Move feathers to 26, 27 index
            initialFeathers = Inventory.find(Constants.FEATHERS_ID)[0].getStack();
            while (Inventory.find(Constants.FLY_ROD_ID)[0].getIndex() != 26 &&
                    Inventory.find(Constants.FLY_ROD_ID)[0].getIndex() != 27){
                initializeInventory();
            }
        }
    }

    // ensures that inventory includes feathers and a fly fishing rod and then moves it to a safe index
    private boolean initializeInventory() {
        // Add fail safe for game up-text.
        Methods.shouldUndoUpText("Use Feather ->", "Use Fly fishing rod ->");

        // Moves rod to 2nd last inventory spot (index = 26)
        Mouse.drag((new Point(  (int)(Inventory.find(Constants.FLY_ROD_ID)[0].getArea().getX()),
                        (int)(Inventory.find(Constants.FLY_ROD_ID)[0].getArea().getY()))),
                new Point(661,445), 1);

        // Waits till rod is officially at index 26
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return (Inventory.find(Constants.FLY_ROD_ID)[0].getIndex() == 26);
            }
        }, General.random(1500, 1700));

        // Moves feathers to last inventory spot (index = 27)
        Mouse.drag((new Point((int)(Inventory.find(Constants.FEATHERS_ID)[0].getArea().getX()),
                              (int)(Inventory.find(Constants.FEATHERS_ID)[0].getArea().getY()))),
                    new Point(706,445),1);

        // Waits till feathers are officially at index 27
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return (Inventory.find(Constants.FLY_ROD_ID)[0].getIndex() == 27);
            }
        }, General.random(1500, 1700));
        return true;
    }

    private void activateShiftClick(RSInterfaceMaster optionInterface) {
        // It is not enabled, so make it enabled.
        System.out.println("onStart: Shift-drop not enabled. Enabling...");

        // Opens the settings tab.
        GameTab.open(GameTab.TABS.OPTIONS);

        // Click the "Action Control" options if it already isn't opened.
        RSInterfaceComponent actionControl = optionInterface.getChild(Constants.INTERFACE_CHILD_4_OPTIONS_ID)
                .getChild(Constants.INTERFACE_COMPONENT_ACTION_CONTROLS_ID);

        if (actionControl.getTextureID() == Constants.OPTIONS_NOT_SELECTED_ID) {
            // Specific option is not selected, select it.
            General.sleep(1200);
            actionControl.click();

            // Wait.
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return actionControl.getTextureID() != Constants.OPTIONS_NOT_SELECTED_ID;
                }
            }, General.random(1500, 1700));
        }
        // Proper option screen is on-screen. Now click shift click.
        optionInterface.getChild(Constants.INTERFACE_CHILD_SHIFT_CLICK_ID).click();

        // Wait till shift click is activated
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return  optionInterface.getChild(Constants.INTERFACE_CHILD_SHIFT_CLICK_ID).getTextureID()
                        != Constants.OPTIONS_NOT_SELECTED_ID;
            }
        }, General.random(1500, 1700));

        // Go to inventory.
        GameTab.open(GameTab.TABS.INVENTORY);
    }

    private boolean isShiftClickNotActivated(RSInterfaceMaster optionInterface) {
        return (optionInterface.getChild(Constants.INTERFACE_CHILD_SHIFT_CLICK_ID).getTextureID() ==
                Constants.OPTIONS_NOT_SELECTED_ID);
    }

    @Override
    public void run() {
        Collections.addAll(nodes,
                new DropFish(),
                new FlyFish());

        loop(250, 500);
    }

    private void loop(int min, int max) {
        while (Variables.shouldExecute) {
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
        General.println("TheGengar's *GnomeFisher* has completed.");
        General.println("Runtime: " + timeRan);
        General.println("Fish caught: " + gainedFish);
        General.println("Fishing XP gained: " + gainedXP);
        General.println("Fishing LVLs gained: " + gainedLVL);
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("                                                                                        ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("TheGengar's *GnomeFisher* has completed.");
        System.out.println("Runtime: " + Timing.msToString(timeRan));
        System.out.println("Fish caught: " + gainedFish);
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
        gainedFish = initialFeathers - Inventory.find(Constants.FEATHERS_ID)[0].getStack();
        long fishPerHour = (gainedFish * 3600000 / timeRan);

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Fish caught: " + gainedFish, 200, 390);
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
