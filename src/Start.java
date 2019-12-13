import javax.swing.*;
import java.io.File;

/**
 * The Start-class containing the main method of the program MyUnitTester.
 *
 * Public attribute path points to the path/URL the program is run from.
 *
 * @author Timmy Eklund
 * @version 13 dec 2019
 */
@SuppressWarnings("WeakerAccess")
public class Start
{
    private static File jarFile = new File(Start.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    public static String path = jarFile.getParent();

    /**
     * The main method which starts the program
     *
     * Sets the Look-and-Feel to the system default.
     *
     * Starts GUI.
     */
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e)
        {
            System.out.println("Look-and-Feel unsupported");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Class not found");
        }
        catch (InstantiationException e)
        {
            System.out.println("Instantiation error");
        }
        catch (IllegalAccessException e)
        {
            System.out.println("Illegal Access");
        }

        Controller controller = new Controller();
        controller.createGUI();
    }
}