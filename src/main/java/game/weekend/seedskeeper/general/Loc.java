package game.weekend.seedskeeper.general;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Локализация.
 */
public class Loc {

	/**
	 * Класс содержит только статические методы и создавать объект нет
	 * необходимости.
	 */
	private Loc() {
	}

	/**
	 * Установить текущий язык UI.
	 * 
	 * @param language текущий язык UI.
	 */
	public static void setLanguage(String language) {
		try {
			Loc.language = language;
			bundle = ResourceBundle.getBundle("messages", new Locale(language));
		} catch (MissingResourceException ignored) {
		}
	}

	/**
	 * Получить текущий язык UI.
	 * 
	 * @return текущий язык UI.
	 */
	public static String getLanguage() {
		return language;
	}

	/**
	 * Получить локализованную строку.
	 * 
	 * @param name название строки.
	 * @return локализованая строка.
	 */
	public static String get(String name) {
		if (bundle != null)
			try {
				return bundle.getString(name);
			} catch (MissingResourceException e) {
			}

		return getDefString(name);
	}

	private static String getDefString(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1).replace('_', ' ');
	}

	private static ResourceBundle bundle;
	private static String language;
}
