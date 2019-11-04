package scripts.clayminer.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Node;
import scripts.clayminer.framework.Validators;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GClicking;


public class LeaveBottomFloor implements Node
{
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The LeaveBottomFloor Node has been Validated! Executing...");

        RSObject ladder = Entities.find(ObjectEntity::new)
                            .nameEquals("Ladder")
                            .getFirstResult();

        if (ladder == null || !GClicking.clickObject(ladder, "Climb-up", Constants.LADDER_CLIMB_ANIMATION))
        {
            System.out.println("execute: Failed to click Ladder :/");
            WebWalking.walkTo(Constants.BOTTOM_LADDER_TILE);
        }

        Timing.waitCondition(()->
        {
            General.sleep(150);
            return Validators.isInMiddleFloor();
        }, General.random(2450, 2600));
    }

    @Override
    public boolean validate()
    {
        return Validators.isInBottomFloor() && Inventory.isFull() && !Validators.isInMine();
    }
}
