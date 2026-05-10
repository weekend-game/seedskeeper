package game.weekend.seedskeeper.dictionaries.other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import game.weekend.seedskeeper.data.db.TablesOf;
import game.weekend.seedskeeper.data.types.ComboItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KindTables extends TablesOf {

	public ObservableList<KindData> getListForTable() {
		ObservableList<KindData> list = FXCollections.observableArrayList();

		Connection c = getConnection();
		if (c == null)
			return list;

		try {
			if (getListForTable == null)
				getListForTable = getConnection().prepareStatement("SELECT id, name FROM Kinds");

			ResultSet rs = getListForTable.executeQuery();

			while (rs.next())
				list.add(new KindData(rs.getInt(1), rs.getString(2).trim()));
			rs.close();
		} catch (SQLException e) {
			System.out.println("KindTables.getListForTable() - " + e);
		}

		return list;
	}

	public ObservableList<ComboItem> getListForCombo() {
		ObservableList<ComboItem> list = FXCollections.observableArrayList();

		Connection c = getConnection();
		if (c == null)
			return list;

		try {
			if (getListForCombo == null)
				getListForCombo = getConnection().prepareStatement("SELECT id, name FROM Kinds ORDER BY numb");

			list.add(new ComboItem(0, "")); // Это соответсвует отсутствию указания качества в UI

			ResultSet rs = getListForCombo.executeQuery();

			while (rs.next())
				list.add(new ComboItem(rs.getInt(1), rs.getString(2)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("KindTables.getListForCombo() - " + e);
		}

		return list;
	}

	public void add(KindData kind) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (addKind == null)
				addKind = getConnection().prepareStatement("INSERT INTO Kinds (name) VALUES (?)",
						Statement.RETURN_GENERATED_KEYS);

			addKind.setString(1, kind.getName());
			addKind.executeUpdate();

			ResultSet rs = addKind.getGeneratedKeys();
			if (rs.next())
				kind.setId(rs.getInt(1));
			rs.close();
		} catch (SQLException e) {
			System.out.println("KindTables.add(KindData kind) - " + e);
		}
	}

	public void set(KindData kind) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (setKind == null)
				setKind = getConnection().prepareStatement("UPDATE Kinds SET name = ? WHERE id = ?");

			setKind.setString(1, kind.getName());
			setKind.setInt(2, kind.getId());
			setKind.executeUpdate();
		} catch (SQLException e) {
			System.out.println("KindTables.set(KindData kind) - " + e);
		}
	}

	public boolean canRemove(KindData kind) {
		Connection c = getConnection();
		if (c == null)
			return false;

		int count = 0;
		try {
			if (usedInSeeds == null)
				usedInSeeds = getConnection().prepareStatement("SELECT count(*) FROM Seeds WHERE kind_id = ?");

			usedInSeeds.setInt(1, kind.getId());
			ResultSet rs = usedInSeeds.executeQuery();

			if (rs.next())
				count = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("KindTables.canRemove(KindData kind) - " + e);
		}

		return (count == 0);
	}

	public boolean remove(KindData kind) {
		boolean deleted = false;

		Connection c = getConnection();
		if (c == null)
			return deleted;

		try {
			if (removeKind == null)
				removeKind = getConnection().prepareStatement("DELETE FROM Kinds WHERE id = ?");

			removeKind.setInt(1, kind.getId());
			removeKind.executeUpdate();

			deleted = true;
		} catch (SQLException e) {
			System.out.println("KindTables.remove(KindData kind) - " + e);
		}

		return deleted;
	}

	private PreparedStatement getListForTable = null;
	private PreparedStatement getListForCombo = null;

	private PreparedStatement addKind = null;
	private PreparedStatement setKind = null;
	private PreparedStatement usedInSeeds = null;
	private PreparedStatement removeKind = null;
}
