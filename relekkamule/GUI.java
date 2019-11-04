package scripts.relekkamule;

import org.tribot.api.General;
import scripts.relekkamule.data.Vars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame
{
    private JTextField textField;
    private JTextArea textArea;
    private JButton addButton;
    private JButton beginButton;

    private List<String> fishers = new ArrayList<String>();

    public GUI()
    {
        // Initialize components
        JLabel label1 = new JLabel("Enter Fisher's Name: ");
        textField = new JTextField(10);
        textField.setEditable(true);
        textArea = new JTextArea(10, 10);

        addButton = new JButton("Add fisher");
        addButton.addActionListener(new muleAddingListener());
        beginButton = new JButton("Begin");
        beginButton.addActionListener(new beginListerner());

        // Initialize panels
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel mainPanel = new JPanel();

        // Add to panels
        panel1.add(label1);
        panel1.add(textField);
        panel2.add(addButton);
        panel3.add(textArea);

        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);

        // Add panel to frame
        this.setTitle("The Gengar's DMM Relekka mule");
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(beginButton, BorderLayout.SOUTH);

        // Frame properties
        this.setSize(new Dimension(159, 408));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // add some vars bullshit here
                Vars.fishers = new String[0];
                Vars.fishers[0] = "Get out of loop";

                e.getWindow().dispose();
            }
        });
        this.setVisible(true);
    }

    class muleAddingListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.print("1");
            String text = textField.getText();

            if (!text.equals(""))
            {
                fishers.add(text);
                textField.setText("");

                textArea.append(text);
                textArea.append("\n");
            }
            else
            {
                System.out.print("Empty fisher Text box dude.");
            }
        }
    }

    class beginListerner implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.out.print("2");
            // Initializes Var.fisher, giving it the values.
            Vars.fishers = new String[GUI.this.fishers.size()];
            Vars.shouldExecute = true;

            GUI.this.fishers.toArray(Vars.fishers);
            GUI.this.dispose();
        }
    }
}
