import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Controller
{
    //private File jarFile = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    //public String path = jarFile.getParent();
    private Gui gui;


    public void createGUI()
    {
        SwingUtilities.invokeLater(() -> {
            gui = new Gui();
            setListeners();
            gui.show();
        });
    }

    private void setListeners()
    {
        gui.setRunButtonListener(e -> runButton());
        gui.setClearButtonListener(e -> clearButton());
    }

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
                // define what the event dispatch thread
                // will do with the intermediate results received
                // while the thread is executing
                String str = chunks.get(chunks.size()-1);
                Controller.this.gui.printToLog(str);
            }

            @Override
            protected void done()
            {
                // this method is called when the background
                // thread finishes execution
                try
                {
                    String str = get();
                    Controller.this.gui.printToLog(str);

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

    private void clearButton() {
        SwingUtilities.invokeLater(() -> gui.clearText());
    }
}
