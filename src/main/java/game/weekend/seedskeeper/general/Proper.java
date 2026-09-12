package game.weekend.seedskeeper.general;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Локально сохраняемые свойства приложения.
 */
public class Proper {

	/**
	 * Создание объектов этого класса запрещено. Класс содержит только статические
	 * методы.
	 */
	private Proper() {
	}

	/**
	 * Прочитать ранее сохранённые свойства приложения.
	 * 
	 * @param name имя файла свойств приложения без указания типа.
	 */
	public static void read(String name) {
		fileName = name.toLowerCase() + ".properties";
		try {
			InputStream inp = new FileInputStream(fileName);
			properties.load(inp);
			inp.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Сохранить свойства приложения.
	 */
	public static void save() {
		OutputStream out;
		try {
			out = new FileOutputStream(fileName);
			properties.store(out, "");
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Сохранить свойство name с целым значением value.
	 * 
	 * @param name имя свойства.
	 * @param value целое значение.
	 */
	public static void setProperty(String name, int value) {
		properties.setProperty(name, "" + value);
	}

	/**
	 * Получить целое свойство name.
	 * 
	 * @param name имя свойства.
	 * @param def   значение свойства по умолчанию.
	 * @return целочисленное значение свойства.
	 */
	public static int getProperty(String name, int def) {
		return Integer.parseInt(properties.getProperty(name, "" + def));
	}

	public static double getProperty(String name, double def) {
		return Double.parseDouble(properties.getProperty(name, "" + def));
	}

	public static void setProperty(String name, double value) {
		properties.setProperty(name, "" + value);
	}

	/**
	 * Сохранить свойство name со строковым значением value.
	 * 
	 * @param name имя свойства.
	 * @param value строковое значение.
	 */
	public static void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}

	/**
	 * Получить строковое свойство name.
	 * 
	 * @param name имя свойства.
	 * @param def   значение свойства по умолчанию.
	 * @return строковое значение свойства.
	 */
	public static String getProperty(String name, String def) {
		return properties.getProperty(name, def);
	}

	public static Integer getIntegerProperty(String name) {
		String s = getProperty(name, "null");
		Integer ret;
		if (s.equalsIgnoreCase("null"))
			ret = null;
		else
			ret = Integer.parseInt(s);
		return ret;
	}

	public static void setIntegerProperty(String name, Integer value) {
		if (value == null)
			properties.setProperty(name, "null");
		else
			properties.setProperty(name, "" + value);
	}

	private static Properties properties = new Properties();
	private static String fileName = "application.properties";
}
