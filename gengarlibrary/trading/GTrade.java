package scripts.gengarlibrary.trading;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Trading;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSPlayer;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.PlayerEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GString;

import java.util.ArrayList;
import java.util.List;

// A trading class that is used over Tribot default trading API.
public class GTrade
{
    private static final int masterSecondTradeScreen = 334;
    private static final int masterFirstTradeScreen = 335;
    private static final int masterOffer = 336;

    // Trading statistics; used in ABC2
    private static long averagePlayerTradeWaitTime = 6000;
    private static long totalPlayerTradeWaitTime;
    private static long totalPlayerTradeInstances;

    private static long averageFirstTradeWaitTime = 4000;
    private static long totalFirstTradeWaitTime;
    private static long totalFirstTradeInstances;

    private static long averageSecondTradeWaitTime = 2500;
    private static long totalSecondTradeWaitTime;
    private static long totalSecondTradeInstances;

    // For right click player trade
    private static void updateTradeStatistics(long waitTime)
    {
        totalPlayerTradeWaitTime += waitTime;
        averagePlayerTradeWaitTime = totalPlayerTradeWaitTime / ++totalPlayerTradeInstances;

        System.out.println("updateFirstTradePlayerStatistics: Average wait = " + averagePlayerTradeWaitTime);
    }

    // For first trade screen offer
    private static void updateFirstTradePlayerStatistics(long waitTime)
    {
        totalFirstTradeWaitTime += waitTime;
        averageFirstTradeWaitTime = totalFirstTradeWaitTime / ++totalFirstTradeInstances;

        System.out.println("updateFirstTradePlayerStatistics: Average wait = " + averageFirstTradeWaitTime);
    }

    // For second trade screen
    private static void updateSecondTradePlayerStatistics(long waitTime)
    {
        totalSecondTradeWaitTime += waitTime;
        averageSecondTradeWaitTime = totalSecondTradeWaitTime / ++totalSecondTradeInstances;

        System.out.println("updateSecondTradePlayerStatistics: Average wait = " + averageSecondTradeWaitTime);
    }

    //******************************************************************************************************************
    //___________________________________________TRADING METHODS______________________________________________________\\

    /**
     * Trades the specified player. Has antiban embedded within it along with many other checks.
     *
     * To offer/accept a variable amount of an item, enter 0 for the count in GItem
     *
     *
     * @param playerName
     * @param useAntiban
     * @param offerItems
     * @param acceptItems
     * @return
     */
    public static boolean tradePlayer(String playerName, boolean useAntiban, GTradeItem[] offerItems, GTradeItem[] acceptItems)
    {
        // In case trade screen opens ups after timeout
        if (!isInFirstTradeScreen())
        {
            RSPlayer playerToTrade = Entities.find(PlayerEntity::new).nameEquals(playerName).getFirstResult();

            if (playerToTrade == null)
            {
                System.out.println("tradePlayer: Unable to locate player to trade.");
                return false;
            }

            long tradePlayerStartTime = Timing.currentTimeMillis();

            playerToTrade.click("Trade");

            if (useAntiban)
            {
                Antiban.get().generateTrackers((int)averagePlayerTradeWaitTime);
            }

            // Wait for trade screen
            while(!isInFirstTradeScreen())
            {
                General.sleep(250, 400);
                Antiban.get().timedActions();

                // timeout check
                if (Timing.currentTimeMillis() - tradePlayerStartTime > 11000)
                {
                    System.out.println("tradePlayer: First trade screen did not open during alloted time period.");
                    return false;
                }
            }

            updateTradeStatistics(Timing.currentTimeMillis() - tradePlayerStartTime);
            Antiban.get().sleepReactionTime((int)averagePlayerTradeWaitTime);
        }

        return  handleFirstTradeScreen(playerName, useAntiban, offerItems, acceptItems) &&
                handleSecondTradeScreen(playerName, useAntiban, offerItems, acceptItems);
    }

