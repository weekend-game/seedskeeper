package game.weekend.seedskeeper.controls;

import game.weekend.seedskeeper.general.Journal;
import javafx.scene.control.ColorPicker;

public class XColorPicker extends ColorPicker {

	public XColorPicker(Journal<?> journal) {
		super();

		focusedProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue)
				if (journal != null)
					journal.setCurrentNode(XColorPicker.this);
		});
	}
}
