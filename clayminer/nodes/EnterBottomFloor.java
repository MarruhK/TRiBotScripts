package scripts.clayminer.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.clayminer.framework.Node;
import scripts.clayminer.framework.Validators;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;

public class EnterBottomFloor implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The EnterBottomFloor Node has been Validated! Executing...");

        RSObject trapdoor = Entities.find(ObjectEntity::new).nameEquals("trapdoor").getFirstResult();

        if (trapdoor != null)
        {
            final int trapDoorAnimation = 827;

            GClicking.clickObject(trapdoor, "Open", trapDoorAnimation);

            Timing.waitCondition(()->
            {
                General.sleep(150);
                return Validators.isInBottomFloor();
            }, General.random(2450, 2600));
        }
    }

    @Override
    public boolean validate()
    {
        return Validators.isInMiddleFloor() && !Inventory.isFull();
    }
}
