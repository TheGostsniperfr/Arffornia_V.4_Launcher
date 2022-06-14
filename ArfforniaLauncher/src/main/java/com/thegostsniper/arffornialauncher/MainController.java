package com.thegostsniper.arffornialauncher;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.json.CurseFileInfo;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.download.json.OptiFineInfo;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
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

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

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
		  private  String gameVersion = "1.18.2";
		  private  String gameForgeVersion = "40.1.51";
		  private String gameMCPVersion = "20220404.173914";
		  private String gameOptifineVersion = "1.18.2_HD_U_H7";

		  private Boolean OnlineModeStatue = true;
		  private MicrosoftAuthResult authentificationResult = null;


		  @FXML
		  public void onSliderChanged() {
					allocatedRamValue = (int) ramSlider.getValue() * 1024;

					if(saver.get("allocatedRam") != String.valueOf(allocatedRamValue)){
							  saver.set("allocatedRam",String.valueOf(allocatedRamValue));
							  ramLabel.setText(allocatedRamValue + "Mo");
					}
					System.out.println(allocatedRamValue);


		  }

		  @FXML
		  void SendPseudo(){
					System.out.println("button sendPseudo pressed");

					//Check username not empty
					if(!pseudoTextField.getText().isEmpty()) {
							  //when the playButton is pressed, send the pseudo to authenticated him
							  if (OnlineModeStatue == true) {
										authentication(pseudoTextField.getText(), PasswordTextField.getText());
							  } else {
										authentication(pseudoTextField.getText());
							  }
					}else {
							  System.out.println("username is empty");
							  MainApp.getInstance().getLogger().err("username is empty");
					}
		  }

		  public void authentication(String username){
					if(username.length() <= 16){
							  saver.set("username", username);
							  saver.set("accessToken", "");
							  saver.set("refreshToken", "");
							  switchConnexionView(true);

					}else {
							  System.out.println("username too long");
					}


		  }
		  public void authentication(String email, String password) {
					String username;
					if (!email.isEmpty() && !password.isEmpty()) {

							  //if player have token
							  MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();

							  try {

										authentificationResult = authenticator.loginWithCredentials(email, password);
										username = authentificationResult.getProfile().getName();
										System.out.println("comtpe ok, username : " + username);

										saver.set("username", authentificationResult.getProfile().getName());
										saver.set("accessToken", authentificationResult.getAccessToken());
										saver.set("refreshToken", authentificationResult.getRefreshToken());

										switchConnexionView(true);




							  } catch (MicrosoftAuthenticationException e) {
										MainApp.getInstance().getLogger().err(e.toString());
										e.printStackTrace();
							  }

					}
		  }


		  public void switchConnexionView(Boolean statue){
					String username = saver.get("username");

					//Get Steve View
					Image steveAvatarImage = new Image(getClass().getResourceAsStream("images/Steve.png"));

					if(!username.isEmpty()){
							  //Get avatar View
							  Image playerAvatarImage = new Image("https://minotar.net/avatar/" + username + ".png");

							  if (playerAvatarImage.getWidth() != 0) {

										//Authentification isTrue
										PseudoImageView.setImage(playerAvatarImage);
							  }

					} else {
							  PseudoImageView.setImage(steveAvatarImage);
					}


					PseudoLabel.setText(username);
					playButton.setDisable(!statue);

					ConnexionView.setVisible(!statue);
					LogoutView.setVisible(statue);




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
							  pseudoTextField.setPromptText("Taper votre pseudo");

							  System.out.println("onlineMode Saver turn false");

					}else {
							  OnlineModeStatue = true;
							  OnlineModeImage.setImage(new Image(getClass().getResourceAsStream("images/wifi.png")));
							  PasswordTextField.setDisable(false);
							  OnlineModeBtn.setText("Online mode");
							  pseudoTextField.setPromptText("Taper votre email");
							  saver.set("onlineMode", "true");
							  System.out.println("onlineMode Saver turn true");


					}
		  }

		  @FXML
		  void Logout(){
					PseudoImageView.setImage(new Image(getClass().getResourceAsStream("images/Steve.png")));
					saver.set("username", "");
					saver.set("accessToken", "");
					saver.set("refreshToken", "");

					switchConnexionView(false);

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
							  final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
									  .withName(gameVersion)
									  .build();

							  final UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
									  .build();

							  List<CurseFileInfo> curseMods = CurseFileInfo.getFilesFromJson("http://arffornia.ddns.net/public/dowload/Arffornia_V.4_mods_list.json");
							  List<Mod> mods = Mod.getModsFromJson("http://arffornia.ddns.net/public/dowload/Arffornia_V.4_mods_list.json");



							  final AbstractForgeVersion forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
									  .withForgeVersion(gameForgeVersion)
									  .withCurseMods(curseMods)
									  .withMods(mods)
									  .withOptiFine(new OptiFineInfo(gameOptifineVersion, false))
									  .withFileDeleter(new ModFileDeleter(true))
									  .build();


							  final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
									  .withVanillaVersion(version)
									  .withModLoaderVersion(forgeVersion)
									  .withLogger(MainApp.getInstance().getLogger())
									  .withProgressCallback(callback)
									  .withUpdaterOptions(options)
									  .build();


							  updater.update(MainApp.getInstance().getLauncherDir());
							  startGame();

					}catch (Exception e){
							  MainApp.getInstance().getLogger().err(e.toString());
							  e.printStackTrace();
					}
		  }




		  public void startGame(){
					try {
							  //Recupération des infos

							  //check online mode
							  if(OnlineModeStatue == false) {
										this.authInfos = new AuthInfos(pseudoTextField.getText(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), "", "");
							  }else {
										this.authInfos = new AuthInfos(pseudoTextField.getText(), authentificationResult.getAccessToken(), authentificationResult.getProfile().getId(), "", "");

							  }
							  System.out.println("test gameInfo");
							  GameInfos infos = new GameInfos("Arffornia_V.4",
									  new GameVersion(gameVersion, GameType.V1_13_HIGHER_FORGE.setNFVD(new NewForgeVersionDiscriminator(gameForgeVersion, gameVersion,gameMCPVersion))),
									  new GameTweak[]{});

							  //ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER , authInfos);
							  NoFramework noFramework = new NoFramework(MainApp.getInstance().getLauncherDir(),
									  authInfos, GameFolder.FLOW_UPDATER,
									  Arrays.asList("-Xms256m", "-Xmx" + saver.get("allocatedRam") + "m"), Collections.emptyList());

							 Process p =  noFramework.launch(gameVersion, gameForgeVersion);

							  //add memory to
							  //profile.getVmArgs().add("-Xmx" + saver.get("allocatedRam") + "M");

							  //ExternalLauncher launcher = new ExternalLauncher(profile);


							  //Process p = launcher.launch();

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
															MainApp.getInstance().getLogger().err(e.toString());

															e.printStackTrace();
												  }
										}
							  }).start();



					}catch (Exception e){
							  MainApp.getInstance().getLogger().err(e.toString());

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

							  if (OnlineModeStatue == false){
										if(!saver.get("username").isEmpty()) {


												  //offline connexion
												  authentication(saver.get("username"));
										} else if (!saver.get("accessToken").isEmpty() && !saver.get("refreshToken").isEmpty() ) {
												  //online connexion
												  authentication(saver.get("accessToken"), saver.get("refreshToken"));

										}

							  }
					}catch (Exception e){
							  MainApp.getInstance().getLogger().err(e.toString());
							  System.out.println(e);
					}
					System.out.println("test Allocated ram : " + allocatedRamValue);
					ramSlider.setValue(allocatedRamValue/1024);

		  }
}
