package scripts.gengarcooker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import scripts.gengarcooker.data.Vars;
import scripts.gengarcooker.framework.Node;

public class BankFood extends Node {

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BankFood Node has been Validated! Executing...");

        Banking.openBank();

        if (Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Banking.isBankScreenOpen();
            }
        }, General.random(1000, 1050))) {
            // Banking screen is open
            Banking.depositAll();

            if (Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Inventory.getAll().length == 0;
                }
            }, General.random(1000, 1050))) {

                // Withdraw raw food you cooking.
                if (Banking.find(Vars.rawFoodID).length > 0) {
                    Banking.withdraw(28, Vars.rawFoodID);
                } else {
                    // Apparently no food, double check.
                    if (isNoFoodEnsured()) {
                        // No food.
                        System.out.println("BankFood: Ran out of food to cook. Ending script soon...");
                        Vars.shouldExecute = false;
                    }
                }

                // Wait to ensure food in inventory.
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return Inventory.getAll().length > 0;
                    }
                }, General.random(1300, 1350));
            }
            Banking.close();
        }
        System.out.println("_________________________________________________________________________________________");
    }

    private boolean isNoFoodEnsured(){
        if (Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Banking.isBankScreenOpen();
            }
        }, General.random(1200, 1300))){
            // EdgeBanking screen is open.
            General.sleep(1000);

            if (Banking.find(Vars.rawFoodID).length <= 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validate() {
        return (Inventory.find(Vars.rawFoodID).length == 0  &&
                Banking.isInBank());
    }
}
