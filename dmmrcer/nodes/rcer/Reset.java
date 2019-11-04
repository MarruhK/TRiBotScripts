package scripts.dmmrcer.nodes.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.dmmrcer.framework.Validator;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ItemEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;

/**
 * Leaves altar and prepares for the waitForMule
 */
public class Reset implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The Reset Node has been Validated! Executing...");

        Vars.rcer.reset();
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
