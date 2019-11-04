package scripts.gengarnmz.framework;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.data.potions.NmzPotion;
import scripts.gengarnmz.data.weapons.Blowpipe;

public class Validators
{
    private Validators(){}

    private static boolean isInBank()
    {
        return Constants.AREA_BANK.contains(Player.getPosition());
    }

    private static boolean isInNmzArea()
    {
        return Constants.AREA_NMZ.contains(Player.getPosition());
    }

    private static boolean isUsingBlowpipe()
    {
        return Vars.weapon instanceof Blowpipe;
    }

    private static boolean isBlowpipeEmpty()
    {
        return Vars.outOfScales || Vars.outOfDarts;
    }

    private static boolean isBlowpipeConfigured()
    {
        return !(isBlowpipeEmpty() && shouldChargeBlowpipe());
    }

    private static boolean isUtilityPotionsInInventory()
    {
        return Inventory.find(Vars.utilityPotion.getIds()).length >= 20;
    }

    private static boolean isCombatPotionsInInventory()
    {
        return Inventory.find(Vars.combatPotion.getIds()).length >= 6;
    }

    public static boolean isDreaming()
    {
        return  Player.getPosition().getPlane() == 3;
    }

    public static boolean shouldChargeBlowpipe()
    {
        return  (Inventory.find(Constants.MITHRIL_DARTS_ID).length > 0 && !Vars.isFullDarts) ||
                (Inventory.find(Constants.ZULRAH_SCALES_ID).length > 0 && !Vars.isFullScales);
    }

    public static boolean shouldDepositCoffer()
    {
        return  Vars.isCofferEmpty &&
                Inventory.find(Constants.COINS_ID).length > 0;
    }

    private static boolean needPotionsFromBank()
    {
        return  (!(Vars.combatPotion instanceof NmzPotion) && !isCombatPotionsInInventory()) ||
                (!(Vars.utilityPotion instanceof NmzPotion) && !isUtilityPotionsInInventory());
    }

    public static boolean shouldBank()
    {
        if (isUsingBlowpipe())
        {
            return  isInBank()              &&
                    !shouldChargeBlowpipe() &&
                    (needPotionsFromBank() || isBlowpipeEmpty() || Vars.isCofferEmpty);
        }

        return  isInBank()                  &&
                (needPotionsFromBank() || (Vars.isCofferEmpty && Inventory.find(Constants.COINS_ID).length <= 0));
    }

    public static boolean shouldGetNmzPots()
    {
        // Don't need to get NMZ pots
        if (!(Vars.utilityPotion instanceof NmzPotion || Vars.combatPotion instanceof NmzPotion))
            return false;

        return  !isDreaming()                   &&
                isInNmzArea()                   &&
                isUtilityPotionsInInventory()   &&
                !isCombatPotionsInInventory()   &&
                (!isUsingBlowpipe() || isBlowpipeConfigured());
    }

    private static boolean shouldStartDream()
    {
        return  !isDreaming()                   &&
                isInNmzArea()                   &&
                isUtilityPotionsInInventory()   &&
                isCombatPotionsInInventory()    &&
                !Vars.isCofferEmpty             &&
                (!isUsingBlowpipe() || isBlowpipeConfigured());
    }

    public static boolean shouldStartDreamDominic()
    {
        RSObject potion = Entities.find(ObjectEntity::new)
                            .actionsEquals("Investigate")
                            .getFirstResult();

        return shouldStartDream() && potion != null;
    }

    public static boolean shouldStartDreamPotion()
    {
        RSObject potion = Entities.find(ObjectEntity::new)
                            .actionsEquals("Investigate")
                            .getFirstResult();

        return shouldStartDream() && potion == null;
    }

    public static boolean shouldWalkNmz()
    {
        return  !isDreaming()                                       &&
                !isInNmzArea()                                      &&
                (!isUsingBlowpipe() || !shouldChargeBlowpipe());
    }

    public static boolean shouldWalkBank()
    {
        return  !isDreaming()                                   &&
                !isInBank()                                     &&
                (!isUsingBlowpipe() ||!shouldChargeBlowpipe())  &&
                (!(Vars.combatPotion instanceof NmzPotion) && !isUtilityPotionsInInventory() || (isUsingBlowpipe() && isBlowpipeEmpty()) || (!(Vars.combatPotion instanceof NmzPotion) && !isCombatPotionsInInventory()) || Vars.isCofferEmpty);
    }
}
