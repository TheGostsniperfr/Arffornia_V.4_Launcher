module com.thegostsniper.testfx {
		  requires javafx.controls;
		  requires javafx.fxml;
		  requires com.github.oshi;
		  requires openlauncherlib;


		  opens com.thegostsniper.ArfforniaLauncher to javafx.fxml;
		  exports com.thegostsniper.ArfforniaLauncher;
}