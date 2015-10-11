package gp.e3.autheo;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.OrganizationBusiness;
import gp.e3.autheo.authentication.domain.business.PasswordTokenBusiness;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.domain.business.UserBusiness;
import gp.e3.autheo.authentication.infrastructure.config.RedisConfig;
import gp.e3.autheo.authentication.infrastructure.config.ServiceClients;
import gp.e3.autheo.authentication.infrastructure.config.SqlDbConfig;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.healthchecks.RedisHealthCheck;
import gp.e3.autheo.authentication.infrastructure.healthchecks.SQLHealthCheck;
import gp.e3.autheo.authentication.infrastructure.utils.sql.SqlUtils;
import gp.e3.autheo.authentication.persistence.daos.BusinessRolesDAO;
import gp.e3.autheo.authentication.persistence.daos.OrganizationDAO;
import gp.e3.autheo.authentication.persistence.daos.PasswordTokenDAO;
import gp.e3.autheo.authentication.persistence.daos.SystemBusinessRolesDAO;
import gp.e3.autheo.authentication.persistence.daos.TokenCacheDAO;
import gp.e3.autheo.authentication.persistence.daos.TokenDAO;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;
import gp.e3.autheo.authentication.persistence.factories.businessRoles.BusinessRolesSQLFactorySingleton;
import gp.e3.autheo.authentication.persistence.factories.businessRoles.IBusinessRolesSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.organization.IOrganizationSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.organization.OrganizationSQLFactorySingleton;
import gp.e3.autheo.authentication.persistence.factories.passwordtoken.IPasswordTokenSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.passwordtoken.PasswordTokenSQLFactorySingleton;
import gp.e3.autheo.authentication.persistence.factories.systemBusinessRoles.ISystemBusinessRolesSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.systemBusinessRoles.SystemBusinessRolesSQLFactorySingleton;
import gp.e3.autheo.authentication.persistence.factories.token.ITokenSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.token.TokenSQLFactorySingleton;
import gp.e3.autheo.authentication.persistence.factories.user.IUserSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.user.UserSQLFactorySingleton;
import gp.e3.autheo.authentication.service.resources.OrganizationResource;
import gp.e3.autheo.authentication.service.resources.TokenResource;
import gp.e3.autheo.authentication.service.resources.UserResource;
import gp.e3.autheo.authorization.domain.business.PermissionBusiness;
import gp.e3.autheo.authorization.domain.business.RoleBusiness;
import gp.e3.autheo.authorization.domain.business.TicketBusiness;
import gp.e3.autheo.authorization.filter.InternalRequestFilter;
import gp.e3.autheo.authorization.persistence.daos.PermissionDAO;
import gp.e3.autheo.authorization.persistence.daos.RoleDAO;
import gp.e3.autheo.authorization.persistence.factories.permission.IPermissionSQLFactory;
import gp.e3.autheo.authorization.persistence.factories.permission.PermissionSQLFactorySingleton;
import gp.e3.autheo.authorization.persistence.factories.role.IRoleSQLFactory;
import gp.e3.autheo.authorization.persistence.factories.role.RoleSQLFactorySingleton;
import gp.e3.autheo.authorization.providers.InternalTokenDTOProvider;
import gp.e3.autheo.authorization.service.resources.PermissionResource;
import gp.e3.autheo.authorization.service.resources.RoleResource;
import gp.e3.autheo.authorization.service.resources.TicketResource;
import gp.e3.autheo.infrastructure.cache.SystemSettingsCache;
import gp.e3.autheo.infrastructure.clients.rs.CustomerSettingsClient;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.apache.commons.dbcp2.BasicDataSource;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Autheo main class
 */
public class Autheo extends Application<AutheoConfig> {

	@Override
	public void initialize(Bootstrap<AutheoConfig> bootstrap) {

		bootstrap.addBundle(new Java8Bundle());
	}

