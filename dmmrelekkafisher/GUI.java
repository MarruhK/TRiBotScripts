package scripts.dmmrelekkafisher;

import org.tribot.api.General;
import org.tribot.api2007.types.RSTile;
import scripts.dmmrelekkafisher.data.Constants;
import scripts.dmmrelekkafisher.data.Vars;
import scripts.dmmrelekkafisher.nodes.Bank.BankCammy;
import scripts.dmmrelekkafisher.nodes.Bank.BankLumby;
import scripts.dmmrelekkafisher.nodes.GoCammyBank;
import scripts.dmmrelekkafisher.nodes.GoLumbyBank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener
{
    private JTextField textFieldMule;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private String[] fish = {"", "Shrimp", "Lobster", "Shark"};
    private String[] spawnLocation = {"", "Lumby", "Cammy"};

    GUI()
    {
        // Components
        JLabel label1 = new JLabel("What to fish: ");
        comboBox1 = new JComboBox(fish);

        JLabel label2 = new JLabel("Spawn Location: ");
        comboBox2 = new JComboBox(spawnLocation);

        JLabel label3 = new JLabel("Mule Name: ");
        textFieldMule = new JTextField(10);

        JButton button = new JButton("Begin");

        // Button registration
        button.addActionListener(this);

        // Panels
        JPanel mainPanel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        // Add components to panels
        panel1.add(label1);
        panel1.add(comboBox1);

        panel2.add(label2);
        panel2.add(comboBox2);

        panel3.add(label3);
        panel3.add(textFieldMule);

        panel4.add(button);

        // Add sub-panels to main panel
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);

        // Add panel to frame
        this.setTitle("The Gengar's DMM Relekka Fisher");
        this.add(mainPanel);

        // Frame properties
        this.setSize(new Dimension(250, 408));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Vars.fishingEquipmentID = -1;
                e.getWindow().dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String comboBox1String = comboBox1.getSelectedItem().toString();
        String comboBox2String = comboBox2.getSelectedItem().toString();
        int depoBoxID;

        if (comboBox1String.equals("Shark") &&
            !comboBox2String.equals("")     &&
            textFieldMule.getText().length() > 0)
        {
            depoBoxID = getDepoBoxID(comboBox2String);

            initializeVars(Constants.SHARK_FISHING_SPOT_NAME, "Harpoon", Constants.SHARK_NPC_ID,
                           Constants.HARPOON_ID, Constants.FISHING_SHARKS_ANIMATION, Constants.SHARK_TILE, depoBoxID,
                           Constants.RAW_SHARK_ID);
        }
        else if (comboBox1String.equals("Lobster")  &&
                 !comboBox2String.equals("")        &&
                 textFieldMule.getText().length() > 0)
        {
            depoBoxID = getDepoBoxID(comboBox2String);

            initializeVars(Constants.LOBSTER_FISHING_SPOT_NAME, "Lobster pot", Constants.LOBSTER_NPC_ID,
                           Constants.LOBSTER_POT_ID, Constants.FISHING_LOBSTERS_ANIMATION, Constants.LOBSTER_TILE, depoBoxID,
                           Constants.RAW_LOBSTER_ID);
        }
        else if (comboBox1String.equals("Shrimp")   &&
                 !comboBox2String.equals("")        &&
                 textFieldMule.getText().length() > 0)
        {
            depoBoxID = getDepoBoxID(comboBox2String);

            initializeVars(Constants.SHRIMP_FISHING_SPOT_NAME, "Small fishing net", Constants.SHRIMP_NPC_ID,
                           Constants.FISHING_NET_ID, Constants.FISHING_SHRIMPS_ANIMATION, Constants.SHRIMP_TILE, depoBoxID,
                           Constants.RAW_SHRIMP_ID, Constants.RAW_ANCHOVY_ID);
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                    "Please select a valid fishing spot/Enter Mule Name/Spawn Location.",
                    "Invalid selection!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getDepoBoxID(String spawnLocation)
    {
        int id;

        if (spawnLocation.equals("Lumby"))
        {
            id = Constants.DEPO_NPC_ID;
        }
        else if (spawnLocation.equals("Cammy"))
        {
            id = Constants.CAMMY_DEPO_NPC_ID;
        }
        else
        {
            General.println("GUI: You fucked up somehow...");
            throw new RuntimeException("Something fucked up");  // Horrible implementation but cba.
        }
        return id;
    }

    private void initializeVars(String fishingSpot, String equipment, int fishingSpotNPCID, int fishingEquipmentID,
                                int fishingAnimation, RSTile fishingTile, int depoBoxNPCID, int... fishID)
    {
        System.out.println("GUI: initializing variables...");

        Vars.muleName = textFieldMule.getText();
        Vars.fishingSpotName = fishingSpot;
        Vars.equipmentName = equipment;
        Vars.fishingSpotNPCID = fishingSpotNPCID;
        Vars.fishID = fishID;
        Vars.fishingEquipmentID = fishingEquipmentID;
        Vars.fishingAnimation = fishingAnimation;
        Vars.fishingTile = fishingTile;
        Vars.depoBoxNPCID = depoBoxNPCID;

        if (depoBoxNPCID == Constants.CAMMY_DEPO_NPC_ID)
        {
            Vars.Bank = new BankCammy();
            Vars.walkToBank = new GoCammyBank();
        }
        else
        {
            Vars.Bank = new BankLumby();
            Vars.walkToBank = new GoLumbyBank();
        }

        this.dispose();
    }
}
