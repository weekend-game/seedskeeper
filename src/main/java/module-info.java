module SeedsKeeper {
	requires javafx.base;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	requires transitive java.sql;
	requires org.apache.commons.io;
	requires org.apache.poi.poi;
	requires org.apache.derby.engine;
	requires javafx.swing;
	requires org.apache.poi.ooxml;
	requires org.apache.commons.text;

	opens game.weekend.seedskeeper to javafx.graphics, javafx.fxml;

	exports game.weekend.seedskeeper;
	exports game.weekend.seedskeeper.general;
	exports game.weekend.seedskeeper.journals.seeds;
}