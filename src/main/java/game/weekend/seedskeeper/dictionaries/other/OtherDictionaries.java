package game.weekend.seedskeeper.dictionaries.other;

import game.weekend.seedskeeper.general.IReadOnly;
import game.weekend.seedskeeper.general.Journal;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OtherDictionaries extends Journal<Object> implements IReadOnly {

	QualityDictionary qualityDictionary = new QualityDictionary(this);

	private final Button btnOk = getButtonOk();
	private final Button btnCancel = getButtonCancel();

	public OtherDictionaries() {
		qualityDictionary.addReadOnlyObject(this);
	}

	@Override
	public VBox getPane() {
		VBox vb = super.getPane();

		HBox hb1 = new HBox();
		hb1.getChildren().addAll(qualityDictionary.getPane());

		vb.getChildren().addAll(hb1, getVSpacer(), makeSaveButtons());
		return vb;
	}

	private HBox makeSaveButtons() {
		HBox hb = new HBox();
		hb.setSpacing(10);
		hb.setPadding(new Insets(5, 10, 0, 10));
		hb.getChildren().addAll(getHSpacer(), btnOk, btnCancel);
		return hb;
	}

	@Override
	public boolean isEditMode() {
		return qualityDictionary.isEditMode();
	}

	@Override
	public void setReadOnlyMode(boolean readOnly) {
		super.setReadOnlyMode(readOnly);

		btnOk.setDisable(!readOnly);
		btnCancel.setDisable(!readOnly);
	}

	@Override
	public void activate() {
		btnCancel.setCancelButton(true);

		qualityDictionary.activate();

		if (getCurrentNode() != null)
			Platform.runLater(() -> getCurrentNode().requestFocus());
	}

	@Override
	public void deactivate() {
		btnCancel.setCancelButton(false);
	}

	@Override
	public void enter() {
		if (qualityDictionary.isEditMode())
			qualityDictionary.enter();
	}

	@Override
	public void doOk() {
		if (qualityDictionary.isEditMode())
			qualityDictionary.doOk();
	}

	@Override
	public void doCancel(boolean checkChange) {
		if (qualityDictionary.isEditMode())
			qualityDictionary.doCancel(checkChange);
	}
}
