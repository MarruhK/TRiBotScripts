package scripts.fishingtrawler;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Starting;

import scripts.fishingtrawler.data.Constants;
import scripts.fishingtrawler.data.Vars;
import scripts.fishingtrawler.framework.Node;
import scripts.fishingtrawler.nodes.PlayingTrawler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ScriptManifest(
        authors =       "TheGengar",
        name =          "Fishing Trawler",
        category =      "Fishing",
        description =   "Fishing trawler")
public class FishingTrawler extends Script implements Starting, Ending, ActionListener {

    // All actions are stored here.
    private final List<Node> nodes = new ArrayList<>();

    // Paint stuff
    private long startTime;
    private static Font font = new Font("Verdana", Font.BOLD, 14);
    private int startLvl = Skills.getActualLevel(Skills.SKILLS.FISHING);
    private int startXP = Skills.getXP(Skills.SKILLS.FISHING);
    private int initialBaitAmount;
    private int gainedFish = 0;
    private int gainedXP = 0;
    private int gainedLVL = 0;
    private long timeRan = 0;

    private JTextField textField;
    private JFrame frame;

    @Override
    public void onStart() {
        // MAKE SURE BUCKETS ARE AT THE TOP OF THE INVENTORY. Ensure swamp past and rope exist as well.

        initializeGUI();

        // Waits to hear back from the GUI
        while (Vars.dryTrawlerArea == null || Vars.floodedTrawlerArea == null){
            General.sleep(500);
        }

        General.useAntiBanCompliance(true);
        startTime = System.currentTimeMillis();

        // Get index of bucket
        RSItem[] buckets = Inventory.find(Constants.EMPTY_BAILING_BUCKET_ID, Constants.FULL_BAILING_BUCKET_ID);

        for (RSItem bucket: buckets) {
            int tempIndex = bucket.getIndex();

            if (tempIndex > Vars.indexOfBuckets){
                Vars.indexOfBuckets = tempIndex;
            }
        }
    }

    public void initializeGUI(){
        frame = new JFrame();
        JPanel mainPanel = new JPanel();

        // COMPONENTS
        textField = new JTextField(5);
        JButton startButton = new JButton("START");
        startButton.addActionListener(this);

        // Add components to panels
        mainPanel.add(textField);
        mainPanel.add(startButton);

        // Add panels to frame
        frame.add(mainPanel);
        // Frame properties
        frame.setSize(new Dimension(400, 120));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        Collections.addAll(nodes,
                new PlayingTrawler());

        loop(250, 500);
    }

    public void loop(int min, int max){

        while (Vars.shouldExecute) {
            for (final Node node : nodes) {
                if (node.validate()) {
                    node.execute();
                    General.sleep(General.random(min, max));	//time in between executing nodes
                }
            }
        }

        do {
            System.out.println("Time to log out.");
            Login.logout();

            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100);
                    return Login.getLoginState() == Login.STATE.LOGINSCREEN;
                }
            }, General.random(10500, 10600));
        } while (Login.getLoginState() != Login.STATE.LOGINSCREEN);
    }

    @Override
    public void onEnd() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String areaIndex = textField.getText();


        if   (!(areaIndex.equals("1") ||
                areaIndex.equals("2"))) {

            // close gui u fucked up

        } else {
            int intValOfAreaIndex = Integer.parseInt(areaIndex);

            Vars.dryTrawlerArea = Constants.DUO_DRY_BOAT_AREAS[intValOfAreaIndex-1];
            Vars.floodedTrawlerArea = Constants.DUO_FLOODED_BOAT_AREAS[intValOfAreaIndex-1];

            if (intValOfAreaIndex == 1){
                Vars.isNetFixer = true;

                Vars.dryTrawlerArea = Constants.ONE_DRY_AREA;
                Vars.drySouthHoleTrawlerArea = Constants.ONE_DRY_HOLE_AREA;
                Vars.floodedTrawlerArea = Constants.ONE_FLOODED_AREA;
                Vars.floodedSouthHoleTrawlerArea = Constants.ONE_FLOODED_HOLE_AREA;
            } else {
                Vars.dryTrawlerArea = Constants.TWO_DRY_AREA;
                Vars.drySouthHoleTrawlerArea = Constants.TWO_DRY_HOLE_AREA;
                Vars.floodedTrawlerArea = Constants.TWO_FLOODED_AREA;
                Vars.floodedSouthHoleTrawlerArea = Constants.TWO_FLOODED_HOLE_AREA;
            }
            frame.dispose();
        }


        /*
        if   (!(areaIndex.equals("1") ||
                areaIndex.equals("2"))) {

            // close gui u fucked up

        } else {
            int intValOfAreaIndex = Integer.parseInt(areaIndex);

            Vars.dryTrawlerArea = Constants.DRY_BOAT_AREAS[intValOfAreaIndex-1];
            Vars.floodedTrawlerArea = Constants.FLOODED_BOAT_AREAS[intValOfAreaIndex-1];

            if (intValOfAreaIndex == 1 || intValOfAreaIndex == 2){
                if (intValOfAreaIndex == 1){
                    Vars.isNetFixer = true;
                }
                Vars.isTop = true;
            } else {
                Vars.isTop = false;

                if (intValOfAreaIndex == 3){
                    Vars.drySouthHoleTrawlerArea = Constants.DRY_BOT_LEFT_HOLE_AREA;
                    Vars.floodedSouthHoleTrawlerArea = Constants.FLOODED_BOT_LEFT_HOLE_AREA;
                } else { // it's 4
                    Vars.drySouthHoleTrawlerArea = Constants.DRY_BOT_RIGHT_HOLE_AREA;
                    Vars.floodedSouthHoleTrawlerArea = Constants.FLOODED_BOT_RIGHT_HOLE_AREA;
                }
            }
            frame.dispose();
        }
        */
    }
}