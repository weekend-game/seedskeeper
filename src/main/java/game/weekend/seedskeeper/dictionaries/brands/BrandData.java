package game.weekend.seedskeeper.dictionaries.brands;

import game.weekend.seedskeeper.data.types.Error;
import game.weekend.seedskeeper.data.types.Record;
import game.weekend.seedskeeper.general.Loc;

public class BrandData extends Record {
	private int id;
	private String code;
	private String name;
	private String link;

	public BrandData() {
		this(0, "", "", "");
	}

	public BrandData(int id, String code, String name, String link) {
		setId(id);
		setCode(code);
		setName(name);
		setLink(link);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = (code == null) ? "" : code.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Error check() {
		if (getCode() == null || getCode().trim().length() == 0)
			return new Error(Loc.get("enter_the_brand_code") + ".", 2);

		return null;
	}

	@Override
	public boolean hasDifference(Object o) {
		StringBuilder sb = new StringBuilder();
		setDifferences("");

		if (this == o)
			return false;

		BrandData other = (BrandData) o;

		boolean result;
		result = checkDifference(Loc.get("code_is"), code, other.code, sb);
		result |= checkDifference(Loc.get("name_is"), name, other.name, sb);
		result |= checkDifference(Loc.get("link_is"), link, other.link, sb);

		if (result)
			setDifferences(sb.toString());

		return result;
	}
}
