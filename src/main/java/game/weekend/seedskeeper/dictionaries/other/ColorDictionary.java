package game.weekend.seedskeeper.dictionaries.other;

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
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ColorDictionary extends Journal<ColorData> implements IReadOnly {

	private final XTextField txtName;

	private final Button btnNew = getButtonNew();
	private final Button btnEdit = getButtonEdit();
	private final Button btnDelete = getButtonDelete();

	private final Label lblTitle = new Label(Loc.get("colors"));
	private final Label lblMode = new Label("");

	public ColorDictionary(Journal<?> parentJournal) {
		txtName = new XTextField(parentJournal);
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

	private TableView<ColorData> makeTableView() {
		TableColumn<ColorData, String> colName = getTextColumn(Loc.get("name"), "name", 260);
		getTableView().getColumns().add(colName);

		getTableView().setItems(getDB().color.getListForTable());

		makeTableHandlers(getTableView());

		getTableView().getSortOrder().add(colName);
		getTableView().sort();

		getTableView().setPrefHeight(256);
		getTableView().setPrefWidth(280);

		return getTableView();
	}

	private VBox makeTextFields() {
		VBox vbName = new VBox();
		vbName.getChildren().addAll(new Label(Loc.get("name") + ":"), getTextBox("", 8, txtName, 32));

		VBox vb = new VBox();
		vb.setPadding(new Insets(5, 5, 5, 10));
		vb.getChildren().addAll(vbName, getVSpacer(), new Label(""), getVSpacer());
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
		txtName.setEditable(editMode);

		btnNew.setDisable(editMode);
		btnEdit.setDisable(editMode);
		btnDelete.setDisable(editMode);
	}

	@Override
	public ColorData getEditedRecord() {
		ColorData color = new ColorData(0, txtName.getText());
		if (!isAppendMode())
			color.setId(getCurrentRecord().getId());

		return color;
	}

	private boolean check(ColorData category) {
		Error err = category.check();
		if (err != null) {
			Dialogues.errMes(err.mes);
			switch (err.fieldNum) {
			case 2:
				this.txtName.requestFocus();
				break;
			}
			return false;
		}
		return true;
	}

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
	protected void doDisplay(ColorData ColorData) {
		if (ColorData == null) {
			ColorData = new ColorData();
		}

		txtName.setText(ColorData.getName());
	}

	@Override
	protected boolean doNew() {
		if (!super.doNew())
			return false;

		ColorData newQuality = new ColorData();
		doDisplay(newQuality);

		txtName.requestFocus();

		return true;
	}

	@Override
	protected boolean doEdit() {
		if (!super.doEdit())
			return false;

		txtName.requestFocus();

		return true;
	}

	@Override
	protected boolean doDelete() {
		if (!super.doDelete())
			return false;

		ColorData color = getCurrentRecord();
		if (color != null) {

			if (!getDB().color.canRemove(color)) {
				Dialogues.errMes(Loc.get("the_specified_color_is_in_use_and_cannot_be_removed") + ".");
				requestFocusForTableView();
				return false;
			}

			String mes = Loc.get("are_you_sure_you_want_to_remove") + ": \"" + color.getName() + "\"?";
			Dialogues.conMes(mes, (event) -> {
				if (getDB().color.remove(color)) {
					getTableView().getItems().remove(color);
					getTableView().getSelectionModel().selectBelowCell();
				}
			});
		}

		requestFocusForTableView();
		return true;
	}

	@Override
	public void doOk() {
		ColorData color = getEditedRecord();

		if (!check(color))
			return;

		if (isAppendMode()) {
			getDB().color.setEdited(true);
			getDB().color.add(color);
			getTableView().getItems().add(color);

		} else {
			if (getCurrentRecord().hasDifference(color)) {
				getDB().color.setEdited(true);

				getDB().color.set(color);
				getTableView().getItems().set(getTableView().getSelectionModel().getSelectedIndex(), color);
			}
		}

		getTableView().getSelectionModel().select(color);

		if (isAppendMode())
			getTableView().scrollTo(color);

		doCancel(false);
	}
}
