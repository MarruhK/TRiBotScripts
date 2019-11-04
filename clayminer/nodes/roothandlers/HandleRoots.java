package scripts.clayminer.nodes.roothandlers;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.clayminer.data.Constants;
import scripts.clayminer.framework.Validators;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GClicking;

public abstract class HandleRoots
{
    boolean passRoots()
    {
        RSObject root = Entities.find(ObjectEntity::new)
                            .nameEquals("Roots")
                            .actionsEquals("Push")
                            .getFirstResult();

        if (root != null)
        {
            boolean isInMine = Validators.isInMine();

            if (GClicking.clickObject(root, "Push", Constants.PASSING_ROOT_ANIMATION))
            {
                // Wait till you fully pass the roots.
                System.out.println("passRoots: Successfully clicked the root now waiting to pass.");

                return Timing.waitCondition(()->
                {
                    General.sleep(100);
                    return (isInMine != Validators.isInMine() && !Player.isMoving());
                }, General.random(3200, 3600));
            }
        }

        return false;
    }
}
