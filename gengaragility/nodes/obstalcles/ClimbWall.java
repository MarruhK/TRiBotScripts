package scripts.gengaragility.nodes.obstalcles;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.ACamera;
import scripts.gengaragility.nodes.Obstacle;

public class ClimbWall extends Obstacle
{
    // Constants
    private static final RSTile TILE_SE = new RSTile(2733, 3480, 0);
    private static final RSTile TILE_NW = new RSTile(2721, 3486, 0);
    private static final RSArea AREA = new RSArea(TILE_NW, TILE_SE);

    private static final int ID_OBSTACLE = 11373;

    public ClimbWall(ACamera aCamera)
    {
        super(aCamera);
        averageWaitingTime = 5634;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The ClimbWall Node has been Validated! Executing...");

        if (shouldLootMarkOfGrace())
            executeShouldLootMarkOfGrace(AREA);

        clickObstacle("Climb-up", ID_OBSTACLE);
    }

    @Override
    protected boolean shouldLootMarkOfGrace()
    {
        return false;
    }

    @Override
    public boolean validate()
    {
        return AREA.contains(Player.getPosition());
    }
}
