package scripts.fruitstallthiever;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MouseActions;
import org.tribot.script.interfaces.Painting;
import scripts.fruitstallthiever.data.Vars;
import scripts.fruitstallthiever.framework.Node;
import scripts.fruitstallthiever.nodes.Drop;
import scripts.fruitstallthiever.nodes.Thieve;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FruitStallThiever extends Script implements Painting,MouseActions
{
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private static long startTime = System.currentTimeMillis();
    private static int startLvl = Skills.getActualLevel(Skills.SKILLS.THIEVING);
    private static int startXP = Skills.getXP(Skills.SKILLS.THIEVING);
    private static final Rectangle TOGGLE_PAINT = new Rectangle(0, 339, 55, 23);
    private static boolean hidePaint = false;
    private static int gainedXP;
    private static int gainedLvl;
    private static long timeRan;
    private static long xpPerHour;

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                            new Thieve(),
                            new Drop());

        loop();
    }

    private void loop()
    {
        while (Vars.shouldExecute)
        {
            for (final Node node : nodes)
            {
                if (!Vars.shouldExecute)
                    break;

                if (node.validate())
                {
                    node.execute();
                    General.sleep(General.random(250, 500));
                }
            }
        }

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
        Graphics2D gg = (Graphics2D)g;
        gg.draw(TOGGLE_PAINT);

        if (!hidePaint)
        {
            if (Game.getGameState() == 30)
            {
                // General
                g.setFont(font);
                g.setColor(Color.WHITE);

                // Gengar Background
                Image img = getImage();
                gg.drawImage(img, 0, 339, null);

                // Toggler
                gg.draw(new Rectangle(0, 339, 55, 23));
                g.drawString("Toggle", 0, 359);

                // Text data
                timeRan = System.currentTimeMillis() - startTime;
                int currentLvl = Skills.getActualLevel(Skills.SKILLS.THIEVING);
                gainedXP = Skills.getXP(Skills.SKILLS.THIEVING) - startXP;
                gainedLvl = currentLvl - startLvl;
                int nextLvl = currentLvl + 1;
                xpPerHour = (long) (gainedXP * 3600000d / timeRan);
                long xpToNextLvl = (Skills.getXPToNextLevel(Skills.SKILLS.THIEVING));

                // Implementation of text
                g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
                g.drawString("Current Level: " + currentLvl, 200, 390);
                g.drawString("Gained Range XP: " + gainedXP + " (Levels: " + gainedLvl + ")", 200, 410);
                g.drawString("XP to level " + nextLvl + ": " + xpToNextLvl, 200, 430);
                g.drawString("Range XP/H: " + xpPerHour, 200, 450);
            }
        }
    }

    private Image getImage()
    {
        try
        {
            return ImageIO.read(new URL("https://i.imgur.com/0aQy9Ce.png"));
        }
        catch (IOException e)
        {
            System.out.println("Didn't find paint image.");
            return null;
        }
    }

    @Override
    public void mouseMoved(Point point, boolean b)
    {

    }

    @Override
    public void mouseReleased(Point point, int i, boolean b)
    {
        if (TOGGLE_PAINT.contains(point.getLocation()))
        {
            hidePaint = !hidePaint;
        }
    }

    @Override
    public void mouseClicked(Point point, int i, boolean b)
    {

    }

    @Override
    public void mouseDragged(Point point, int i, boolean b)
    {

    }
}
