package game.weekend.seedskeeper.journals.brands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import game.weekend.seedskeeper.data.db.TablesOf;
import game.weekend.seedskeeper.data.types.ComboItem;
import game.weekend.seedskeeper.general.Dialogues;
import game.weekend.seedskeeper.general.Loc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BrandTables extends TablesOf {

	public ObservableList<BrandData> getListForTable() {
		ObservableList<BrandData> list = FXCollections.observableArrayList();

		Connection c = getConnection();
		if (c == null)
			return list;

		try {
			if (getListForTable == null)
				getListForTable = getConnection().prepareStatement("SELECT id, code, name, link FROM Brands");

			ResultSet rs = getListForTable.executeQuery();

			while (rs.next())
				list.add(new BrandData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("BrandTables.getListForTable() - " + e);
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
				getListForCombo = getConnection().prepareStatement("SELECT id, code FROM Brands ORDER BY UPPER(code)");

			list.add(new ComboItem(0, ""));
			ResultSet rs = getListForCombo.executeQuery();

			while (rs.next())
				list.add(new ComboItem(rs.getInt(1), rs.getString(2)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("BrandTables.getListForCombo() - " + e);
		}

		return list;
	}

	public BrandData get(int id) {
		BrandData brand = new BrandData();

		Connection c = getConnection();
		if (c == null || id == 0)
			return brand;

		try {
			if (getBrand == null)
				getBrand = getConnection().prepareStatement("SELECT id, code, name, link FROM Brands WHERE id = ?");

			getBrand.setInt(1, id);
			ResultSet rs = getBrand.executeQuery();

			if (rs.next())
				brand = new BrandData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
			rs.close();
		} catch (SQLException e) {
			System.out.println("BrandTables.get(int id) - " + e);
		}

		return brand;
	}

	public void add(BrandData brand) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (addBrand == null)
				addBrand = getConnection().prepareStatement("INSERT INTO Brands (code, name, link) VALUES (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);

			addBrand.setString(1, brand.getCode());
			addBrand.setString(2, brand.getName());
			addBrand.setString(3, brand.getLink());
			addBrand.executeUpdate();

			ResultSet rs = addBrand.getGeneratedKeys();
			if (rs.next())
				brand.setId(rs.getInt(1));
			rs.close();
		} catch (SQLException e) {
			switch (e.getSQLState()) {
			case "23505":
				Dialogues.errMes(Loc.get("specified_code_is_already_in_the_dictionary") + ".");
				break;
			default:
				Dialogues.errMes(Loc.get("BrandTables.add(BrandData brand) - " + e));
			}
		}
	}

	public void set(BrandData brand) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (setBrand == null)
				setBrand = getConnection()
						.prepareStatement("UPDATE Brands SET code = ?, name = ?, link = ? WHERE id = ?");

			setBrand.setString(1, brand.getCode());
			setBrand.setString(2, brand.getName());
			setBrand.setString(3, brand.getLink());
			setBrand.setInt(4, brand.getId());
			setBrand.executeUpdate();

		} catch (SQLException e) {
			System.out.println("BrandTables.set(BrandData brand) - " + e);
		}
	}

	public boolean canRemove(BrandData brand) {
		return true;
	}

	public boolean remove(BrandData brand) {
		boolean deleted = false;

		Connection c = getConnection();
		if (c == null)
			return deleted;

		try {
			if (removeBrand == null)
				removeBrand = getConnection().prepareStatement("DELETE FROM Brands WHERE id = ?");

			removeBrand.setInt(1, brand.getId());
			removeBrand.execute();

			deleted = true;
		} catch (SQLException e) {
			System.out.println("BrandTables.remove(BrandData brand) - " + e);
		}

		return deleted;
	}

	private static PreparedStatement getListForTable = null;
	private static PreparedStatement getListForCombo = null;
	private static PreparedStatement getBrand = null;
	private static PreparedStatement addBrand = null;
	private static PreparedStatement setBrand = null;
	private static PreparedStatement removeBrand = null;
}
