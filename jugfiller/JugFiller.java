package scripts.jugfiller;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Skills;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MouseActions;
import org.tribot.script.interfaces.Painting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.gengarlibrary.Antiban;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;
import scripts.jugfiller.nodes.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Jug Fillers",
        category =      "Dmm",
        description =   "Fills jugs with water in Falador.")
public class JugFiller extends Script implements Painting, MouseActions, Ending
{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private static Font font = new Font("Verdana", Font.BOLD, 16);
    private static long startTime = System.currentTimeMillis();
    private static final Rectangle TOGGLE_PAINT = new Rectangle(0, 339, 55, 23);
    private boolean hidePaint = false;

    // Paint GUI vars



    @Override
    public void run()
    {
        DaxWalker.setCredentials(new DaxCredentialsProvider()
        {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        Collections.addAll(nodes,
                new WalkPump(),
                new FillJugs(),
                new WalkBank(),
                new BankJugs(),
                new GetJugs());

        loop();
    }

    private void loop()
    {
        while (!Vars.isOutOfCoins)
        {
            for (final Node node : nodes)
            {
                if (node.validate())
                {
                    node.execute();
                    sleep(General.random(250, 500));
                }
            }
        }
    }

    @Override
    public void onEnd()
    {
        System.out.println("Jugs Filled: " + Vars.jugsFilled);
        General.println("Jugs Filled: " + Vars.jugsFilled);
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
                long jugsPerHour = (long) (Vars.jugsFilled * 3600000d / timeRan);

                // Implementation of text
                g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
                g.drawString( "Jugs Filled " + Vars.jugsFilled, 200, 410);
                g.drawString( "Jugs Filled/H: " + jugsPerHour, 200, 450);
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

