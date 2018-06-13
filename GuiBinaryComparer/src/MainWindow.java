import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindow {

	public void start(Stage stage) {
		Scene scene = mainWindowVisuals(stage);
		stage.setMaxWidth(700);
		stage.setTitle("BinaryComparer");
		stage.setScene(scene);
		stage.show();
	}

	private Scene mainWindowVisuals(Stage stage) {
		InputStream input = getClass().getResourceAsStream("/waiting.gif");
		InputStream hookInput = getClass().getResourceAsStream("/greenCheck.png");
		Image image = new Image(input);
		Image imageHook = new Image(hookInput);
		ImageView waitingIcon = new ImageView(image);
		ImageView waitingIcon2 = new ImageView(image);
		ImageView ok = new ImageView(imageHook);
		ImageView ok2 = new ImageView(imageHook);
		
		VisualSegments segment = new VisualSegments();
		
		
		
		VBox root = new VBox();root.setSpacing(20);
			HBox titleBox = new HBox();titleBox.setAlignment(Pos.CENTER);titleBox.centerShapeProperty();
				Label title = new Label(); title.setText("Binarie Comparer");
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
					Button compareFolder = new Button("Compare both folders");
					VBox folderSegmentB = segment.getFileOrFolderComparisonSegment(FileOrDirectory.DIRECTORY);
					folderComparison.getChildren().addAll(folderSegmentA,compareFolder,folderSegmentB);
					
					HBox infoBox = new HBox();
						Label informationMappingFilesA = new Label();
						VBox progress = new VBox();						
							Label percentageDone = new Label();
							ProgressBar progressBar = new ProgressBar(0.0);
						progress.getChildren().addAll(progressBar,percentageDone);
						Label informationMappingFilesB = new Label();infoBox.setAlignment(Pos.CENTER);infoBox.centerShapeProperty();infoBox.setSpacing(30);
						infoBox.getChildren().addAll(informationMappingFilesA, progress, informationMappingFilesB);
					VBox folderResults = new VBox();
						Pane scrollPane = new Pane();
						folderResults.getChildren().add(scrollPane);
			folderComparisonAndResult.getChildren().addAll(folderComparison,infoBox,folderResults);
		
		root.getChildren().addAll(titleBox, fileComparison,folderComparisonAndResult);
		
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(root);
		
		Scene scene = new Scene(scroll, 650, 500, Color.BLACK);
		MainWindowController controller = new MainWindowController();
		
		compareFile.setOnAction(e -> {
			if (controller.areFoldersInSegmentBinaryEqual(segmentA, segmentB)) {
				resultLabel.setText("Files are binary equal");
			} else {
				resultLabel.setText("not equal Files");
				
			}
		});
		
		compareFolder.setOnAction(e -> {
			ProgressValues values = new ProgressValues();
			values.setFinishedMappingA(false);
			values.setFinishedMappingB(false);
			folderResults.getChildren().clear();
			values.clearList();
			values.setProgressValue(0);
			informationMappingFilesA.setGraphic(waitingIcon);informationMappingFilesA.setText("Started Mapping ");
			informationMappingFilesB.setText("Started Mapping ");informationMappingFilesB.setGraphic(waitingIcon2);informationMappingFilesB.setAlignment(Pos.CENTER_RIGHT);
			controller.areFoldersInSegmentBinaryEqual(folderSegmentA, folderSegmentB);
		});
		
		
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(5), e -> {
			ProgressValues values = new ProgressValues();
			percentageDone.setText(Double.toString(values.getProgressValue()));
			progressBar.setProgress(values.getProgressValue()/100);
			
			if(values.isFinishedMappingA()) {
				informationMappingFilesA.setGraphic(ok);informationMappingFilesA.setText("Finished Mapping");
			}
			if(values.isFinishedMappingB()) {
				informationMappingFilesB.setGraphic(ok2);informationMappingFilesB.setText("Finished Mapping");
			}
			
			
			for(int i = 0; i < values.getFolderComparisonResult().size(); i++) {
				//not contains
				if(folderResults.getChildren().contains(values.getFolderComparisonResult().get(i)) == false) {
					folderResults.getChildren().add(values.getFolderComparisonResult().get(i));
				} 
			}
				
	
		}));
		fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
		fiveSecondsWonder.play();
		
		
		
		
		return scene;
	}



}
