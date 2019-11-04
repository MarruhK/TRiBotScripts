package scripts.aiohunter;

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
import scripts.gengarlibrary.ACamera;
import scripts.aiohunter.data.Vars;
import scripts.aiohunter.framework.Node;
import scripts.aiohunter.nodes.HuntSallys;
import scripts.aiohunter.nodes.Release;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Hunter Trainer",
        category =      "Hunter",
        description =   "Hunts sallies and shti.")
public class AIOHunter extends Script implements Starting, Ending, Painting
{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.HUNTER);
    private int startXP = Skills.getXP(Skills.SKILLS.HUNTER);
    private int gainedXP = 0;
    private int gainedLVL = 0;
    private long timeRan = 0;

    @Override
    public void onStart()
    {
        Vars.cam = new ACamera(this);
    }

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                        new HuntSallys(Vars.cam),
                        new Release(Vars.cam));

        loop(250, 500);
    }

    private void loop(int min, int max)
    {
        while (Vars.shouldExecute)
        {
            for (final Node node : nodes)
            {
                if (node.validate())
                {
                    node.execute();
                    sleep(General.random(min, max));

                    if (!Vars.shouldExecute)
                    {
                        break;
                    }
                }
            }
        }
        logout();
    }

    private void logout(){
        do
        {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(new Condition()
            {
                @Override
                public boolean active()
                {
                    General.sleep(100);
                    return Login.getLoginState() == Login.STATE.LOGINSCREEN;
                }
            }, General.random(10500, 10600));
        }
        while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }


    @Override
    public void onEnd()
    {
        General.println("                                                                                        ");
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        General.println("TheGengar's *AIOHunter* has completed.");
        General.println("Runtime: " + timeRan);
        General.println("Hunter XP gained: " + gainedXP);
        General.println("Hunter LVLs gained: " + gainedLVL);
        General.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("                                                                                        ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("TheGengar's *AIOHunter* has completed.");
        System.out.println("Runtime: " + Timing.msToString(timeRan));
        System.out.println("Hunter XP gained: " + gainedXP);
        System.out.println("Hunter LVLs gained: " + gainedLVL);
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
        int currentLvl = Skills.getActualLevel(Skills.SKILLS.HUNTER);
        gainedLVL = currentLvl - startLvl;

        gainedXP = Skills.getXP(Skills.SKILLS.HUNTER) - startXP;
        long xpPerHour = (long) (gainedXP * 3600000d / timeRan);

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Gained HUNTER XP: " + gainedXP + " (Levels: " + gainedLVL + ")", 200, 410);

        g.drawString("HUNTER XP/H: " + xpPerHour, 200, 450);
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
