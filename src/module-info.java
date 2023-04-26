module Edgar {
	requires javafx.controls;
	requires java.sql;
	requires java.desktop;
	requires javafx.base;
	requires junit;
	requires org.junit.jupiter.api;
	opens application to javafx.graphics, javafx.fxml;
}