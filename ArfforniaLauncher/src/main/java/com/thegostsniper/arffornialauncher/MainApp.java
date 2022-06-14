package com.thegostsniper.arffornialauncher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class MainApp extends Application {

		  private Saver saver;
		  private ILogger logger;


		  private final Path launcherDir = GameDirGenerator.createGameDir("Arffornia_V.4", true);
		  private static MainApp instance;


		  public MainApp() {
					instance = this;
					this.logger = new Logger("[Arffornia_Launcher]", Path.of(this.launcherDir.toString(), "launcher.log"));
					if (!this.launcherDir.toFile().exists()) {
							  if (!this.launcherDir.toFile().mkdir()) {
										this.logger.err("Unable to create launcher folder");
							  }
					}
					saver = new Saver(this.launcherDir.resolve("launcher.properties"));
					saver.load();


		  }


		  @Override
		  public void start(Stage primaryStage) throws IOException {
					this.logger.info("Starting launcher");
					Parent root = FXMLLoader.load(MainApp.class.getResource("Main.fxml"));

					primaryStage.setTitle("Arffornia Launcher");
					primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/Crafting_Table.png")));

					Scene scene = new Scene(root, 1280, 720);
					scene.getStylesheets().add(String.valueOf(getClass().getResource("css/style.css")));


					primaryStage.setScene(scene);
					primaryStage.show();


		  }


		  public Saver getSaver() {
					return saver;
		  }

		  public ILogger getLogger() { return  logger; }

		  public Path getLauncherDir() {
					return launcherDir;
		  }

		  public static MainApp getInstance() {
					return instance;
		  }


		  public static void main(String[] args) {
					launch();
		  }

}
