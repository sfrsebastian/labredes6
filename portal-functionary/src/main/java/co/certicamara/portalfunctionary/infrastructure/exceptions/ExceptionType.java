package co.certicamara.portalfunctionary.infrastructure.exceptions;

/**
 * This enum lists the types of exceptions used by the application.
 * 
 * @author LeanFactory
 */
public enum ExceptionType {

    TECHNICAL("technical"),
    BUSINESS("business");

    private final String name;

    private ExceptionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}