package scripts.gengarlibrary.trading;

public class GTradeItem
{
    private int id;
    private int count;

    public GTradeItem(int id, int count)
    {
        this.id = id;
        this.count = count;
    }

    public int getId()
    {
        return id;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
