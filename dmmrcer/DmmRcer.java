package scripts.dmmrcer;


import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MouseActions;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.framework.Node;
import scripts.dmmrcer.framework.Validator;
import scripts.dmmrcer.gui.Gui;
import scripts.gengarlibrary.Antiban;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Plans:
 *
 * Stats Needed for nature runes:
 *      50 con
 *      44 RC
 *      36 wc
 *      31 crafting
 *      melee combat stats (base 50s prob good enuff)
 *      43 prayer
 *      some agility pref *
 *
 * Get fairy ring tele unlocked (only need restless ghost and nature spirit and fairy ring 1 quest
 *
 * Tele home, outside home trade mules who bank at yanille. Give them runes they give essence. Go north to fairy ring
 * to go to nature altar one. Rinse and repeat.
 *
 * Stats needed for law runes:
 *      Troll stronghold
 *
 *      - 15 agility
 *      - 13 magic
 *      - 40 prayer for prot range
 *
 *
 */

@ScriptManifest(
        authors =       "TheGengar",
        name =          "DMM Runecrafter",
        category =      "DMM",
        description =   "Runecrafts.")
public class DmmRcer extends Script implements Starting, Painting, MouseActions
{
    private final List<Node> nodes = new ArrayList<>();

    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private static long startTime = System.currentTimeMillis();
    private static final Rectangle TOGGLE_PAINT = new Rectangle(0, 339, 55, 23);
    private boolean hidePaint = false;
    private int gainedXP;
    private int gainedLvl;
    private long timeRan;
    private long xpPerHour;

    // Paint GUI vars
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.RUNECRAFTING);
    private int startXP = Skills.getXP(Skills.SKILLS.RUNECRAFTING);
    private String skillToTrain = "Runecrafting";
    private Skills.SKILLS skill = Skills.SKILLS.RUNECRAFTING;

    @Override
    public void onStart()
    {
        DaxWalker.setCredentials(new DaxCredentialsProvider()
        {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        new Gui();

        while (!Vars.shouldExecute)
        {
            General.sleep(500);
        }

        // ENSURE THAT YOU AHVE THE RELVANT SKILL STATS BEFORE YOU CAN DO SCRIPT
        // ENSURE ZOOM, ENSURE ATTACK OPTIONS OFF, ENSUER TALISMAN OR TIARA IS ON
    }

    @Override
    public void run()
    {
        new Validator().execute();

        do
        {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(()->
            {
                General.sleep(100);
                return Login.getLoginState() == Login.STATE.LOGINSCREEN;
            }, General.random(12500, 13600));
        } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }

    @Override
    public void onPaint(Graphics g)
    {
        Graphics2D gg = (Graphics2D)g;
        gg.draw(TOGGLE_PAINT);

        if (!hidePaint)
        {
            if (Game.getGameState() == 30)
            {
                // General
                g.setFont(font);
                g.setColor(Color.WHITE);

                // Gengar Background
                BufferedImage img = getImage();
                gg.drawImage(img, 0, 339, null);

                // Toggler
                gg.draw(new Rectangle(0, 339, 55, 23));
                g.drawString("Toggle", 0, 359);

                timeRan = System.currentTimeMillis() - startTime;
                g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);

                if (Vars.rcerName == null)
                {
                    // Text data
                    int currentLvl = Skills.getActualLevel(skill);
                    gainedXP = Skills.getXP(skill) - startXP;
                    gainedLvl = currentLvl - startLvl;
                    int nextLvl = currentLvl + 1;
                    xpPerHour = (long) (gainedXP * 3600000d / timeRan);
                    long xpToNextLvl = (Skills.getXPToNextLevel(skill));

                    // Implementation of text
                    g.drawString("Current Level: " + currentLvl, 200, 390);
                    g.drawString("Gained runecrafting XP: " + gainedXP + " (Levels: " + gainedLvl + ")", 200, 410);
                    g.drawString( skillToTrain + " XP to level " + nextLvl + ": " + xpToNextLvl, 200, 430);
                    g.drawString( skillToTrain + " XP/H: " + xpPerHour, 200, 450);
                }
                else
                {
                    // Text data
                    int essenceTraded = Vars.runner.getEssenceTraded();
                    gainedXP = (int) (essenceTraded * Vars.rune.getXp());
                    xpPerHour = (long) (gainedXP * 3600000d / timeRan);

                    // Implementation of text
                    g.drawString("Assisted rc exp: " + gainedXP, 200, 410);
                    g.drawString( skillToTrain + " XP/H: " + xpPerHour, 200, 450);
                }
            }
        }
    }

    private BufferedImage getImage()
    {
        try
        {
            return ImageIO.read(new File("C:/Users/Hamza/Desktop/Dev/OSRS/Botting/TriBot/TRiBot Projects/resources/background.png"));
        }
        catch (IOException e)
        {
            System.out.println("Didn't find paint image.");
            return null;
        }
    }

    @Override
    public void mouseReleased(Point point, int i, boolean b)
    {
        if (TOGGLE_PAINT.contains(point.getLocation()))
        {
            hidePaint = !hidePaint;
        }
    }

    // Not in use
    @Override public void mouseClicked(Point point, int i, boolean b) {}
    @Override public void mouseDragged(Point point, int i, boolean b) {}
    @Override public void mouseMoved(Point point, boolean b) {}
}
