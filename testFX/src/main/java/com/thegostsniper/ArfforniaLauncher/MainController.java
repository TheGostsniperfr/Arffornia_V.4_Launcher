package com.thegostsniper.ArfforniaLauncher;

import fr.theshark34.openlauncherlib.util.Saver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable  {

		  private int menuSelectorVar = 1;

		  @FXML
		  private Button homeBtn, parameterBtn;
		  @FXML
		  private Pane settingsPagePane, homePagePane;
		  @FXML
		  private Slider ramSlider;
		  private int allocatedRamValue;

		  private Saver saver  = MainApp.getInstance().getSaver();



		  @FXML
		  public void onSliderChanged() {
					allocatedRamValue = (int) ramSlider.getValue() * 1024;

					if(saver.get("allocatedRam") != String.valueOf(allocatedRamValue)){
							  saver.set("allocatedRam",String.valueOf(allocatedRamValue));
					}
					System.out.println(allocatedRamValue);

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

		  @Override
		  public void initialize(URL url, ResourceBundle resourceBundle) {

					SystemInfo systemInfo = new SystemInfo();
					GlobalMemory memory = systemInfo.getHardware().getMemory();

					ramSlider.setMax((int) (Math.floor((Math.ceil(memory.getTotal() / Math.pow(1024, 2))) / 1024)));

					try {


							  if (saver.get("allocatedRam") != "") {
										allocatedRamValue = Integer.parseInt(saver.get("allocatedRam"));
							  } else {
										saver.set("allocatedRam", "3072");
										allocatedRamValue = 3072;
							  }
					}catch (Exception e){
							  System.out.println(e);
					}
					System.out.println("test Allocated ram : " + allocatedRamValue);
					ramSlider.setValue(allocatedRamValue/1024);








		  }
}