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

public class QualityTables extends TablesOf {

	public ObservableList<QualityData> getListForTable() {
		ObservableList<QualityData> list = FXCollections.observableArrayList();

		Connection c = getConnection();
		if (c == null)
			return list;

		try {
			if (getListForTable == null)
				getListForTable = getConnection().prepareStatement("SELECT id, numb, code, color FROM Qualities");

			ResultSet rs = getListForTable.executeQuery();

			while (rs.next())
				list.add(new QualityData(rs.getInt(1), rs.getInt(2), rs.getString(3).trim(), rs.getString(4)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("QualityTables.getListForTable() - " + e);
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
				getListForCombo = getConnection().prepareStatement("SELECT id, code FROM Qualities");

			list.add(new ComboItem(0, "")); // Это соответсвует отсутствию указания качества в UI

			ResultSet rs = getListForCombo.executeQuery();

			while (rs.next())
				list.add(new ComboItem(rs.getInt(1), rs.getString(2)));
			rs.close();
		} catch (SQLException e) {
			System.out.println("QualityTables.getListForCombo() - " + e);
		}

		return list;
	}

	public void add(QualityData quality) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (renumberDown == null)
				renumberDown = getConnection().prepareStatement("UPDATE Qualities SET numb = numb + 1 WHERE numb >= ?");

			if (addQuality == null)
				addQuality = getConnection().prepareStatement(
						"INSERT INTO Qualities (numb, code, color ) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			getConnection().setAutoCommit(false);

			renumberDown.setInt(1, quality.getNumb());
			renumberDown.executeUpdate();

			addQuality.setInt(1, quality.getNumb());
			addQuality.setString(2, quality.getCode());
			addQuality.setString(3, quality.getColor());
			addQuality.executeUpdate();

			ResultSet rs = addQuality.getGeneratedKeys();
			if (rs.next())
				quality.setId(rs.getInt(1));
			rs.close();

			getConnection().commit();
		} catch (SQLException e) {
			System.out.println("QualityTables.add(QualityData quality) - " + e);
		} finally {
			try {
				getConnection().setAutoCommit(true);
			} catch (SQLException ignored) {
			}
		}
	}

	public void set(QualityData quality) {
		Connection c = getConnection();
		if (c == null)
			return;

		try {
			if (setQuality == null)
				setQuality = getConnection()
						.prepareStatement("UPDATE Qualities SET numb = ?, code = ?, color = ? WHERE id = ?");

			setQuality.setInt(1, quality.getNumb());
			setQuality.setString(2, quality.getCode());
			setQuality.setString(3, quality.getColor());
			setQuality.setInt(4, quality.getId());
			setQuality.executeUpdate();

		} catch (SQLException e) {
			System.out.println("QualityTables.set(QualityData quality) - " + e);
		}
	}

	public boolean canRemove(QualityData quality) {
		return true;
	}

	public boolean remove(QualityData quality) {
		boolean deleted = false;

		Connection c = getConnection();
		if (c == null)
			return deleted;

		try {
			if (removeQuality == null)
				removeQuality = getConnection().prepareStatement("DELETE FROM Qualities WHERE id = ?");

			if (renumberUp == null)
				renumberUp = getConnection().prepareStatement("UPDATE Qualities SET numb = numb - 1 WHERE numb > ?");

			getConnection().setAutoCommit(false);

			removeQuality.setInt(1, quality.getId());
			removeQuality.execute();

			renumberUp.setInt(1, quality.getNumb());
			renumberUp.executeUpdate();

			getConnection().commit();

			deleted = true;
		} catch (SQLException e) {
			System.out.println("QualityTables.remove(QualityData quality) - " + e);
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
				setNumber = getConnection().prepareStatement("UPDATE Qualities SET numb = ? WHERE id = ?");

			getConnection().setAutoCommit(false);

			setNumber.setInt(1, numb1);
			setNumber.setInt(2, id1);
			setNumber.executeUpdate();

			setNumber.setInt(1, numb2);
			setNumber.setInt(2, id2);
			setNumber.executeUpdate();

			getConnection().commit();
		} catch (SQLException e) {
			System.out.println("QualityTables.setNumb(int id1, int numb1, int id2, int numb2) - " + e);
		} finally {
			try {
				getConnection().setAutoCommit(true);
			} catch (SQLException ignored) {
			}
		}
	}

	private static PreparedStatement getListForTable = null;
	private static PreparedStatement getListForCombo = null;

	private static PreparedStatement addQuality = null;
	private static PreparedStatement setQuality = null;
	private static PreparedStatement removeQuality = null;

	private static PreparedStatement renumberUp = null;
	private static PreparedStatement renumberDown = null;
	private static PreparedStatement setNumber = null;
}
