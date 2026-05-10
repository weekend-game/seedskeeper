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

public class ColorTables extends TablesOf {

	public ObservableList<ColorData> getListForTable() {
		ObservableList<ColorData> list = FXCollections.observableArrayList();

		Connection c = getConnection();
		if (c == null)
			return list;

		try {
			if (getListForTable == null)
				getListForTable = getConnection().prepareStatement("SELECT id, name FROM Colors");

			ResultSet rs = getListForTable.executeQuery();

			while (rs.next())
				list.add(new ColorData(rs.getInt(1), rs.getString(2).trim()));
			rs.close();
		} catch (SQLException e) {
			System.out.println("ColorTables.getListForTable() - " + e);
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
				getListForCombo = getConnection().prepareStatement("SELECT id, name FROM Colors ORDER BY numb");

			list.add(new ComboItem(0, "")); // Это соответсвует отсутствию указания качества в UI

			ResultSet rs = getListForCombo.executeQuery();

			while (rs.next())
				list.add(new ComboItem(rs.getInt(1), rs.getString(2)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("ColorTables.getListForCombo() - " + e);
		}

		return list;
	}

	public void add(ColorData color) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (addColor == null)
				addColor = getConnection().prepareStatement("INSERT INTO Colors (name) VALUES (?)",
						Statement.RETURN_GENERATED_KEYS);

			addColor.setString(1, color.getName());
			addColor.executeUpdate();

			ResultSet rs = addColor.getGeneratedKeys();
			if (rs.next())
				color.setId(rs.getInt(1));
			rs.close();
		} catch (SQLException e) {
			System.out.println("ColorTables.add(ColorData color) - " + e);
		}
	}

	public void set(ColorData color) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (setColor == null)
				setColor = getConnection().prepareStatement("UPDATE Colors SET name = ? WHERE id = ?");

			setColor.setString(1, color.getName());
			setColor.setInt(2, color.getId());
			setColor.executeUpdate();
		} catch (SQLException e) {
			System.out.println("ColorTables.set(ColorData color) - " + e);
		}
	}

	public boolean canRemove(ColorData color) {
		Connection c = getConnection();
		if (c == null)
			return false;

		int count = 0;
		try {
			if (usedInSeeds == null)
				usedInSeeds = getConnection().prepareStatement("SELECT count(*) FROM Seeds WHERE color_id = ?");

			usedInSeeds.setInt(1, color.getId());
			ResultSet rs = usedInSeeds.executeQuery();

			if (rs.next())
				count = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("ColorTables.canRemove(ColorData color) - " + e);
		}

		return (count == 0);
	}

	public boolean remove(ColorData color) {
		boolean deleted = false;

		Connection c = getConnection();
		if (c == null)
			return deleted;

		try {
			if (removeColor == null)
				removeColor = getConnection().prepareStatement("DELETE FROM Colors WHERE id = ?");

			removeColor.setInt(1, color.getId());
			removeColor.executeUpdate();

			deleted = true;
		} catch (SQLException e) {
			System.out.println("ColorTables.remove(ColorData color) - " + e);
		}

		return deleted;
	}

	private PreparedStatement getListForTable = null;
	private PreparedStatement getListForCombo = null;

	private PreparedStatement addColor = null;
	private PreparedStatement setColor = null;
	private PreparedStatement usedInSeeds = null;
	private PreparedStatement removeColor = null;
}
