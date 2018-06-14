import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class MainWindowController{
	
	/*
	 * returns false if inputs cant be read correctly
	 */
	public boolean areFoldersInSegmentBinaryEqual(Pane parentA, Pane parentB, MainWindow mainWindow) {
		GuiAndWorkerSharedValues sharedValuesGuiWorker = new GuiAndWorkerSharedValues();
		File directoryOrFileA = null;
		File directoryOrFileB = null;
		FileComparison fileComparison = new FileComparison();
		
		//get textfield from file segment
		for (int i = 0; i < parentA.getChildrenUnmodifiable().size(); i++) {
			if (parentA.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentA.getChildrenUnmodifiable().get(i);
				directoryOrFileA = new File(textfield.getText());
			}
		}

		//get textfield from file segment
		for (int i = 0; i < parentB.getChildrenUnmodifiable().size(); i++) {
			if (parentB.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentB.getChildrenUnmodifiable().get(i);
				directoryOrFileB = new File(textfield.getText());
			}
		}
		
		
		Thread folderComparison = new Thread(new FolderComparison(directoryOrFileA, directoryOrFileB));
		
		// get values from textfield and read content as files 
		
		//first case is that folders/directories are in both segments
		if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isDirectory()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isDirectory()) {
			
			sharedValuesGuiWorker.setFinishedMappingA(false);
			sharedValuesGuiWorker.setFinishedMappingB(false);
			mainWindow.folderResults.getChildren().clear();
			sharedValuesGuiWorker.clearList();
			sharedValuesGuiWorker.setProgressValue(0);
			mainWindow.informationMappingFilesA.setGraphic(mainWindow.images.getWaitingIcon());mainWindow.informationMappingFilesA.setText("Started Mapping ");
			mainWindow.informationMappingFilesB.setText("Started Mapping ");mainWindow.informationMappingFilesB.setGraphic(mainWindow.images.getWaitingIcon());mainWindow.informationMappingFilesB.setAlignment(Pos.CENTER_RIGHT);
			mainWindow.compareFolder.setDisable(true);
			sharedValuesGuiWorker.setWorkerRunning(true);
			
			folderComparison.start();
			
			//return folderComparison.areFoldersEqual(directoryOrFileA, directoryOrFileB, target);
			

		// second case is that both values represent files
		} else if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isFile()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isFile()) {
			return fileComparison.areFilesBinaryEqual(directoryOrFileA, directoryOrFileB);
		} else {
			return false;
		}
		
		return false;
		
	}

	}
