import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TestRunner is a Multithreaded Class that loads and runs test methods inside a java .class file.
 *
 * The constructor takes an URL for the folder where the file is located and
 * the filename of the .class file to be tested (without .extension).
 *
 * @author Timmy Eklund
 * @version 12 nov 2019
 */
@SuppressWarnings("WeakerAccess")
public class TestRunner  implements Runnable
{
    private Class<?> testFile;

    public TestRunner(String folderURL, String filename) throws MalformedURLException, ClassNotFoundException, NotTestClassException
    {
        System.out.println(folderURL + filename);
        URL[] urls = new URL[]{ new URL(folderURL) };

        ClassLoader loader = new URLClassLoader(urls);

        testFile = loader.loadClass(filename);

        if(!TestClass.class.isAssignableFrom(testFile))
        {
            throw new NotTestClassException();
        }
        else
        {
            printToUI(filename + ".class loaded successfully.");
        }

    }

    /**
     * The run()-method  of a Runnable class.
     * Creates a List of Strings with the names of all the methods in the .class file and passes them to
     * the runTest method.
     */
    @Override
    public void run()
    {
        List methodsList = Arrays.asList(testFile.getMethods());
        List<String> methodNames = new ArrayList<>();

        for (Object method : methodsList)
        {
            methodNames.add(((Method)method).getName());
        }

        runTest(methodNames);

    }

    /**
     * runTest checks the .class file for methods beginning with "testXXXXX" and invokes them.
     * Whether the invocation was successful, or not, is then printed to the GUI.
     * If the .class file has methods named setUp and/or tearDown, these methods are run before and after
     * the "testXXXXX"-method is, respectively.
     *
     * @param methodNames A list of the names of all the methods in the .class file.
     */
    private void runTest(List<String> methodNames)
    {
        int counter = 0;
        int succeeded = 0;
        for (String methodName : methodNames)
        {
            if (methodName.startsWith("test"))
            {
                counter++;
                try
                {
                    Method testMethod = testFile.getMethod(methodName);
                    Object instance = testFile.newInstance();

                    if(methodNames.contains("setUp"))
                    {
                        runMethod(instance,"setUp");
                    }

                    if((Boolean) testMethod.invoke(instance))
                    {
                        printToUI("\u2713 Test " + methodName + " succeeded.");
                        succeeded++;
                    }
                    else
                    {
                        printToUI("\u274C Test " + methodName + " failed.");
                    }

                    if(methodNames.contains("tearDown"))
                    {
                        runMethod(instance,"tearDown");
                    }

                }
                catch (NoSuchMethodException | IllegalAccessException | InstantiationException e)
                {
                    printToUI("ERROR - Testing of " + methodName + " failed " + e);
                }
                catch (InvocationTargetException e)
                {
                    printToUI("\u274C Test " + methodName + " failed due to " + e.getCause());
                }
            }

        }
        printToUI(succeeded + " of " + counter + " tests succeeded.");
        printToUI("----------------");

    }



    /**
     * Invokes the method setUp in the .class file.
     *
     * @param instance The instance of the .class file being tested.
     * @param name The name of the method to be invoked
     */
    private void runMethod(Object instance, String name)
    {
        try
        {
            Method method = testFile.getMethod(name);

            method.invoke(instance);
        }
        catch (NoSuchMethodException | IllegalAccessException e)
        {
            printToUI("ERROR - " + name + " failed " + e);
        }
        catch (InvocationTargetException e)
        {
            printToUI("ERROR - " + name + " failed " + e.getCause());
        }
    }

    /**
     * Prints a message in the GUI.
     *
     * @param str The message to be printed in the GUI.
     */
    public void printToUI(String str)
    {
        SwingUtilities.invokeLater(() -> Start.gui.printToLog(str));
    }
}
