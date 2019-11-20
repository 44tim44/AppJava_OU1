import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ButtonListener which handles what happens when the "Clear" button in the GUI is pressed
 *
 * Clears the provided JTextArea when button is pressed.
 *
 * @author Timmy Eklund
 * @version 12 nov 2019
 */
@SuppressWarnings("WeakerAccess")
class ClearButtonListener implements ActionListener
{
    private final JTextArea textArea;

    /**
     * Constructor for the button listener.
     *
     * @param textArea The JTextArea containing the Log of the GUI where messages and results are displayed
     *                 to the user.
     */
    public ClearButtonListener(JTextArea textArea)
    {
        this.textArea = textArea;
    }

    /**
     * Clears the provided JTextArea.
     *
     * @param event The ActionEvent performed
     */
    public void actionPerformed(ActionEvent event)
    {
        textArea.setText("");
    }


}