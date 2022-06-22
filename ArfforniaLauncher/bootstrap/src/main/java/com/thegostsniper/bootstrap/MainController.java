package com.thegostsniper.bootstrap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MainController {
		  @FXML
		  private Label updateLabel;

		  @FXML
		  private AnchorPane anchorBack;

		  private static MainController instance;


		  public void setUpdateLabel(String newText){
					updateLabel.setText(newText);
		  }
		  public void setAnchorBack(Boolean newStatue){
					anchorBack.setVisible(newStatue);
		  }


		  public MainController(){
					instance = this;
		  }

		  public static MainController getInstance() {
					return instance;
		  }




}