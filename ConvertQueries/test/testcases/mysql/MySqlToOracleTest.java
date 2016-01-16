package testcases.mysql;

import org.junit.Test;

import constants.DatabaseName;
import databases.MySQLDatabase;

public class MySqlToOracleTest {

	@Test
	public void test() {
		MySQLDatabase mysql = new MySQLDatabase();
		mysql.parse(DatabaseName.ORACLE, "");
	}
}
