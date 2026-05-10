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

public class CategoryDictionary extends Journal<CategoryData> implements IReadOnly {

	// Поля
	private final XTextField txtName;

	// Кнопки управления
	private final Button btnNew = getButtonNew();
	private final Button btnEdit = getButtonEdit();
	private final Button btnDelete = getButtonDelete();
	private final Button btnShiftUp = getButtonShiftUp();
	private final Button btnShiftDown = getButtonShiftDown();

	private final Label lblTitle = new Label(Loc.get("categories"));
	private final Label lblMode = new Label("");

	public CategoryDictionary(Journal<?> parentJournal) {
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
	private TableView<CategoryData> makeTableView() {
		// Колонки экранной таблицы

		// По этой колонке будет сортировка по умолчанию
		TableColumn<CategoryData, Integer> colNumb = getIntColumn(Loc.get("number"), "numb", 75);
		getTableView().getColumns().add(colNumb);

		getTableView().getColumns().add(getTextColumn(Loc.get("name"), "name", 220));

		// Данные для экранной таблицы
		getTableView().setItems(getDB().category.getListForTable());

		// Полезные перехваты клавиш и кликов
		makeTableHandlers(getTableView());

		/*
		 * Т.к. это справочник с произвольным расположением записей, то указываю колонку
		 * numb для сортировки и сортирую список таблицы. Пользователь может
		 * отсорировать по другим колонкам, но для сохранения списка в отсортированном
		 * состоянии метод doOK() обязан вызывать getTableView().sort() и
		 * getTableView().refresh() если значение в сортируемой колонке не поменялось.
		 */
		getTableView().getSortOrder().add(colNumb);
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
		vb.getChildren().addAll(vbName, getVSpacer(), btnShiftUp, new Label(""), btnShiftDown, getVSpacer());
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
		btnShiftUp.setDisable(editMode);
		btnShiftDown.setDisable(editMode);
	}

	@Override
	public CategoryData getEditedRecord() {
		int numb = (getCurrentRecord() == null) ? 0 : getCurrentRecord().getNumb();
		CategoryData categoryData = new CategoryData(0, numb, txtName.getText());
		if (!isAppendMode())
			categoryData.setId(getCurrentRecord().getId());
		else
			categoryData.setNumb(++numb);

		return categoryData;
	}

	// Проверка записи
	private boolean check(CategoryData category) {
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
	protected void doDisplay(CategoryData categoryData) {
		if (categoryData == null) {
			categoryData = new CategoryData();
		}

		txtName.setText(categoryData.getName());
	}

	@Override
	protected boolean doNew() {
		if (!super.doNew())
			return false;

		CategoryData newQuality = new CategoryData();
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

		CategoryData category = getCurrentRecord();
		if (category != null) {

			if (!getDB().category.canRemove(category)) {
				Dialogues.errMes(Loc.get("the_specified_category_is_in_use_and_cannot_be_removed") + ".");
				requestFocusForTableView();
				return false;
			}

			String mes = Loc.get("are_you_sure_you_want_to_remove_the_category") + ": \"" + category.getName() + "\"?";
			Dialogues.conMes(mes, (event) -> {
				if (getDB().category.remove(category)) {

					// Перенумерация списка таблицы (поле numb)
					for (CategoryData q : getTableView().getItems())
						if (q.getNumb() > category.getNumb())
							q.setNumb(q.getNumb() - 1);

					getTableView().getSelectionModel().selectBelowCell();
					getTableView().getItems().remove(category);

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
		CategoryData categoryData = getEditedRecord();
		if (!check(categoryData))
			return;

		if (isAppendMode()) {
			getDB().category.setEdited(true);

			// Добавление записи в таблицу БД
			getDB().category.add(categoryData);

			// Перенумерация списка таблицы (поле numb)
			for (CategoryData q : getTableView().getItems())
				if (q.getNumb() >= categoryData.getNumb())
					q.setNumb(q.getNumb() + 1);

			// Добавление записи в экранную таблицу (можно в конец, т.к. метод завершится
			// вызовом getTableView().sort()
			getTableView().getItems().add(categoryData);

		} else {
			if (getCurrentRecord().hasDifference(categoryData)) { // Этот метод проверит два поля и именно они важны для
																	// setEdited()
				getDB().category.setEdited(true);

				getDB().category.set(categoryData);
				getTableView().getItems().set(getTableView().getSelectionModel().getSelectedIndex(), categoryData);
			}
		}

		// Исправленная/новая запись делается текущей
		getTableView().getSelectionModel().select(categoryData);

		// Обязательно отсортировать
		getTableView().sort();

		doCancel(false);
	}

	@Override
	protected void doShiftUp() {
		CategoryData currentCategory = getCurrentRecord();
		CategoryData nextCategory = null;

		int currentNumb = (currentCategory == null) ? 0 : currentCategory.getNumb();
		if (currentNumb > 1) {

			for (CategoryData s : getTableView().getItems())
				if (s.getNumb() == currentNumb - 1) {
					nextCategory = s;
					break;
				}

			if (nextCategory != null) {
				currentCategory.setNumb(currentNumb - 1);
				nextCategory.setNumb(currentNumb);

				getDB().category.setNumb(currentCategory.getId(), currentCategory.getNumb(), nextCategory.getId(),
						nextCategory.getNumb());
			}

			getTableView().sort();

			// Если текущая сортировка будет не по полю numb, или её не будет вообще,
			// то без этого вызова на экране не будет отображено изменение в numb
			getTableView().refresh();
		}
		getTableView().requestFocus();
	}

	@Override
	protected void doShiftDown() {
		CategoryData currentCategory = getCurrentRecord();
		CategoryData nextCategory = null;

		int currentNumb = (currentCategory == null) ? 0 : currentCategory.getNumb();
		if (currentNumb > 0 && currentNumb < getTableView().getItems().size()) {

			for (CategoryData q : getTableView().getItems())
				if (q.getNumb() == currentNumb + 1) {
					nextCategory = q;
					break;
				}

			if (nextCategory != null) {
				currentCategory.setNumb(currentNumb + 1);
				nextCategory.setNumb(currentNumb);

				getDB().category.setNumb(currentCategory.getId(), currentCategory.getNumb(), nextCategory.getId(),
						nextCategory.getNumb());
			}

			getTableView().sort();

			// Если текущая сортировка будет не по полю numb, или её не будет вообще,
			// то без этого вызова на экране не будет отображено изменение в numb
			getTableView().refresh();
		}
		getTableView().requestFocus();
	}
}
