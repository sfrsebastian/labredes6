package gp.e3.autheo.authentication.infrastructure.exceptions;

public class ExceptionFactory {

    public static IException getTechnicalException(Exception exception) {

        return new TechnicalException(exception.getMessage());
    }

    public static IException getBusinessException(Exception exception) {

        return new BusinessException(exception.getMessage());
    }
}