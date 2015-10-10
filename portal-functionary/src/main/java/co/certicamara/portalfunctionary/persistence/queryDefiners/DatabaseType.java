package co.certicamara.portalfunctionary.persistence.queryDefiners;

public enum DatabaseType {

    POSTGRES("PostgreSQL"), ORACLE("Oracle");


    private final String name;

    private DatabaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
