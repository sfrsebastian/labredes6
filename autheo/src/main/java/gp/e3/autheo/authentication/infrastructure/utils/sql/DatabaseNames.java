package gp.e3.autheo.authentication.infrastructure.utils.sql;

public enum DatabaseNames {
	
	MY_SQL("mysql"),
	POSTGRE_SQL("postgresql");
	
	private final String name;

	private DatabaseNames(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}