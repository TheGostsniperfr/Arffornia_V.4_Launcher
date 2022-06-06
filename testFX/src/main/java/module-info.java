module com.thegostsniper.testfx {
		  requires javafx.controls;
		  requires javafx.fxml;


		  opens com.thegostsniper.ArfforniaLauncher to javafx.fxml;
		  exports com.thegostsniper.ArfforniaLauncher;
}