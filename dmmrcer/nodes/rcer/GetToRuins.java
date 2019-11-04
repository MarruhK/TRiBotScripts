package scripts.dmmrcer.nodes.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.WebWalking;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.dmmrcer.framework.Validator;
import scripts.gengarlibrary.GBooleanSuppliers;

public class GetToRuins implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The GetToRuins Node has been Validated! Executing...");

        Vars.rcer.getToRuins();
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
