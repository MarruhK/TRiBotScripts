package scripts.gengarnmz.data.weapons;

import scripts.gengarnmz.data.Constants;

public class Bludgeon extends Weapon
{

    public Bludgeon()
    {
        this.ids = Constants.BLUDGEON_IDS;

        this.combatStyleIndexMapping.put("Strength", 1);
    }

    @Override
    public String toString()
    {
        return "Bludgeon";
    }
}
