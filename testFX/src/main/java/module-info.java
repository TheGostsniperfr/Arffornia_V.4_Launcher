module com.thegostsniper.testfx {
		  requires javafx.controls;
		  requires javafx.fxml;
		  requires com.github.oshi;
		  requires openlauncherlib;
		  requires flowupdater;


		  opens com.thegostsniper.ArfforniaLauncher to javafx.fxml;
		  exports com.thegostsniper.ArfforniaLauncher;
}