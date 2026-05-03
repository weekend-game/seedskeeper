package game.weekend.seedskeeper.controls;

import game.weekend.seedskeeper.general.Journal;
import javafx.scene.control.TextField;

public class XTextField extends TextField {

	public XTextField() {
		super();

		setOnKeyPressed(e -> {
			if (e.getCode().toString().equals("ESCAPE"))
				e.consume();
		});
	}

	public XTextField(Journal<?> journal) {
		this();

		focusedProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue)
				if (journal != null)
					journal.setCurrentNode(XTextField.this);
		});
	}

}
