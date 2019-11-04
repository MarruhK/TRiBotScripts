package scripts.piscfavour.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import scripts.piscfavour.data.Constants;
import scripts.piscfavour.framework.Node;

public class CollectWorms extends Node {

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The CollectWorms Node has been Validated! Executing...");

        // has atleast an empty bucket along with being close to worm tile.
        RSObject[] worms = Objects.findNearest(10, Constants.WORMS_ON_GROUND_ID);

        if (worms.length > 0){
            System.out.println("Attempting to click worm. Nearby worms = " + worms.length);
            if (!worms[0].isOnScreen()){
                Camera.turnToTile(worms[0].getPosition());
            }
            worms[0].hover();

            // Wait for mouse to hover the worms
            if (!Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Game.isUptext("Dig Sandworm castings");
                }
            }, General.random(1400,1500))){
                Mouse.click(3);
                if (ChooseOption.isOpen()){
                    ChooseOption.select("Dig Sandworm castings");
                }
            } else {
                worms[0].click();
            }

            // Wait for picking animation.
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Player.getAnimation() == Constants.WORM_PICKING_ANIMATION;
                }
            }, General.random(4100, 4150));
        } else {
            System.out.println("Did not find any worms to pick from ground.");
        }
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return (Inventory.find(Constants.BUCKETS_ID).length > 0 &&
                Constants.WORM_FIELD_TILE.distanceTo(Player.getPosition()) <= 11);
    }
}