    private static boolean handleFirstTradeScreen(String playerName, boolean useAntiban, GTradeItem[] offerItems, GTradeItem[] acceptItems)
    {
        // Ensure it is the mule we traded, then press Accept.
        String tradersName = getTradersName();
        if (tradersName == null || !GString.convertToTribotString(tradersName).equals(playerName))
        {
            System.out.println("handleFirstTradeScreen: Realized you are trading the wrong player??!? CLosing...");
            decline();
            return false;
        }

        // Offer your items.
        if (offerItems != null && offerItems.length > 0)
        {
            for (GTradeItem item : offerItems)
            {
                General.sleep(150, 200);

                if (!offer(item))
                {
                    System.out.println("handleFirstTradeScreen: Failed to offer an item with ID: " + item.getId());
                    decline();
                    return false;
                }
            }
        }

        long startTimeWaitForOpponentsItems = Timing.currentTimeMillis();

        if (useAntiban)
        {
            Antiban.get().generateTrackers((int)averageFirstTradeWaitTime);
        }

        // Wait for opponent to offer items
        while (!areOpponentsItemsOffered(acceptItems))
        {
            General.sleep(150, 200);
            doInTradeAntiban();

            // Timeout
            if (Timing.currentTimeMillis() - startTimeWaitForOpponentsItems > 13000)
            {
                System.out.println("handleFirstTradeScreen: Opponent took too long to offer items, timing out.");
                decline();
                return false;
            }
        }

        if (useAntiban)
        {
            updateFirstTradePlayerStatistics(Timing.currentTimeMillis() - startTimeWaitForOpponentsItems);
            Antiban.get().sleepReactionTime((int)averageFirstTradeWaitTime);
        }

        return accept(playerName, useAntiban);
    }

    private static boolean handleSecondTradeScreen(String playerName, boolean useAntiban, GTradeItem[] offerItems, GTradeItem[] acceptItems)
    {
        return accept(playerName, useAntiban);
    }

    /**
     * Declines the trade, whether in first or second screen. Has no antiban as there's no variable reaction time after
     * decline click
     *
     * @return whether decline click was successful or not
     */
    public static boolean decline()
    {
        if (Trading.getWindowState() == null)
        {
            System.out.println("decline: Not in a trade screen.");
            return false;
        }

        RSInterface inventInterface = Entities.find(InterfaceEntity::new).textEquals("Decline").getFirstResult();

        if (inventInterface == null)
        {
            System.out.println("decline: Unable to locate interface.");
            return false;
        }

        if (!inventInterface.click())
        {
            System.out.println("decline: Failed to click decline button (TriBot API error).");
            return false;
        }

        return true;
    }

    public static boolean accept(String playerName, boolean useAntiban)
    {
        if (Trading.getWindowState() == null)
        {
            System.out.println("accept: Not in a trade screen.");
            return false;
        }

        RSInterface inventInterface = Entities.find(InterfaceEntity::new).textEquals("Accept").getFirstResult();

        if (inventInterface == null)
        {
            System.out.println("accept: Unable to locate interface.");
            decline();
            return false;
        }

        boolean isInSecondTradeScreen = isInSecondTradeScreen();
        long startTime = Timing.currentTimeMillis();

        if (!inventInterface.click())
        {
            System.out.println("accept: Failed to click accept button (TriBot API error).");
            decline();
            return false;
        }

        // If on second trade screen then reset the currentSession shit.
        if (useAntiban && isInSecondTradeScreen)
        {
            Antiban.get().generateTrackers((int)averageSecondTradeWaitTime);
        }

        // Wait for accept to work i.e. go to next window or finish
        if (isInSecondTradeScreen)
        {
            if (!Timing.waitCondition(()->
            {
                General.sleep(150, 260);
                return !isInSecondTradeScreen();
            }, General.random(6500, 8000)))
            {
                System.out.println("accept: Opponent did not accept, timing out.");
                decline();
                return false;
            }
        }
        else
        {   // In the off chance the opponent adds/removes items and we need to click accept again, handle.
            while (isInFirstTradeScreen())
            {
                // If the test does not say waitign for other opponent
                RSInterface opponentAccepted = Entities.find(InterfaceEntity::new).textEquals("Other player has accepted.").getFirstResult();

                if (opponentAccepted != null && !inventInterface.click())
                {
                    System.out.println("accept: Failed to click accept button (TriBot API error).");
                    decline();
                    return false;
                }

                if (Timing.currentTimeMillis() - startTime > General.random(11000, 13000))
                {
                    System.out.println("accept: Opponent did not accept, timing out.");
                    decline();
                    return false;
                }
            }

            // Fail safe
            if (!Timing.waitCondition(()->
            {
                General.sleep(150);
                return isInSecondTradeScreen();
            }, 2500))
            {
                System.out.println("accept: Failed to go to second trade screen.");
                decline();
                return false;
            }
        }

        if (useAntiban && isInSecondTradeScreen)
        {
            updateSecondTradePlayerStatistics(Timing.currentTimeMillis() - startTime);
            Antiban.get().sleepReactionTime((int)averageSecondTradeWaitTime);
        }

        return true;
    }

