package scripts.dmmblastfurnace;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.dmmblastfurnace.data.Vars;
import scripts.dmmblastfurnace.framework.Node;
import scripts.dmmblastfurnace.gui.GUI;
import scripts.dmmblastfurnace.nodes.TravelToBlastFurnace;
import scripts.dmmblastfurnace.nodes.TravelToKeldagrim;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DMMBlastFurnace extends Script implements Starting, Painting, Ending
{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private long recentChangeTime;

    @Override
    public void onStart()
    {
        General.useAntiBanCompliance(true);
        new GUI();

        // Wait for GUI to get all variables.
        while (!Vars.shouldExecute)
        {
            General.sleep(500);

            if (Vars.safeCloseGUI)
                break;
        }

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });
    }

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                Vars.task,
                new TravelToKeldagrim(),
                new TravelToBlastFurnace());

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

    private static final int ID_BROKEN_PIPE_1 = 9117;
    private static final int ID_BROKEN_PIPE_2 = 9121;
    private static final int ID_PIPES = 9090;
    private static final int ANIMATION_OPERATE = 2432;

    @Override
    public void onPaint(Graphics g)
    {
        drawText(g);



    }

    private void drawText(Graphics g)
    {
        // Implementation of text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("pumpTime: " + Vars.pumpTime, 200, 370);
        g.drawString("idleTime: " + Vars.idleTime, 200, 410);
        g.drawString("netTime: " + Vars.netTime, 200, 450);
    }

    private boolean isPumping()
    {
        return Player.getAnimation() == ANIMATION_OPERATE;
    }

    private boolean arePipesBroken()
    {
        return Objects.find(15, ID_BROKEN_PIPE_1, ID_BROKEN_PIPE_2).length > 0;
    }

    @Override
    public void onEnd()
    {
        System.out.println("pumpTime: " + Vars.pumpTime);
        System.out.println("idleTime: " + Vars.idleTime);
        System.out.println("netTime: " + Vars.netTime);
    }
}
