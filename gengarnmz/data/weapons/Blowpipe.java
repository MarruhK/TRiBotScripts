package scripts.gengarnmz.data.weapons;

import scripts.gengarnmz.data.Constants;

public class Blowpipe extends Weapon
{
    public Blowpipe()
    {
        this.ids = Constants.BLOWPIPE_IDS;

        this.combatStyleIndexMapping.put("Accurate Range", 0);
        this.combatStyleIndexMapping.put("Range", 1);
        this.combatStyleIndexMapping.put("Long Range", 3);
    }

    @Override
    public String toString()
    {
        return "Blowpipe";
    }
}
