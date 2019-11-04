package scripts.gengarnmz.gui;

import org.tribot.api2007.Combat;
import org.tribot.api2007.Skills;
import scripts.gengarnmz.data.AttackStyles;
import scripts.gengarnmz.data.Constants;
import scripts.gengarnmz.data.Vars;
import scripts.gengarnmz.data.potions.combat.RangingPotion;
import scripts.gengarnmz.data.potions.combat.superpotions.OverloadPotion;
import scripts.gengarnmz.data.potions.combat.superpotions.SuperCombatPotion;
import scripts.gengarnmz.data.potions.combat.superpotions.SuperRangingPotion;
import scripts.gengarnmz.data.potions.utility.AbsorptionPotion;
import scripts.gengarnmz.data.potions.utility.PrayerPotion;
import scripts.gengarnmz.data.weapons.AbyssalDagger;
import scripts.gengarnmz.data.weapons.Blowpipe;
import scripts.gengarnmz.data.weapons.Bludgeon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class NmzGui extends JFrame implements ActionListener
{
    private String[] weapons = {"", "Bludgeon", "Abyssal Dagger", "Blowpipe"};
    private JComboBox<String> comboBox1 = new JComboBox<>(weapons);

    private String[] styles = {"", "Attack", "Strength", "Defence", "Controlled", "Accurate Range", "Range", "Long Range"};
    private JComboBox<String> comboBox2 = new JComboBox<>(styles);

    private String[] combatPotions = {"", "Ranging Potion", "Super Ranging Potion", "Overload Potion", "Super Combat Potion"};
    private JComboBox<String> comboBox3 = new JComboBox<>(combatPotions);

    private String[] utilityPotions = {"", "Prayer Potion", "Absorption Potion"};
    private JComboBox<String> comboBox4 = new JComboBox<>(utilityPotions);

    public NmzGui()
    {
        JLabel label1 = new JLabel("Weapon");
        JLabel label2 = new JLabel("Attack style");
        JLabel label3 = new JLabel("Combat Potion");
        JLabel label4 = new JLabel("Utility Potion");
        JButton button = new JButton("Start NMZ!");
        button.addActionListener(this);
        comboBox1.setPrototypeDisplayValue("------------------------------------------------------------");
        comboBox2.setPrototypeDisplayValue("------------------------------------------------------------");
        comboBox3.setPrototypeDisplayValue("------------------------------------------------------------");
        comboBox4.setPrototypeDisplayValue("------------------------------------------------------------");

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        panel1.add(label1);
        panel1.add(comboBox1);
        panel2.add(label2);
        panel2.add(comboBox2);
        panel3.add(label3);
        panel3.add(comboBox3);
        panel4.add(label4);
        panel4.add(comboBox4);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.SOUTH);

        // Frame properties
        setSize(354, 250);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        // Handle X close
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // Need to set to true to escape infinite loop
                Vars.shouldExecute = true;
                e.getWindow().dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String selectedTask1 = comboBox1.getSelectedItem().toString();
        String selectedTask2 = comboBox2.getSelectedItem().toString();
        String selectedTask3 = comboBox3.getSelectedItem().toString();
        String selectedTask4 = comboBox4.getSelectedItem().toString();

        if (handleJcb1and2(selectedTask1, selectedTask2) && handleJcb3(selectedTask3) && handleJcb4(selectedTask4))
        {
            printGuiVars();
            Vars.shouldExecute = true;
            this.dispose();
        }
    }

    private boolean handleJcb1and2(String item1, String item2)
    {
        if (handleJcb(item1) && handleJcb(item2))
        {
            // JCB1
            final int[] weaponIds;
            switch (item1)
            {
                case "Bludgeon":
                    Vars.weapon = new Bludgeon();
                    break;
                case "Abyssal Dagger":
                    Vars.weapon = new AbyssalDagger();
                    break;
                case "Blowpipe":
                    Vars.weapon = new Blowpipe();
                    break;
            }

            // JCB2
            int combatIndex = Vars.weapon.getCombatStyleIndex(item2);

            if(handleCombatIndex(combatIndex))
            {
                Skills.SKILLS skillToTrain = determineSkill(item2);

                if (skillToTrain == null)
                {
                    System.out.println("handleJcb1and2: skillToTrain is null, returning...");
                    return false;
                }

                Vars.weapon.setSkill(skillToTrain);
                Vars.weapon.setAttackStyle(item2);
                return Combat.selectIndex(combatIndex);
            }
        }
        return false;
    }

    private boolean handleJcb3(String item)
    {
        if (handleJcb(item))
        {
            switch (item)
            {
                case "Ranging Potion":
                    Vars.combatPotion = new RangingPotion();
                    break;
                case "Super Ranging Potion":
                    Vars.combatPotion = new SuperRangingPotion();
                    break;
                case "Overload Potion":
                    Vars.combatPotion = new OverloadPotion();
                    break;
                case "Super Combat Potion":
                    Vars.combatPotion = new SuperCombatPotion();
                    break;
            }
            return true;
        }
        return false;
    }

    private boolean handleJcb4(String item)
    {
        if (handleJcb(item))
        {
            switch (item)
            {
                case "Prayer Potion":
                    Vars.utilityPotion = new PrayerPotion();
                    break;
                case "Absorption Potion":
                    Vars.utilityPotion = new AbsorptionPotion();
                    break;
            }
            return true;
        }
        return false;
    }

    private boolean handleJcb(String item)
    {
        if (item.equals(""))
        {
            // select task
            JOptionPane.showMessageDialog(this,
                    "Please select a valid task.",
                    "Invalid selection!",
                    JOptionPane.ERROR_MESSAGE);

            return false;
        }
        return true;
    }

    private boolean handleCombatIndex(int index)
    {
        if (index == -1)
        {
            JOptionPane.showMessageDialog(this,
                                "Please select a valid attack style for the weapon.",
                                "Invalid selection!",
                                JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }

    private Skills.SKILLS determineSkill(String attackStyle)
    {
        switch (attackStyle)
        {
            case "Accurate Range":
            case "Long Range":
            case "Range":
                return Skills.SKILLS.RANGED;
            case "Strength":
            case "Controlled":
                return Skills.SKILLS.STRENGTH;
            case "Attack":
                return Skills.SKILLS.ATTACK;
            case "Defence":
                return Skills.SKILLS.DEFENCE;
        }

        return null;
    }

    private void printGuiVars()
    {
        System.out.println("printGuiVars____________________________________________________________");
        System.out.println("Weapon: " + Vars.weapon.toString());
        System.out.println("Attack Style: " + Vars.weapon.getAttackStyle());
        System.out.println("Combat Potion: " + Vars.combatPotion.toString());
        System.out.println("Utility Potion: " + Vars.utilityPotion.toString());
    }
}
