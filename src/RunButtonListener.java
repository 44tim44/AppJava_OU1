import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        //Thread t1 = new Thread(new TestRunner("file://" + Start.path + "\\", inputField.getText()));
        //t1.start();

        SwingWorker sw = new SwingWorker<String, String>()
        {

            @Override
            protected String doInBackground() throws Exception
            {
                TestRunner tr = new TestRunner("file://" + Start.path + "\\", inputField.getText());
                publish(tr.getOutput());
                if(tr.isReady())
                {
                    return tr.run();
                }
                else
                {
                    return "The test has been cancelled.";
                }
            }

            @Override
            protected void process(List<String> chunks)
            {
                // define what the event dispatch thread
                // will do with the intermediate results received
                // while the thread is executing
                String str = chunks.get(chunks.size()-1);
                Start.gui.printToLog(str);
            }

            @Override
            protected void done()
            {
                // this method is called when the background
                // thread finishes execution
                try
                {
                    String str = get();
                    Start.gui.printToLog(str);

                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
        };

        sw.execute();
    }

}