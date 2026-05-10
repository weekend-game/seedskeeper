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

public class CategoryTables extends TablesOf {

	public ObservableList<CategoryData> getListForTable() {
		ObservableList<CategoryData> list = FXCollections.observableArrayList();

		Connection c = getConnection();
		if (c == null)
			return list;

		try {
			if (getListForTable == null)
				getListForTable = getConnection().prepareStatement("SELECT id, numb, name FROM Categories");

			ResultSet rs = getListForTable.executeQuery();

			while (rs.next())
				list.add(new CategoryData(rs.getInt(1), rs.getInt(2), rs.getString(3).trim()));
			rs.close();
		} catch (SQLException e) {
			System.out.println("CategoryTables.getListForTable() - " + e);
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
				getListForCombo = getConnection().prepareStatement("SELECT id, name FROM Categories ORDER BY numb");

			list.add(new ComboItem(0, "")); // Это соответсвует отсутствию указания качества в UI

			ResultSet rs = getListForCombo.executeQuery();

			while (rs.next())
				list.add(new ComboItem(rs.getInt(1), rs.getString(2)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("CategoryTables.getListForCombo() - " + e);
		}

		return list;
	}

	public void add(CategoryData category) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (renumberDown == null)
				renumberDown = getConnection()
						.prepareStatement("UPDATE Categories SET numb = numb + 1 WHERE numb >= ?");

			if (addCategory == null)
				addCategory = getConnection().prepareStatement("INSERT INTO Categories (numb, name) VALUES (?, ?)",
						Statement.RETURN_GENERATED_KEYS);

			getConnection().setAutoCommit(false);

			renumberDown.setInt(1, category.getNumb());
			renumberDown.executeUpdate();

			addCategory.setInt(1, category.getNumb());
			addCategory.setString(2, category.getName());
			addCategory.executeUpdate();

			ResultSet rs = addCategory.getGeneratedKeys();
			if (rs.next())
				category.setId(rs.getInt(1));
			rs.close();

			getConnection().commit();
		} catch (SQLException e) {
			System.out.println("CategoryTables.add(CategoryData category) - " + e);
		} finally {
			try {
				getConnection().setAutoCommit(true);
			} catch (SQLException ignored) {
			}
		}
	}

	public void set(CategoryData category) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (setCategory == null)
				setCategory = getConnection().prepareStatement("UPDATE Categories SET numb = ?, name = ? WHERE id = ?");

			setCategory.setInt(1, category.getNumb());
			setCategory.setString(2, category.getName());
			setCategory.setInt(3, category.getId());
			setCategory.executeUpdate();
		} catch (SQLException e) {
			System.out.println("CategoryTables.set(CategoryData category) - " + e);
		}
	}

	public boolean canRemove(CategoryData category) {
		return true;
	}

	public boolean remove(CategoryData category) {
		boolean deleted = false;

		Connection c = getConnection();
		if (c == null)
			return deleted;

		try {
			if (removeCategory == null)
				removeCategory = getConnection().prepareStatement("DELETE FROM Categories WHERE id = ?");

			if (renumberUp == null)
				renumberUp = getConnection().prepareStatement("UPDATE Categories SET numb = numb - 1 WHERE numb > ?");

			getConnection().setAutoCommit(false);

			removeCategory.setInt(1, category.getId());
			removeCategory.executeUpdate();

			renumberUp.setInt(1, category.getNumb());
			renumberUp.executeUpdate();

			getConnection().commit();

			deleted = true;
		} catch (SQLException e) {
			System.out.println("CategoryTables.remove(CategoryData category) - " + e);
		} finally {
			try {
				getConnection().setAutoCommit(true);
			} catch (SQLException ignored) {
			}
		}

		return deleted;
	}

	public void setNumb(int id1, int numb1, int id2, int numb2) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (setNumber == null)
				setNumber = getConnection().prepareStatement("UPDATE Categories SET numb = ? WHERE id = ?");

			getConnection().setAutoCommit(false);

			setNumber.setInt(1, numb1);
			setNumber.setInt(2, id1);
			setNumber.executeUpdate();

			setNumber.setInt(1, numb2);
			setNumber.setInt(2, id2);
			setNumber.executeUpdate();

			getConnection().commit();
		} catch (SQLException e) {
			System.out.println("CategoryTables.setNumb(int id1, int numb1, int id2, int numb2) - " + e);
		} finally {
			try {
				getConnection().setAutoCommit(true);
			} catch (SQLException ignored) {
			}
		}
	}

	private PreparedStatement getListForTable = null;
	private PreparedStatement getListForCombo = null;

	private PreparedStatement addCategory = null;
	private PreparedStatement setCategory = null;
	private PreparedStatement removeCategory = null;

	private PreparedStatement renumberUp = null;
	private PreparedStatement renumberDown = null;
	private PreparedStatement setNumber = null;
}
