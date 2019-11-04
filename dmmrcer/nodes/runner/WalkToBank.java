package scripts.dmmrcer.nodes.runner;

import org.tribot.api2007.types.RSTile;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;

public class WalkToBank implements Node
{

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToBank Node has been Validated! Executing...");

        Vars.runner.walkToBank();
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
