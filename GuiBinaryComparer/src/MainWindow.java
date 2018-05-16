import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow {

	public void start(Stage stage) {
		Scene scene = mainWindowVisuals(stage);
		stage.setTitle("TestFX");
		stage.setScene(scene);
		stage.show();
	}

	private Scene mainWindowVisuals(Stage stage) {
		MainWindowController controller = new MainWindowController();
		VisualSegments segment = new VisualSegments();
		
		VBox root = new VBox();root.setSpacing(20);
			HBox titleBox = new HBox();titleBox.setAlignment(Pos.CENTER);titleBox.centerShapeProperty();
				Label title = new Label();title.setText("Binarie Comparer");	
			titleBox.getChildren().add(title);

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
				VBox folderResults = new VBox();
			folderComparisonAndResult.getChildren().addAll(folderComparison,folderResults);
		
		root.getChildren().addAll(titleBox, fileComparison,folderComparisonAndResult);
		
		
		Scene scene = new Scene(root, 650, 500, Color.BLACK);
		
		compareFile.setOnAction(e -> {
			if (controller.areFoldersInSegmentBinaryEqual(segmentA, segmentB, null)) {
				resultLabel.setText("Files are binary equal");
			} else {
				resultLabel.setText("not equal Files");
				
			}
		});
		
		compareFolder.setOnAction(e -> {
			folderResults.getChildren().clear();
			controller.areFoldersInSegmentBinaryEqual(folderSegmentA, folderSegmentB, folderResults);
			
			
		});

		

		return scene;
	}



}