	/**
	 * Method to allow Jetty handle CORS requests.
	 * 
	 * @param environment, the environment of the application.
	 */
	private void initializeCORSSettings(Environment environment) {

		// Jetty CORS support, see: http://jitterted.com/tidbits/2014/09/12/cors-for-dropwizard-0-7-x/
		Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,HEAD,GET,POST,PUT,DELETE,PATCH");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Content-Length,Accept,Origin,Authorization");
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}
	
	private BasicDataSource getInitializedDataSource(SqlDbConfig sqlDbConfig) {

		BasicDataSource basicDataSource = new BasicDataSource();

		basicDataSource.setDriverClassName(sqlDbConfig.getDriverClass());
		basicDataSource.setUrl(sqlDbConfig.getUrl());
		basicDataSource.setUsername(sqlDbConfig.getUsername());
		basicDataSource.setPassword(sqlDbConfig.getPassword());

		basicDataSource.setRemoveAbandonedTimeout(sqlDbConfig.getRemoveAbandonedTimeoutInSeconds());
		basicDataSource.setRemoveAbandonedOnBorrow(sqlDbConfig.isAbleToRemoveAbandonedConnections());
		basicDataSource.setRemoveAbandonedOnMaintenance(sqlDbConfig.isAbleToRemoveAbandonedConnections());

		// int maxValue = 100;
		// basicDataSource.setMaxIdle(maxValue);
		// basicDataSource.setMaxTotal(maxValue);

		return basicDataSource;
	}

	private String getDBName(Connection dbConnection) throws SQLException {

		return dbConnection.getMetaData().getDatabaseProductName();
	}

	private void initializeTablesIfNeeded(Connection dbConnection, ITokenSQLFactory tokenSQLFactory, IUserSQLFactory userSQLFactory, 
			IPermissionSQLFactory permissionSQLFactory, IRoleSQLFactory roleSQLFactory, IOrganizationSQLFactory organizationSQLFactory) {

	    OrganizationDAO organizationDAO = new OrganizationDAO(organizationSQLFactory);
        organizationDAO.createOrganizationssTableIfNotExists(dbConnection); 
        
		TokenDAO tokenDAO = new TokenDAO(tokenSQLFactory);
        tokenDAO.createTokensTableIfNotExists(dbConnection);

        PermissionDAO permissionDAO = new PermissionDAO(permissionSQLFactory);
        permissionDAO.createPermissionsTableIfNotExists(dbConnection);

        RoleDAO roleDAO = new RoleDAO(roleSQLFactory);
        roleDAO.createRolesTableIfNotExists(dbConnection);
        roleDAO.createRolesAndPermissionsTable(dbConnection);
        roleDAO.createBusinessRolesTable(dbConnection);
        roleDAO.createSystemBusinessRolesTable(dbConnection);
        UserDAO userDAO = new UserDAO(userSQLFactory);
        userDAO.createUsersTableIfNotExists(dbConnection);
        roleDAO.createRolesAndUsersTable(dbConnection);
        
        
        
	}

	private JedisPool getRedisPoolInstance(RedisConfig redisConfig) {

		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisConfig.getHost(), redisConfig.getPort(), 
				Protocol.DEFAULT_TIMEOUT, null, redisConfig.getDatabase());

		return jedisPool;
	}
	
	private TokenBusiness getTokenBusiness(BasicDataSource dataSource, ITokenSQLFactory tokenSQLFactory, JedisPool jedisPool) {
	    
	    TokenDAO tokenDAO = new TokenDAO(tokenSQLFactory);
        TokenCacheDAO tokenCacheDao = new TokenCacheDAO(jedisPool);
        TokenBusiness tokenBusiness = new TokenBusiness(dataSource, tokenDAO, tokenCacheDao);
        
        return tokenBusiness;
	    
	}

	private PermissionResource getPermissionResource(BasicDataSource dataSource, IPermissionSQLFactory permissionSQLFactory) {

		final PermissionDAO permissionDao = new PermissionDAO(permissionSQLFactory);
		final PermissionBusiness permissionBusiness = new PermissionBusiness(dataSource, permissionDao);
		PermissionResource permissionResource = new PermissionResource(permissionBusiness);

		return permissionResource;
	}

	private RoleResource getRoleResource(RoleBusiness roleBusiness) {

		return new RoleResource(roleBusiness);
	}

	private TokenResource getTokenResource(TokenBusiness tokenBusiness) {

		return new TokenResource(tokenBusiness);
	}

	private RoleBusiness getRoleBusiness(BasicDataSource dataSource, IPermissionSQLFactory permissionSQLFactory, IRoleSQLFactory roleSQLFactory, 
			JedisPool jedisPool) {

		PermissionDAO permissionDao = new PermissionDAO(permissionSQLFactory);
		RoleDAO roleDao = new RoleDAO(roleSQLFactory);
		PermissionBusiness permissionBusiness = new PermissionBusiness(dataSource, permissionDao);
		
		return new RoleBusiness(dataSource, jedisPool, roleDao, permissionBusiness);
	}

	private UserResource getUserResource(BasicDataSource dataSource, TokenBusiness tokenBusiness, RoleBusiness roleBusiness, IUserSQLFactory userSQLFactory, 
			IRoleSQLFactory roleSQLFactory, IPasswordTokenSQLFactory passwordTokenSQLFactory, JedisPool jedisPool, ServiceClients serviceClients) 
			throws ClassNotFoundException {
		
		final UserDAO userDAO = new UserDAO(userSQLFactory);
		final RoleDAO roleDao = new RoleDAO(roleSQLFactory);
		final SystemSettingsCache systemSettingsCache = new SystemSettingsCache(jedisPool);
		final CustomerSettingsClient customerSettingsClient = new CustomerSettingsClient(serviceClients.getCustomerSettings(), systemSettingsCache);
		final UserBusiness userBusiness = new UserBusiness(dataSource, userDAO, roleDao, customerSettingsClient);

		final PasswordTokenDAO passwordTokenDAO = new PasswordTokenDAO(passwordTokenSQLFactory);
        final PasswordTokenBusiness passwordTokenBusiness = new PasswordTokenBusiness(dataSource, passwordTokenDAO, userDAO);

		return new UserResource(userBusiness, roleBusiness, tokenBusiness, passwordTokenBusiness);
	}

	private TicketResource getTicketResource(TokenBusiness tokenBusiness, RoleBusiness roleBusiness) {

		TicketBusiness ticketBusiness = createTicketBusiness(tokenBusiness, roleBusiness);

		return new TicketResource(ticketBusiness);
	}
	
	private OrganizationResource getOrganizationResource(BasicDataSource dataSource, TokenBusiness tokenBusiness, IUserSQLFactory userSQLFactory,
            IRoleSQLFactory roleSQLFactory, IOrganizationSQLFactory organizationSQLFactory, IBusinessRolesSQLFactory businessRolesSQLFactory, ISystemBusinessRolesSQLFactory systemBusinessRolesSQLFactory) {

        OrganizationDAO organizationDAO = new OrganizationDAO(organizationSQLFactory);
        UserDAO userDao = new UserDAO(userSQLFactory);
        BusinessRolesDAO businessRolesDao = new BusinessRolesDAO(businessRolesSQLFactory);
        SystemBusinessRolesDAO systemBusinessRolesDAO = new SystemBusinessRolesDAO(systemBusinessRolesSQLFactory);
        
        OrganizationBusiness organizationBusiness = new OrganizationBusiness(organizationDAO, dataSource, userDao, businessRolesDao, systemBusinessRolesDAO);
	    
        return new OrganizationResource(tokenBusiness, organizationBusiness);
    }

    private TicketBusiness createTicketBusiness(TokenBusiness tokenBusiness, RoleBusiness roleBusiness) {
        
		TicketBusiness ticketBusiness = new TicketBusiness(tokenBusiness, roleBusiness);
		
        return ticketBusiness;
    }

	private void addSQLHealthCheck(Environment environment, BasicDataSource dataSource) {

		try (Connection dbConnection = dataSource.getConnection()) {

			SQLHealthCheck sqlHealthCheck = new SQLHealthCheck(dbConnection);
			environment.healthChecks().register(getDBName(dbConnection), sqlHealthCheck);

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	private void addRedisHealthCheck(Environment environment, Jedis redisClient) {

		RedisHealthCheck redisHealthCheck = new RedisHealthCheck(redisClient);
		environment.healthChecks().register("redis", redisHealthCheck);
	}
	
    /**
     * Authentication filter
     * @param environment
     * @param tokenBusiness 
     * @param roleBusiness 
     */
    private void addAutheoFilterAndProvider(Environment environment, TokenBusiness tokenBusiness, 
            RoleBusiness roleBusiness) {

        String urlPattern = "/*";
        
        TicketBusiness ticketBusiness = createTicketBusiness(tokenBusiness, roleBusiness);

        environment.servlets().addFilter("autheoFilter", new InternalRequestFilter(ticketBusiness)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, urlPattern);
        environment.jersey().register(InternalTokenDTOProvider.class);
    }
    
    private void initializeRolesInCache(RoleBusiness roleBusiness) {
        
        Either<IException, List<String>> allRolesNamesEither = roleBusiness.getAllRolesNames();
        
        if(allRolesNamesEither.isRight()) {
            
            List<String> allRolesNames = allRolesNamesEither.right().value();
            
            for (String roleName : allRolesNames) {
                
                roleBusiness.addRolePermissionsToRedis(roleName);
                
            }
            
        }
        
    }

	@Override
	public void run(AutheoConfig autheoConfig, Environment environment) throws Exception {

		// Jetty CORS support.
		initializeCORSSettings(environment);

		// Get Configurations.
		SqlDbConfig sqlDbConfig = autheoConfig.getSqlDbConfig();
		RedisConfig redisConfig = autheoConfig.getRedisConfig();
		ServiceClients serviceClients = new ServiceClients();
		// Initialize data source.
		BasicDataSource dataSource = getInitializedDataSource(sqlDbConfig);
		
		// Initialize SQL factories.
		Connection dbConnection = dataSource.getConnection();
		String dbName = getDBName(dbConnection);
		ITokenSQLFactory tokenSQLFactory = TokenSQLFactorySingleton.INSTANCE.getTokenSQLFactory(dbName);  
		IUserSQLFactory userSQLFactory = UserSQLFactorySingleton.INSTANCE.getUserSQLFactory(dbName);
		IBusinessRolesSQLFactory businessRolesSQLFactory = BusinessRolesSQLFactorySingleton.INSTANCE.getBusinessRolesSQLFactory(dbName);
		IPermissionSQLFactory permissionSQLFactory = PermissionSQLFactorySingleton.INSTANCE.getPermissionSQLFactory(dbName);
		IRoleSQLFactory roleSQLFactory = RoleSQLFactorySingleton.INSTANCE.getRoleSQLFactory(dbName);
		IOrganizationSQLFactory organizationSQLFactory = OrganizationSQLFactorySingleton.INSTANCE.getOrganizationSQLFactory(dbName);
		IPasswordTokenSQLFactory passwordTokenSQLFactory = PasswordTokenSQLFactorySingleton.INSTANCE.getTokenSQLFactory(dbName);
		ISystemBusinessRolesSQLFactory systemBusinessRolesSQLFactory = SystemBusinessRolesSQLFactorySingleton.INSTANCE.getOrganizationSQLFactory(dbName);
		initializeTablesIfNeeded(dbConnection, tokenSQLFactory, userSQLFactory, permissionSQLFactory, roleSQLFactory, organizationSQLFactory);
		SqlUtils.closeDbConnection(dbConnection);

		// Initialize Redis.
		JedisPool jedisPool = getRedisPoolInstance(redisConfig);
		
		//Initializes the token business
		TokenBusiness tokenBusiness = getTokenBusiness(dataSource, tokenSQLFactory, jedisPool);
		
		//Initializes the role business
		RoleBusiness roleBusiness = getRoleBusiness(dataSource, permissionSQLFactory, roleSQLFactory, jedisPool);
		initializeRolesInCache(roleBusiness);
		
		//Initializes the internal authorization filter
		addAutheoFilterAndProvider(environment, tokenBusiness, roleBusiness);

		// Add health checks.
		addSQLHealthCheck(environment, dataSource);
		addRedisHealthCheck(environment, jedisPool.getResource());

		// Add Permission resource to the environment.
		PermissionResource permissionResource = getPermissionResource(dataSource, permissionSQLFactory);
		environment.jersey().register(permissionResource);

		// Add Role resource to the environment.
		RoleResource roleResource = getRoleResource(roleBusiness);
		environment.jersey().register(roleResource);

		// Add token resource to the environment.
		TokenResource tokenResource = getTokenResource(tokenBusiness);
		environment.jersey().register(tokenResource);

		// Add user resource to the environment.
		UserResource userResource = getUserResource(dataSource, tokenBusiness, roleBusiness, userSQLFactory, roleSQLFactory, passwordTokenSQLFactory, jedisPool, serviceClients);
		environment.jersey().register(userResource);

		// Add ticket resource to the environment.
		TicketResource ticketResource = getTicketResource(tokenBusiness, roleBusiness);
		environment.jersey().register(ticketResource);
		
		// Add Organization resource to the environment.
        OrganizationResource organizationResource = getOrganizationResource(dataSource, tokenBusiness, userSQLFactory, roleSQLFactory, organizationSQLFactory, 
                                businessRolesSQLFactory, systemBusinessRolesSQLFactory);
        environment.jersey().register(organizationResource);
	}

	public static void main( String[] args ) throws Exception {

		Autheo autheo = new Autheo();
		autheo.run(args);
	}
}