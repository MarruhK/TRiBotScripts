package scripts.gnomefisher.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import scripts.gnomefisher.data.Variables;


public class Methods {

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
        System.out.println("---- The safeClick method began.");
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

    public static void clickObject(RSObject object, int animation, String desiredUpText){
        System.out.println("-- The clickObject method began.");
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
            }, General.random(2300, 2400))){
                System.out.println("-- Successfully animated.");

                // Wait for reg animation
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return Player.getAnimation() == -1;
                    }
                }, General.random(1600, 1700));
            } else {
                System.out.println("-- No animation occured.");
            }
        }
        // Didn't properly click
        System.out.print("-------------------------------------------------------------");
        System.out.print("UNABLE TO CLICK THE OBJECT. SOMETHING FUCKED UP.");
        System.out.print("Object position: " + object.getPosition());
        System.out.print("Object ID: " + object.getID());
        System.out.print("Object Name: " + object.getDefinition().getName());
        System.out.print("OBJECT NULL: " + (object == null));
        System.out.print("-------------------------------------------------------------");
    }

    public static void shouldUndoUpText(String undesiredUpText1, String undesiredUpText2){
        // Ensure uptext is fine, then drop
        if (Game.isUptext(undesiredUpText1) || Game.isUptext(undesiredUpText2)){
            System.out.println("Need to undo uptext");
            // Undo uptext
            Inventory.getAll()[0].click();
        }
    }

    public static boolean isFeathersSufficient(){
        // Check if you have baits.
        if (Inventory.find(Constants.FEATHERS_ID).length == 0){
            System.out.println("No more feathers to fish. Logging out.");
            Variables.shouldExecute = false;
            return false;
        }
        return true;
    }
}
