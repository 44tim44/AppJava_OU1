import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * TestRunner is a Multithreaded Class that loads and runs test methods inside a java .class file.
 *
 * The constructor takes an URL for the folder where the file is located and
 * the filename of the .class file to be tested (without .extension).
 *
 * @author Timmy Eklund
 * @version 13 dec 2019
 */
@SuppressWarnings("WeakerAccess")
public class TestRunner
{
    private Class<?> testFile;
    private StringBuilder output = new StringBuilder();
    private boolean ready = true;

    /**
     * Constructor for the TestRunner
     * Validates the .class file which will be tested later.
     */
    public TestRunner(String folderURL, String filename)
    {
        try {
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
                output.append(filename).append(".class loaded successfully.\n");
            }
        }
        catch (MalformedURLException e)
        {
            output.append("An error occurred when loading ").append(filename).append(".class.\n").append(e).append("\n");
            ready = false;
        }
        catch (ClassNotFoundException e)
        {
            output.append(filename).append(".class does not exist at:\n").append(Start.path).append("\\").append(filename).append("\n");
            ready = false;
        }
        catch (NotTestClassException e)
        {
            output.append(filename).append(".class does not implement the interface TestClass.\n");
            ready = false;
        }

    }

    /**
     * getOutput makes the StringBuilder which contains text for the log publicly accessible
     *
     * @return Returns the StringBuilder output as a String
     */
    public String getOutput() {
        return output.toString();
    }

    /**
     * Checks if TestRunner is ready to perform tests, i.e. use the run() method.
     * If false, it is likely that an error occurred when loading the .class file in the constructor.
     *
     * @return Returns whether the TestRunner is ready to perform tests or not.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Creates a List of Strings with the names of all the methods in the .class file and passes them to
     * the runTest method.
     */
    public String run()
    {
        output = new StringBuilder();
        Method[] methodsList = testFile.getMethods();
        List<String> methodNames = new ArrayList<>();

        for (Object method : methodsList)
        {
            methodNames.add(((Method)method).getName());
        }

        runTest(methodNames);

        return output.toString();
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
        output.append("--------- START ---------\n");
        int counter = 0;
        int succeeded = 0;
        int failed = 0;
        int crashed = 0;
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
                        output.append("\u2713 Test ").append(methodName).append(" succeeded.\n");
                        succeeded++;
                    }
                    else
                    {
                        output.append("\u274C Test ").append(methodName).append(" failed.\n");
                        failed++;
                    }

                    if(methodNames.contains("tearDown"))
                    {
                        runMethod(instance,"tearDown");
                    }

                }
                catch (NoSuchMethodException | IllegalAccessException | InstantiationException e)
                {
                    output.append("ERROR - Testing of ").append(methodName).append(" failed ").append(e).append("\n");
                }
                catch (InvocationTargetException e)
                {
                    output.append("\u274C Test ").append(methodName).append(" failed due to ").append(e.getCause()).append("\n");
                    crashed++;
                }
            }
        }
        output.append("-------- RESULTS --------\n");
        output.append(succeeded).append(" of ").append(counter).append(" test(s) succeeded.\n");
        if(failed > 0) {
            output.append(failed).append(" test(s) failed.\n");
        }
        if(crashed > 0) {
            output.append(crashed).append(" test(s) failed due to internal exceptions.\n");
        }
        output.append("---------- END ----------\n");
    }

    /**
     * Invokes a method in the .class file.
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
            output.append("ERROR - ").append(name).append(" failed ").append(e).append("\n");
        }
        catch (InvocationTargetException e)
        {
            output.append("ERROR - ").append(name).append(" failed ").append(e.getCause()).append("\n");
        }
    }
}
