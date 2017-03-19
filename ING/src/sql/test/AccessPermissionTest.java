package sql.test;

import sql.exceptions.SQLLayerException;
import sql.services.SQLAccessPermissionService;

public class AccessPermissionTest {

	public static void main(String[] args) {
		try {
			System.out.println(SQLAccessPermissionService.hasPermission(46, "NL13370000000001"));
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

	}

}
