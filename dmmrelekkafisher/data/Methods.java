package scripts.dmmrelekkafisher.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSNPC;

public abstract class Methods {

    public static boolean waitToStopMoving(int low, int high){
        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(100);
                return !Player.isMoving();
            }
        }, General.random(low, high));
    }

    // Needs mouse to be hovering positionable
    public static boolean safeClick(String desiredUptext){
        // Wait for mouse to hover positionable
        General.sleep(400,500);

        // fail safe for spam right click on option
        if (ChooseOption.isOpen()){
            ChooseOption.select("Cancel");
            return false;
        }

        // Before clicking, ensure nothing is in the way
        if (!Game.isUptext(desiredUptext)){
            Mouse.click(3);

            if (Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return ChooseOption.isOpen();
                }
            }, General.random(400,500))){
                if (ChooseOption.isOptionValid(desiredUptext)){
                    ChooseOption.select(desiredUptext);
                    return true;
                }
            }
            // Didn't find right option or didn't right click properly.
            return false;
        } else {
            Mouse.click(1);
            return true;
        }
    }

    public static void clickNPC(RSNPC object, int animation, String desiredUpText){
        if (!object.isOnScreen()){
            Camera.turnToTile(object.getPosition());
        }
        object.hover();
        System.out.print("Just hovered over obj at position " + object.getPosition());

        if (Methods.safeClick(desiredUpText)){
            if(Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Player.getAnimation() == animation;
                }
            }, General.random(4000, 4100))){
                System.out.println("clickNPC: Successfully animated.");
                return;
            }
            // make it so that if still moving forgive and wait a vit more.
            if (Player.isMoving()){
                if(Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return Player.getAnimation() == animation;
                    }
                }, General.random(2000, 2050))){
                    System.out.println("clickNPC: Successfully animated.");
                    return;
                }
            }
        }
        // Didn't properly click
        System.out.print("clickNPC -------------------------------------------------------------");
        System.out.print("UNABLE TO CLICK THE OBJECT. SOMETHING FUCKED UP.");
        if (object == null) {
            System.out.print("Object Null: TRUE");
            System.out.print("-------------------------------------------------------------");
            return;
        }
        System.out.print("Object position: " + object.getPosition());
        System.out.print("Object ID: " + object.getID());
        System.out.print("Object Null: " + (object == null));
        System.out.print("clickNPC -------------------------------------------------------------");
    }

    public static void logout(){
        do {
            System.out.println("logout: Time to log out.");
            Login.logout();

            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Login.getLoginState() == Login.STATE.LOGINSCREEN;
                }
            }, General.random(10500, 10600));
        } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }
}
