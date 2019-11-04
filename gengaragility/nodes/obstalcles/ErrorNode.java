package scripts.gengaragility.nodes.obstalcles;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.gengarlibrary.ACamera;
import scripts.gengaragility.nodes.Obstacle;

public class ErrorNode extends Obstacle
{
    // Constants
    private static final RSTile TILE_SE = new RSTile(2730, 3490, 1);
    private static final RSTile TILE_NW = new RSTile(2722, 3494, 1);
    private static final RSArea AREA = new RSArea(TILE_NW, TILE_SE);
    private static final int ID_OBSTACLE = 25939;

    public ErrorNode(ACamera aCamera)
    {
        super(aCamera);
    }

    @Override
    protected boolean shouldLootMarkOfGrace()
    {
        return false;
    }

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The ErrorNode Node has been Validated! Executing...");

        if (shouldLootMarkOfGrace())
            executeShouldLootMarkOfGrace(AREA);

        clickObstacle("Climb-down", ID_OBSTACLE);
    }

    @Override
    public boolean validate()
    {
        return AREA.contains(Player.getPosition());
    }
}
