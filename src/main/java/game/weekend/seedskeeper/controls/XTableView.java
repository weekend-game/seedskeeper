package game.weekend.seedskeeper.controls;

import game.weekend.seedskeeper.general.Journal;
import javafx.scene.control.TableView;

public class XTableView<T> extends TableView<T> {

	public XTableView(Journal<?> journal) {
		super();

		focusedProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue)
				if (journal != null)
					journal.setCurrentNode(XTableView.this);
		});
	}
}
