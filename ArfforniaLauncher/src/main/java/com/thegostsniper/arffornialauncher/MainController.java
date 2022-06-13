package com.thegostsniper.arffornialauncher;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainController implements Initializable {

		  private int menuSelectorVar = 1;
		  private boolean isRuntime =false;

		  @FXML
		  private Button homeBtn, parameterBtn, pseudoValidateBtn, playButton, OnlineModeBtn, LogoutBtn;
		  @FXML
		  private TextField pseudoTextField, PasswordTextField;
		  @FXML
		  private ImageView PseudoImageView, OnlineModeImage;
		  @FXML
		  private Pane settingsPagePane, homePagePane, LogoutView, ConnexionView;
		  @FXML
		  private Slider ramSlider;
		  @FXML
		  private ProgressBar progressBar;
		  @FXML
		  private Label ramLabel, PseudoLabel;
		  private int allocatedRamValue;


		  private Saver saver  = MainApp.getInstance().getSaver();
		  private AuthInfos authInfos= null;
		  private  String gameVersion = "1.16.5";

		  private Boolean OnlineModeStatue = true;



		  @FXML
		  public void onSliderChanged() {
					allocatedRamValue = (int) ramSlider.getValue() * 1024;

					if(saver.get("allocatedRam") != String.valueOf(allocatedRamValue)){
							  saver.set("allocatedRam",String.valueOf(allocatedRamValue));
							  ramLabel.setText(allocatedRamValue + "Mo");
					}
					System.out.println(allocatedRamValue);

		  }


		  //Avatar Display Management
		  @FXML
		  void SendPseudo(){
					System.out.println("test du bouton valider");
					String username = pseudoTextField.getText();

					//Get Steve View
					Image steveAvatarImage = new Image(getClass().getResourceAsStream("images/Steve.png"));

					//test username field is empty
					if(username != ""){

							  //Max length Minecraft Pseudo Check
							  if (username.length() <= 16){
										System.out.println("pseudo entré : " + username);
										//Get avatar View
										Image playerAvatarImage = new Image("https://minotar.net/avatar/" + username + ".png");

										if(playerAvatarImage.getWidth() != 0){

												  //Authentification isTrue
												  PseudoImageView.setImage(playerAvatarImage);
												  saver.set("username", username);
												  ConnexionView.setVisible(false);
												  LogoutView.setVisible(true);
												  PseudoLabel.setText(username);
												  playButton.setDisable(false);


										}else {
												  PseudoImageView.setImage(steveAvatarImage);
										}
							  }else {
										//Pseudo is too long
										pseudoTextField.setText("");
							  }




					}else {
							  //Authentification isFalse
							  //pop up : "no username !"
							  System.out.println("Aucun pseudo !");
							  PseudoImageView.setImage(steveAvatarImage);

					}

		  }
		  @FXML
		  void menuSwitchEvent(ActionEvent e) {
					// menu choice
					if (e.getSource() == homeBtn & menuSelectorVar != 1) {
							  System.out.println("home button pressed");
							  menuSelectorVar = 1;
							  updateStyleBtn();
					} else if (e.getSource() == parameterBtn & menuSelectorVar != 2) {
							  System.out.println("parameter button pressed");
							  menuSelectorVar = 2;
							  updateStyleBtn();
					}
		  }

		  void updateStyleBtn() {
					if (menuSelectorVar == 1) {
							  //set style to homeBtn
							  homeBtn.getStyleClass().add("menu_test");
							  parameterBtn.getStyleClass().remove("menu_test");

							  //switch  Pane
							  homePagePane.setVisible(true);
							  settingsPagePane.setVisible(false);

					} else {
							  //set style to parameterBtn
							  parameterBtn.getStyleClass().add("menu_test");
							  homeBtn.getStyleClass().remove("menu_test");

							  //switch Pane
							  homePagePane.setVisible(false);
							  settingsPagePane.setVisible(true);
					}
		  }

		  @FXML
		  void OnlineMode(){
					if (OnlineModeStatue == true){
							  OnlineModeStatue = false;
							  OnlineModeImage.setImage(new Image(getClass().getResourceAsStream("images/no-wifi.png")));
							  PasswordTextField.setDisable(true);
							  OnlineModeBtn.setText("Offline mode");
							  PasswordTextField.setText("");
							  saver.set("onlineMode", "false");
							  System.out.println("onlineMode Saver turn false");

					}else {
							  OnlineModeStatue = true;
							  OnlineModeImage.setImage(new Image(getClass().getResourceAsStream("images/wifi.png")));
							  PasswordTextField.setDisable(false);
							  OnlineModeBtn.setText("Online mode");
							  saver.set("onlineMode", "true");
							  System.out.println("onlineMode Saver turn true");


					}
		  }

		  @FXML
		  void Logout(){
					PseudoImageView.setImage(new Image(getClass().getResourceAsStream("images/Steve.png")));
					saver.set("username", "");
					ConnexionView.setVisible(true);
					LogoutView.setVisible(false);
					playButton.setDisable(true);

		  }



		  public void  update()  {
					IProgressCallback callback = new IProgressCallback() {
							  @Override
							  public void update(DownloadList.DownloadInfo info) {
										IProgressCallback.super.update(info);
										if(info.getDownloadedBytes() != 0) {
												  double progressBarValue = (double) info.getDownloadedBytes() / info.getTotalToDownloadBytes();
												  progressBar.setProgress(progressBarValue);
										}
							  }
					};
					try {
							  VanillaVersion version = new VanillaVersion.VanillaVersionBuilder().withName(gameVersion).build();
							  UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();
							  FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(version).withProgressCallback(callback).withUpdaterOptions(options).build();
							  updater.update(MainApp.getInstance().getLauncherDir());
							  startGame();

					}catch (Exception e){
							  e.printStackTrace();
					}
		  }




		  public void startGame(){
					try {
							  //Recupération des infos
							  this.authInfos = new AuthInfos(pseudoTextField.getText(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
							  GameInfos infos = new GameInfos("Arffornia_V.4", new GameVersion(gameVersion, GameType.V1_13_HIGHER_VANILLA), new GameTweak[]{});

							  ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER , authInfos);

							  //add memory to
							  profile.getVmArgs().add("-Xmx" + saver.get("allocatedRam") + "M");

							  ExternalLauncher launcher = new ExternalLauncher(profile);

							  Process p = launcher.launch();

							  Platform.runLater(() -> {
										playButton.setText("En cours");

										//reset progressBar
										progressBar.setProgress(0);
										progressBar.setVisible(false);

							  });

							  new Thread(new Runnable() {
										@Override
										public void run() {
												  try {
															p.waitFor();
															Platform.runLater(() -> {
																	  //reset playButton
																	  playButton.setText("Jouer");

																	  //reset playButton
																	  playButton.setDisable(false);

																	  //turn false isDownloading
																	  isRuntime = false;

																	  //parameterBtn active
																	  parameterBtn.setDisable(false);
																	  LogoutBtn.setDisable(false);

															});
												  } catch (InterruptedException e) {
															e.printStackTrace();
												  }
										}
							  }).start();



					}catch (Exception e){
							  e.printStackTrace();
							  //input in logger
					}

		  }

		  //When the play button is pressed
		  public void play(){
					//verif double click
					if(isRuntime == false){

							  playButton.setText("Lancement...");
							  isRuntime = true;
							  progressBar.setVisible(true);
							  playButton.setDisable(true);

							  //parameterBtn active
							  parameterBtn.setDisable(true);
							  LogoutBtn.setDisable(true);



							  //call update function in new thread
							  Platform.runLater(() -> new Thread(this::update).start());

					}
		  }

		  @Override
		  public void initialize(URL url, ResourceBundle resourceBundle) {

					SystemInfo systemInfo = new SystemInfo();
					GlobalMemory memory = systemInfo.getHardware().getMemory();

					ramSlider.setMax((int) (Math.floor((Math.ceil(memory.getTotal() / Math.pow(1024, 2))) / 1024)));

					try {
							  if(saver.get("username") != null){
										pseudoTextField.setText(saver.get("username"));
										SendPseudo();
							  }
							  if (saver.get("allocatedRam") != null) {
										allocatedRamValue = Integer.parseInt(saver.get("allocatedRam"));
							  } else {
										saver.set("allocatedRam", "3072");
										allocatedRamValue = 3072;
							  }

							  //init ConnexionView
							  if (saver.get("onlineMode") != null){
										//if online mode saver is false, init onlineMode false view
										if (Objects.equals(saver.get("onlineMode"), "false")){
												  OnlineMode();
										}
							  }
					}catch (Exception e){
							  System.out.println(e);
					}
					System.out.println("test Allocated ram : " + allocatedRamValue);
					ramSlider.setValue(allocatedRamValue/1024);

		  }
}
