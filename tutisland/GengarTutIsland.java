package scripts.tutisland;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.tutisland.data.Vars;
import scripts.tutisland.framework.Node;
import scripts.tutisland.nodes.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Gengar's Tutorial Island",
        category =      "Misc",
        description =   "Does Tutorial Island.")
public class GengarTutIsland extends Script implements Starting, Ending, Painting
{
    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    @Override
    public void onStart()
    {

    }

    // To do:
    /*
    Weird click to continue interface that would happen on error. have fix for that perhaps another thread.
    scripts.gengarlibrary.Antiban
    Reading account info from external source
     */


    @Override
    public void run() {
        Collections.addAll(nodes,
                new CombatTrainingRoom(),
                new DesignCharacter(),
                new DoBanking(),
                new DoChefStuff(),
                new DoMonkStuff(),
                new DoSurvivalTraining(),
                new DoWizardStuff(),
                new GettingStarted(),
                new MiningArea(),
                new NextArea(),
                new QuestGuideRoom());

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

                    if (!Vars.shouldExecute)
                        break;
                }
            }
            General.sleep(General.random(min, max));
        }

        do
        {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Login.getLoginState() == Login.STATE.LOGINSCREEN;
                }
            }, General.random(10500, 10600));
        } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }

    @Override
    public void onEnd()
    {

    }

    @Override
    public void onPaint(Graphics graphics)
    {

    }
}
