package scripts.gengarcooker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.gengarcooker.data.Constants;
import scripts.gengarcooker.data.Methods;
import scripts.gengarcooker.data.Vars;
import scripts.gengarcooker.framework.Node;
import scripts.gengarcooker.utility.Antiban;

public class CookFish extends Node{

    // Fishing statistics; used in ABC2
    private long lastCookingWaitTime;
    private long averageCookingWaitTime = 64000;
    private long totalCookingWaitTime = 0;
    private long totalCookingInstances = 0;

    private void updateCookingStatistics(long waitTime){
        lastCookingWaitTime = waitTime;
        totalCookingWaitTime += lastCookingWaitTime;
        totalCookingInstances++;
        averageCookingWaitTime = totalCookingWaitTime / totalCookingInstances;

        General.println("Average Fishing Time is: " + averageCookingWaitTime);
    }

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The CookFish Node has been Validated! Executing...");

        RSItem[] rawFood = Inventory.find(Vars.rawFoodID);

        if (rawFood.length > 0){
            rawFood[0].click();

            if (!Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Game.isUptext("Use Raw " + Vars.rawFoodName + " ->");
                }
            }, General.random(500, 550))){
                // Didn't click the food.
                System.out.println("CookFish: Failed to click on the raw food in inventory. Returning...");
                return;
            }

            // Uptext is good.
            RSObject range[] = Objects.find(5, Vars.rangeID);

            if (range.length == 0){
                System.out.println("CookFish: Failed to find cooking surface. Returning...");
                return;
            }

            range[0].hover();
            Methods.safeClick("Use Raw " + Vars.rawFoodName + " -> " + Vars.cookingSurfaceName);

            // Cooking interface
            if (!Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Interfaces.isInterfaceValid(270);
                }
            }, General.random(1600, 1700))){
                System.out.println("CookFish: Cooking interface did not show up. Leaving Node.");
                return;
            }

            RSInterface cookingInterface = Interfaces.get(270);
            RSInterfaceChild allInterfaceButton = Interfaces.get(270, 12);

            if (allInterfaceButton.getActions() != null){
                // The all button isn't selected. select it.
                allInterfaceButton.click();
            }

            // Begins the cooking.
            cookingInterface.getChild(14).click();

            // Wait to start animating
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Player.getAnimation() == Constants.COOKING_ANIMATION;
                }
            }, General.random(1500, 1600));

            long startingCookingTime = System.currentTimeMillis();
            if (isCooking()){
                // Prior to idle scripts.gengarlibrary.Antiban procedures
                Antiban.get().setHoverAndMenuBoolValues();
                Antiban.get().generateTrackers((int)averageCookingWaitTime);
                System.out.println("CookFish: Began cooking.");

                while (isCooking()){
                    General.sleep(200);
                    Antiban.get().timedActions();
                }
                updateCookingStatistics(System.currentTimeMillis() - startingCookingTime);
                Antiban.get().sleepReactionTime((int) averageCookingWaitTime);
            }
        }
        System.out.println("_________________________________________________________________________________________");
    }

    private boolean isCooking(){
        return Player.getAnimation() == Constants.COOKING_ANIMATION;
    }

    @Override
    public boolean validate() {
        // Has raw food and is in range house
        return  (Inventory.getCount(Vars.rawFoodID) > 0                     &&
                (Constants.RANGE_AREA_1.contains(Player.getPosition())      ||
                        Constants.RANGE_AREA_2.contains(Player.getPosition())));
    }
}
