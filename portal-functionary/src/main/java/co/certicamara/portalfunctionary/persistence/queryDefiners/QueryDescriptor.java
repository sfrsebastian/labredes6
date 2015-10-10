package co.certicamara.portalfunctionary.persistence.queryDefiners;

public class QueryDescriptor {

    private final String databaseParamName;

    private final String databaseReturnName;

    private final String databaseType;

    private final String queryOperation;

    public QueryDescriptor(String databaseParamName, String databaseReturnName,
            String databaseType, String queryOperation) {
        super();
        this.databaseParamName = databaseParamName;
        this.databaseReturnName = databaseReturnName;
        this.databaseType = databaseType;
        this.queryOperation = queryOperation;
    }

    public String getDatabaseParamName() {
        return databaseParamName;
    }

    public String getDatabaseReturnName() {
        return databaseReturnName;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getQueryOperation() {
        return queryOperation;
    }
}
