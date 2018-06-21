
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindow {
	private int windowSize = 800;
	public Integer totalFiles = 0;
	
	VBox folderResults;
	Label informationMappingFilesA;
	Label informationMappingFilesB;
	ImageLoader images = new ImageLoader();
	Button compareFolder;
	ProgressBar progressBar;
	Label percentageDone;
	Label mappedTotalFiles;
	Button compareFile;
	Label resultLabel;
	VBox segmentA;
	VBox segmentB;
	VBox folderSegmentA;
	VBox folderSegmentB;
	
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
					resultLabel = new Label();resultLabel.setText("Result:               ");
					compareFile = new Button("Compare Both Files");
				resultBox.getChildren().addAll(resultLabel, compareFile);

				segmentA = segment.getFileOrFolderComparisonSegment(FileOrDirectory.FILE);
				segmentB = segment.getFileOrFolderComparisonSegment(FileOrDirectory.FILE);
			fileComparison.getChildren().addAll(segmentA, resultBox, segmentB);
		
			VBox folderComparisonAndResult = new VBox();
				HBox folderComparison = new HBox(); folderComparison.setAlignment(Pos.CENTER); folderComparison.setSpacing(30);
					folderSegmentA = segment.getFileOrFolderComparisonSegment(FileOrDirectory.DIRECTORY);
					compareFolder = new Button("Compare both folders");
					folderSegmentB = segment.getFileOrFolderComparisonSegment(FileOrDirectory.DIRECTORY);
					folderComparison.getChildren().addAll(folderSegmentA,compareFolder,folderSegmentB);
					
					HBox infoBox = new HBox();infoBox.setAlignment(Pos.CENTER);infoBox.centerShapeProperty();infoBox.setSpacing(80);
						informationMappingFilesA = new Label();informationMappingFilesA.setAlignment(Pos.CENTER_LEFT);
						VBox progress = new VBox();progress.setAlignment(Pos.CENTER);						
							mappedTotalFiles = new Label();
							percentageDone = new Label();percentageDone.setTextAlignment(TextAlignment.CENTER);
							progressBar = new ProgressBar(0.0);progressBar.setPrefWidth(150);
						progress.getChildren().addAll(mappedTotalFiles,progressBar,percentageDone);
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
		
	
		MainWindowController controller = new MainWindowController(this);
		return scene;
	}



}
