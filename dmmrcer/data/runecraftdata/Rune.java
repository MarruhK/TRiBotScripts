package scripts.dmmrcer.data.runecraftdata;

import scripts.dmmrcer.data.runecraftdata.Runes;

public class Rune
{
    private int id;
    private double xp;
    private String name;

    public Rune(Runes rune)
    {
        switch (rune)
        {
            case FIRE:
                this.id = 554;
                this.xp = 7;
                this.name = "Fire Rune";
                break;

            case WATER:
                this.id = 555;
                this.xp = 6;
                this.name = "Water Rune";
                break;

            case AIR:
                this.id = 556;
                this.xp = 5;
                this.name = "Air Rune";
                break;

            case EARTH:
                this.id = 557;
                this.xp = 6.5;
                this.name = "Earth Rune";
                break;

            case MIND:
                this.id = 558;
                this.xp = 5.5;
                this.name = "Mind Rune";
                break;

            case BODY:
                this.id = 559;
                this.xp = 7.5;
                this.name = "Body Rune";
                break;

            case DEATH:
                this.id = 560;
                this.xp = 10;
                this.name = "Death Rune";
                break;

            case NATURE:
                this.id = 561;
                this.xp = 9;
                this.name = "Nature Rune";
                break;

            case CHAOS:
                this.id = 562;
                this.xp = 8.5;
                this.name = "Chaos Rune";
                break;

            case LAW:
                this.id = 563;
                this.xp = 9.5;
                this.name = "Law Rune";
                break;

            case COSMIC:
                this.id = 564;
                this.xp = 8;
                this.name = "Cosmic Rune";
                break;

            case BLOOD:
                this.id = 565;
                this.xp = 23.8;
                this.name = "Blood Rune";
                break;

            case SOUL:
                this.id = 566;
                this.xp = 29.7;
                this.name = "Soul Rune";
                break;

            case WRATH:
                this.id = 21880;
                this.xp = 8;
                this.name = "Wrath Rune";
                break;
        }
    }

    public int getId()
    {
        return id;
    }

    public double getXp()
    {
        return xp;
    }

    public String getName()
    {
        return name;
    }

}
