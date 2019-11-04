package scripts.dmmrcer.nodes.runner;

import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;

public class WalkToRcer implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToRcer Node has been Validated! Executing...");

        Vars.runner.walkToRcer();
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
