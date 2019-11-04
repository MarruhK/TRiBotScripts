package scripts.jugfiller.nodes;

import org.tribot.api.General;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;
import scripts.jugfiller.nodes.subnodes.BuyJugs;
import scripts.jugfiller.nodes.subnodes.WalkToGenStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetJugs extends Node
{
    private final List<Node> subnodes = new ArrayList<>();

    public GetJugs()
    {
        Collections.addAll(subnodes,
                            new BuyJugs(),
                            new WalkToGenStore());
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The GetJugs Node has been Validated! Executing...");

        while(Vars.isOutOfJugs && !Vars.isOutOfCoins)
        {
            for (final Node node : subnodes)
            {
                if (node.validate())
                {
                    node.execute();
                    General.sleep(100, 150);	//time in between executing nodes
                }

                if (!Vars.isOutOfJugs || Vars.isOutOfCoins)
                {
                    break;
                }
            }
        }
    }

    @Override
    public boolean validate() {
        return Vars.isOutOfJugs;
    }
}
