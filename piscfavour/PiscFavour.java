package scripts.piscfavour;

import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import scripts.piscfavour.framework.Node;
import scripts.piscfavour.nodes.BuyBuckets;
import scripts.piscfavour.nodes.CollectWorms;
import scripts.piscfavour.nodes.GoStore;
import scripts.piscfavour.nodes.GoWormField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Piscarilius Favour",
        category =      "Bitch Work",
        description =   "Picks worms for favour. " +
                "          Requires 15 Hunter and 30% favour.")
public class PiscFavour extends Script {
    // Small efficiency errors like misclicking NPC and not stopping when 100% favour

    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    @Override
    public void run() {
        Collections.addAll(nodes,
                new BuyBuckets(),
                new CollectWorms(),
                new GoStore(),
                new GoWormField());

        loop(250, 500);
    }

    private void loop(int min, int max) {
        while (true) {
            for (final Node node : nodes) {
                if (node.validate()) {
                    node.execute();
                    sleep(General.random(min, max));	//time in between executing nodes
                }
            }
        }
    }
}
