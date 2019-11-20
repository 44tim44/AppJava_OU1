import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.*;

/**
 * ButtonListener which handles what happens when the "Run file" button in the GUI is pressed
 *
 * Creates a new Thread running the TestRunner-class which analyzes the .class file at the URL
 * and runs all test methods in the .class file and reports the results to the GUI.
 *
 * @author Timmy Eklund
 * @version 12 nov 2019
 */
class RunButtonListener implements ActionListener
{
    private final JTextField inputField;
    private final JTextArea outArea;

    /**
     * Constructor for the button listener.
     *
     * @param inputField    The JTextField containing the text input from the user which should be the
     *                      filename of the file to be tested.
     * @param outArea       The JTextArea containing the Log of the GUI where messages and results are displayed
     *                      to the user.
     */
    @SuppressWarnings("WeakerAccess")
    public RunButtonListener(JTextField inputField, JTextArea outArea)
    {
        this.inputField = inputField;
        this.outArea = outArea;
    }

    /**
     * Starts a new Thread running TestRunner.
     * Prints exceptions in the Log in the GUI.
     *
     * @param event The ActionEvent performed
     */
    public void actionPerformed(ActionEvent event)
    {
        try
        {
            Thread t1 = new Thread(new TestRunner("file://" + Start.path + "\\", inputField.getText()));
            t1.start();
        }
        catch (MalformedURLException e)
        {
            outArea.append("An error occurred when loading " + inputField.getText() + ".class.\n" + e + "\n");
        }
        catch (ClassNotFoundException e)
        {
            outArea.append(inputField.getText() + ".class does not exist at:\n" + Start.path + "\\" + inputField.getText() + "\n");
        }
        catch (NotTestClassException e)
        {
            outArea.append(inputField.getText() + ".class does not implement the interface TestClass.\n");
        }


    }

}