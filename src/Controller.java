import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The controller class, which initializes the GUI, sets ButtonListener and executes logic
 * related to the testing of a .class file using a TestRunner.
 *
 * @author Timmy Eklund
 * @version 13 dec 2019
 */
@SuppressWarnings("WeakerAccess")
public class Controller
{
    private Gui gui;

    /**
     * Creates the GUI on the EDT.
     */
    public void createGUI()
    {
        SwingUtilities.invokeLater(() -> {
            gui = new Gui();
            setButtonListeners();
            gui.show();
        });
    }

    /**
     * Maps the Buttons' ActionListeners in the GUI to a corresponding method in Controller.
     */
    private void setButtonListeners()
    {
        gui.setRunButtonListener(e -> runButton());
        gui.setClearButtonListener(e -> clearButton());
    }

    /**
     * The method which is called when the Run-button in the GUI is pressed.
     * Creates a SwingWorker and runs the testing of a .class file using a TestRunner.
     */
    private void runButton(){
        String inputField = gui.getInputFileField();
        SwingWorker sw = new SwingWorker<String, String>()
        {

            @Override
            protected String doInBackground()
            {
                TestRunner tr = new TestRunner("file://" + Start.path + "\\", inputField);
                publish(tr.getOutput());
                if(tr.isReady())
                {
                    return tr.run();
                }
                else
                {
                    return "---------- END ----------\n";
                }
            }

            @Override
            protected void process(List<String> chunks)
            {
                String str = chunks.get(chunks.size()-1);
                Controller.this.gui.printToLog(str);
            }

            @Override
            protected void done()
            {
                try
                {
                    String str = get();
                    Controller.this.gui.printToLog(str);
                }
                catch (InterruptedException | ExecutionException e)
                {
                    Controller.this.gui.printToLog(e.toString());
                }
            }
        };

        sw.execute();
    }

    /**
     * The method which is called when the Clear-button in the GUI is pressed.
     * Asks the EDT to use the GUI method clearText() which empties the body of the log.
     */
    private void clearButton() {
        SwingUtilities.invokeLater(() -> gui.clearText());
    }
}
