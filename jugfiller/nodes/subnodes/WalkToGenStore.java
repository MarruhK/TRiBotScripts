package scripts.jugfiller.nodes.subnodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.jugfiller.data.Constants;
import scripts.jugfiller.data.Vars;
import scripts.jugfiller.framework.Node;
import scripts.jugfiller.framework.Validators;

public class WalkToGenStore extends Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The WalkToGenStore subnode has been Validated! Executing...");

        if (!DaxWalker.walkTo(Constants.GEN_STORE_AREA_1.getRandomTile()) || !Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), General.random(3500, 3700)))
        {
            System.out.println("execute: Failed to walk to the gen store.");
        }
    }

    @Override
    public boolean validate()
    {
        return  Vars.isOutOfJugs &&
                !Validators.isInGeneralStore();

    }
}
