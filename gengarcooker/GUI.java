package scripts.gengarcooker;

import org.tribot.api.General;
import scripts.gengarcooker.data.Constants;
import scripts.gengarcooker.data.Vars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener{
    JComboBox gameModeJCB;
    JComboBox locationJCB;
    JComboBox foodJCB;

    GUI(){
        // Makes GUI not look retarded. CBA making excep handling.
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        // Panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JPanel jcbPanel1 = new JPanel();
        JPanel jcbPanel2 = new JPanel();
        JPanel jcbPanel3 = new JPanel();

        // Make components
        JLabel gameModeLabel = new JLabel("Game Mode:    ");
        gameModeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gameModeJCB = new JComboBox(Constants.GAME_MODES);
        gameModeJCB.setPrototypeDisplayValue("-----------------------------------------");

        JLabel locationLabel = new JLabel("Location:     ");
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationJCB = new JComboBox(Constants.LOCATIONS);
        locationJCB.setPrototypeDisplayValue("-----------------------------------------");

        JLabel foodLabel = new JLabel("Food to cook: ");
        foodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        foodJCB = new JComboBox(Constants.COOKABLE_FOODS);
        foodJCB.setPrototypeDisplayValue("-----------------------------------------");

        JButton button = new JButton("Begin");
        button.addActionListener(this);

        // Add to panel
        addCompsToPanel(jcbPanel1, gameModeLabel, gameModeJCB);
        addCompsToPanel(jcbPanel2, locationLabel, locationJCB);
        addCompsToPanel(jcbPanel3, foodLabel, foodJCB);

        mainPanel.add(jcbPanel1);
        mainPanel.add(jcbPanel2);
        mainPanel.add(jcbPanel3);

        // Add to frame
        this.getContentPane().add(BorderLayout.CENTER, mainPanel);
        this.getContentPane().add(BorderLayout.SOUTH, button);
        this.setTitle("The Gengar's Cooker");

        // Frame properties
        this.setSize(new Dimension(313, 250));
        this.setMinimumSize(new Dimension(313, 250));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                // Ask for confirmation before terminating the program.
                int option = JOptionPane.showConfirmDialog(
                        GUI.this,
                        "Are you sure you want to close the application?",
                        "Close Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    GUI.this.dispose();
                    Vars.rawFoodID = -1;
                }
            }
        });
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Ensure an option is selected in all three JCB

        if (gameModeJCB.getSelectedIndex() == 0 || locationJCB.getSelectedIndex() == 0 || foodJCB.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(this,
                    "Please select a valid value in one(s) of the JComboBoxes.",
                    "Invalid input!",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (initializeVars()){
                // Close GUI
                System.out.println("GUI: Vars successfully initialized.");
                Vars.isInitialized = true;
            } else {
                System.out.println("GUI: Vars failed to initialize.");
            }
        }
    }

    private void addCompsToPanel(JPanel panel, JLabel label, JComboBox comboBox){
        panel.add(label);
        panel.add(comboBox);
    }
    public static final String[] GAME_MODES = {" ", "07", "DMM", "SDMM"};
    public static final String[] LOCATIONS = {" ", "Hosidius", "Varrock East"};

    private boolean initializeVars(){
        int indexOfFood = getIndexValue(Constants.COOKABLE_FOODS,foodJCB.getName());
        int indexOfGameMode = getIndexValue(Constants.GAME_MODES, gameModeJCB.getName());
        int indexOfLocation = getIndexValue(Constants.LOCATIONS, locationJCB.getName());
        System.out.println("GUI: indexOfFood = " + indexOfFood);
        System.out.println("GUI: indexOfGameMode = " + indexOfGameMode);
        System.out.println("GUI: indexOfLocation = " + indexOfLocation);

        if (indexOfFood != -1 && indexOfGameMode != -1 && indexOfLocation != -1){
            // Found valid index values
            Vars.rawFoodName = Constants.COOKABLE_FOODS[indexOfFood];
            Vars.rawFoodID = Constants.COOKABLE_FOODS_ID[indexOfFood];
            Vars.expOfFood = Constants.GAME_MODE_MULTIPLIER[indexOfGameMode] * Constants.COOKABLE_FOODS_EXP[indexOfFood];

            Vars.cookingSurfaceName = Constants.RANGE_NAMES[indexOfLocation];
            Vars.rangeID = Constants.RANGE_IDS[indexOfLocation];
            Vars.location = locationJCB.getName();
            return true;
        }

        System.out.println("GUI: Invalid index value.");
        return false;
    }

    // Gets the index value of the food selected. Returns -1 if not found.
    private int getIndexValue(String[] arrayOfString, String jcbText){
        int indexOfFood = 0;

        // Get index value of the food to simply input that index to determine other vars.
        for (String str: arrayOfString){
            if (str.equals(jcbText)){
                Vars.rawFoodName = foodJCB.getName();
                break;
            }
            indexOfFood++;
        }

        if (indexOfFood == 0){
            System.out.print("GUI: Initialization of Vars failed. Reason: unable to determine indexOfFood.");
            return -1;
        }

        return indexOfFood;
    }

    public void windowClosing(WindowEvent e){
        System.out.print("CLOSED X ON GUI.");
    }
}
