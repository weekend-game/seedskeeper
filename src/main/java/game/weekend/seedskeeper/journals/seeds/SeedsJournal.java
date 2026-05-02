package game.weekend.seedskeeper.journals.seeds;

import game.weekend.seedskeeper.general.Journal;
import javafx.scene.layout.VBox;

public class SeedsJournal extends Journal<Object> {

	@Override
	public VBox getPane() {
		VBox vb = super.getPane();
		return vb;
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	protected void doDisplay() {
	}
}
