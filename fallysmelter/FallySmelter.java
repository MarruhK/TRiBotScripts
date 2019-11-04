package scripts.fallysmelter;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.fallysmelter.data.Vars;
import scripts.fallysmelter.framework.Node;
import scripts.fallysmelter.nodes.Bank;
import scripts.fallysmelter.nodes.Smelt;
import scripts.fallysmelter.nodes.WalkToBank;
import scripts.fallysmelter.nodes.WalkToFurnace;
import scripts.gnomefisher.data.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FallySmelter extends Script implements Painting
{
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startXP = Skills.getXP(Skills.SKILLS.SMITHING);

    @Override
    public void run()
    {
        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });


        Collections.addAll( nodes,
                            new WalkToBank(),
                            new WalkToFurnace(),
                            new Bank(),
                            new Smelt());

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

        General.println("Ran out of ores or rings, ending script.");
        do
        {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(()->
            {
                General.sleep(100);
                return Login.getLoginState() == Login.STATE.LOGINSCREEN;
            }, General.random(10500, 10600));
        } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }

    @Override
    public void onPaint(Graphics g)
    {
        // Paint image
        Image img = getImage("https://i.imgur.com/0aQy9Ce.png");
        Graphics2D gg = (Graphics2D)g;
        gg.drawImage(img, 0, 339, null);

        // Text data
        double ironBarExp = 62.5;
        long timeRan = System.currentTimeMillis() - startTime;
        int gainedXP = Skills.getXP(Skills.SKILLS.SMITHING) - startXP;
        int barsMade = (int) (gainedXP / ironBarExp);
        int barsPerHour = (int) (barsMade * 3600000d / timeRan);

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Bars Made: " + barsMade, 200, 450);
        g.drawString("Bars Made/H : " + barsPerHour, 200, 470);
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
