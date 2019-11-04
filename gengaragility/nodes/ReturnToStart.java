package scripts.gengaragility.nodes;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.ACamera;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.gengaragility.framework.Node;


public class ReturnToStart extends Node
{
    // Constants
    private static final RSTile TILE_SE = new RSTile(2733, 3480, 0);
    private static final RSTile TILE_NW = new RSTile(2721, 3486, 0);
    private static final RSArea AREA = new RSArea(TILE_NW, TILE_SE);

    // Teleport stuff

    public ReturnToStart(ACamera aCamera)
    {
        super(aCamera);
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The ReturnToStart Node has been Validated! Executing...");

        // run to start or tele to cammy
        DaxWalker.walkTo(AREA.getRandomTile());
    }

    @Override
    public boolean validate()
    {
        return Player.getPosition().getPlane() == 0;
    }
}
