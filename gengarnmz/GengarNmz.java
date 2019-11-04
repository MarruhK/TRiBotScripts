package scripts.gengarnmz;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.*;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.gengarlibrary.initialsetup.CameraZoom;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.framework.Node;
import scripts.gengarnmz.gui.NmzGui;
import scripts.gengarnmz.nodes.*;
import scripts.gengarnmz.nodes.blowpipe.ChargeBlowpipe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Gengar's NMZ",
        category =      "combat",
        description =   "A full automated, afk, blowpipe NMZ bot. It is able to:\n" +
                        "    Bank to get prayer potions, cash for coffer and darts/scales for pipe\n" +
                        "    Detects when you have no cash in coffer and gets it from bank\n" +
                        "    Obtains super range pots from the barrel. If none exist, purchases them from the shop.")
public class GengarNmz extends Script implements Starting, Ending, Painting, MessageListening07, MouseActions
{
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private static long startTime = System.currentTimeMillis();
    private static final Rectangle TOGGLE_PAINT = new Rectangle(0, 339, 55, 23);
    private boolean hidePaint = false;
    private int gainedXP;
    private int gainedLvl;
    private long timeRan;
    private long xpPerHour;

    // Paint GUI vars
    private int startLvl;
    private int startXP;
    private String skillToTrain;
    private Skills.SKILLS skill;

    @Override
    public void onStart()
    {
        General.useAntiBanCompliance(true);
        new NmzGui();

        // Constants.initializeMappings();

        // Wait for GUI to get all variables.
        while (!Vars.shouldExecute)
        {
            General.sleep(500);
        }

        skillToTrain = Vars.weapon.getAttackStyle();
        skill = Vars.weapon.getSkill();

        DaxWalker.setCredentials(new DaxCredentialsProvider()
        {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });

        CameraZoom.zoomOut();
        Combat.setAutoRetaliate(true);
    }

    private void checkWeapon()
    {
        RSItem[] weapons = Equipment.find(Vars.weapon.getIds());

        if (weapons.length <= 0)
        {
            System.out.println("Weapon is not equipped, ending script.");
            Vars.shouldExecute = false;
            return;
        }

        // initializeCombatStyleVars();
        initializePaintVars();

        // Check Blowpipe charges, if you are wearing BP
        if (Vars.weapon.getIds() == Constants.BLOWPIPE_IDS)
        {
            weapons[0].click("Check");
            System.out.println("GengarNmz: checking blowpipe charges...");

            if (Timing.waitCondition(()-> Vars.outOfDarts || Vars.outOfScales, 3000))
            {
                System.out.println("Blowpipe is out of charges...");
            }
        }

        // Need a similar check for BSS
    }

    /*private void initializeCombatStyleVars()
    {
        int selectedCombatIndex = Combat.getSelectedStyleIndex();
        HashMap<Integer, AttackStyles> attackStylesHashMap = Constants.WEAPON_MAPPING.get(Vars.weaponIds);

        Vars.attackStyles = attackStylesHashMap.get(selectedCombatIndex);
        Vars.skill = AttackStyles.styleToSkill(Vars.attackStyles);
        skillToTrain = AttackStyles.styleToString(Vars.attackStyles);
    }*/

    private void initializePaintVars()
    {
        startLvl = Skills.getActualLevel(skill);
        startXP = Skills.getXP(skill);
    }

    @Override
    public void run()
    {
        checkWeapon();

        Collections.addAll(nodes,
                new Bank(),
                new Dreaming(),
                new GetNmzPots(),
                new StartDreamDominic(),
                new WalkBank(),
                new WalkNmz(),
                new ChargeBlowpipe(),
                new DepositCoffer(),
                new StartDreamPotion());

        loop();
    }

    private void loop()
    {
        while (Vars.shouldExecute)
        {
            for (final Node node : nodes)
            {
                if (!Vars.shouldExecute)
                    break;

                if (node.validate())
                {
                    node.execute();
                    General.sleep(General.random(250, 500));
                }
            }
        }

        do
        {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(()->
            {
                General.sleep(100);
                return Login.getLoginState() == Login.STATE.LOGINSCREEN;
            }, General.random(10500, 10600));
        } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }

