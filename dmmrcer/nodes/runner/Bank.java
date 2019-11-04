package scripts.dmmrcer.nodes.runner;

        import scripts.dmmrcer.data.Constants;
        import scripts.dmmrcer.data.Vars;
        import scripts.dmmrcer.framework.Node;
        import scripts.gengarlibrary.GBanking;

public class Bank implements Node
{

    @Override
    public void execute()
    {
        System.out.println("_________________________________________________________________________________________");
        System.out.println("The Bank Node has been Validated! Executing...");

        Vars.runner.bank();
    }

    @Override
    public boolean validate()
    {
        return false;
    }
}
