package game.weekend.seedskeeper.general;

import java.util.ArrayList;

import game.weekend.seedskeeper.controls.HyperlinkCellFactory;
import game.weekend.seedskeeper.controls.XTableView;
import game.weekend.seedskeeper.data.db.DB;
import game.weekend.seedskeeper.data.types.Record;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class Journal<T> {

	private final XTableView<T> tableView = new XTableView<>(this);

	private boolean readOnlyMode = false;

	private boolean editMode = false;

	private boolean appendMode = false;

	private boolean enterWhileEditing = false;

	private ArrayList<IReadOnly> listOfReadOnly = new ArrayList<>();

	private Node currentNode = null;

	public void addReadOnlyObject(IReadOnly object) {
		listOfReadOnly.add(object);
	}

	public VBox getPane() {
		VBox vb = new VBox();
		vb.setPadding(new Insets(10, 10, 10, 10));
		return vb;
	}

	public DB getDB() {
		return DB.getInstance();
	}

	public TableView<T> getTableView() {
		return tableView;
	}

	public boolean isEditMode() {
		return editMode;
	}

	protected void setEditMode(boolean editMode) {
		for (IReadOnly o : listOfReadOnly)
			o.setReadOnlyMode(editMode);

		this.editMode = editMode;
	}

	protected boolean isAppendMode() {
		return appendMode;
	}

	protected void setAppendMode(boolean appendMode) {
		this.appendMode = appendMode;
	}

	protected void setReadOnlyMode(boolean readOnly) {
		this.readOnlyMode = readOnly;
	}

	protected boolean isReadOnlyMode() {
		return readOnlyMode;
	}

	public T getCurrentRecord() {
		return tableView.getSelectionModel().getSelectedItem();
	}

	public T getEditedRecord() {
		return tableView.getSelectionModel().getSelectedItem();
	}

	protected void displayMode(String mode) {
	}

	public void makeTableHandlers(TableView<T> tableView) {

		tableView.addEventHandler(KeyEvent.KEY_RELEASED, (e) -> {
			if (e.getCode() == KeyCode.INSERT) {
				doNew();
			} else if (e.getCode() == KeyCode.ENTER) {
				if (enterWhileEditing)
					enterWhileEditing = false;
				else
					doEdit();
			} else if (e.getCode() == KeyCode.DELETE) {
				doDelete();
			}
		});

		tableView.setOnMouseClicked(me -> {
			if (me.getClickCount() == 2)
				doEdit();
		});

		tableView.getSelectionModel().selectedItemProperty().addListener((value, oldVal, newVal) -> doDisplay(newVal));
	}

	public TableColumn<T, String> getTextColumn(String title, String field, int width) {
		TableColumn<T, String> column = new TableColumn<>(title);
		column.setCellValueFactory(new PropertyValueFactory<>(field));
		column.setCellFactory(TextFieldTableCell.forTableColumn());
		column.setEditable(false);
		column.setPrefWidth(width);
		return column;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TableColumn<T, String> getLinkColumn(String title, String field, int width) {
		TableColumn<T, String> column = new TableColumn<>(title);
		column.setCellValueFactory(new PropertyValueFactory<>(field));
		column.setCellFactory(new HyperlinkCellFactory());
		column.setEditable(false);
		column.setPrefWidth(width);
		return column;
	}

	protected HBox getTextBox(String name, double width, TextField txtField, int columnCount) {
		Label lblName = new Label(name);
		lblName.setMinWidth(width);
		txtField.setPrefColumnCount(columnCount);

		HBox hb = new HBox();
		hb.setSpacing(5);
		hb.setPadding(new Insets(3, 0, 3, 0));
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.getChildren().addAll(lblName, txtField);

		return hb;
	}

	protected Button getButtonNew() {
		Button btn = new Button(Loc.get("new"));
		btn.setOnAction((e) -> doNew());
		return btn;
	}

	protected Button getButtonNewCopy() {
		Button btn = new Button(Loc.get("new_copy"));
		btn.setOnAction((e) -> doNewCopy());
		return btn;
	}

	protected Button getButtonEdit() {
		Button btn = new Button(Loc.get("edit"));
		btn.setOnAction((e) -> doEdit());
		return btn;
	}

	protected Button getButtonDelete() {
		Button btn = new Button(Loc.get("delete") + "...");
		btn.setOnAction((e) -> doDelete());
		return btn;
	}

	protected Button getButtonOk() {
		Button btn = new Button(Loc.get("save"));
		btn.setOnAction((e) -> doOk());
		return btn;
	}

	protected Button getButtonCancel() {
		Button btn = new Button(Loc.get("cancel"));
		btn.setOnAction(e -> doCancel(true));
		return btn;
	}

	protected Pane getHSpacer() {
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		spacer.setMinSize(5, 5);
		return spacer;
	}

	protected Pane getHSpacer(int width) {
		Pane spacer = new Pane();
		spacer.setMinSize(width, 5);
		return spacer;
	}

	protected Pane getVSpacer() {
		Pane spacer = new Pane();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		spacer.setMinSize(5, 5);
		return spacer;
	}

	protected Pane getVSpacer(int hight) {
		Pane spacer = new Pane();
		spacer.setMinHeight(hight);
		return spacer;
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public void writeProp() {
	}

	public void enter() {
		if (isEditMode()) {
			enterWhileEditing = true;
			doOk();
		}
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node node) {
		this.currentNode = node;
	}

	public void requestFocusForTableView() {
		Platform.runLater((() -> getTableView().requestFocus()));
		Platform.runLater((() -> getTableView().getFocusModel().focusRightCell()));
	}

	protected void doFilter() {
	}

	protected void doDisplay(T o) {
	}

	protected boolean doNew() {
		if (isReadOnlyMode())
			return false;

		displayMode(Loc.get("adding"));
		setAppendMode(true);
		setEditMode(true);

		return true;
	}

	protected boolean doNewCopy() {
		if (isReadOnlyMode())
			return false;

		if (getCurrentRecord() == null)
			return false;

		displayMode(Loc.get("adding_a_copy"));
		setAppendMode(true);
		setEditMode(true);

		return true;
	}

	protected boolean doEdit() {
		if (isReadOnlyMode())
			return false;

		if (getCurrentRecord() == null)
			return false;

		displayMode(Loc.get("editing"));
		setAppendMode(false);
		setEditMode(true);

		return true;
	}

	protected boolean doDelete() {
		return (!isReadOnlyMode());
	}

	public void doOk() {
	}

	public void doCancel(boolean checkChange) {
		if (checkChange) {
			Record newRec = (Record) getEditedRecord();
			Record oldRec = (Record) getCurrentRecord();

			if (isAppendMode() || oldRec.hasDifference(newRec))
				if (!Dialogues.getConfirmation(
						Loc.get("are_you_sure_you_want_to_revert_the_changes") + "\n" + oldRec.getDifferences()))
					return;
		}

		displayMode("");
		setAppendMode(false);
		setEditMode(false);

		doDisplay(getCurrentRecord());
		if (getTableView() != null)
			getTableView().requestFocus();
	}
}
