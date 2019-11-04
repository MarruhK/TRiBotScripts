package scripts.dmmrcer.nodes.rcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.dmmrcer.framework.Validator;
import scripts.gengarlibrary.GClicking;

public class EnterRuins implements Node
{
    private Boolean isUsingTalisman = null;

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The EnterRuins Node has been Validated! Executing...");

        Vars.rcer.enterRuins();
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
