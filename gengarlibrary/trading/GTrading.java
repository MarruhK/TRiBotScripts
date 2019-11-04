package scripts.gengarlibrary.trading;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSPlayer;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.PlayerEntity;
import scripts.gengarlibrary.Antiban;
import scripts.gengarlibrary.GBooleanSuppliers;
import scripts.gengarlibrary.GClicking;
import scripts.gengarlibrary.GString;

@Deprecated
public class GTrading
{
    public static boolean tradePlayerAcceptOnly(String playerName, int itemAcceptingId)
    {
        return tradePlayer(playerName, -1, itemAcceptingId);
    }

    public static boolean tradePlayerOfferOnly(String playerName, int itemOfferId)
    {
        return tradePlayer(playerName, itemOfferId, -1);
    }

    public static boolean tradePlayer(String playerName, int itemOfferId, int itemAcceptingId)
    {
        // Check if trade screen is already opened - this prevents trying to click trade on player when screen is already opened.
        if (!isTradeScreenOpen(Trading.WINDOW_STATE.FIRST_WINDOW, 650, 700))
        {
            RSPlayer rsPlayer = Entities.find(PlayerEntity::new).nameEquals(playerName).getFirstResult();

            if (rsPlayer == null)
            {
                System.out.println("tradePlayer: Unable to find and trade player named " + playerName + "...");
                return false;
            }

            if (!GClicking.clickPlayer(rsPlayer, "Trade with " + rsPlayer.getName()))
            {
                System.out.println("tradePlayer: Failed to trade mule " + playerName);
                return false;
            }

            // Waits for first trade window to open
            if (!isTradeScreenOpen(Trading.WINDOW_STATE.FIRST_WINDOW, 5000, 5200))
            {
                System.out.println("tradePlayer: Failed to open the first trade screen...");
                return false;
            }
        }

        return isFirstTradeWindowSuccessful(playerName, itemOfferId, itemAcceptingId) &&
               isSecondTradeWindowSuccessful(playerName, itemOfferId, itemAcceptingId);
    }

    private static boolean isFirstTradeWindowSuccessful(String playerName, int itemOfferId, int itemAcceptingId)
    {
        System.out.println("inFirstTradeWindow: Offering items if any and waiting for items, if any...");

        // Offer items, if you have any to offer
        if (itemOfferId > 0)
        {
            Trading.offer(0, itemOfferId);

            // Items are offered.
            if (!Timing.waitCondition(()->
            {
                General.sleep(125);
                // Check if you offered items or if trade screen has clsoed.
                return Trading.getOfferedItems(false).length > 0;
            }, General.random(2500, 2900)))
            {
                System.out.println("inFirstTradeWindow: Failed to offer required items, closing screen...");
                Trading.close();
                return false;
            }
        }

        // Wait for accepted items to appear, if any
        if (itemAcceptingId > 0 && !Timing.waitCondition(()->
        {
            General.sleep(400);
            return Trading.find(true, itemAcceptingId).length > 0;
        }, General.random(3600, 3850)))
        {
            System.out.println("inFirstTradeWindow: Failed to get offered required items, closing screen...");
            Trading.close();
            return false;
        }

        // Ensure it is the mule we traded, then press Accept.
        if (!GString.convertToTribotString(Trading.getOpponentName()).equals(playerName))
        {
            System.out.println(Trading.getOpponentName());
            System.out.println(playerName);

            System.out.println("inFirstTradeWindow: Realized you are trading the wrong player??!? CLosing...");
            Trading.close();
            return false;
        }

        // Accept the trade and wait a little bit before timing out
        Trading.accept();

        if (!isTradeScreenOpen(Trading.WINDOW_STATE.SECOND_WINDOW, 4000, 4100))
        {
            System.out.println("inFirstTradeWindow: Took to long to accept, timing out...");
            Trading.close();
            return false;
        }

        return true;
    }

    private static boolean isSecondTradeWindowSuccessful(String playerName, int itemOfferId, int itemAcceptingId)
    {
        if (isTradeScreenOpen(Trading.WINDOW_STATE.SECOND_WINDOW, 1200, 1400))
        {
            Trading.accept();

            // Make it wait till trade screen removed.
            if (isTradeScreenOpen(null, 4900, 5002))
            {
                System.out.println("isSecondTradeWindowSuccessful: Successful ...");
                return true;
            }

            System.out.println("isSecondTradeWindowSuccessful: Failed to accept second trade screen (timed out)...");
        }
        else
        {
            System.out.println("isSecondTradeWindowSuccessful: Second trade screen isn't even open...");
        }

        if (isTradeScreenOpen(null, 600, 800))
        {
            Trading.close();
        }

        return false;
    }

    private static boolean isTradeScreenOpen(Trading.WINDOW_STATE state, int low, int high)
    {
        return Timing.waitCondition(GBooleanSuppliers.isTradeScreenState(state), General.random(low,high));
    }
}
