package scripts.gengaragility.nodes.obstalcles;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.ACamera;
import scripts.gengaragility.nodes.Obstacle;

public class CrossRope extends Obstacle
{
    // Constants
    private static final RSTile TILE_SE = new RSTile(2705, 3495, 2);
    private static final RSTile TILE_NW = new RSTile(2713, 3488, 2);
    private static final RSArea AREA = new RSArea(TILE_NW, TILE_SE);

    private static final int ID_OBSTACLE = 11378;

    public CrossRope(ACamera aCamera)
    {
        super(aCamera);
        averageWaitingTime = 7940;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The CrossRope Node has been Validated! Executing...");

        if (shouldLootMarkOfGrace())
            executeShouldLootMarkOfGrace(AREA);

        clickObstacle("Cross", ID_OBSTACLE);
    }

    @Override
    protected boolean shouldLootMarkOfGrace()
    {
        return true;
    }

    @Override
    public boolean validate()
    {
        return AREA.contains(Player.getPosition());
    }
}
