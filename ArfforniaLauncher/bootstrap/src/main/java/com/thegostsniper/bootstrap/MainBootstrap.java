package com.thegostsniper.bootstrap;

import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainBootstrap extends Application {
		  private Saver saver;
		  private String launcherVersion;
		  private String launcherActualVersion;




		  private final Path launcherDir = GameDirGenerator.createGameDir("Arffornia_V.4", true);
		  File launcherFile = new File(launcherDir.toString()+"/ArfforniaLauncher.jar");


		  public void CheckingForUpdate() {

					try {
							  URL launcherAcutalVersionURL = new URL("http://arffornia.ddns.net/public/dowload/launcherVersion.php");
							  URLConnection con = launcherAcutalVersionURL.openConnection();
							  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
							  launcherActualVersion = in.readLine();
							  //System.out.println("launcher actual version : " + launcherActualVersion);

					}catch (Exception e){
							  e.printStackTrace();
							  //System.out.println("Impossible d'obtenir la version de launcher");
					}


					if(saver.get("launcherVersion") == null){
							  //first starting
							  //System.out.println("first starting");
							  launcherVersion = "1";
							  launcherUpdater();

					}else {
							  //not the first starting
							  //System.out.println("not the first staring");
							  launcherVersion = saver.get("launcherVersion");
							  //System.out.println("launcher version : " + launcherVersion);




										//check if launcher is not up to date
										//System.out.println(launcherVersion + " " + launcherActualVersion);

										if (!Objects.equals(launcherVersion,launcherActualVersion)  || !launcherFile.exists()){
												  //System.out.println("launcher go to update");
												  launcherUpdater();
										}else {
												  //launcher is up to date, start him
												  //System.out.println("launche not got to update");
												  StartLauncher();
										}





					}

		  }


		  public void launcherUpdater(){
					MainController.getInstance().setAnchorBack(true);

					Platform.runLater(() -> {
									  MainController.getInstance().setUpdateLabel("Téléchargement de la mise à jour");
							});
					//remove actual launcher
					if(launcherFile.exists()){
							  //System.out.println("delete actual launcher");
							  launcherFile.delete();
					}

					//download actual launcher
					try {

							  //System.out.println("download new launcher");
							  URL launcherActualURL = new URL("http://arffornia.ddns.net/public/dowload/ArfforniaLauncher.jar");
							  FileUtils.copyURLToFile(launcherActualURL, launcherFile);


					}catch (Exception e){
							  e.printStackTrace();
							  //System.out.println(e.toString());
					}

					//System.out.println("test1");
					//save the version


					//System.out.println(launcherActualVersion);
					saver.set("launcherVersion", launcherActualVersion);




					//System.out.println("test2");

					//Sart the launcher
					StartLauncher();
		  }

		  public void StartLauncher(){
					Platform.runLater(() -> {
									  MainController.getInstance().setUpdateLabel("Lancement");
							});

					//Sarting launcher
					//System.out.println("Starting launcher");
					try{/*
							  Process p = Runtime.getRuntime().exec(launcherDir.resolve("Arffornia.jar").toString());

							  */

							  List<String> command = new ArrayList<String>();

							  command.add("java");
							  command.add("-jar");
							  command.add(launcherDir.resolve("ArfforniaLauncher.jar").toString());

							  ProcessBuilder builder = new ProcessBuilder(command);
							  Process process = builder.start();


							  //System.out.println("Exiting...");
							  System.exit(0);

					}catch (Exception e){
							  e.printStackTrace();
							  //System.out.println("Impossible de lancer le launcher");
					}

		  }

		  @Override
		  public void start(Stage stage) throws IOException, InterruptedException {
					Parent root = FXMLLoader.load(MainBootstrap.class.getResource("Main.fxml"));


					stage.setTitle("Arffornia Launcher");
					stage.getIcons().add(new Image(getClass().getResourceAsStream("images/Crafting_Table.png")));

					Scene scene = new Scene(root, 400, 350);
					stage.initStyle(StageStyle.TRANSPARENT);
					scene.setFill(Color.TRANSPARENT);
					scene.getStylesheets().add(String.valueOf(getClass().getResource("css/style.css")));



					stage.setScene(scene);
					stage.show();

					saver = new Saver(this.launcherDir.resolve("launcher.properties"));
					saver.load();


					Thread thread = new Thread(() -> CheckingForUpdate());
					thread.start();



		  }


		  public static void main(String[] args) {
					launch();
		  }
}