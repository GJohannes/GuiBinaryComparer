import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl;
import com.sun.xml.internal.ws.client.sei.ValueSetter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindow {
	private int windowSize = 800;
	VBox folderResults;
	Label informationMappingFilesA;
	Label informationMappingFilesB;
	ImageLoader images = new ImageLoader();
	Button compareFolder;
	
	public void start(Stage stage) {
		Scene scene = mainWindowVisuals(stage);
		stage.setMaxWidth(windowSize);
		stage.setTitle("BinaryComparer");
		stage.setScene(scene);
		stage.show();
	}

	private Scene mainWindowVisuals(Stage stage) {
		VisualSegments segment = new VisualSegments();
		
		VBox root = new VBox();root.setSpacing(20);root.setStyle("-fx-background-color: #b7b7db;");root.setPrefWidth(windowSize);
			HBox titleBox = new HBox();titleBox.setAlignment(Pos.CENTER);titleBox.centerShapeProperty();
				Label title = new Label(); title.setGraphic(images.getTitleImage());
			titleBox.getChildren().addAll(title);

			HBox fileComparison = new HBox();fileComparison.setSpacing(10);fileComparison.setAlignment(Pos.CENTER);
				VBox resultBox = new VBox();
					Label resultLabel = new Label();resultLabel.setText("Result:               ");
					Button compareFile = new Button("Compare Both Files");
				resultBox.getChildren().addAll(resultLabel, compareFile);

				VBox segmentA = segment.getFileOrFolderComparisonSegment(FileOrDirectory.FILE);
				VBox segmentB = segment.getFileOrFolderComparisonSegment(FileOrDirectory.FILE);
			fileComparison.getChildren().addAll(segmentA, resultBox, segmentB);
		
			VBox folderComparisonAndResult = new VBox();
				HBox folderComparison = new HBox(); folderComparison.setAlignment(Pos.CENTER); folderComparison.setSpacing(30);
					VBox folderSegmentA = segment.getFileOrFolderComparisonSegment(FileOrDirectory.DIRECTORY);
					compareFolder = new Button("Compare both folders");
					VBox folderSegmentB = segment.getFileOrFolderComparisonSegment(FileOrDirectory.DIRECTORY);
					folderComparison.getChildren().addAll(folderSegmentA,compareFolder,folderSegmentB);
					
					HBox infoBox = new HBox();infoBox.setAlignment(Pos.CENTER);infoBox.centerShapeProperty();infoBox.setSpacing(80);
						informationMappingFilesA = new Label();informationMappingFilesA.setAlignment(Pos.CENTER_LEFT);
						VBox progress = new VBox();progress.setAlignment(Pos.CENTER);						
							Label percentageDone = new Label();percentageDone.setTextAlignment(TextAlignment.CENTER);
							ProgressBar progressBar = new ProgressBar(0.0);progressBar.setPrefWidth(150);
						progress.getChildren().addAll(progressBar,percentageDone);
						informationMappingFilesB = new Label();
						infoBox.getChildren().addAll(informationMappingFilesA, progress, informationMappingFilesB);
					folderResults = new VBox();folderResults.setAlignment(Pos.CENTER);
//						Pane scrollPane = new Pane();
//						folderResults.getChildren().add(scrollPane);
			folderComparisonAndResult.getChildren().addAll(folderComparison,infoBox,folderResults);
		
		root.getChildren().addAll(titleBox, fileComparison,folderComparisonAndResult);
		
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(root);
		
		Scene scene = new Scene(scroll, windowSize, 400, Color.BLACK);
		MainWindowController controller = new MainWindowController();
		
		compareFile.setOnAction(e -> {
			if (controller.areFoldersInSegmentBinaryEqual(segmentA, segmentB, this)) {
				resultLabel.setText("Files are binary equal");
				resultLabel.setGraphic(images.getGreenCheckIcon());
			} else {
				resultLabel.setText("not equal Files");
				resultLabel.setGraphic(images.getRedUnCheck());
			}
		});
		
		compareFolder.setOnAction(e -> {
			controller.areFoldersInSegmentBinaryEqual(folderSegmentA, folderSegmentB, this);
		});
		
		
		Timeline updatingGuiTask = new Timeline(new KeyFrame(Duration.millis(50), e -> {
			GuiAndWorkerSharedValues sharedValuesGuiWorker = new GuiAndWorkerSharedValues();
			Number number = Math.round( sharedValuesGuiWorker.getProgressValue());
			percentageDone.setText(number.intValue() + "%");
			progressBar.setProgress(sharedValuesGuiWorker.getProgressValue()/100);
			
			if(sharedValuesGuiWorker.isWorkerRunning()) {
				compareFolder.setDisable(true);
			} else {
				compareFolder.setDisable(false);
			}
			
			if(sharedValuesGuiWorker.isFinishedMappingA()) {
				informationMappingFilesA.setGraphic(images.getGreenCheckIcon());informationMappingFilesA.setText("Finished Mapping");
			}
			if(sharedValuesGuiWorker.isFinishedMappingB()) {
				informationMappingFilesB.setGraphic(images.getGreenCheckIcon());informationMappingFilesB.setText("Finished Mapping");
			}
			
			// add all results that are not already added
			for(int i = 0; i < sharedValuesGuiWorker.getFolderComparisonResult().size(); i++) {
				//not contains
				if(folderResults.getChildren().contains(sharedValuesGuiWorker.getFolderComparisonResult().get(i)) == false) {
					folderResults.getChildren().add(sharedValuesGuiWorker.getFolderComparisonResult().get(i));
				} 
			}
				
	
		}));
		updatingGuiTask.setCycleCount(Timeline.INDEFINITE);
		updatingGuiTask.play();
		
		
		
		
		return scene;
	}



}
