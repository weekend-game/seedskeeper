package game.weekend.seedskeeper.data.db;

import java.sql.Connection;

public class TablesOf {

	private boolean edited = false;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	protected DB getDB() {
		return DB.getInstance();
	}

	protected Connection getConnection() {
		return getDB().getConnection();
	}
}
