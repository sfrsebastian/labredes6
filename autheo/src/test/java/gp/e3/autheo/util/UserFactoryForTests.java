package gp.e3.autheo.util;

import gp.e3.autheo.authentication.domain.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserFactoryForTests {
	
	public static String getDefaultTestSalt() {
		
		return "123";
	}
	
	public static User getNullUser() {
		
		return new User(null, null, null, null, null, null, null, null, false, null, null, null, null);
	}

	public static User getDefaultTestUser() {

		String name = "name";
		
		String documentType = "documentType";
		String documentNumber = "documentNumber";
		String email = "email";
		String telephoneNumber = "telephone";
		String address = "address";
		
		String username = "username";
		String password = "password";
		boolean apiClient = true;
		String organizationId = "organization";
		String roleId = "1";
		String systemRoleId = "2";
		String businessRoleId = "3";

		return new User(name, documentType, documentNumber, email, telephoneNumber, address, username, password, apiClient, organizationId, roleId, systemRoleId, businessRoleId);
	}

	public static User getDefaultTestUser(int userNumber) {

		String name = "name" + userNumber;
		
		String documentType = "documentType" + userNumber;
		String documentNumber = "documentNumber" + userNumber;
		String email = "email" + userNumber;
		String telephoneNumber = "telephone" + userNumber;
        String address = "address" + userNumber;
		
		String username = "username" + userNumber;
		String password = "password" + userNumber;
		boolean apiClient = true;
		String organizationId = "organization" + userNumber;
		String roleId = "1" + userNumber;
		String systemRoleId = "2" + userNumber;
		String businessRoleId = "3" + userNumber;

		return new User(name, documentType, documentNumber, email, telephoneNumber, address, username, password, apiClient, organizationId, roleId, systemRoleId, businessRoleId);
	}

	public static List<User> getUserList(int listSize) {

		List<User> userList = new ArrayList<User>();

		for (int i = 0; i < listSize; i++) {

			userList.add(getDefaultTestUser(i));
		}

		return userList;
	}
}