package scripts.gengarlibrary.fairyring;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.colour.Tolerance;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.dax_api.walker_engine.interaction_handling.AccurateMouse;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.InterfaceEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.gengarlibrary.GBooleanSuppliers;

import java.awt.*;

import static java.lang.Thread.sleep;
import static org.tribot.api.Screen.coloursMatch;

public class FairyRingHandler
{
    private static final int TELEPORT_ANIMATION = 3265;

    // Interfaces
    private static final int master = 398;
    private static final int childRotationOne = 3;
    private static final int childRotationTwo = 4;
    private static final int childRotationThree = 5;
    private static final int leftChildClockwise = 19;
    private static final int midChildClockwise = 21;
    private static final int rightChildClockwise = 23;

    // Rotation values
    private static final int restingRotation = 0;
    private static final int firstRotation = 1536;
    private static final int secondRotation = 1024;
    private static final int thirdRotation = 512;

    // Arrays
    private static final int[] rotations = {restingRotation, firstRotation, secondRotation, thirdRotation};
    private static final char[] validLeftChars = {'A', 'D', 'C', 'B'};
    private static final char[] validMidChars = {'I', 'L', 'K', 'J'};
    private static final char[] validRightChars = {'P', 'S', 'R', 'Q'};

    private static RSTile initialTile;

    public static boolean useFairyRing(String combination)
    {
        if (setFairyRingCombination(combination))
        {
            if (!Interfaces.isInterfaceValid(master))
            {
                // used previous combo
                return true;
            }

            RSInterface confirm = Entities.find(InterfaceEntity::new).actionEquals("Confirm").getFirstResult();

            if (confirm == null)
            {
                System.out.println("setFairyRingCombination: Unable to find the confirm button, closing interface");

                RSInterface close = Entities.find(InterfaceEntity::new).actionEquals("Close").getFirstResult();

                if (close == null)
                {
                    System.out.println("setFairyRingCombination: Unable to find close button?");
                    return false;
                }

                close.click();
                return false;
            }

            confirm.click();

            if (!waitToFInishTeleporting())
            {
                System.out.println("setFairyRingCombination: Failed to teleport?");
                return false;
            }

            return true;
        }

        return false;
    }

    public static boolean setFairyRingCombination(String combination)
    {
        if (combination.length() != 3)
        {
            System.out.println("SetFairyRingCombination: Invalid combination length");
            return false;
        }

        // Rule out invalid character
        combination.toUpperCase();

        char letter1 = combination.charAt(0);
        char letter2 = combination.charAt(1);
        char letter3 = combination.charAt(2);

        if (!areValidCharacters(letter1, letter2, letter3))
        {
            System.out.println("setFairyRingCombination: Invalid characters entered.");
            return false;
        }

        // Open interface if it is not open
        if (!Interfaces.isInterfaceValid(master))
        {
            RSObject fairyRing = Entities.find(ObjectEntity::new).nameEquals("Fairy ring").getFirstResult();

            if (fairyRing == null)
            {
                System.out.println("setFairyRingCombination: No fairy rings are present.");
                return false;
            }

            initialTile = Player.getPosition();
            String previousCombination = getFairyRingCombination();
            String action = "Last-destination (" + previousCombination + ")";

            // Tele to last destination
            if (previousCombination.equals(combination))
            {
                if (AccurateMouse.click(fairyRing, action))
                {
                    waitToFInishTeleporting();
                    return true;
                }

                System.out.println("setFairyRingCombination: Failed to click previous fairy ring.");
                return false;
            }

            // Open the interface
            if (!AccurateMouse.click(fairyRing, "Configure") || !Timing.waitCondition(GBooleanSuppliers.isInterfaceValid(master), General.random(4800, 5340)))
            {
                System.out.println("setFairyRingCombination: Failed to open fairy ring interface.");
                return false;
            }
        }

        setCombinationLetter(letter1, leftChildClockwise);
        setCombinationLetter(letter2, midChildClockwise);
        setCombinationLetter(letter3, rightChildClockwise);

        if (!getFairyRingCombination().equals(combination))
        {
            System.out.println("setFairyRingCombination: Failed to set fairy ring combination, resetting");
            setFairyRingCombination(combination);
        }

        return true;
    }

    private static boolean waitToFInishTeleporting()
    {
        return  Timing.waitCondition(GBooleanSuppliers.waitForAnimation(TELEPORT_ANIMATION), General.random(1850, 2000)) &&
                Timing.waitCondition(GBooleanSuppliers.waitForAnimation(-1), General.random(5050, 5090))                 &&
                Timing.waitCondition(()->
                {
                    General.sleep(250);
                    return Player.getPosition().distanceTo(initialTile) > 30;
                }, General.random(1850, 2100));
    }

    private static boolean areValidCharacters(char one, char two, char three)
    {
        return isValidChar(one, validLeftChars) && isValidChar(two, validMidChars) && isValidChar(three, validRightChars);
    }

