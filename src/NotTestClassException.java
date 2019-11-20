
/**
 * Exception to be thrown when a class to be tested is not an instance of the interface TestClass.
 *
 * @author Timmy Eklund
 * @version 12 nov 2019
 */

@SuppressWarnings("WeakerAccess")

public class NotTestClassException extends Exception
{
    public NotTestClassException() {}

    @SuppressWarnings("unused")
    public NotTestClassException(String message)
    {
        super(message);
    }
}