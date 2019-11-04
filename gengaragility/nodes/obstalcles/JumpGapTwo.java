package scripts.gengaragility.nodes.obstalcles;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.ACamera;
import scripts.gengaragility.nodes.Obstacle;

public class JumpGapTwo extends Obstacle
{
    // Constants
    private static final RSTile TILE_SE = new RSTile(2710, 3481, 2);
    private static final RSTile TILE_NW = new RSTile(2715, 3477, 2);
    private static final RSArea AREA = new RSArea(TILE_NW, TILE_SE);
    private static final int ID_OBSTACLE = 11375;

    public JumpGapTwo(ACamera aCamera)
    {
        super(aCamera);
        averageWaitingTime = 4163;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The JumpGapTwo Node has been Validated! Executing...");

        if (shouldLootMarkOfGrace())
            executeShouldLootMarkOfGrace(AREA);

        clickObstacle("Jump", ID_OBSTACLE);
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
