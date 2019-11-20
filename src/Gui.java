import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * The GUI for the small program MyUnitTester
 *
 * @author Timmy Eklund
 * @version 12 nov 2019
 */
@SuppressWarnings("WeakerAccess")
public class Gui
{
    private JFrame frame;
    private JTextArea logArea;

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

        JTextField inputFileField = new JTextField("Enter a filename...");
        inputFileField.addFocusListener(new FocusAdapter()
        {
            public void focusGained(FocusEvent e)
            {
                JTextField input = (JTextField)e.getComponent();
                input.selectAll();
            }
        });
        topPanel.add(inputFileField, BorderLayout.CENTER);

        JButton runButton = new JButton("Run File");
        runButton.addActionListener(new RunButtonListener(inputFileField, logArea));
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
        midPanel.add(logArea, BorderLayout.CENTER);

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

        JButton clearButton = new JButton("Clear Log");
        clearButton.addActionListener(new ClearButtonListener(logArea));
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
    
    
}
