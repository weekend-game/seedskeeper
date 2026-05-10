package game.weekend.seedskeeper.dictionaries.other;

import game.weekend.seedskeeper.data.types.Error;
import game.weekend.seedskeeper.data.types.Record;
import game.weekend.seedskeeper.general.Loc;

public class KindData extends Record {
	private int id;
	private String name;

	// Пустая запись
	public KindData() {
		this(0, "");
	}

	// Создать запись с передачей всех полей
	public KindData(int id, String name) {
		setId(id);
		setName(name);
	}

	// get и set для всех полей записи

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Error check() {
		if (getName() == null || getName().trim().length() == 0)
			return new Error(Loc.get("enter_the_kind_name") + ".", 2);

		return null;
	}

	@Override
	public boolean hasDifference(Object o) {
		StringBuilder sb = new StringBuilder();
		setDifferences("");

		if (this == o)
			return false;

		KindData other = (KindData) o;

		boolean result;
		result = checkDifference(Loc.get("of_the_name_is"), getName(), other.getName(), sb);

		if (result)
			setDifferences(sb.toString());

		return result;
	}
}
