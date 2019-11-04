package scripts.dmmrelekkafisher;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Methods;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;
import scripts.dmmrelekkafisher.nodes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "DMM Relekka Fisher",
        category =      "Fishing",
        description =   "Fishes at relekka. Can fish shrimp, lobster and shark.")
public class DMMRelekkaFisher extends Script implements Starting, Ending, Painting
{
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

    @Override
    public void onStart()
    {
        General.useAntiBanCompliance(true);
        Vars.shouldExecute = true;

        GUI gui = new GUI();

        // Wait for GUI to get all variables.
        while (Vars.fishingEquipmentID == 0)
        {
            General.sleep(500);
        }

        System.out.print(Vars.fishID.toString());
    }

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                Vars.walkToBank,
                Vars.Bank,
                new FishSharks(),
                new MuleFish(),
                new WalkToMule(),
                new WalkToFishingSpot());

        loop(250, 500);
    }

    private void loop(int min, int max)
    {
        while (Vars.shouldExecute && Vars.fishingEquipmentID != -1)
        {
            for (final Node node : nodes)
            {
                if (node.validate())
                {
                    node.execute();
                    sleep(General.random(min, max));	//time in between executing nodes

                    if (!Vars.shouldExecute)
                    {
                        break;
                    }
                }
            }
        }

        // To make sure GUI doesn't bug out when you click X on it.
        if (Vars.fishingEquipmentID != -1)
        {
            Methods.logout();
        }
        else
        {
            General.println("Hey retard, don't click X on GUI.");
            System.out.println("Hey retard, don't click X on GUI.");
        }
    }

    @Override
    public void onEnd()
    {
        General.println("                                                                                        ");
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        General.println("TheGengar's *DMM Relekka Fisher* has completed.");
        General.println("Runtime: " + timeRan);
        General.println("Fish caught: " + gainedFish);
        General.println("Fishing XP gained: " + gainedXP);
        General.println("Fishing LVLs gained: " + gainedLVL);
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("                                                                                        ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("TheGengar's *DMM Relekka Fisher* has completed.");
        System.out.println("Runtime: " + Timing.msToString(timeRan));
        System.out.println("Fish caught: " + gainedFish);
        System.out.println("Fishing XP gained: " + gainedXP);
        System.out.println("Fishing LVLs gained: " + gainedLVL);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onPaint(Graphics g)
    {
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
        gainedFish = gainedXP * 3 / 1100;
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

    private Image getImage(String url)
    {
        try
        {
            return ImageIO.read(new URL(url));
        }
        catch (IOException e)
        {
            System.out.println("Didn't find paint image.");
            return null;
        }
    }
}
