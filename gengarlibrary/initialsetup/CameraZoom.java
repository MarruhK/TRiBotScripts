package scripts.gengarlibrary.initialsetup;

import org.tribot.api.input.Mouse;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;

public class CameraZoom
{
    public static void zoomOut()
    {
        final int masterOption = 261;
        final int childOption = 10;
        final RSInterfaceChild zoomInterface = Interfaces.get(masterOption, childOption);

        if (GameTab.open(GameTab.TABS.OPTIONS) && zoomInterface != null)
        {
            zoomInterface.click();
            GameTab.open(GameTab.TABS.INVENTORY);
        }
    }
}
