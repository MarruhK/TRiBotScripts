package scripts.softclaymaker;

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


import scripts.softclaymaker.data.Variables;
import scripts.softclaymaker.framework.Node;
import scripts.softclaymaker.nodes.BankSoftClays;
import scripts.softclaymaker.nodes.MakeClays;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Soft Clay Maker",
        category =      "DMM",
        description =   "Makes soft clay using jugs of water and clays. Currently only supports Varrock East EdgeBanking.")
public class SoftClayMaker extends Script implements Starting, Ending, Painting
{
    private final List<Node> nodes = new ArrayList<>();

    @Override
    public void onStart()
    {
        // No GUI or anything else, so this is all that is needed atm.
        General.useAntiBanCompliance(true);
    }

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                new BankSoftClays(),
                new MakeClays());

        loop();
    }

    private void loop()
    {
        while (Variables.shouldExecute)
        {
            for (final Node node : nodes)
            {
                if (node.validate())
                {
                    node.execute();
                    sleep(General.random(250, 400));	//time in between executing nodes
                }
            }
        }

        // Log out
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
    public void onEnd()
    {
    }

    @Override
    public void onPaint(Graphics g)
    {
    }
}
