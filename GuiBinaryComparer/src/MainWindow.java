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
	
	public void start(Stage stage) {
		Scene scene = mainWindowVisuals(stage);
		stage.setMaxWidth(windowSize);
		stage.setTitle("BinaryComparer");
		stage.setScene(scene);
		stage.show();
	}

	private Scene mainWindowVisuals(Stage stage) {
		ImageLoader images = new ImageLoader();
		
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
					Button compareFolder = new Button("Compare both folders");
					VBox folderSegmentB = segment.getFileOrFolderComparisonSegment(FileOrDirectory.DIRECTORY);
					folderComparison.getChildren().addAll(folderSegmentA,compareFolder,folderSegmentB);
					
					HBox infoBox = new HBox();infoBox.setAlignment(Pos.CENTER);infoBox.centerShapeProperty();infoBox.setSpacing(80);
						Label informationMappingFilesA = new Label();informationMappingFilesA.setAlignment(Pos.CENTER_LEFT);
						VBox progress = new VBox();progress.setAlignment(Pos.CENTER);						
							Label percentageDone = new Label();percentageDone.setTextAlignment(TextAlignment.CENTER);
							ProgressBar progressBar = new ProgressBar(0.0);progressBar.setPrefWidth(150);
						progress.getChildren().addAll(progressBar,percentageDone);
						Label informationMappingFilesB = new Label();
						infoBox.getChildren().addAll(informationMappingFilesA, progress, informationMappingFilesB);
					VBox folderResults = new VBox();folderResults.setAlignment(Pos.CENTER);
//						Pane scrollPane = new Pane();
//						folderResults.getChildren().add(scrollPane);
			folderComparisonAndResult.getChildren().addAll(folderComparison,infoBox,folderResults);
		
		root.getChildren().addAll(titleBox, fileComparison,folderComparisonAndResult);
		
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(root);
		
		Scene scene = new Scene(scroll, windowSize, 400, Color.BLACK);
		MainWindowController controller = new MainWindowController();
		
		compareFile.setOnAction(e -> {
			if (controller.areFoldersInSegmentBinaryEqual(segmentA, segmentB)) {
				resultLabel.setText("Files are binary equal");
				resultLabel.setGraphic(images.getGreenCheckIcon());
			} else {
				resultLabel.setText("not equal Files");
				resultLabel.setGraphic(images.getRedUnCheck());
			}
		});
		
		compareFolder.setOnAction(e -> {
			ProgressValues values = new ProgressValues();
			values.setFinishedMappingA(false);
			values.setFinishedMappingB(false);
			folderResults.getChildren().clear();
			values.clearList();
			values.setProgressValue(0);
			informationMappingFilesA.setGraphic(images.getWaitingIcon());informationMappingFilesA.setText("Started Mapping ");
			informationMappingFilesB.setText("Started Mapping ");informationMappingFilesB.setGraphic(images.getWaitingIcon());informationMappingFilesB.setAlignment(Pos.CENTER_RIGHT);
			controller.areFoldersInSegmentBinaryEqual(folderSegmentA, folderSegmentB);
		});
		
		
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(50), e -> {
			ProgressValues values = new ProgressValues();
			Number number = Math.round( values.getProgressValue());
			percentageDone.setText(number.intValue() + "%");
			progressBar.setProgress(values.getProgressValue()/100);
			
			if(values.isFinishedMappingA()) {
				informationMappingFilesA.setGraphic(images.getGreenCheckIcon());informationMappingFilesA.setText("Finished Mapping");
			}
			if(values.isFinishedMappingB()) {
				informationMappingFilesB.setGraphic(images.getGreenCheckIcon());informationMappingFilesB.setText("Finished Mapping");
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
