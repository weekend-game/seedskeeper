package game.weekend.seedskeeper.dictionaries.other;

import game.weekend.seedskeeper.controls.XColorPicker;
import game.weekend.seedskeeper.controls.XTextField;
import game.weekend.seedskeeper.data.types.Error;
import game.weekend.seedskeeper.general.Dialogues;
import game.weekend.seedskeeper.general.IReadOnly;
import game.weekend.seedskeeper.general.Journal;
import game.weekend.seedskeeper.general.Loc;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class QualityDictionary extends Journal<QualityData> implements IReadOnly {

	private final XTextField txtCode;
	private final XColorPicker cprColor;

	private final Button btnNew = getButtonNew();
	private final Button btnEdit = getButtonEdit();
	private final Button btnDelete = getButtonDelete();
	private final Button btnShiftUp = getButtonShiftUp();
	private final Button btnShiftDown = getButtonShiftDown();

	private final Label lblTitle = new Label(Loc.get("quality"));
	private final Label lblMode = new Label("");

	public QualityDictionary(Journal<?> parentJournal) {
		txtCode = new XTextField(parentJournal);
		cprColor = new XColorPicker(parentJournal);
	}

	@Override
	public VBox getPane() {
		VBox vb = super.getPane();

		HBox hb1 = new HBox();
		hb1.getChildren().addAll(lblTitle, getHSpacer(20), lblMode);

		HBox hb2 = new HBox();
		hb2.getChildren().addAll(makeTableView(), makeTextFields(), getHSpacer());

		vb.getChildren().addAll(hb1, hb2, makeEditButtons());

		setEditMode(false);

		getTableView().getSelectionModel().selectFirst();

		return vb;
	}

	private TableView<QualityData> makeTableView() {
		TableColumn<QualityData, Integer> colNumb = getIntColumn(Loc.get("number"), "numb", 60);
		getTableView().getColumns().add(colNumb);

		getTableView().getColumns().add(getTextColumn(Loc.get("code"), "code", 140));
		getTableView().getColumns().add(getTextColumn(Loc.get("color"), "color", 50));

		getTableView().setRowFactory(tv -> new TableRow<>() {
			@Override
			protected void updateItem(QualityData quality, boolean empty) {
				super.updateItem(quality, empty);

				if (tv.getSelectionModel().getSelectedItem() == quality || quality == null
						|| quality.getColor().trim().length() == 0)
					setStyle("");
				else
					setStyle("-fx-background-color: #" + quality.getColor().trim() + ";");
			}
		});

		getTableView().setItems(getDB().quality.getListForTable());

		makeTableHandlers(getTableView());

		getTableView().getSortOrder().add(colNumb);
		getTableView().sort();

		getTableView().setPrefHeight(256);
		getTableView().setPrefWidth(256);

		return getTableView();
	}

	private VBox makeTextFields() {
		HBox hb1 = getTextBox(Loc.get("code") + ":", 35, txtCode, 9);
		HBox hb2 = getColorBox(cprColor);

		VBox vb = new VBox();
		vb.setPadding(new Insets(5, 5, 5, 10));
		vb.getChildren().addAll(hb1, hb2, getVSpacer(), btnShiftUp, new Label(""), btnShiftDown, getVSpacer());
		return vb;
	}

	private HBox makeEditButtons() {
		HBox hb = new HBox(10);
		hb.setPadding(new Insets(5, 10, 0, 10));
		hb.getChildren().addAll(btnNew, btnEdit, btnDelete);
		return hb;
	}

	@Override
	protected void setEditMode(boolean editMode) {
		super.setEditMode(editMode);

		getTableView().setDisable(editMode);

		txtCode.setEditable(editMode);
		cprColor.setDisable(!editMode);

		btnNew.setDisable(editMode);
		btnEdit.setDisable(editMode);
		btnDelete.setDisable(editMode);
		btnShiftUp.setDisable(editMode);
		btnShiftDown.setDisable(editMode);
	}

	@Override
	public QualityData getEditedRecord() {
		int numb = (getCurrentRecord() == null) ? 0 : getCurrentRecord().getNumb();
		String color = cprColor.getValue().toString().substring(2, 8);

		QualityData quality = new QualityData(0, numb, txtCode.getText(), color);
		if (!isAppendMode())
			quality.setId(getCurrentRecord().getId());
		else
			quality.setNumb(++numb);

		return quality;
	}

	private boolean check(QualityData quality) {
		Error err = quality.check();
		if (err != null) {
			Dialogues.errMes(err.mes);
			switch (err.fieldNum) {
			case 2:
				this.txtCode.requestFocus();
				break;
			}

			return false;
		} else {
			return true;
		}
	}

	@Override
	public void setReadOnlyMode(boolean readOnly) {
		super.setReadOnlyMode(readOnly);

		btnNew.setDisable(readOnly);
		btnEdit.setDisable(readOnly);
		btnDelete.setDisable(readOnly);
	}

	@Override
	protected void displayMode(String mode) {
		if (mode.length() == 0)
			lblMode.setText("");
		else
			lblMode.setText("[ " + mode.trim() + " ]");
	}

	@Override
	protected void doDisplay(QualityData quality) {
		if (quality == null) {
			quality = new QualityData();
		}

		txtCode.setText(quality.getCode());
		cprColor.setValue(Color.web("0x" + quality.getColor(), 1));
	}

	@Override
	protected boolean doNew() {
		if (!super.doNew())
			return false;

		QualityData newQuality = new QualityData();
		newQuality.setColor("ffffff");
		doDisplay(newQuality);

		txtCode.requestFocus();

		return true;
	}

	@Override
	protected boolean doEdit() {
		if (!super.doEdit())
			return false;

		txtCode.requestFocus();

		return true;
	}

	@Override
	protected boolean doDelete() {
		if (!super.doDelete())
			return false;

		QualityData quality = getCurrentRecord();
		if (quality != null) {

			if (!getDB().quality.canRemove(quality)) {
				Dialogues.errMes(Loc.get("the_specified_quality_is_in_use_and_cannot_be_removed") + ".");
				requestFocusForTableView();
				return false;
			}

			String mes = Loc.get("are_you_sure_you_want_to_remove") + " \"" + quality.getCode().trim() + "\"?";
			Dialogues.conMes(mes, (event) -> {
				if (getDB().quality.remove(quality)) {

					for (QualityData q : getTableView().getItems())
						if (q.getNumb() > quality.getNumb())
							q.setNumb(q.getNumb() - 1);

					getTableView().getItems().remove(quality);
					getTableView().getSelectionModel().selectBelowCell();

					getTableView().sort();
					getTableView().refresh();
					getTableView().requestFocus();
				}
			});
		}

		requestFocusForTableView();
		return true;
	}

	@Override
	public void doOk() {
		QualityData quality = getEditedRecord();
		if (!check(quality))
			return;

		if (isAppendMode()) {
			getDB().quality.setEdited(true);

			getDB().quality.add(quality);

			for (QualityData q : getTableView().getItems())
				if (q.getNumb() >= quality.getNumb())
					q.setNumb(q.getNumb() + 1);

			getTableView().getItems().add(quality);

		} else {
			if (getCurrentRecord().hasDifference(quality)) {

				getDB().quality.setEdited(true);

				getDB().quality.set(quality);
				getTableView().getItems().set(getTableView().getSelectionModel().getSelectedIndex(), quality);
			}
		}

		getTableView().getSelectionModel().select(quality);

		getTableView().sort();

		doCancel(false);
	}

	@Override
	protected void doShiftUp() {
		QualityData currentQuality = getCurrentRecord();
		QualityData nextQuality = null;

		int currentNumb = (currentQuality == null) ? 0 : currentQuality.getNumb();
		if (currentNumb > 1) {

			for (QualityData q : getTableView().getItems())
				if (q.getNumb() == currentNumb - 1) {
					nextQuality = q;
					break;
				}

			if (nextQuality != null) {
				currentQuality.setNumb(currentNumb - 1);
				nextQuality.setNumb(currentNumb);

				getDB().quality.setNumb(currentQuality.getId(), currentQuality.getNumb(), nextQuality.getId(),
						nextQuality.getNumb());
			}

			getTableView().sort();

			getTableView().refresh();
		}
		getTableView().requestFocus();
	}

	@Override
	protected void doShiftDown() {
		QualityData currentQuality = getCurrentRecord();
		QualityData nextQuality = null;

		int currentNumb = (currentQuality == null) ? 0 : currentQuality.getNumb();
		if (currentNumb > 0 && currentNumb < getTableView().getItems().size()) {

			for (QualityData q : getTableView().getItems())
				if (q.getNumb() == currentNumb + 1) {
					nextQuality = q;
					break;
				}

			if (nextQuality != null) {
				currentQuality.setNumb(currentNumb + 1);
				nextQuality.setNumb(currentNumb);

				getDB().quality.setNumb(currentQuality.getId(), currentQuality.getNumb(), nextQuality.getId(),
						nextQuality.getNumb());
			}

			getTableView().sort();

			getTableView().refresh();
		}
		getTableView().requestFocus();
	}
}
