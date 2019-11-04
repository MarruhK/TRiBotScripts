package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.data.potions.NmzPotion;
import scripts.gengarnmz.data.potions.Potion;
import scripts.gengarnmz.data.potions.combat.CombatPotion;
import scripts.gengarnmz.data.potions.utility.AbsorptionPotion;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class GetNmzPots extends Node
{
    private static final RSTile CHEST_TILE = new RSTile(2609, 3118, 0);

    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("GetNmzPots initiated... Executing...");

        if (Vars.combatPotion instanceof NmzPotion)
        {
            handleCombatPotion();
        }

        if (Vars.utilityPotion instanceof NmzPotion)
        {
            handleUtilityPotion();
        }
    }

    private void handleCombatPotion()
    {
        Potion potion = Vars.combatPotion;
        final int totalDoses = ((NmzPotion) potion).getDoses();

        if (totalDoses == potion.getRegularDoseAmount())
        {
            return;
        }

        if (Vars.isBarrelEmptyCombat)
        {
            buyPotions(potion);
            Vars.isBarrelEmptyCombat = false;
        }

        getPotionsFromBarrel(potion, totalDoses);
    }

    private void handleUtilityPotion()
    {
        Potion potion = Vars.utilityPotion;
        final int totalDoses = ((NmzPotion) potion).getDoses();

        if (totalDoses == potion.getRegularDoseAmount())
        {
            return;
        }

        if (Vars.isBarrelEmptyAbsorp)
        {
            buyPotions(potion);
            Vars.isBarrelEmptyAbsorp = false;
        }

        getPotionsFromBarrel(potion, totalDoses);
    }

    private void getPotionsFromBarrel(Potion potion, int totalDoses)
    {
        DaxWalker.walkTo(Constants.AREA_BARRLES.getRandomTile());
        Timing.waitCondition(()-> Player.isMoving(), 1200);
        Timing.waitCondition(()-> !Player.isMoving(), 12500);

        RSObject potionBarrel = Entities.find(ObjectEntity::new)
                                    .nameContains(potion.toString())
                                    .getFirstResult();

        if (potionBarrel == null)
        {
            System.out.println("getPotionsFromBarrel: Unable to located potion barrel, returning...");
            return;
        }

        final int regularDoseAmount = potion.getRegularDoseAmount();

        // Store potions
        if (totalDoses > regularDoseAmount                              &&
            GClicking.clickObject(potionBarrel, "Store")   &&
            Timing.waitCondition(GBooleanSuppliers.isNpcOptionValid(), 1600))
        {
            NPCChat.selectOption("Yes, please.", true);
        }
        else
        {
            System.out.println("getPotionsFromBarrel: Failed to obtain" + potion.toString() + " from barrel. Returning...");
            return;
        }

        // Take potions
        GClicking.clickObject(potionBarrel, "Take");

        RSInterface barrelInterface = Entities.find(InterfaceEntity::new)
                                        .textContains("How many doses of")
                                        .getFirstResult();

        // Fix for when barrel is empty, should not run code can be refactored
        if (Timing.waitCondition(()->
        {
            General.sleep(150);
            return (barrelInterface != null && !barrelInterface.isHidden());
        }, 5000))
        {
            String dosesLeft = barrelInterface.getText();
            dosesLeft = dosesLeft.substring(dosesLeft.indexOf("(") + 1);
            int doses = Integer.parseInt(dosesLeft.substring(0, dosesLeft.length() - 1));

            if (doses < regularDoseAmount)
            {
                if (potion instanceof CombatPotion)
                {
                    Vars.isBarrelEmptyCombat = !buyPotions(potion);
                }
                else
                {
                    Vars.isBarrelEmptyAbsorp = !buyPotions(potion);
                }

                return;
            }

            General.sleep(450, 900);
            String dosesNeeded = "" + regularDoseAmount;                    // "" + (24 - totalDoses);
            Keyboard.typeSend(dosesNeeded);
            Timing.waitCondition(()-> Inventory.find(potion.getIds()).length == regularDoseAmount, 1800);
        }
    }

    private boolean buyPotions(Potion potion)
    {
        DaxWalker.walkTo(CHEST_TILE);
        Timing.waitCondition(GBooleanSuppliers.waitToStartMoving(), 1200);
        Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), 7500);

        RSObject nmzChest = Entities.find(ObjectEntity::new)
                .nameEquals("Rewards Chest")
                .getFirstResult();

        if (nmzChest != null)
        {
            final int masterIndex = 206;

            if (GClicking.clickObject(nmzChest, "Search") && Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(masterIndex), General.random(1250, 1400)))
            {
                System.out.println("buyPotions: Chest has been opened");

                RSInterface pointsInterface = Entities.find(InterfaceEntity::new)
                                                .textContains("Reward points:")
                                                .getFirstResult();

                int dosageAmount = potion.getRegularDoseAmount();
                int cost = ((NmzPotion) potion).getCost();
                int points = Integer.parseInt(pointsInterface.getText().substring(15).replaceAll(",", ""));
                int pointsNeeded = dosageAmount * cost;

                if (points <= pointsNeeded)
                {
                    System.out.println("buyPotions: Not enough points for potions, log out");
                    Vars.shouldExecute = false;
                    return false;
                }

                RSInterface benefitsButton = Entities.find(InterfaceEntity::new)
                                                .actionContains("Benefits")
                                                .getFirstResult();

                if (benefitsButton == null)
                {
                    System.out.println("buyPotions: Unable to click benefits button.");
                    return false;
                }

                if (benefitsButton.click())
                {
                    System.out.println("buyPotions: Going to purchase potions now.");

                    String potionName = potion.toString().replace(" Potion","");
                    RSInterface potionInterface = Entities.find(InterfaceEntity::new)
                                                    .componentNameContains(potionName)
                                                    .getFirstResult();

                    if (potionInterface != null && potionInterface.click("Buy-50"))
                    {
                        if (potion instanceof AbsorptionPotion)
                        {
                            // Cba with non static sleep
                            General.sleep(250, 400);
                            potionInterface.click("Buy-50");
                        }

                        System.out.println("buyPotions: Potions were purchased successfully.");
                    }
                    else
                    {
                        System.out.println("buyPotions: Failed to purchase potions");
                    }

                    RSInterface closeButton = Entities.find(InterfaceEntity::new)
                                                .actionEquals("Close")
                                                .getFirstResult();

                    if (closeButton != null && closeButton.click())
                    {
                        System.out.println("buyPotions: Successfully closed the chest interface.");
                        return true;
                    }
                }
                else
                {
                    System.out.println("buyPotions: Failed to open the main interface");
                }
            }
        }

        return false;
    }

    /*private boolean buyPotions()
    {
        DaxWalker.walkTo(CHEST_TILE);
        Timing.waitCondition(GBooleanSuppliers.waitToStartMoving(), 1200);
        Timing.waitCondition(GBooleanSuppliers.waitToStopMoving(), 7500);

        RSObject nmzChest = Entities.find(ObjectEntity::new)
                                .nameEquals("Rewards Chest")
                                .getFirstResult();

        if (nmzChest != null)
        {
            final int masterIndex = 206;

            if (GClicking.clickObject(nmzChest, "Search") && Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(masterIndex), General.random(1250, 1400)))
            {
                System.out.println("buyPotions: Chest has been opened");
                final int rangePotCost = 250;

                final int absorptionPotCost = 1000;
                int potionCost;
                String potionName;



                if (Vars.combatPotionIds == Constants.ID_SUPER_RANGING_POTIONS)
                {
                    potionCost = rangePotCost;
                    potionName = "Super ranging";
                }
                else
                {
                    potionCost = overloadPotCost;
                    potionName = "Overload";
                }

                RSInterface pointsInterface = Entities.find(InterfaceEntity::new)
                                                .textContains("Reward points:")
                                                .getFirstResult();

                int points = Integer.parseInt(pointsInterface.getText().substring(15).replaceAll(",", ""));
                int pointsNeeded = 24 * potionCost;

                if (points <= pointsNeeded)
                {
                    System.out.println("buyPotions: Not enough points for potions, log out");
                    Vars.shouldExecute = false;
                    return false;
                }

                RSInterface benefitsButton = Entities.find(InterfaceEntity::new)
                        .actionContains("Benefits")
                        .getFirstResult();

                if (benefitsButton == null)
                {
                    System.out.println("buyPotions: Unable to click benefits button.");
                    return false;
                }

                if (benefitsButton.click())
                {
                    System.out.println("buyPotions: Going to purchase potions now.");

                    RSInterface potionInterface = Entities.find(InterfaceEntity::new)
                            .componentNameContains(potionName)
                            .getFirstResult();

                    if (potionInterface != null && potionInterface.click("Buy-50"))
                    {
                        System.out.println("buyPotions: Potions were purchased successfully.");
                    }
                    else
                    {
                        System.out.println("buyPotions: ");
                    }
                }

                RSInterface closeButton = Entities.find(InterfaceEntity::new)
                        .actionEquals("Close")
                        .getFirstResult();

                if (closeButton != null && closeButton.click())
                {
                    System.out.println("buyPotions: Successfully closed the chest interface.");
                    return true;
                }
            }
            else
            {
                System.out.println("buyPotions: Failed to open the main interface");
            }
        }

        return false;
    }*/


    /*private boolean isCofferEmpty()
    {
        final int master = 207;
        final int child = 8;

        RSInterface cofferInterface = null;
        RSInterface[] cofferInterfaces = Entities.find(InterfaceEntity::new)
                .inMasterAndChild(master, child)
                .getResults();

        for (RSInterface rsInterface : cofferInterfaces)
        {
            if (rsInterface.getComponentItem() == Constants.COINS_ID)
            {
                cofferInterface = rsInterface;
            }
        }

        if (cofferInterface != null && !cofferInterface.isHidden())
        {
            int cofferCoins = cofferInterface.getComponentStack();

            if (cofferCoins < 26000)
            {
                System.out.println("isCofferEmpty: coffer is empty. Getting Gp from bank to fill it up.");
                Vars.isCofferEmpty = true;
                return true;
            }
        }

        return false;
    }*/

    /*private void getPotionsFromBarrel(int totalDoses)
    {
        final int idRangePotBucket = 26277;
        final int idOverloadPotBucket = 26279;
        int idPotBucket;
        RSTile tileToWalkTo;

        if (Vars.combatPotionIds == Constants.ID_SUPER_RANGING_POTIONS)
        {
            tileToWalkTo = BARREL_TILE_RANGING;
            idPotBucket = idRangePotBucket;
        }
        else
        {
            tileToWalkTo = BARREL_TILE_OVERLOAD;
            idPotBucket = idOverloadPotBucket;
        }

        DaxWalker.walkTo(tileToWalkTo);
        Timing.waitCondition(()-> Player.isMoving(), 1200);
        Timing.waitCondition(()-> !Player.isMoving(), 12500);

        // Check coffer gp interface
        if (isCofferEmpty())
            return;

        final int masterIndex = 162;
        final int childIndex = 44;

        RSObject[] potBucket = Objects.find(8, idPotBucket);
        RSInterface barrelInterface = Interfaces.get(masterIndex, childIndex);

        if (potBucket.length <= 0)
        {
            System.out.println("getPotions: Can't find potion bucket.");
            return;
        }

        // Store potions
        if (totalDoses > 24)
        {
            GClicking.clickObject(potBucket[0], "Store");

            if (Timing.waitCondition(()-> NPCChat.getOptions() != null, 5000))
                NPCChat.selectOption("Yes, please.", false);
        }

        AccurateMouse.click(potBucket[0], "Take");

        // Fix for when barrel is empty, should not run code can be refactored
        if (Timing.waitCondition(()-> (barrelInterface != null && !barrelInterface.isHidden()), 5000))
        {
            String dosesLeft = barrelInterface.getText();
            dosesLeft = dosesLeft.substring(dosesLeft.indexOf("(") + 1);
            int doses = Integer.parseInt(dosesLeft.substring(0, dosesLeft.length() - 1));

            if (doses < 24)
            {
                Vars.isBarrelEmptyCombat = !buyPotions();
                return;
            }

            General.sleep(450, 900);
            String dosesNeeded = "" + (24 - totalDoses);
            Keyboard.typeSend(dosesNeeded);
            Timing.waitCondition(()-> Inventory.find(Constants.ID_OVERLOADS).length >= 24, 1800);
        }
    }*/



    @Override
    public boolean validate()
    {
        return  Validators.shouldGetNmzPots();
    }
}
