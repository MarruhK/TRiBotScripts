package scripts.gengarnmz.data.potions.utility;

import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSInterface;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.gengarlibrary.algorithms.NormalDistribution;
import scripts.gengarnmz.data.potions.NmzPotion;

public class AbsorptionPotion extends UtilityPotion implements NmzPotion
{
    private int masterIndex;
    private int childIndex;
    private int componentIndex;

    public AbsorptionPotion()
    {
        super();
        oneDoseId = 11737;
        twoDoseId = 11736;
        threeDoseId = 11735;
        fourDoseId = 11734;

        boostAmount = getBoostAmount();
        setValueToDrinkAt();
    }

    @Override
    public int getDoses()
    {
        final int dosesOneDose =     Inventory.getCount(oneDoseId);
        final int dosesTwoDose =     Inventory.getCount(twoDoseId) * 2;
        final int dosesThreeDose =   Inventory.getCount(threeDoseId) * 3;
        final int dosesFourDose =    Inventory.getCount(fourDoseId) * 4;

        return dosesOneDose + dosesTwoDose + dosesThreeDose + dosesFourDose;
    }

    @Override
    public int getCost()
    {
        return 1000;
    }

    @Override
    public boolean shouldDrinkPotion()
    {
        if (masterIndex != 0 && childIndex != 0 && componentIndex != 0 && Interfaces.isInterfaceValid(masterIndex))
        {
            if (getAbsorptionDosesRemaining() <= valueToDrinkAt)
            {
                System.out.println("AbsorptionPotion - shouldDrinkPotion: Going to take a sip of the potion.");
                return true;
            }

            return false;
        }

        if (cacheIndexes())
        {
            return shouldDrinkPotion();
        }

        return false;
    }

    @Override
    public void drinkPotion()
    {
        int counter = getAbsorptionDosesRemaining() / boostAmount;

        for (int i = 0; i < counter; i++)
        {
            General.sleep(150, 300);
            super.drinkPotion();
        }
    }

    @Override
    protected int getBoostAmount()
    {
        return 50;
    }

    @Override
    protected void setValueToDrinkAt()
    {
        if (rangeOfLvls == null)
        {
            cacheRangeOfLvls();
        }

        valueToDrinkAt = NormalDistribution.getGeneratedRandomNormalValue(rangeOfLvls, 50, 1000);;
        System.out.println("AbsorptionPotion - setValueToDrinkAt: New value to drink at: " + valueToDrinkAt);
    }

    private void cacheRangeOfLvls()
    {
        rangeOfLvls = new double[950];
        int counter = 50;

        for (int i = 0; i < rangeOfLvls.length; i++)
        {
            rangeOfLvls[i] = counter++;
        }
    }

    private int getAbsorptionDosesRemaining()
    {
        RSInterface absorptionInterface = Interfaces.get(masterIndex, childIndex, componentIndex);

        if (absorptionInterface == null)
        {
            System.out.println("shouldDrinkPotion: AbsorptionPotion interface DNE. Unable to determine if you should drink potion, returning false.");
            return -1;
        }

        return Integer.parseInt(absorptionInterface.getText());
    }

    private boolean cacheIndexes()
    {
        // Get the interface that has text absorption
        RSInterface absorptionTextInterface = Entities.find(InterfaceEntity::new).textEquals("AbsorptionPotion").getFirstResult();

        if (absorptionTextInterface == null)
        {
            System.out.println("cacheIndexes: absorptionTextInterface is null. Unable to determine if you should drink potion, returning false.");
            return false;
        }

        RSInterface absorptionTextInterfaceParents = absorptionTextInterface.getParent();

        for (RSInterface children : absorptionTextInterfaceParents.getChildren())
        {
            if (children.getFontID() == 645)
            {
                masterIndex = children.getParent().getParent().getIndex();
                childIndex = children.getParent().getIndex();
                componentIndex = children.getIndex();

                return true;
            }
        }

        System.out.println("cacheIndexes: Failed to find indexes for absorption potion interface. Unable to determine if you should drink potion, returning false.");
        return false;
    }

    @Override
    public String toString()
    {
        return "Absorption Potion";
    }
}
