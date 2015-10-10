package gp.e3.autheo.authorization.infrastructure.validators;

import org.apache.commons.lang.StringUtils;


public class HttpVerbValidator {

	private static final String[] VALID_HTTP_VERBS = { "OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD"};

	public static boolean isValidHttpVerb(String httpVerb) {

		boolean found = false;

		if (!StringUtils.isBlank(httpVerb)) {

			for (int i = 0; i < VALID_HTTP_VERBS.length && !found; i++) {

				String validVerb = VALID_HTTP_VERBS[i];
				found = validVerb.equalsIgnoreCase(httpVerb);
			}
		}

		return found;
	}
}