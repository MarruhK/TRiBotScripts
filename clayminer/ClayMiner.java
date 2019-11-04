package scripts.clayminer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MouseActions;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.clayminer.data.Vars;
import scripts.clayminer.framework.Node;
import scripts.clayminer.nodes.*;
import scripts.clayminer.nodes.LeaveBottomFloor;
import scripts.clayminer.nodes.roothandlers.EnterMine;
import scripts.clayminer.nodes.roothandlers.LeaveMine;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.gengarlibrary.initialsetup.CameraZoom;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Clay Miner",
        category =      "Dmm",
        description =   "Mines clay rocks in the dungeon of the grand tree.",
        version =       1.0)

public class ClayMiner extends Script implements Starting, Ending, Painting, MouseActions
{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private static long startTime = System.currentTimeMillis();
    private static final Rectangle TOGGLE_PAINT = new Rectangle(0, 339, 55, 23);
    private boolean hidePaint = false;

    // Paint GUI vars
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.MINING);
    private int startXP = Skills.getXP(Skills.SKILLS.MINING);

    @Override
    public void onStart()
    {
        DaxWalker.setCredentials(new DaxCredentialsProvider()
        {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        CameraZoom.zoomOut();
        GameTab.open(GameTab.TABS.INVENTORY);
    }

    @Override
    public void run()
    {
        General.useAntiBanCompliance(true);

        Collections.addAll(nodes,
                new WalkToClayRocks(),
                new MineOre(),
                new LeaveMine(),
                new LeaveBottomFloor(),
                new WalkBank(),
                new BankOre(),
                new EnterMine(),
                new WalkToMainFloor(),
                new EnterBottomFloor());

        loop();
    }

    private void loop()
    {
        while (true)
        {
            for (final Node node : nodes)
            {
                if (node.validate())
                {
                    node.execute();
                    sleep(General.random(250, 500));	//time in between executing nodes
                }
            }
        }
    }

    @Override
    public void onEnd()
    {
        System.out.println("Mined Clay: " + Vars.minedOres);
        General.println("Mined Clay: " + Vars.minedOres);
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
                long timeRan = System.currentTimeMillis() - startTime;
                int currentLvl = Skills.getActualLevel(Skills.SKILLS.MINING);
                int gainedXP = Skills.getXP(Skills.SKILLS.MINING) - startXP;
                int gainedLvl = currentLvl - startLvl;
                long claysPerHour = (long) (Vars.minedOres * 3600000d / timeRan);

                // Implementation of text
                g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
                g.drawString("Current Level: " + currentLvl, 200, 390);
                g.drawString("Gained Mining XP: " + gainedXP + " (Levels: " + gainedLvl + ")", 200, 410);
                g.drawString( "Clays Mined " + Vars.minedOres, 200, 430);
                g.drawString( "Clays Mined/H: " + claysPerHour, 200, 450);
            }
        }
    }

    private BufferedImage getImage()
    {
        try
        {
            return ImageIO.read(new File("C:/Users/Hamza/Desktop/Dev/OSRS/Botting/TriBot/TRiBot Projects/resources/background.png"));
        }
        catch (IOException e)
        {
            System.out.println("Didn't find paint image.");
            return null;
        }
    }

    @Override
    public void mouseReleased(Point point, int i, boolean b)
    {
        if (TOGGLE_PAINT.contains(point.getLocation()))
        {
            hidePaint = !hidePaint;
        }
    }

    // Not in use
    @Override public void mouseClicked(Point point, int i, boolean b) {}
    @Override public void mouseDragged(Point point, int i, boolean b) {}
    @Override public void mouseMoved(Point point, boolean b) {}
}
