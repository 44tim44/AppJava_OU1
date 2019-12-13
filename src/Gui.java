import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * The GUI for the small program MyUnitTester
 *
 * @author Timmy Eklund
 * @version 13 dec 2019
 */
@SuppressWarnings("WeakerAccess")
public class Gui
{
    private JFrame frame;
    private JTextArea logArea;
    private JTextField inputFileField;
    private JButton runButton;
    private JButton clearButton;

    /**
     * Constructor for the GUI
     */
    @SuppressWarnings("WeakerAccess")
    public Gui()
    {
        frame = new JFrame("My Unit Tester");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Build panels
        JPanel midPanel = createMidPanel(); //Must be first to initialize Log-TextArea
        JPanel topPanel = createTopPanel();
        JPanel bottomPanel = createBotPanel();

        //Add panels to the frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(midPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setPreferredSize(new Dimension(480, 360));
        frame.pack();
    }

    /**
     * Creates the JPanel to be displayed at the top of the GUI window.
     *
     * @return The finished JPanel
     */
    private JPanel createTopPanel()
    {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("File"));
        topPanel.setLayout(new BorderLayout());

        inputFileField = new JTextField("Enter a filename...");
        inputFileField.addFocusListener(new FocusAdapter()
        {
            public void focusGained(FocusEvent e)
            {
                JTextField input = (JTextField)e.getComponent();
                input.selectAll();
            }
        });
        topPanel.add(inputFileField, BorderLayout.CENTER);

        this.runButton = new JButton("Run File");
        topPanel.add(runButton,BorderLayout.EAST);
        return topPanel;
    }

    /**
     * Creates the JPanel to be displayed in the middle of the GUI window.
     *
     * @return The finished JPanel
     */
    private JPanel createMidPanel()
    {
        JPanel midPanel = new JPanel();
        midPanel.setBorder(BorderFactory.createTitledBorder("Log"));
        midPanel.setLayout(new BorderLayout());

        logArea = new JTextArea("");
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane (logArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        midPanel.add(scroll, BorderLayout.CENTER);

        return midPanel;
    }

    /**
     * Creates the JPanel to be displayed at the bottom of the GUI window.
     *
     * @return The finished JPanel
     */
    private JPanel createBotPanel()
    {
        JPanel botPanel = new JPanel();
        botPanel.setBorder(BorderFactory.createTitledBorder("Buttons"));
        botPanel.setLayout(new BorderLayout());

        this.clearButton = new JButton("Clear Log");
        botPanel.add(clearButton,BorderLayout.EAST);

        return botPanel;
    }

    /**
     * Sets the GUI visible
     */
    @SuppressWarnings("WeakerAccess")
    public void show()
    {
        frame.setVisible(true);
    }

    /**
     * Prints the provided string to the Log JTextArea.
     * @param str The message to be printed.
     */
    public void printToLog(String str)
    {
        logArea.append(str + "\n");
    }

    /**
     * Clears the Log JTextArea
     */
    public void clearText()
    {
        logArea.setText("");
    }

    /**
     * Makes the content of the JTextField inputFileField publicly accessible
     * @return Returns the text in the inputFileField as a string
     */
    public String getInputFileField() {
        return inputFileField.getText();
    }

    /**
     * Adds an ActionListener to the Run-button
     * @param actionListener The ActionListener which will be called when the button is pressed
     */
    public void setRunButtonListener(ActionListener actionListener)
    {
        runButton.addActionListener(actionListener);
    }

    /**
     * Adds an ActionListener to the Clear-button
     * @param actionListener The ActionListener which will be called when the button is pressed
     */
    public void setClearButtonListener(ActionListener actionListener)
    {
        clearButton.addActionListener(actionListener);
    }


}
