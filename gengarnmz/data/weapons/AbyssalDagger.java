package scripts.gengarnmz.data.weapons;

import scripts.gengarnmz.data.Constants;

public class AbyssalDagger extends Weapon
{
    public AbyssalDagger()
    {
        this.ids = Constants.BLOWPIPE_IDS;

        this.combatStyleIndexMapping.put("Attack", 0);
        this.combatStyleIndexMapping.put("Strength", 1);
        this.combatStyleIndexMapping.put("Controlled", 2);
        this.combatStyleIndexMapping.put("Defence", 3);
    }

    @Override
    public String toString()
    {
        return "Abyssal Dagger";
    }
}
