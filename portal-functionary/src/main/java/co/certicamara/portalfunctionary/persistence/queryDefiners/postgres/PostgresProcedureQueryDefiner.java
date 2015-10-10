package co.certicamara.portalfunctionary.persistence.queryDefiners.postgres;

import co.certicamara.portalfunctionary.persistence.queryDefiners.interfaces.IProcedureQueryDefiner;

public class PostgresProcedureQueryDefiner implements IProcedureQueryDefiner {

    private final static String PROCEDURES_TABLE = "\"PROCEDURES\"";

    @Override
    public String createProcedure() {
        String createProcedureSQL =   "INSERT INTO " + PROCEDURES_TABLE  
                + "	(name,description, request_expiration) "
                + "VALUES (?,?,?)";

        return createProcedureSQL;

    }
}