    @Override
    public void serverMessageReceived(String s)
    {
        String outOfDarts1 = "Your blowpipe has run out of darts.";
        String outOfDarts2 = "Your blowpipe contains no darts.";
        String outOfScales = "Your blowpipe needs to be charged with Zulrah's scales.";
        String checkDarts = "Darts: <col=007f00>Mithril dart x";

        if (s.equals(outOfDarts1) || s.equals(outOfDarts2))
        {
            System.out.println("Blowpipe is empty, suiciding... :D");
            Vars.outOfDarts = true;
        }
        else if (s.equals(outOfScales))
        {
            System.out.println("Blowpipe is out of scales, suiciding... :D");
            Vars.outOfScales = true;
        }
        else if (s.contains(checkDarts))
        {
            // Have around 1500 darts and 20% scales
            String[] checkBlowpipeSplit = s.split("</col>");

            String darts = checkBlowpipeSplit[0];
            String scales = checkBlowpipeSplit[1];

            String tempString1 = scales.substring(scales.indexOf(">") + 1, scales.indexOf("%"));
            String tempString2 = tempString1.substring(0, tempString1.indexOf("."));
            int scalesPercentage = Integer.parseInt(tempString2);
            int dartsAmount = Integer.parseInt(darts.substring(darts.lastIndexOf("x") + 2).replaceAll(",", ""));

            if (dartsAmount < 1500)
            {
                Vars.outOfDarts = true;
            }

            if (scalesPercentage < 20)
            {
                Vars.outOfScales = true;
            }
        }
        else if (s.equals("This barrel is empty."))
        {
            Vars.isBarrelEmpty = true;
        }
        else if (s.equals("The blowpipe can't hold any more scales."))
        {
            Vars.isFullScales = true;
        }
        else if (s.equals("The blowpipe can't hold any more darts."))
        {
            Vars.isFullDarts = true;
        }
    }

    @Override
    public void onEnd()
    {
        General.println("GengarNmz has completed.");
        General.println("Total time ran: " + timeRan);
        General.println("Total " + Vars.weapon.getAttackStyle() + " XP gained: " + gainedXP);
        General.println("Total " + skillToTrain + " Lvls gained: " + gainedLvl);
        General.println("Average " + skillToTrain + " XP/H: " + xpPerHour);
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
                Image img = getImage();
                gg.drawImage(img, 0, 339, null);

                // Toggler
                gg.draw(new Rectangle(0, 339, 55, 23));
                g.drawString("Toggle", 0, 359);

                // Text data
                timeRan = System.currentTimeMillis() - startTime;
                int currentLvl = Skills.getActualLevel(skill);
                gainedXP = Skills.getXP(skill) - startXP;
                gainedLvl = currentLvl - startLvl;
                int nextLvl = currentLvl + 1;
                xpPerHour = (long) (gainedXP * 3600000d / timeRan);
                long xpToNextLvl = (Skills.getXPToNextLevel(skill));

                // Implementation of text
                g.drawString("Runtime: " + Timing.msToString(timeRan), 200, 370);
                g.drawString("Current Level: " + currentLvl, 200, 390);
                g.drawString("Gained " + skillToTrain + " XP: " + gainedXP + " (Levels: " + gainedLvl + ")", 200, 410);
                g.drawString( skillToTrain + " XP to level " + nextLvl + ": " + xpToNextLvl, 200, 430);
                g.drawString( skillToTrain + " XP/H: " + xpPerHour, 200, 450);
            }
        }
    }

    private Image getImage()
    {
        try
        {
            return ImageIO.read(new URL("https://i.imgur.com/0aQy9Ce.png"));
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
    @Override public void clanMessageReceived(String s, String s1) {}
    @Override public void playerMessageReceived(String s, String s1) {}
    @Override public void tradeRequestReceived(String s) {}
    @Override public void personalMessageReceived(String s, String s1) {}
    @Override public void duelRequestReceived(String s, String s1) {}
}

