package game.weekend.seedskeeper.data.types;

import game.weekend.seedskeeper.general.Loc;

public class Record {

	private String differences = "";

	public String getDifferences() {
		String result = this.differences;
		this.differences = "";
		return result;
	}

	protected void setDifferences(String differences) {
		this.differences = differences;
	}

	protected boolean checkDifference(String fieldName, Object oldValue, Object newValue, StringBuilder sb) {
		oldValue = oldValue == null ? "" : "" + oldValue;
		newValue = newValue == null ? "" : "" + newValue;

		if (!oldValue.equals(newValue)) {
			if (sb != null)
				sb.append(Loc.get("the_old_value_of_the") + " " + fieldName + " \"" + oldValue + "\", "
						+ Loc.get("and_the_new_value_is") + " \"" + newValue + "\".\n");

			return true;
		}
		return false;
	}

	public boolean hasDifference(Object o) {
		return true;
	}
}
