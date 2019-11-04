package scripts.cooker;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSArea;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.cooker.data.Constants;
import scripts.cooker.data.Vars;
import scripts.cooker.framework.Node;
import scripts.cooker.nodes.Bank;
import scripts.cooker.nodes.Cook;
import scripts.cooker.nodes.WalkToBank;
import scripts.cooker.nodes.WalkToRange;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cooker extends Script implements Painting, Starting
{
    private final List<Node> nodes = new ArrayList<>();

    @Override
    public void onStart()
    {
        // No GUI or anything else, so this is all that is needed atm.
        General.useAntiBanCompliance(true);

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        Vars.rawFoodId = Constants.RAW_SHARK_ID;
        Vars.bankArea = Constants.BANK_AREA_VARROCK;
        Vars.rangeArea = Constants.RANGE_AREA_VARROCK;
    }

    @Override
    public void run()
    {
        Collections.addAll(nodes,
                    new Bank(),
                    new Cook(),
                    new WalkToBank(),
                    new WalkToRange());

        loop();
    }

    private void loop()
    {
        while (Vars.shouldExecute)
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
    public void onPaint(Graphics graphics)
    {

    }
}
