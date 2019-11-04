package scripts.dmmrelekkafisher;

import org.tribot.api.General;
import org.tribot.api2007.Players;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSPlayer;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.nodes.WalkToMule;

public class PkerRunnable implements Runnable{
    boolean isNoPkers = true;
    String[] names = {"dogtalker5", "beanheadlol", "UDiebr0", "plzdontman", "turtlelvr96"};
    int[] skullIconVal = {0, 12, 11, 10, 9, 8};

    // UNSK = -1, SKULL = 0, 1 KEY = 12, 2 KEY = 11, 3 KEY = 10, 4 KEY = 9, 5 KEY = 8

    @Override
    public void run() {
        while (true){
            General.sleep(100);
            RSPlayer[] players = Players.getAll(Filters.Players.nameNotEquals(names));
            // if player skulled near by, run
            if (players.length > 0){
                // Unknown player{s} nearby. Check if he has skull
                for (RSPlayer player: players){
                    if (isSkulled(player)){
                        // He is skulled. Tele
                        System.out.println("SKULLED PLAYER RUN FUCKER!");

                        WalkToMule escape = new WalkToMule();
                        escape.execute();

                        Vars.isPkerDetected = true;
                        General.sleep(100000);
                        Vars.isPkerDetected  = false;
                    }
                }
            }
        }
    }

    private boolean isSkulled(RSPlayer player){
        for (int i: skullIconVal){
            if (player.getSkullIcon() == i){
                return true;
            }
        }
        return false;
    }
}
