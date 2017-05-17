package sql.test;

import sql.actors.SQLAccessPermissionService;
import sql.exceptions.SQLLayerException;

public class AccessPermissionTest {

	public static void main(String[] args) {
		try {
			System.out.println(SQLAccessPermissionService.hasPermission(46, "NL13370000000001"));
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

	}

}
