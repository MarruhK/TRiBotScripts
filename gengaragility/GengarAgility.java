package scripts.gengaragility;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.gengarlibrary.ACamera;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.gengaragility.data.Vars;
import scripts.gengaragility.framework.Node;
import scripts.gengaragility.nodes.*;
import scripts.gengaragility.nodes.obstalcles.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GengarAgility extends Script implements Starting, Painting
{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();
    private ACamera aCamera = new ACamera(this);

    // Paint stuff
    private long startTime = System.currentTimeMillis();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.AGILITY);
    private int startXP = Skills.getXP(Skills.SKILLS.AGILITY);

    @Override
    public void onStart()
    {
        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjcfqN4YkIxm8", "PUBLIC-KEY");
            }
        });
    }

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                        new ClimbWall(this.aCamera),
                        new CrossRope(this.aCamera),
                        new JumpEdge(this.aCamera),
                        new JumpGapOne(this.aCamera),
                        new JumpGapTwo(this.aCamera),
                        new JumpGapThree(this.aCamera),
                        new ReturnToStart(this.aCamera),
                        new ErrorNode(this.aCamera));

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
    }

    @Override
    public void onPaint(Graphics g) {
        // Paint image
        Image img = getImage("https://i.imgur.com/0aQy9Ce.png");
        Graphics2D gg = (Graphics2D)g;
        gg.drawImage(img, 0, 339, null);

        // Text data
        long timeRan = System.currentTimeMillis() - startTime;
        int currentLvl = Skills.getActualLevel(Skills.SKILLS.AGILITY);
        int gainedLVL = currentLvl - startLvl;

        int gainedXP = Skills.getXP(Skills.SKILLS.AGILITY) - startXP;
        long xpPerHour = (long) (gainedXP * 3600000d / timeRan);

        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
        g.drawString("Gained AGILITY XP: " + gainedXP + " (Levels: " + gainedLVL + ")", 200, 410);
        g.drawString("AGILITY XP/H: " + xpPerHour, 200, 450);
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
