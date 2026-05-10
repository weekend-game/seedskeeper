package game.weekend.seedskeeper.dictionaries.other;

import game.weekend.seedskeeper.data.types.Error;
import game.weekend.seedskeeper.data.types.Record;
import game.weekend.seedskeeper.general.Loc;

public class CategoryData extends Record {
	private int id;
	private int numb;
	private String name;

	// Пустая запись
	public CategoryData() {
		this(0, 1, "");
	}

	// Создать запись с передачей всех полей
	public CategoryData(int id, int numb, String name) {
		setId(id);
		setNumb(numb);
		setName(name);
	}

	// get и set для всех полей записи

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumb() {
		return numb;
	}

	public void setNumb(int numb) {
		this.numb = numb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Error check() {
		if (getName() == null || getName().trim().length() == 0)
			return new Error(Loc.get("enter_the_category_name") + ".", 2);

		return null;
	}

	@Override
	public boolean hasDifference(Object o) {
		StringBuilder sb = new StringBuilder();
		setDifferences("");

		if (this == o)
			return false;

		CategoryData other = (CategoryData) o;

		boolean result;
		result = checkDifference(Loc.get("of_the_name_is"), getName(), other.getName(), sb);

		if (result)
			setDifferences(sb.toString());

		return result;
	}
}
