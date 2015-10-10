package gp.e3.autheo.authentication.infrastructure.exceptions;

import java.util.List;
import java.util.Optional;

import fj.data.Either;

public class ExceptionUtils {
	
	private ExceptionUtils(){
		//Private constructor
	}

    /**
     * Iterates over the list and return the first element for which the predicate (either.isLeft()) is true.
     * 
     * @param eitherList The list of Either elements.
     * @return the first element for which the predicate (either.isLeft()) is true.
     */
    public static Optional<Either<IException, ?>> getFirstLeftEither(List<Either<IException, ?>> eitherList) {

        Optional<Either<IException, ?>> optional = Optional.empty();

        for (int i = 0; i < eitherList.size() && (!optional.isPresent()); i++) {

            Either<IException, ?> either2 = eitherList.get(i);

            if ((either2 != null) && either2.isLeft()) {

                optional = Optional.of(either2);
            }
        }

        return optional;
    }

    public static IException getIExceptionFromEither(Optional<Either<IException, ?>> optionalEither) {

        IException exception = optionalEither.get().left().value();
        return exception;
    }

    public static IException getIExceptionFromEither(Either<IException, ?> leftEither) {

        IException exception = leftEither.left().value();
        return exception;
    }
}
