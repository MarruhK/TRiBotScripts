package scripts.piscfavour.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.*;
import scripts.piscfavour.data.Constants;
import scripts.piscfavour.framework.Node;

public class BuyBuckets extends Node {
    @Override
    public void execute() {
        // At store with at least one worm bucket and no empty buckets
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The BuyBuckets Node has been Validated! Executing...");

        // Give NPC the buckets of worms by simply clicking on him.
        RSNPC[] bucketSeller = NPCs.find(Constants.BUCKET_SELLER_ID);

        if (bucketSeller.length > 0){
            bucketSeller[0].click();

            // Add sleep to ensure that u don't have worm buckets anymore
            if(!Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Inventory.find(Constants.BUCKETS_WORMS_ID).length < 5;
                }
            }, General.random(1000,1100))){
                // For some reason buckets of worm still in invent. leave.
                System.out.println("5 or more buckets of worm exists, leaving Node.");
                return;
            }

            // Worm buckets are gone, empty sand buckets, if any exists.
            RSItem[] sandBuckets = Inventory.find(Constants.BUCKETS_SAND_ID);

            if (sandBuckets.length > 0){
                for (RSItem sandBucket: sandBuckets){
                    System.out.println("Emptying sand buckets.");
                    sandBucket.click("Empty");
                }
            }

            if (Inventory.find(Constants.COINS_ID).length > 0){
                // Buy a bunch of buckets, if you have coins.
                bucketSeller[0].click("Trade");
                System.out.println("General Store should be opened. Checking...");

                RSInterfaceMaster genStoreInterface = Interfaces.get(Constants.GEN_STORE_INT_ID);

                // Checks if the general store interface
                if (Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        General.sleep(100);
                        return genStoreInterface != null;
                    }
                }, General.random(4000, 4250))){
                    System.out.println("General store interface has opened.");

                    genStoreInterface.getChild(Constants.GEN_STORE_CHILD_ID).
                            getChild(Constants.GEN_STORE_COMP_ID).click("Buy 50");

                    // Once buckets in inventory, close interface window
                    if(Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            General.sleep(100);
                            return Inventory.find(Constants.BUCKETS_ID).length > 0;
                        }
                    }, General.random(2200, 2300))){
                        System.out.println("Purchased buckets, closing general store interface.");
                    } else {
                        System.out.println("Failed to purchase buckets. Closing interface and leaving nodes.");
                    }
                    // Close interface.
                    Interfaces.get(300, 1).getChild(11).click();
                } else {
                    System.out.println("General store interface did not open. Leaving Node.");
                    return;
                }
            } else {
                // No coinds time to log out
                System.out.println("No coins, leaving nodes.");
                return;
            }
            System.out.println("_________________________________________________________________________________________");
        }
    }

    @Override
    public boolean validate() {
        return (Inventory.find(Constants.BUCKETS_ID).length < 22                   &&
                Player.getPosition().distanceTo(Constants.GEN_STORE_TILE) <= 7)    &&
                (Inventory.find(Constants.COINS_ID).length > 0);
    }
}
