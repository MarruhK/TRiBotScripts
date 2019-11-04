package scripts.gengarnmz.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.framework.Validators;

public class StartDreamPotion extends Node
{
    private static final int unselectedColour = 9408399;
    private static final int selectedColour = 16750623;
    private static final int master = 129;

    @Override
    public void execute()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("StartDreamPotion initiated... Executing...");

        // if dream selected, then drink the potion and ensure monsters are selected that are needed
        RSObject potion = Entities.find(ObjectEntity::new)
                            .actionsEquals("Drink")
                            .nameNotContains("Spectator potion")
                            .getFirstResult();

        if (!(potion != null && GClicking.clickObject(potion, "Drink Potion") && initializeDream()))
        {
            System.out.println("execute: Failed to properly click potion or initialize dream.");

            // CHeck if coffer is empty
            RSInterface emptyCofferInterface = Entities.find(InterfaceEntity::new)
                                                .textContains("Click here to continue")
                                                .getFirstResult();

            if (emptyCofferInterface != null)
            {
                System.out.println("execute: Out of gp in the coffer...");

                final int master = emptyCofferInterface.getIndex();
                Vars.isCofferEmpty = true;

                emptyCofferInterface.click();
                Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(master), General.random(1350, 1500));
            }
        }
    }

    private boolean initializeDream()
    {
        // Wait for interface to show up
        if (!Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(master), 700))
        {
            System.out.println("initializeDream: Failed to open master interface, returning...");
            return false;
        }

        if (selectMobs(true) && selectMobs(false))
        {
            RSInterface acceptInterface = Entities.find(InterfaceEntity::new)
                                            .inMaster(master)
                                            .textContains("Accept")
                                            .getFirstResult();

            if (acceptInterface != null && acceptInterface.click() && Timing.waitCondition(()-> Validators.isDreaming(), General.random(4200, 7000)))
            {
                System.out.println("initializeDream: Succesfully started dream.");
                return true;
            }

            System.out.println("initializeDream: Failed to start dream, closing interface and ending..");

            RSInterface closeInterface = Entities.find(InterfaceEntity::new)
                                            .inMaster(master)
                                            .textContains("Cancel")
                                            .getFirstResult();

            if (closeInterface != null)
            {
                closeInterface.click();
                return false;
            }
        }

        return false;
    }

    private boolean selectMobs(boolean isSelectingValidMobs)
    {
        RSInterface[] mobComponents;
        int colourToChangeTo;

        if (isSelectingValidMobs)
        {
            colourToChangeTo = selectedColour;
            mobComponents = getMobs(unselectedColour, Constants.VALID_MOBS);
        }
        else
        {
            colourToChangeTo = unselectedColour;
            mobComponents = getMobs(selectedColour, Constants.INVALID_MOBS);
        }

        // Iterate over relevant component and ensure that mobs are selected.
        for (RSInterface rsInterface : mobComponents)
        {
            selectComponent(rsInterface, rsInterface.getText(), colourToChangeTo);
        }

        // Double check here and return bool based on that
        if (!(isSelectingValidMobs && getMobs(unselectedColour, Constants.VALID_MOBS).length == 0) &&
            !(getMobs(selectedColour, Constants.INVALID_MOBS).length == 0))
        {
            System.out.println("selectMobs: Failed to select the correct mobs (i.e. double check failed).");
            return false;
        }

        return true;
    }

    private void selectComponent(RSInterface rsInterface, String name, int colourToChangeTo)
    {
        // Need this check because clicking on a monster removes all relevant monsters from the same quest (i.e. clicking one DT boss removes all of them).
        // This prevents spam clicking cached interfaces that have been unselected already via the above
        RSInterface tempMob1 = Entities.find(InterfaceEntity::new)
                                .inMaster(master)
                                .textContains(name)
                                .textColourEquals(colourToChangeTo)
                                .getFirstResult();

        if (tempMob1 != null)
            return;

        rsInterface.click();

        if (!Timing.waitCondition(()->
        {
            General.sleep(250, 350);

            RSInterface tempMob2 = Entities.find(InterfaceEntity::new)
                                        .inMaster(master)
                                        .textContains(name)
                                        .textColourEquals(colourToChangeTo)
                                        .getFirstResult();

            return tempMob2 != null;
        }, General.random(1500, 1650)))
        {
            System.out.println("selectComponent: Failed to change colour");
        }
    }

    private RSInterface[] getMobs(int colour, String... mobs)
    {
        return Entities.find(InterfaceEntity::new)
                .inMaster(master)
                .textContains(mobs)
                .textColourEquals(colour)
                .getResults();
    }

    @Override
    public boolean validate()
    {
        return Validators.shouldStartDreamPotion();
    }
}