    private static boolean isValidChar(char givenChar, char[] validChars)
    {
        for (char character : validChars)
        {
            if (givenChar == character)
            {
                return true;
            }
        }

        return false;
    }

    private static void setCombinationLetter(char letter, int clockwiseIndex)
    {
        if (!Interfaces.isInterfaceValid(master))
        {
            System.out.println("setCombinationLetter: Fairy ring interface is not open.");
            return;
        }

        // case variables
        char[] characterCombination;
        int rotationChild;

        switch (clockwiseIndex)
        {
            case 19:
                characterCombination = validLeftChars;
                rotationChild = childRotationOne;
                break;
            case 21:
                characterCombination = validMidChars;
                rotationChild = childRotationTwo;
                break;
            case 23:
                characterCombination = validRightChars;
                rotationChild = childRotationThree;
                break;
            default:
                System.out.println("setCombinationLetter: Invalid lettrs, returning.");
                return;
        }

        // Find letter placement in the array.
        int currentRotation = Interfaces.get(master, rotationChild).getRotationZ();
        int desiredRotationIndex = getDesiredRotationIndex(characterCombination, letter);
        int currentRotationIndex = getCurrentRotationIndex(currentRotation);

        // Compare the two indexes and determine if you need to rotate clock, counter or nothing.
        int numberOfRotations;
        RSInterface rotationInterface;

        // TODO make it so if 3 rots required, do the other way (i.e. from clock to counter)
        if (currentRotationIndex < desiredRotationIndex)
        {
            // rotate clockwise the difference
            numberOfRotations = desiredRotationIndex - currentRotationIndex;
            rotationInterface = Interfaces.get(master, clockwiseIndex);
        }
        else
        {
            numberOfRotations = currentRotationIndex - desiredRotationIndex;
            rotationInterface = Interfaces.get(master, clockwiseIndex + 1);
        }

        // Rotate
        for (int i = 0; i < numberOfRotations; i++)
        {
            // CLick specified index
            rotationInterface.click();

            Timing.waitCondition(()->
            {
                General.sleep(125);
                return isRotationValueValid(rotationChild);
            }, General.random(1450, 1600));
        }
    }

    private static boolean isRotationValueValid(int rotationChild)
    {
        RSInterface rotationInterface = Interfaces.get(master, rotationChild);

        for (Integer rotation : rotations)
        {
            if (rotation == rotationInterface.getRotationZ())
            {
                return true;
            }
        }

        return false;
    }

    private static int getDesiredRotationIndex(char[] characterCombination, char letter)
    {
        int counter = 0;

        for (char character : characterCombination)
        {
            if (character == letter)
            {
                return counter;
            }

            counter++;
        }

        System.out.println("getDesiredRotationIndex: Error, unable to find character in given array");
        return -1;
    }

    private static int getCurrentRotationIndex(int currentRotation)
    {
        int counter = 0;

        for (int rotation : rotations)
        {
            if (rotation == currentRotation)
            {
                return counter;
            }

            counter++;
        }

        System.out.println("getCurrentRotationIndex: Error, unable to find rotation in given array");
        return -1;
    }

    /**
     * Returns a three-letter string whcih represents the fairy code combination. Please note that this should only be
     * called when the interface is not rotating (i.e. is at rest).
     *
     * @return combination
     */
    public static String getFairyRingCombination()
    {
        // Get rotation indexes and get the corresponding letters
        if (Interfaces.isInterfaceValid(master))
        {
            RSInterface masterInterface = Interfaces.get(master);

            int rotationIndexOne = getCurrentRotationIndex(masterInterface.getChild(childRotationOne).getRotationZ());
            int rotationIndexTwo = getCurrentRotationIndex(masterInterface.getChild(childRotationTwo).getRotationZ());
            int rotationIndexThree = getCurrentRotationIndex(masterInterface.getChild(childRotationThree).getRotationZ());

            if (rotationIndexOne == -1 || rotationIndexTwo == -1 || rotationIndexThree == -1)
            {
                System.out.println("getFairyRingCombination: The fairy ring is rotating thus unable to determine combination.");
                System.out.println("getFairyRingCombination: Please wait for it to stop rotating.");
                return "";
            }

            char charOne = validLeftChars[rotationIndexOne];
            char charTwo = validMidChars[rotationIndexTwo];
            char charThree = validRightChars[rotationIndexThree];

            return new String(new char[]{charOne, charTwo, charThree});
        }
        else
        {
            RSObject fairyRing = Entities.find(ObjectEntity::new).nameEquals("Fairy ring").getFirstResult();

            if (fairyRing == null)
            {
                System.out.println("getFairyRingCombination: No fairy rings are present.");
                return "";
            }

            String[] actions = fairyRing.getDefinition().getActions();
            String action = null;

            for (String s : actions)
            {
                if (s.startsWith("Last"))
                {
                    action = s;
                }
            }

            if (action == null)
            {
                System.out.println("getFairyRingCombination: Action is null some how ?.");
                return "";
            }

            return action.split("[()]")[1];
        }
    }
}
