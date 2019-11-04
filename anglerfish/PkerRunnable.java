package scripts.anglerfish;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSPlayer;
import scripts.anglerfish.data.Constants;

public class PkerRunnable implements Runnable{

    String oddName1 = ("Spuce" + "\u00A0" + "Needle");
    String[] names = {"Gengar Mule", "turtlelvr96", "poopsniffer5", oddName1, "D00mIsap4aki",
            "Khurram"};

    @Override
    public void run() {
        boolean isNoPkers = true;
        RSItem[] teleTabs = Inventory.find(Constants.TELETAB_ID[0], Constants.TELETAB_ID[1]);
        // ENSURE BANKING INTERFACE ISN'T OPEN WHJEN CLICKING TAB

        while (isNoPkers){
            General.sleep(50);
            RSPlayer[] players = Players.getAll(Filters.Players.nameNotEquals(names));

            // if player skulled near by, tele.
            if (players.length > 0){
                if (teleTabs.length > 0){
                    teleTabs[0].click();
                    teleTabs[0].click();
                    teleTabs[0].click();
                    teleTabs[0].click();

                    System.out.println("Unknown player(s) nearby. Teleporting away.");

                }
                System.out.print(Player.getRSPlayer().getName());
                for (RSPlayer player: players){
                    System.out.println("Player name: " + player.getName());
                    System.out.println("combat Level: " + player.getCombatLevel());
                    if (player.getSkullIcon() == 0){
                        System.out.println("He is skulled.");
                    } else {
                        System.out.println("He is NOT skulled.");
                    }
                    System.out.println("                        ");
                }
                isNoPkers = false;
            }
        }
    }
}

