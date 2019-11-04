package scripts.tutisland.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Player;
import scripts.tutisland.data.APIMethods;
import scripts.tutisland.data.Constants;
import scripts.tutisland.framework.Node;

import java.util.HashMap;

/**
 * Node used for designing the character (first interface upon account creation)
 */
public class DesignCharacter implements Node
{
    private static final int MASTER_INTERFACE_DESIGN = 269;

    // 113-119 for left bar
    // 121, 127, 129-131 for right side
    // 138 choose male, 139 choose female
    //  click accept

    /*
    23 hair styles
    14 bears
    13 shirts
    11 arms
    2 hands
    10 legs
    2 feet
    24 hair
    28 shirts
    28 pants
    5 feet
    7 skins
     */

    @Override
    public void execute()
    {
        System.out.println("___________________________________________________________________________________________");
        System.out.println("DesignCharacter Node has been initialized!");

        if (APIMethods.isDesignInterfaceOpen())
            designCharacter();
    }

    private static void designCharacter()
    {
        // Randomize gender
        if (Math.random() <= 0.5)
            Interfaces.get(MASTER_INTERFACE_DESIGN, 139).click();

        General.sleep(55, 70);

        // Randomize outfit shit
        clickButton(113, (int) Math.random() * 23);
        clickButton(114, (int) Math.random() * 14);
        clickButton(115, (int) Math.random() * 13);
        clickButton(116, (int) Math.random() * 11);
        clickButton(117, (int) Math.random() * 2);
        clickButton(118, (int) Math.random() * 10);
        clickButton(119, (int) Math.random() * 2);
        clickButton(121, (int) Math.random() * 24);
        clickButton(127, (int) Math.random() * 28);
        clickButton(129, (int) Math.random() * 28);
        clickButton(130, (int) Math.random() * 5);
        clickButton(131, (int) Math.random() * 7);

        General.sleep(788, 800);

        // click accept
        Interfaces.get(MASTER_INTERFACE_DESIGN, 100).click();

        Timing.waitCondition(APIMethods.conditionIsInterfaceNotValid(MASTER_INTERFACE_DESIGN), 3600);
    }

    private static void clickButton(int child, int amountOfClicks)
    {
        Interfaces.get(MASTER_INTERFACE_DESIGN, child).click();

        for (int i = 0; i < amountOfClicks; i++)
        {
            Mouse.click(1);
            General.sleep(50, 85);
        }
        General.sleep(305, 344);
    }

    @Override
    public boolean validate()
    {
        return APIMethods.isDesignInterfaceOpen() && Constants.AREA_DCGS.contains(Player.getPosition());
    }
}
