package game.weekend.seedskeeper.data.types;

public class Error {
	public final String mes;
	public final int fieldNum;

	public Error(String mes, int fieldNum) {
		this.mes = mes;
		this.fieldNum = fieldNum;
	}
}