    /**
     * Gets the amount of inventory spaces the other guy has available.
     *
     * @return Inventory spaces trader has available
     */
    public static int getOpponentInventorySpaces()
    {
        RSInterface inventInterface = Entities.find(InterfaceEntity::new).textContains("inventory slot").getFirstResult();

        if (!isInFirstTradeScreen() || inventInterface == null)
        {
            System.out.println("getOpponentInventorySpaces: Unable to locate interface.");
            return -1;
        }

        String text = inventInterface.getText();

        if (text.contains("getOpponentInventorySpaces: No free inventory slots"))
        {
            System.out.println(0);
            return 0;
        }
        else
        {
            String name = getTradersName();

            if (name == null)
            {
                System.out.println("getOpponentInventorySpaces: Traders name is null?");
                return -1;
            }

            int index1 = getTradersName().length() + 5; // 5 comes from the characters " has "
            int index2 = text.indexOf(" free inventory slots");

            if (index2 == -1)
            {
                System.out.println("getOpponentInventorySpaces: Error with index of");
                return -1;
            }

            return Integer.parseInt(text.substring(index1, index2));
        }
    }


    public static String getTradersName()
    {
        RSInterface inventInterface = Entities.find(InterfaceEntity::new).textContains("Trading with: ").getFirstResult();

        if (inventInterface == null)
        {
            System.out.println("getTradersName: Unable to locate interface.");
            return null;
        }

        String text = inventInterface.getText();

        if (text == null)
        {
            System.out.println("getTradersName: Error, the text is null");
            return null;
        }

        // 14 represents the amount of characters in "Trading with: "
        return text.substring(14);
    }

