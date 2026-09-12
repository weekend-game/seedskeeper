package game.weekend.seedskeeper.general;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public abstract class Journal<T> {

	public VBox getPane() {
		VBox vb = new VBox();
		vb.setPadding(new Insets(10, 10, 10, 10));
		return vb;
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public void writeProp() {
	}

	public void enter() {
	}

	public void requestFocusForTableView() {
	}

	protected void doShiftUp() {
	}

	protected void doShiftDown() {
	}
}
