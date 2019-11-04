package scripts.anglerfish.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceMaster;
import scripts.anglerfish.data.Constants;
import scripts.anglerfish.data.Vars;
import scripts.anglerfish.framework.Node;

public class Bank extends Node {
    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The EdgeBanking Node has been Validated! Executing...");

        Banking.openBank();

        if (Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return Banking.isBankScreenOpen();
            }
        }, General.random(1000, 1050))){
            // Banking screen is open
            Banking.depositAllExcept(Constants.BAIT_ID, Constants.FISHING_ROD_ID, Constants.NOTED_ANGLERFISH_ID,
                    Constants.TELETAB_ID[0], Constants.TELETAB_ID[1]);

            if (!isBankNotedOn()){
                setBankNotedOn();
            }

            Banking.withdraw(200, Constants.ANGLERFISH_ID);
            Banking.close();
        }
        System.out.println("_________________________________________________________________________________________");
    }

    private static boolean isBankNotedOn(){
        if (Game.getSetting(115) == 1){
            // It is on
            System.out.println("EdgeBanking already has noted set ON.");
            return true;
        } else {
            System.out.println("EdgeBanking has noted set OFF.");
            return false;
        }
    }

    private static boolean setBankNotedOn(){
        RSInterfaceMaster bankInterface = Interfaces.get(12);
        if (bankInterface.isValid()){
            // EdgeBanking is open so check if it noted is selected.
            if (!isBankNotedOn()){
                // Turn noted setting on.
                bankInterface.getChild(24).click();

                if(Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return isBankNotedOn();
                    }
                }, General.random(1000,1100))){
                    System.out.println("EdgeBanking noted setting is on now.");
                    return true;
                } else {
                    System.out.println("Failed to set on noted.");
                    return false;
                }
            }
            // Already on.
            return true;
        }
        System.out.println("Banking window isn't even opened, leaving.");
        return false;
    }

    @Override
    public boolean validate() {
        // Full inventory and player is in the bank.
        return (Vars.shouldExecute &&
                Inventory.isFull() &&
                Banking.isInBank());
    }
}
