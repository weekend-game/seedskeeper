package game.weekend.seedskeeper.dictionaries.brands;

import game.weekend.seedskeeper.controls.StatusBar;
import game.weekend.seedskeeper.controls.XTextField;
import game.weekend.seedskeeper.data.types.Error;
import game.weekend.seedskeeper.general.Dialogues;
import game.weekend.seedskeeper.general.Journal;
import game.weekend.seedskeeper.general.Loc;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BrandDictionary extends Journal<BrandData> {

	private final XTextField txtCode = new XTextField(this);
	private final XTextField txtName = new XTextField(this);
	private final XTextField txtLink = new XTextField(this);

	private final Button btnNew = getButtonNew();
	private final Button btnNewCopy = getButtonNewCopy();
	private final Button btnEdit = getButtonEdit();
	private final Button btnDelete = getButtonDelete();
	private final Button btnOk = getButtonOk();
	private final Button btnCancel = getButtonCancel();

	private StatusBar statusBar;

	private final Label lblMode = new Label("");

	@Override
	public VBox getPane() {
		VBox vb = super.getPane();

		vb.getChildren().addAll(makeTableView(), makeTextFields(), makeControls());

		setEditMode(false);

		getTableView().getSelectionModel().selectFirst();

		return vb;
	}

	private TableView<BrandData> makeTableView() {

		TableColumn<BrandData, String> colCode = getTextColumn(Loc.get("code"), "code", 200);
		getTableView().getColumns().add(colCode);

		getTableView().getColumns().add(getTextColumn(Loc.get("name"), "name", 400));
		getTableView().getColumns().add(getLinkColumn(Loc.get("link"), "link", 800));

		getTableView().setItems(getDB().brand.getListForTable());

		makeTableHandlers(getTableView());

		getTableView().getSortOrder().add(colCode);
		getTableView().sort();

		getTableView().setPrefHeight(4096);

		return getTableView();
	}

	private VBox makeTextFields() {
		HBox hb1 = getTextBox(Loc.get("code") + ":", 85, txtCode, 32);
		HBox hb2 = getTextBox(Loc.get("name") + ":", 85, txtName, 80);
		HBox hb3 = getTextBox(Loc.get("link") + ":", 85, txtLink, 120);

		VBox vb = new VBox();
		vb.setPadding(new Insets(5, 5, 5, 5));
		vb.getChildren().addAll(hb1, hb2, hb3);
		return vb;
	}

	private HBox makeControls() {
		lblMode.setMinWidth(150);
		lblMode.setAlignment(Pos.CENTER);

		Label lblStatus = new Label("");
		lblStatus.setMinWidth(150);
		lblStatus.setAlignment(Pos.CENTER);
		statusBar = new StatusBar(lblStatus);

		HBox hb = new HBox();
		hb.setSpacing(10);
		hb.setPadding(new Insets(5, 10, 0, 10));
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.getChildren().addAll(btnNew, btnNewCopy, btnEdit, lblMode, btnDelete, lblStatus, getHSpacer(), btnOk,
				btnCancel);
		return hb;
	}

	private boolean check(BrandData brand) {
		Error err = brand.check();
		if (err != null) {
			statusBar.showErrorWithDelay(err.mes);
			switch (err.fieldNum) {
			case 2:
				this.txtCode.requestFocus();
				break;
			case 3:
				this.txtName.requestFocus();
				break;
			case 4:
				this.txtLink.requestFocus();
				break;
			}

			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void setEditMode(boolean editMode) {
		super.setEditMode(editMode);

		getTableView().setDisable(editMode);

		txtCode.setEditable(editMode);
		txtName.setEditable(editMode);
		txtLink.setEditable(editMode);

		btnNew.setDisable(editMode);
		btnNewCopy.setDisable(editMode);
		btnEdit.setDisable(editMode);
		btnDelete.setDisable(editMode);
		btnOk.setDisable(!editMode);
		btnCancel.setDisable(!editMode);
	}

	public BrandData getEditedRecord() {
		BrandData brand = new BrandData(0, txtCode.getText(), txtName.getText(), txtLink.getText());
		if (!isAppendMode())
			brand.setId(getCurrentRecord().getId());

		return brand;
	}

	@Override
	protected void displayMode(String mode) {
		if (mode.length() == 0)
			lblMode.setText("");
		else
			lblMode.setText("[ " + mode + " ]");
	}

	@Override
	public void activate() {
		btnCancel.setCancelButton(true);

		if (isEditMode()) {
			if (getCurrentNode() != null)
				Platform.runLater(() -> getCurrentNode().requestFocus());
		} else
			requestFocusForTableView();
	}

	@Override
	public void deactivate() {
		btnCancel.setCancelButton(false);
	}

	@Override
	protected void doDisplay(BrandData brand) {
		if (brand == null) {
			brand = new BrandData();
		}

		txtCode.setText(brand.getCode());
		txtName.setText(brand.getName());
		txtLink.setText(brand.getLink());
	}

	@Override
	protected boolean doNew() {
		if (!super.doNew())
			return false;

		doDisplay(new BrandData());

		txtCode.requestFocus();

		return true;
	}

	@Override
	protected boolean doNewCopy() {
		if (!super.doNewCopy())
			return false;

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

		BrandData brand = getCurrentRecord();
		if (brand != null) {
			if (!getDB().brand.canRemove(brand)) {
				Dialogues.errMes(Loc.get("the_specified_brand_is_in_use_and_cannot_be_removed") + ".");
				requestFocusForTableView();
				return false;
			}

			String mes = Loc.get("are_you_sure_you_want_to_remove") + " \"" + brand.getCode() + "\"?";
			Dialogues.conMes(mes, (event) -> {
				if (getDB().brand.remove(brand)) {
					getTableView().getItems().remove(brand);
					getTableView().getSelectionModel().selectBelowCell();
				}
			});
		}

		requestFocusForTableView();
		return true;
	}

	@Override
	public void doOk() {
		BrandData brand = getEditedRecord();
		if (!check(brand))
			return;

		if (isAppendMode()) {
			getDB().brand.setEdited(true);

			getDB().brand.add(brand);
			getTableView().getItems().add(brand);
		} else {
			if (getCurrentRecord().hasDifference(brand)) {
				String oldValue = getCurrentRecord().getCode() == null ? "" : getCurrentRecord().getCode().trim();
				String newValue = brand.getCode() == null ? "" : brand.getCode().trim();
				if (!oldValue.equals(newValue))
					getDB().brand.setEdited(true);

				getDB().brand.set(brand);
				getTableView().getItems().set(getTableView().getSelectionModel().getSelectedIndex(), brand);
			}
		}

		getTableView().getSelectionModel().select(brand);

		if (isAppendMode())
			getTableView().scrollTo(brand);

		doCancel(false);
	}
}
