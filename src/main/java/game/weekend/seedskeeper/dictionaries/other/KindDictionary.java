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

public class KindDictionary extends Journal<KindData> implements IReadOnly {

	// Поля
	private final XTextField txtName;

	// Кнопки управления
	private final Button btnNew = getButtonNew();
	private final Button btnEdit = getButtonEdit();
	private final Button btnDelete = getButtonDelete();

	private final Label lblTitle = new Label(Loc.get("kinds"));
	private final Label lblMode = new Label("");

	public KindDictionary(Journal<?> parentJournal) {
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

		// Объекты управления переводятся в режим просмотра
		setEditMode(false);
		// Текущей является первая строка в списке
		getTableView().getSelectionModel().selectFirst();

		return vb;
	}

	// Составные части журнала //

	// Экранная таблица
	private TableView<KindData> makeTableView() {
		// Колонки экранной таблицы

		// По этой колонке будет сортировка по умолчанию
		TableColumn<KindData, String> colName = getTextColumn(Loc.get("name"), "name", 260);
		getTableView().getColumns().add(colName);

		// Данные для экранной таблицы
		getTableView().setItems(getDB().kind.getListForTable());

		// Полезные перехваты клавиш и кликов
		makeTableHandlers(getTableView());

		// Сортировка по колонке по-умолчанию
		getTableView().getSortOrder().add(colName);
		getTableView().sort();

		// Размеры экранной таблицы
		getTableView().setPrefHeight(256);
		getTableView().setPrefWidth(300);

		return getTableView();
	}

	// Область для редактирования записи

	private VBox makeTextFields() {
		VBox vbName = new VBox();
		vbName.getChildren().addAll(new Label(Loc.get("name") + ":"), getTextBox("", 8, txtName, 32));

		VBox vb = new VBox();
		vb.setPadding(new Insets(5, 5, 5, 10));
		vb.getChildren().addAll(vbName, getVSpacer(), new Label(""), getVSpacer());
		return vb;
	}

	// Кнопки редактирования
	private HBox makeEditButtons() {
		HBox hb = new HBox(10);
		hb.setPadding(new Insets(5, 10, 0, 10));
		hb.getChildren().addAll(btnNew, btnEdit, btnDelete);
		return hb;
	}

	// Включение или выключение режима редактирования
	// (добавление и добавление копии это тоже режим редактирования)
	@Override
	protected void setEditMode(boolean editMode) {
		super.setEditMode(editMode);

		// Список
		getTableView().setDisable(editMode);

		// Поля
		txtName.setEditable(editMode);

		// Кнопки
		btnNew.setDisable(editMode);
		btnEdit.setDisable(editMode);
		btnDelete.setDisable(editMode);
	}

	@Override
	public KindData getEditedRecord() {
		KindData kind = new KindData(0, txtName.getText());
		if (!isAppendMode())
			kind.setId(getCurrentRecord().getId());

		return kind;
	}

	// Проверка записи
	private boolean check(KindData category) {
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
	protected void doDisplay(KindData KindData) {
		if (KindData == null) {
			KindData = new KindData();
		}

		txtName.setText(KindData.getName());
	}

	@Override
	protected boolean doNew() {
		if (!super.doNew())
			return false;

		KindData newQuality = new KindData();
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

		KindData kind = getCurrentRecord();
		if (kind != null) {

			if (!getDB().kind.canRemove(kind)) {
				Dialogues.errMes(Loc.get("the_specified_kind_is_in_use_and_cannot_be_removed") + ".");
				requestFocusForTableView();
				return false;
			}

			String mes = Loc.get("are_you_sure_you_want_to_remove") + ": \"" + kind.getName() + "\"?";
			Dialogues.conMes(mes, (event) -> {
				if (getDB().kind.remove(kind)) {
					getTableView().getItems().remove(kind);
					getTableView().getSelectionModel().selectBelowCell();
				}
			});
		}

		requestFocusForTableView();
		return true;
	}

	@Override
	public void doOk() {
		KindData kind = getEditedRecord();

		if (!check(kind))
			return;

		if (isAppendMode()) {
			getDB().kind.setEdited(true);

			// Добавление записи в таблицу БД
			getDB().kind.add(kind);

			// Добавление записи в экранную таблицу (можно в конец, т.к. метод завершится
			// вызовом getTableView().sort()
			getTableView().getItems().add(kind);

		} else {
			if (getCurrentRecord().hasDifference(kind)) { // Этот метод проверит два поля и именно они важны для
															// setEdited()
				getDB().kind.setEdited(true);

				getDB().kind.set(kind);
				getTableView().getItems().set(getTableView().getSelectionModel().getSelectedIndex(), kind);
			}
		}

		// Исправленная/новая запись делается текущей
		getTableView().getSelectionModel().select(kind);

		// Скроллинг к новой записи только при добавлении
		if (isAppendMode())
			getTableView().scrollTo(kind);

		// Не сортируем. Редактированное остается на месте, добавленное добавляется в
		// конец списка

		doCancel(false);
	}
}
