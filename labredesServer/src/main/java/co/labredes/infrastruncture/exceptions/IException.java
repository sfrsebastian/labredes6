package co.labredes.infrastruncture.exceptions;


/**
 * The common interface implemented by all the exceptions handled by the system.
 * 
 * @author LeanFactory
 */
public interface IException extends Comparable<IException> {

    public String getType();

    public String getErrorMessage();

}