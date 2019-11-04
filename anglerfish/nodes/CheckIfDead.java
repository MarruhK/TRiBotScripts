package scripts.anglerfish.nodes;

import org.tribot.api2007.Player;
import scripts.anglerfish.data.Constants;
import scripts.anglerfish.data.Vars;
import scripts.anglerfish.framework.Node;

public class CheckIfDead extends Node {
    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The CheckIfDead Node has been Validated! Executing...");

        if (Constants.VARROCK.contains(Player.getPosition()))
        {
            System.out.print("You teleported away without dying, well done.");
        }
        else
        {
            System.out.println("You are dead... Stupid fuck.");
        }
        Vars.shouldExecute = false;
    }

    @Override
    public boolean validate()
    {
        return  Vars.shouldExecute &&
                (Constants.LUMBRIDGE.contains(Player.getPosition()) ||
                        Constants.VARROCK.contains(Player.getPosition()));
    }
}
