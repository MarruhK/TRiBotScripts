package scripts.fruitstallthiever.nodes;

import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import scripts.fruitstallthiever.framework.Node;

import java.awt.event.KeyEvent;

public class Drop extends Node
{
    private static String[] useableTexts = {"Cooking apple", "Golovanova fruit top", "Pineapple", "Redberries"};

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The Drop Node has been Validated! Executing...");

        RSItem[] inventItems = Inventory.getAll();

        // Holds shift key
        Keyboard.sendPress(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_SHIFT);

        for (RSItem item : inventItems)
        {
            if (shouldUndoUpText())
            {
                Inventory.getAll()[0].click();
                General.sleep(130, 245);
            }

            item.click();
            General.sleep(130, 245);
        }

        // Releases shift key
        Keyboard.sendRelease(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_SHIFT);
    }

    private boolean shouldUndoUpText()
    {
        for (String text : useableTexts)
        {
            if (Game.getUptext().equals("Use " + text + " ->"))
            {
                System.out.println("Need to undo uptext");
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean validate()
    {
        return Inventory.isFull() && !Player.getRSPlayer().isInCombat();
    }
}
