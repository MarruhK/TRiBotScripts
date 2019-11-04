package scripts.dmmrelekkafisher.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSPlayer;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.framework.Node;

import java.util.ArrayList;

public class MuleFish implements Node
{
    private int tradeCount = 0;

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The MuleShark Node has been Validated! Executing...");
        // Check if mule is online. If he is, then commence muling. If not, stop script.
        RSPlayer[] mules = Players.find(Vars.muleName);

        if (mules.length > 0)
        {
            if (!mules[0].isOnScreen())
            {
                Camera.turnToTile(mules[0].getPosition());
                WebWalking.walkTo(mules[0].getPosition());
            }

            if (!mules[0].click("Trade with " + Vars.muleName))
            {
                System.out.println("muleShark: Failed to trade mule " + Vars.muleName);
                return;
            }

            System.out.println("muleShark: Traded with mule" + Vars.muleName + ", waiting for response.");

            // Now must wait for the trade screen to pop-up (i.e. Trading begin).
            if (isTradeScreenOpen(Trading.WINDOW_STATE.FIRST_WINDOW))
            {
                System.out.println("muleShark: First trade window opened, offering sharks.");
                // Offer all the fish. 0 means offer all.

                if (Vars.fishingEquipmentID == Constants.FISHING_NET_ID)
                {
                    Trading.offer(0, Vars.fishID[0]);
                    General.sleep(1400);
                    Trading.offer(0, Vars.fishID[1]);
                }
                else
                {
                    Trading.offer(0, Vars.fishID);
                }

                // Items are offered.
                if (Timing.waitCondition(new Condition(){
                    @Override
                    public boolean active()
                    {
                        General.sleep(100);
                        return Trading.getOfferedItems(false).length > 0;
                    }
                }, General.random(4000, 4100)))
                {
                    // Ensure it is the mule we traded, then press Accept.
                    if (Trading.getOpponentName().equals(Vars.muleName))
                    {
                        System.out.println("muleShark: Accepted first trade window. Waiting for Second window.");
                        Trading.accept();

                        // Wait for 2nd screen to open up.
                        if (isTradeScreenOpen(Trading.WINDOW_STATE.SECOND_WINDOW))
                        {
                            System.out.println("muleShark: Successfully left first trade window.");
                            // Once again, double check mule name and then accept.
                            if (Trading.getOpponentName().equals(Vars.muleName))
                            {
                                Trading.accept();
                                System.out.println("muleShark: Accepted second trade window.");

                                // Make it wait till trade screen removed.
                                if(isTradeScreenOpen(null))
                                {
                                    System.out.println("muleShark: Successfully muled.");
                                    tradeCount++;
                                }
                                else
                                {
                                    System.out.println("muleShark: Failed to mule. Returning.");
                                    Trading.close();
                                    return;
                                }
                            }
                        }
                        else
                        {
                            System.out.println("muleShark: Failed to leave first trade window. Returning.");
                            Trading.close();
                            return;
                        }
                    }
                    else
                    {
                        System.out.println("muleShark: NOT TRADING WITH MULE, LEAVING.");
                        Trading.close();
                        return;
                    }
                }
                else
                {
                    System.out.println("muleShark: Failed to give items. Decline and return.");
                    Trading.close();
                    return;
                }
            }
        }
        // SHOULD HAVE NO SHARKS NOW.
    }

    // Waits 75~ seconds for trade screen to show up.
    private boolean isTradeScreenOpen(Trading.WINDOW_STATE state)
    {
        int low;
        int high;

        if (tradeCount == 0)
        {
            low = 3500;
            high = 3600;
        }
        else
        {
            low = 10000;
            high = 11000;
        }

        return Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(1000);
                return Trading.getWindowState() == state;
            }
        }, General.random(low,high));
    }

    @Override
    public boolean validate()
    {
        return (Inventory.find(Vars.fishingEquipmentID).length > 0  &&
                Inventory.isFull()                                  &&
                Player.getPosition().distanceTo(Constants.MULE_TILE) <= 10);
    }

}
