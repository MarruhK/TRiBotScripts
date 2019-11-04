package scripts.dmmblastfurnace.gui;

import scripts.dmmblastfurnace.data.Vars;
import scripts.dmmblastfurnace.nodes.PumpPipes;
import scripts.dmmblastfurnace.nodes.RefuelCoke;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener
{
    String[] tasks = {"", "Pumper", "Refueler"};

    JButton button;
    JComboBox comboBox;

    public GUI()
    {
        // Components
        comboBox = new JComboBox(tasks);
        button = new JButton("Start");
        button.addActionListener(this);

        // Add to frame
        this.add(comboBox, BorderLayout.NORTH);
        this.add(button, BorderLayout.SOUTH);

        // Frame properties
        this.setSize(new Dimension(250, 408));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);

        // Handle X close
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Vars.safeCloseGUI = true;
                e.getWindow().dispose();
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String selectedTask = comboBox.getSelectedItem().toString();
        System.out.println(selectedTask);

        if (selectedTask.equals(""))
        {
            // select task
            JOptionPane.showMessageDialog(this,
                    "Please select a valid task.",
                    "Invalid selection!",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        System.out.println("TEST");
        if (selectedTask.equals("Pumper"))
        {
            Vars.task = new PumpPipes();
        }
        else
        {
            Vars.task = new RefuelCoke();
        }

        Vars.shouldExecute = true;
        this.dispose();
    }
}