    /**
     * Given the required items, this method returns whether the opponent has offered said items.
     *
     * Note that counts equal to 0 are a variable amount of number > 0.
     *
     * @param requiredItems
     * @return
     */
    private static boolean areOpponentsItemsOffered(GTradeItem[] requiredItems)
    {
        List<GTradeItem> offeredItems = getOpponentOfferedItems();

        if (offeredItems == null)
        {
            System.out.println("areOpponentsItemsOffered: No items offered so far.");
            return false;
        }

        for (GTradeItem requiredItem : requiredItems)
        {
            boolean isOffered = false;

            for (GTradeItem offeredItem : offeredItems)
            {
                if (offeredItem.getId() == -1)
                {
                    continue;
                }

                if (offeredItem.getId() == requiredItem.getId())
                {
                    // Handle 0 count parameter and normal counts
                    if ((requiredItem.getCount() == 0 && offeredItem.getCount() > 0) ||
                         offeredItem.getCount() == requiredItem.getCount())
                    {
                        isOffered = true;
                        break;
                    }
                }
            }

            if (!isOffered)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Obtains a list of GTradeItem that the opponent has offered in the first trade window.
     *
     * @return List of GTradeItem that the opponent has offered in the first trade window.
     */
    private static List<GTradeItem> getOpponentOfferedItems()
    {
        if (!isInFirstTradeScreen())
        {
            return null;
        }

        RSInterface tradeInterface = Interfaces.get(masterFirstTradeScreen, 28);

        List<GTradeItem> listOfItems = new ArrayList<>();

        for (int i = 0; i < 28; i++)
        {
            boolean isNewId = true;
            RSInterface offerInterfaceSlot = tradeInterface.getChild(i);

            int offerItemId = offerInterfaceSlot.getComponentItem();
            int offerItemCount = offerInterfaceSlot.getComponentStack();

            if (offerInterfaceSlot.isHidden() || offerItemId == -1)
            {
                continue;
            }

            // Check if list already has the item.
            for (GTradeItem item : listOfItems)
            {
                // Item exists so increase the quantity
                if (item.getId() == offerItemId)
                {
                    item.setCount(item.getCount() + offerItemCount);
                    isNewId = false;
                }
            }

            if (isNewId)
            {
                listOfItems.add(new GTradeItem(offerItemId, offerItemCount));
            }
        }

        return listOfItems;
    }



    public static boolean offer(GTradeItem item)
    {
        // Ensure is in trade and ensure offer item interface is valid
        if (!Interfaces.isInterfaceValid(masterFirstTradeScreen) || !Interfaces.isInterfaceValid(masterOffer))
        {
            System.out.println("offer: Unable to trade as the interface DNE");
            return false;
        }

        final int id = item.getId();
        RSInterface itemInterface = null;
        RSInterface[] offerInterfaces = Entities.find(InterfaceEntity::new).actionContains("Offer").getResults();

        // Find item in inventory to offer
        for (RSInterface rsInterface : offerInterfaces)
        {
            if (rsInterface.getComponentItem() == id)
            {
                itemInterface = rsInterface;
                break;
            }
        }

        if (itemInterface == null)
        {
            System.out.println("offer: Unable to find item to offer.");
            return false;
        }

        boolean isItemStackable = isItemStackable(item.getId());
        int previousAmount = getInventoryItemCount(item.getId());
        int count = item.getCount() == 0 ? previousAmount : item.getCount();
        int attempts = 0;
        int attemptsLimit = (int) (Math.random() * 4) + 2;

        // Account for API Errors
        do
        {
            // FIRST CHECK IF OPPONENT EVEN HAS ROOM FOR THE ITEMS
            final int opponentAvailableSlots = getOpponentInventorySpaces();

            if ((isItemStackable && opponentAvailableSlots > 0) ||
                 opponentAvailableSlots >= count)
            {

                String offerAction = getOfferAction(itemInterface.getActions(), count, previousAmount);
                itemInterface.click(offerAction);

                // Handle X offer
                if (offerAction.equals("Offer-X"))
                {
                    // opponent has space so simply enter the count and press enter
                    Keyboard.typeSend("" + count);
                }

                // Check if worked
                Timing.waitCondition(()->
                {
                    General.sleep(150, 200);
                    return getInventoryItemCount(item.getId()) < previousAmount;
                }, General.random(950, 1200));

                // Timeout
                if (++attempts < attemptsLimit)
                {
                    break;
                }
            }
            else
            {
                System.out.println("Opponent does not have sufficient room in inventory.");
                return false;
            }
        } while(getInventoryItemCount(item.getId()) == previousAmount);

        if (attempts == attemptsLimit)
        {
            System.out.println("offer: Failed to trade the specified item.");
            return false;
        }

        System.out.println("offer: Successfully offered item with id: " + item.getId());
        return true;
    }

    /**
     * Determines if an item is stackable given its id.
     *
     * Player must be in a trade screen for this to return true, as it checks the stack via master index 336 (offer)
     *
     * @param itemId item id
     * @return true if stackable and if you are in the first trade screen, false otherwise.
     */
    private static boolean isItemStackable(int itemId)
    {
        if (Interfaces.isInterfaceValid(masterOffer))
        {
            for (int i = 0; i < 28; i++)
            {
                RSInterfaceComponent componentInterface = Interfaces.get(masterOffer).getComponent(i);
                int inventItemId = componentInterface.getComponentItem();

                if (inventItemId == -1)
                {
                    continue;
                }

                if (inventItemId == itemId)
                {
                    return componentInterface.getComponentStack() > 1;
                }
            }
        }

        System.out.println("isItemStackable: You are not in offer screen or unable to find said item");
        return false;
    }

    /**
     * Gets the count of the specific item id in your inventory while you are in a trade (first screen).
     *
     * @param id
     * @return inventory count of specified id. -1 if DNE.
     */
    private static int getInventoryItemCount(int id)
    {
        List<GTradeItem> listOfItems = getInventoryItems();

        if (listOfItems == null)
            return -1;

        // Check if list already has the item.
        for (GTradeItem item : listOfItems)
        {
            // Item exists so increase the quantity
            if (item.getId() == id)
            {
                return item.getCount();
            }
        }

        System.out.println("getInventoryItemCount: Item does not exist");
        return -1;
    }

    /**
     * Returns a list of GItems in your inventory while int he first trade screen
     *
     * @return List of GTradeItem that exist in your inventory, including the proper counts
     */
    private static List<GTradeItem> getInventoryItems()
    {
        if (!Interfaces.isInterfaceValid(masterOffer))
        {
            System.out.println("getInventoryItems: You are not in offer screen");
            return null;
        }

        List<GTradeItem> listOfItems = new ArrayList<>();

        // Find item in inventory to offer
        for (int i = 0; i < 28; i++)
        {
            RSInterfaceComponent componentInterface = Interfaces.get(masterOffer).getComponent(i);

            int inventItemId = componentInterface.getComponentItem();
            int inventItemCount = componentInterface.getComponentStack();

            if (inventItemId == -1)
            {
                continue;
            }

            boolean shouldAddItem = true;

            // Check if list already has the item.
            for (GTradeItem item : listOfItems)
            {
                // Item exists so increase the quantity
                if (item.getId() == inventItemId)
                {
                    item.setCount(item.getCount() + inventItemCount);
                    shouldAddItem = false;
                }
            }

            if (shouldAddItem)
            {
                listOfItems.add(new GTradeItem(inventItemId, inventItemCount));
            }
        }

        return listOfItems;
    }

    /**
     * Obtains the offer action used to determine which option to choose when offering an item (i.e. 5, 10, X, All).
     *
     * @param actions           Possible actions
     * @param offerAmount       Item count in inventory
     * @param totalItemCount    If item is stackbale
     * @return String option that is used when offering said item.
     */
    private static String getOfferAction(String[] actions, int offerAmount, int totalItemCount)
    {
        String appendedString;

        switch (offerAmount)
        {
            case 1:
                appendedString = "Offer";
                break;

            case 5:
                appendedString = "Offer-" +  + 5;
                break;

            case 10:
                appendedString = "Offer-" +  + 10;
                break;

            default:
                // Here we need to determine if we should offer X or offer All
                if (offerAmount == totalItemCount)
                {
                    appendedString = "Offer-All";
                    break;
                }
                else
                {
                    appendedString = "Offer-X";
                    break;
                }
        }

        return appendedString;
    }


    //__________________________________________________________________________________________
    private static boolean isInFirstTradeScreen()
    {
        return Trading.getWindowState() == Trading.WINDOW_STATE.FIRST_WINDOW;
    }

    private static boolean isInSecondTradeScreen()
    {
        return Trading.getWindowState() == Trading.WINDOW_STATE.SECOND_WINDOW;
    }

    //__________________________________________________________________________________________
    private static void doInTradeAntiban()
    {
        Antiban.get().checkMoveMouse();
        Antiban.get().checkPickupMouse();
        Antiban.get().checkLeaveGame();
    }
}
