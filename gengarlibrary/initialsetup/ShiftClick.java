package scripts.gengarlibrary.initialsetup;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;

public class ShiftClick
{
    public static void activateShiftClick()
    {
        final int selectedTextureId = 762;

        RSInterface shiftClickInterface = Entities.find(InterfaceEntity::new)
                                            .actionEquals("Toggle shift click to drop")
                                            .getFirstResult();

        while (shiftClickInterface.getTextureID() != selectedTextureId)
        {
            RSInterface controlsInterface = Entities.find(InterfaceEntity::new)
                                                .actionEquals("Controls")
                                                .getFirstResult();

            if (controlsInterface == null)
                continue;

            if (controlsInterface.isHidden())
                GameTab.open(GameTab.TABS.OPTIONS);

            if (controlsInterface.getTextureID() != selectedTextureId)
            {
                General.sleep(175, 325);
                controlsInterface.click();

                if (!Timing.waitCondition(()-> !shiftClickInterface.isHidden(), General.random(1250, 1370)))
                {
                    System.out.println("ShiftClick: Failed to click the controls interface...");
                    continue;
                }
            }

            General.sleep(175, 325);
            shiftClickInterface.click();
        }
    }
}
