package co.certicamara.portalfunctionary.infrastructure.constants;

import java.util.ArrayList;
import java.util.List;

public class RequestQueryParamsNames {

    public final static String USER_NAME = "userName";
    public final static String USER_DOCUMENT_TYPE = "userDocumentType";
    public final static String USER_DOCUMENT_NUMBER = "userDocumentNumber";
    public final static String REQUEST_CASE_ID_LIST = "caseIdList";

    @SuppressWarnings("serial")
    public final static List<String> REQUEST_QUERY_PARAMS_LIST = new ArrayList<String>() {
        {
            add(USER_NAME);
            add(USER_DOCUMENT_TYPE);
            add(USER_DOCUMENT_NUMBER);
        }};


}
