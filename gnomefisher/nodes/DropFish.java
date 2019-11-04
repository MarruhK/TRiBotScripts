package scripts.gnomefisher.nodes;

import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.gnomefisher.data.Constants;
import scripts.gnomefisher.data.Methods;
import scripts.gnomefisher.framework.Node;

import java.awt.event.KeyEvent;

public class DropFish extends Node {

    @Override
    public void execute() {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The DropFish Node has been Validated! Executing...");

        RSItem[] inventItems = Inventory.getAll();  // No null check since invent is full

        // Holds shift key
        Keyboard.sendPress(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_SHIFT);

        for (RSItem item : inventItems) {
            if (item.getID() != Constants.FLY_ROD_ID && item.getID() != Constants.FEATHERS_ID){
                if (item.getIndex() == 26 || item.getIndex() == 27){
                    break;
                }

                Methods.shouldUndoUpText("Use Raw salmon ->", "Use Raw trout ->");
                item.click();
                General.sleep(100, 200);
            }
        }

        // Releases shift key
        Keyboard.sendRelease(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_SHIFT);
        System.out.println("_________________________________________________________________________________________");
    }

    @Override
    public boolean validate() {
        return Inventory.isFull();
    }
}
