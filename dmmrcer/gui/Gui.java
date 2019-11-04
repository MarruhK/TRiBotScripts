package scripts.dmmrcer.gui;

import scripts.codetester.CodeTester;
import scripts.dmmrcer.data.runecraftdata.altars.*;
import scripts.dmmrcer.data.runecraftdata.Rune;
import scripts.dmmrcer.data.Vars;
import scripts.dmmrcer.data.runecraftdata.Runes;
import scripts.dmmrcer.data.runecraftdata.botinstance.rcer.AirRcer;
import scripts.dmmrcer.data.runecraftdata.botinstance.rcer.FireRcer;
import scripts.dmmrcer.data.runecraftdata.botinstance.rcer.LawRcer;
import scripts.dmmrcer.data.runecraftdata.botinstance.rcer.NatureRcer;
import scripts.dmmrcer.data.runecraftdata.botinstance.runners.AirRunner;
import scripts.dmmrcer.data.runecraftdata.botinstance.runners.FireRunner;
import scripts.dmmrcer.data.runecraftdata.botinstance.runners.LawRunner;
import scripts.dmmrcer.data.runecraftdata.botinstance.runners.NatureRunner;
import scripts.gengarlibrary.GString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Gui extends JFrame implements ActionListener
{
    private boolean isRunner;

    private String rcer = "Rcer";
    private String runner = "Runner";

    private JRadioButton rcerButton = new JRadioButton(rcer);
    private JRadioButton runnerButton = new JRadioButton(runner);

    private JRadioButton trueButton = new JRadioButton("True");
    private JRadioButton falseButton = new JRadioButton("False");
    private JRadioButton trueButton2 = new JRadioButton("True");
    private JRadioButton falseButton2 = new JRadioButton("False");

    private JComboBox runeCombobox;
    private JTextField muleName;
    private JTextField rcerName;
    private JTextField muleNameRunner;

    private JPanel mainPanel = new JPanel();
    private JPanel runePanel = new JPanel();
    private JPanel radioPanel = new JPanel();
    private JPanel rcerPanel = new JPanel();
    private JPanel runnerPanel = new JPanel();

    public Gui()
    {
        // Default components
        JLabel runeLabel = new JLabel("Runes to craft", SwingConstants.CENTER);
        String[] runesToCraft = {"Law", "Nature", "Air", "Fire"};
        runeCombobox = new JComboBox(Runes.values())
        {
            @Override
            public Dimension getMaximumSize()
            {
                // Needed to fix max height issue im having
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };

        JLabel typeLabel = new JLabel("Runecraft Type", SwingConstants.CENTER);
        ButtonGroup group = new ButtonGroup();  // Groups rbuttons so one can only be selected
        group.add(rcerButton);
        group.add(runnerButton);

        rcerButton.addActionListener(new RadioButtonHandler());
        runnerButton.addActionListener(new RadioButtonHandler());

        // Rcer components
        JLabel runnerLabel = new JLabel("Runner coming inside altar?");
        ButtonGroup rcGroup = new ButtonGroup();
        rcGroup.add(trueButton);
        rcGroup.add(falseButton);

        JLabel bankingLabel = new JLabel("Banking?", SwingConstants.CENTER);
        ButtonGroup rcBankGroup = new ButtonGroup();  // Groups rbuttons so one can only be selected
        rcBankGroup.add(trueButton2);
        rcBankGroup.add(falseButton2);

        JLabel muleLabel = new JLabel("Mule name");
        muleName = new JTextField("", 15);

        // Runner components
        JLabel muleLabelRunner = new JLabel("Mule name");
        muleNameRunner = new JTextField("", 15);

        JLabel rcerLabel = new JLabel("RCer name");
        rcerName = new JTextField("", 15);

        // Button
        JButton button = new JButton("Start!");
        button.addActionListener(this);

        // Default panels
        JPanel runeLabelPanel = new JPanel();
        runeLabelPanel.add(runeLabel);

        runePanel.setLayout(new BoxLayout(runePanel, BoxLayout.Y_AXIS));
        runePanel.add(runeCombobox);

        JPanel typeLabelPanel = new JPanel();
        typeLabelPanel.add(typeLabel);

        radioPanel.add(rcerButton);
        radioPanel.add(runnerButton);

        JPanel defaultPanel = new JPanel();
        defaultPanel.setLayout(new BoxLayout(defaultPanel, BoxLayout.Y_AXIS));
        defaultPanel.add(runeLabelPanel);
        defaultPanel.add(runePanel);
        defaultPanel.add(typeLabelPanel);
        defaultPanel.add(radioPanel);

        // Rcer panels
        JPanel runnerLabelPanel = new JPanel();
        runnerLabelPanel.add(runnerLabel);

        JPanel jrbPannel = new JPanel();
        jrbPannel.add(trueButton);
        jrbPannel.add(falseButton);

        JPanel bankLabelPanel = new JPanel();
        bankLabelPanel.add(bankingLabel);

        JPanel jrbPannel2 = new JPanel();
        jrbPannel2.add(trueButton2);
        jrbPannel2.add(falseButton2);

        JPanel muleLabelPanel = new JPanel();
        muleLabelPanel.add(muleLabel);

        JPanel mulePanel = new JPanel();
        mulePanel.add(muleName);

        rcerPanel.setLayout(new BoxLayout(rcerPanel, BoxLayout.Y_AXIS));
        rcerPanel.add(runnerLabelPanel);
        rcerPanel.add(jrbPannel);
        rcerPanel.add(bankLabelPanel);
        rcerPanel.add(jrbPannel2);
        rcerPanel.add(muleLabelPanel);
        rcerPanel.add(mulePanel);

        // Runner panels
        JPanel muleLabelRunnerPanel = new JPanel();
        muleLabelRunnerPanel.add(muleLabelRunner);

        JPanel mulePanelRunner = new JPanel();
        mulePanelRunner.add(muleNameRunner);

        JPanel rcerLabelPanel = new JPanel();
        rcerLabelPanel.add(rcerLabel);

        JPanel rcerPanel = new JPanel();
        rcerPanel.add(rcerName);

        runnerPanel.setLayout(new BoxLayout(runnerPanel, BoxLayout.Y_AXIS));
        runnerPanel.add(muleLabelRunnerPanel);
        runnerPanel.add(mulePanelRunner);
        runnerPanel.add(rcerLabelPanel);
        runnerPanel.add(rcerPanel);

        // Add to frame - Main panel (holds runner/rcer panels via the reset method below)
        getContentPane().add(defaultPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.SOUTH);

        // Frame properties
        setSize(300, 500);
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


    private class RadioButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals(rcer))
            {
                // Rcer
                isRunner = false;
                reset(rcerPanel);
            }
            else
            {
                // Runner
                isRunner = true;
                reset(runnerPanel);
            }
        }

        private void reset(JPanel panel)
        {
            mainPanel.removeAll();
            mainPanel.add(panel);
            mainPanel.repaint();
            mainPanel.revalidate();
        }
    }



    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (!handleAltar((Runes)runeCombobox.getSelectedItem()))
        {
            System.out.println("actionPerformed: Failed to gui.");
            return;
        }

        if (isRunner)
        {
            Vars.muleName = GString.convertToTribotString(muleNameRunner.getText());
            Vars.rcerName = GString.convertToTribotString(rcerName.getText());
        }
        else
        {
            Vars.isRunnerComingInAltar = trueButton.isSelected();
            Vars.muleName = GString.convertToTribotString(muleName.getText());
            Vars.isRcerBanking = trueButton2.isSelected();
        }

        printGuiVars();
        Vars.shouldExecute = true;
        this.dispose();
    }

    private boolean handleAltar(Runes rune)
    {
        switch (rune)
        {
            case FIRE:
                Vars.rune = new Rune(Runes.FIRE);
                Vars.altar = new FireAltar(Runes.FIRE);
                Vars.runner = isRunner ? new FireRunner() : null;
                Vars.rcer = isRunner ? null : new FireRcer();
                System.out.println("FIRE RUNE");
                return true;

            case WATER:
                return true;

            case AIR:
                Vars.rune = new Rune(Runes.AIR);
                Vars.altar = new AirAltar(Runes.AIR);
                Vars.runner = isRunner ? new AirRunner() : null;
                Vars.rcer = isRunner ? null : new AirRcer();
                System.out.println("AIR RUNE");
                return true;

            case EARTH:
                return true;

            case MIND:
                return true;

            case BODY:
                return true;

            case DEATH:
                return true;

            case NATURE:
                Vars.rune = new Rune(Runes.NATURE);
                Vars.altar = new NatureAltar(Runes.NATURE);
                Vars.runner = isRunner ? new NatureRunner() : null;
                Vars.rcer = isRunner ? null : new NatureRcer();
                System.out.println("NATURE RUNE");
                return true;

            case CHAOS:
                return true;

            case LAW:
                Vars.rune = new Rune(Runes.LAW);
                Vars.altar = new LawAltar(Runes.LAW);
                Vars.runner = isRunner ? new LawRunner() : null;
                Vars.rcer = isRunner ? null : new LawRcer();
                System.out.println("LAW RUNE");
                return true;

            case COSMIC:
                return true;

            case BLOOD:
                return true;

            case SOUL:
                return true;

            case WRATH:
                return true;
        }

        return false;
    }

    private void printGuiVars()
    {
        System.out.println("PRINT_GUI_VARS___________________________________________________________________________");
        System.out.println("Mule name: " + Vars.muleName);
        System.out.println("Altar: " + Vars.altar.getAltarName());
        System.out.println("Rune: " + Vars.rune.getName());

        if (isRunner)
        {
            System.out.println("RCer name: " + Vars.rcerName);
        }
        else
        {
            System.out.println("Is coming into altar: " + Vars.isRunnerComingInAltar);
            System.out.println("Is Banking: " + Vars.isRcerBanking);
        }
    }
}
