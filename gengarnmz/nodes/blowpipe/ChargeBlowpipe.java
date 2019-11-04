package scripts.gengarnmz.nodes.blowpipe;

import org.tribot.api.General;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class ChargeBlowpipe extends Node
{
    private static final int[] BLOWPIPE_IDS = {12926, 12924};

    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("ChargeBlowpipe initiated... Executing...");
        System.out.println("Un-equip blowpipe and charge blowpipe...");

        if (Equipment.remove(Equipment.SLOTS.WEAPON) > 0 || Inventory.find(BLOWPIPE_IDS).length > 0)
        {
            if (clickInventoryItem(Constants.ZULRAH_SCALES_ID))
                clickInventoryItem(BLOWPIPE_IDS);

            if (clickInventoryItem(Constants.MITHRIL_DARTS_ID))
                clickInventoryItem(BLOWPIPE_IDS);

            clickInventoryItem("Wield", BLOWPIPE_IDS);
        }
    }

    private boolean clickInventoryItem(int... id)
    {
        return clickInventoryItem("Use", id);
    }

    private boolean clickInventoryItem(String uptext, int... id)
    {
        RSItem[] items = Inventory.find(id);

        if (items.length > 0)
        {
            items[0].click(uptext);
            General.sleep(250, 384);
            return true;
        }

        return false;
    }

    @Override
    public boolean validate()
    {
        return Validators.shouldChargeBlowpipe();
    }
}
