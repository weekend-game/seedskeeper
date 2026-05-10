package game.weekend.seedskeeper.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import game.weekend.seedskeeper.dictionaries.brands.BrandTables;
import game.weekend.seedskeeper.dictionaries.other.CategoryTables;
import game.weekend.seedskeeper.dictionaries.other.ColorTables;
import game.weekend.seedskeeper.dictionaries.other.KindTables;
import game.weekend.seedskeeper.dictionaries.other.QualityTables;

public class DB {

	private static DB instance;

	private static String url = "jdbc:derby:DB";

	private final static String user = "user";
	private final static String password = "user";

	private final Connection connection;

	public final BrandTables brand;
	public final CategoryTables category;
	public final ColorTables color;
	public final KindTables kind;
	public final QualityTables quality;

	public static DB getInstance() {
		if (instance == null) {
			instance = new DB();
		}
		return instance;
	}

	private DB() {
		Connection c = null;
		try {
			c = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("DB() - " + e);
		}
		connection = c;

		brand = new BrandTables();
		category = new CategoryTables();
		color = new ColorTables();
		kind = new KindTables();
		quality = new QualityTables();
	}

	public Connection getConnection() {
		return connection;
	}

	public static void setURL(String url) {
		DB.url = url;
	}

	public static String getUrl() {
		return url;
	}
}
