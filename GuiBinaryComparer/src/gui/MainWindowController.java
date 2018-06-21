package gui;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import functions.FileComparison;
import functions.FolderComparison;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import miscellaneous.GuiAndWorkerSharedValues;
import miscellaneous.ImageLoader;
import miscellaneous.STATES;

public class MainWindowController {
	private MainWindow mainWindow;
	private ImageLoader images = new ImageLoader();
	
	public MainWindowController(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		mainWindow.compareFile.setOnAction(e -> {
			if (this.areFoldersInSegmentBinaryEqual(mainWindow.segmentA, mainWindow.segmentB)) {
				mainWindow.resultLabel.setText("Files are binary equal");
				mainWindow.resultLabel.setGraphic(images.getGreenCheckIcon());
			} else {
				mainWindow.resultLabel.setText("not equal Files");
				mainWindow.resultLabel.setGraphic(images.getRedUnCheck());
			}
		});
		
		mainWindow.compareFolder.setOnAction(e -> {
			GuiAndWorkerSharedValues.setTotalFiles(0);
			if(!this.areFoldersInSegmentBinaryEqual(mainWindow.folderSegmentA, mainWindow.folderSegmentB)) {
				mainWindow.informationMappingFilesA.setText("Select Directory");
				mainWindow.informationMappingFilesA.setGraphic(images.getRedUnCheck());
				mainWindow.informationMappingFilesB.setText("Select Directory");
				mainWindow.informationMappingFilesB.setGraphic(images.getRedUnCheck());
			}
		});
		
		//update permanent 
		Timeline updatingGuiTask = new Timeline(new KeyFrame(Duration.millis(50), e -> {
			mainWindow.mappedTotalFiles.setText(Integer.toString(GuiAndWorkerSharedValues.getTotalFiles()) + " - Files Mapped");
			//System.out.println(mainWindow.folderResults.getChildren().size());
		}));
		
		updatingGuiTask.setCycleCount(Timeline.INDEFINITE);
		updatingGuiTask.play();
	}
	
	/*
	 * returns false if inputs cant be read correctly
	 */
	public boolean areFoldersInSegmentBinaryEqual(Pane parentA, Pane parentB) {
		GuiAndWorkerSharedValues sharedValuesGuiWorker = new GuiAndWorkerSharedValues();
		File directoryOrFileA = null;
		File directoryOrFileB = null;
		FileComparison fileComparison = new FileComparison();

		// get textfield from file segment
		for (int i = 0; i < parentA.getChildrenUnmodifiable().size(); i++) {
			if (parentA.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentA.getChildrenUnmodifiable().get(i);
				directoryOrFileA = new File(textfield.getText());
			}
		}

		// get textfield from file segment
		for (int i = 0; i < parentB.getChildrenUnmodifiable().size(); i++) {
			if (parentB.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentB.getChildrenUnmodifiable().get(i);
				directoryOrFileB = new File(textfield.getText());
			}
		}


		

		// first case is that folders/directories are in both segments
		if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isDirectory()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isDirectory()) {

			mainWindow.folderResults.getChildren().clear();
			mainWindow.informationMappingFilesA.setGraphic(images.getWaitingIcon());
			mainWindow.informationMappingFilesA.setText("Started Mapping ");
			mainWindow.informationMappingFilesB.setText("Started Mapping ");
			mainWindow.informationMappingFilesB.setGraphic(images.getWaitingIcon());
			mainWindow.informationMappingFilesB.setAlignment(Pos.CENTER_RIGHT);
			mainWindow.compareFolder.setDisable(true);

			FolderComparison folderComparison = new FolderComparison(directoryOrFileA, directoryOrFileB, mainWindow);
			Thread thread = new Thread(folderComparison);

			folderComparison.messageProperty().addListener(e -> {
				if (folderComparison.getMessage().equals(STATES.FINISHED_FIRST_COMPARISON.toString())) {
					mainWindow.informationMappingFilesA.setGraphic(images.getGreenCheckIcon());
					mainWindow.informationMappingFilesA.setText("Finished Mapping");
				} else if (folderComparison.getMessage().equals(STATES.FINISHED_SECOND_COMPARISON.toString())) {
					mainWindow.informationMappingFilesB.setGraphic(images.getGreenCheckIcon());
					mainWindow.informationMappingFilesB.setText("Finished Mapping");
					// Thread sometimes updates so fast that up can not pick it up. first message
					// always already came if this message is displayed.
					// duplicate resolves the bug of not displaying first message result correctly
					mainWindow.informationMappingFilesA.setGraphic(images.getGreenCheckIcon());
					mainWindow.informationMappingFilesA.setText("Finished Mapping");
				}
			});

			folderComparison.valueProperty().addListener(e -> {
				ArrayList<HBox> tempList = new ArrayList<>();
				tempList = folderComparison.getValue();

				for (int i = 0; i < tempList.size(); i++) {
					// if box is not already placed on GUI then add
					if (!mainWindow.folderResults.getChildren().contains(tempList.get(i))) {
						mainWindow.folderResults.getChildren().add(tempList.get(i));
					}
				}
			});

			folderComparison.progressProperty().addListener(e -> {
				mainWindow.progressBar.setProgress(folderComparison.getProgress());
				Number number = Math.round(folderComparison.getProgress() * 100);
				mainWindow.percentageDone.setText(number.intValue() + "%");
			});

			folderComparison.setOnSucceeded(e -> {
				mainWindow.compareFolder.setDisable(false);
			});
			
			thread.start();
			
			// return folderComparison.areFoldersEqual(directoryOrFileA, directoryOrFileB,
			// target);

			// second case is that both values represent files
			return true;
		} else if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isFile()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isFile()) {
			return fileComparison.areFilesBinaryEqual(directoryOrFileA, directoryOrFileB);
		} else {
			return false;
		}

	}

	
}
