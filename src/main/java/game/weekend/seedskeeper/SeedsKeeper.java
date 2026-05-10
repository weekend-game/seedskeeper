package game.weekend.seedskeeper;

import java.awt.Dimension;
import java.awt.Toolkit;

import game.weekend.seedskeeper.dictionaries.brands.BrandDictionary;
import game.weekend.seedskeeper.dictionaries.other.OtherDictionaries;
import game.weekend.seedskeeper.general.Journal;
import game.weekend.seedskeeper.general.Loc;
import game.weekend.seedskeeper.general.Proper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class SeedsKeeper extends Application {

	public static final String APP_NAME = "SeedsKeeper";

	public static final String APP_VERSION = "00.04";

	public final static String APP_DATE = "10.05.2026";

	public static final String APP_COPYRIGHT = "(c) Weekend Game, 2026";

	public final static String TITLE = APP_NAME + " (v" + APP_VERSION + " - " + APP_DATE + ")";

	private static Stage stage = null;
	private static TabPane tabPane = null;

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getStage() {
		return stage;
	}

	@Override
	public void start(Stage stage) {
		Proper.read(APP_NAME);

		Loc.setLanguage(Proper.getProperty("Language", "en"));

		SeedsKeeper.stage = stage;
		SeedsKeeper.tabPane = new TabPane();

		readProp();

		stage.setScene(getScene());
		stage.setTitle(SeedsKeeper.TITLE);
		stage.show();
	}

	@Override
	public void stop() {
		writeProp();

		for (Tab t : tabPane.getTabs())
			((SeedTab) t).journal.writeProp();

		Proper.save();
	}

	private Scene getScene() {
		tabPane.getTabs().add(new SeedTab(new BrandDictionary(), Loc.get("brands")));
		tabPane.getTabs().add(new SeedTab(new OtherDictionaries(), Loc.get("other_dictionaries")));

		tabPane.setOnKeyPressed(ke -> {
			String key = ke.getCode().toString();
			if (key.equalsIgnoreCase("ENTER"))
				for (Tab t : tabPane.getTabs())
					if (t.isSelected())
						((SeedTab) t).journal.enter();
		});

		return new Scene(tabPane);
	}

	private void readProp() {
		final int INSET = 40;

		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		int def_x = INSET;
		int def_y = INSET;
		int def_w = ss.width - INSET * 2;
		int def_h = ss.height - INSET * 3;

		stage.setX(Proper.getProperty("stage.X", def_x));
		stage.setY(Proper.getProperty("stage.Y", def_y));
		stage.setWidth(Proper.getProperty("stage.Width", def_w));
		stage.setHeight(Proper.getProperty("stage.Height", def_h));
	}

	private void writeProp() {
		Proper.setProperty("stage.X", (int) stage.getX());
		Proper.setProperty("stage.Y", (int) stage.getY());
		Proper.setProperty("stage.Width", (int) stage.getWidth());
		Proper.setProperty("stage.Height", (int) stage.getHeight());

		Proper.setProperty("Language", Loc.getLanguage());
	}

	static class SeedTab extends Tab {
		public final Journal<?> journal;

		public SeedTab(Journal<?> journal, String name) {
			super(name, journal.getPane());
			this.journal = journal;
			setClosable(false);
			setOnSelectionChanged(event -> {
				if (isSelected())
					journal.activate();
				else
					journal.deactivate();
			});
		}
	}
}
