package scripts.aiohunter.framework;

import scripts.gengarlibrary.ACamera;

/**
 * The nodes framework base class.
 * @author Worthy from TriBot
 **/

public abstract class Node
{
    protected ACamera aCamera;

    public Node(ACamera aCamera)
    {
        this.aCamera = aCamera;
    }

    public abstract void execute();
    public abstract boolean validate();
}