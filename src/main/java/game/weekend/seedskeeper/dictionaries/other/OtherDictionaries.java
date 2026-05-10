package game.weekend.seedskeeper.dictionaries.other;

import game.weekend.seedskeeper.general.IReadOnly;
import game.weekend.seedskeeper.general.Journal;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OtherDictionaries extends Journal<Object> implements IReadOnly {

	CategoryDictionary categoryDictionary = new CategoryDictionary(this);
	ColorDictionary colorDictionary = new ColorDictionary(this);
	KindDictionary kindDictionary = new KindDictionary(this);
	QualityDictionary qualityDictionary = new QualityDictionary(this);

	// Кнопки управления
	private final Button btnOk = getButtonOk();
	private final Button btnCancel = getButtonCancel();

	public OtherDictionaries() {
		categoryDictionary.addReadOnlyObject(colorDictionary);
		categoryDictionary.addReadOnlyObject(kindDictionary);
		categoryDictionary.addReadOnlyObject(qualityDictionary);
		categoryDictionary.addReadOnlyObject(this);

		colorDictionary.addReadOnlyObject(categoryDictionary);
		colorDictionary.addReadOnlyObject(kindDictionary);
		colorDictionary.addReadOnlyObject(qualityDictionary);
		colorDictionary.addReadOnlyObject(this);

		qualityDictionary.addReadOnlyObject(categoryDictionary);
		qualityDictionary.addReadOnlyObject(colorDictionary);
		qualityDictionary.addReadOnlyObject(kindDictionary);
		qualityDictionary.addReadOnlyObject(this);

		kindDictionary.addReadOnlyObject(categoryDictionary);
		kindDictionary.addReadOnlyObject(colorDictionary);
		kindDictionary.addReadOnlyObject(qualityDictionary);
		kindDictionary.addReadOnlyObject(this);
	}

	@Override
	public VBox getPane() {
		VBox vb = super.getPane();

		HBox hb1 = new HBox();
		hb1.getChildren().addAll(categoryDictionary.getPane(), colorDictionary.getPane());

		HBox hb2 = new HBox();
		hb2.getChildren().addAll(kindDictionary.getPane(), qualityDictionary.getPane());

		vb.getChildren().addAll(hb1, hb2, getVSpacer(), makeSaveButtons());
		return vb;
	}

	// Кнопки сохранения
	private HBox makeSaveButtons() {
		HBox hb = new HBox();
		hb.setSpacing(10);
		hb.setPadding(new Insets(5, 10, 0, 10));
		hb.getChildren().addAll(getHSpacer(), btnOk, btnCancel);
		return hb;
	}

	@Override
	public boolean isEditMode() {
		// Для метода escape()
		return categoryDictionary.isEditMode() || qualityDictionary.isEditMode();
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

		categoryDictionary.activate();
		colorDictionary.activate();
		kindDictionary.activate();
		qualityDictionary.activate();

		// Возвращаю фокус активному полю, а иначе он останется на заголовке Tab-а
		if (getCurrentNode() != null)
			Platform.runLater(() -> getCurrentNode().requestFocus());
	}

	@Override
	public void deactivate() {
		btnCancel.setCancelButton(false);
	}

	@Override
	public void enter() {
		if (categoryDictionary.isEditMode())
			categoryDictionary.enter();
		if (colorDictionary.isEditMode())
			colorDictionary.enter();
		if (kindDictionary.isEditMode())
			kindDictionary.enter();
		if (qualityDictionary.isEditMode())
			qualityDictionary.enter();
	}

	@Override
	public void doOk() {
		if (categoryDictionary.isEditMode())
			categoryDictionary.doOk();
		if (colorDictionary.isEditMode())
			colorDictionary.doOk();
		if (kindDictionary.isEditMode())
			kindDictionary.doOk();
		if (qualityDictionary.isEditMode())
			qualityDictionary.doOk();
	}

	@Override
	public void doCancel(boolean checkChange) {
		if (categoryDictionary.isEditMode())
			categoryDictionary.doCancel(checkChange);
		if (colorDictionary.isEditMode())
			colorDictionary.doCancel(checkChange);
		if (kindDictionary.isEditMode())
			kindDictionary.doCancel(checkChange);
		if (qualityDictionary.isEditMode())
			qualityDictionary.doCancel(checkChange);
	}
}
