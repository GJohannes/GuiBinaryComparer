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
		VBox root = new VBox();

		HBox titleBox = new HBox();
		Label title = new Label();
		title.setText("Binarie Comparer");
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(title);

		HBox fileComparison = new HBox();

		VBox resultBox = new VBox();
		Label resultLabel = new Label();
		resultLabel.setText("Result:               ");
		Button compareFile = new Button();
		resultBox.getChildren().addAll(resultLabel, compareFile);

		FileSegment segment = new FileSegment();
		VBox segmentA = segment.getFileSegment();
		VBox segmentB = segment.getFileSegment();

		compareFile.setOnAction(e -> {
			this.compareFiles(segmentA, segmentB, resultLabel);
		});

		fileComparison.getChildren().addAll(segmentA, resultBox, segmentB);

		root.getChildren().addAll(titleBox, fileComparison);
		titleBox.centerShapeProperty();

		Scene scene = new Scene(root, 500, 500, Color.BLACK);

		return scene;
	}

	private void compareFiles(Parent parentA, Parent parentB, Label result) {
		File fileA = null;
		File fileB = null;

		for (int i = 0; i < parentA.getChildrenUnmodifiable().size(); i++) {
			if (parentA.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentA.getChildrenUnmodifiable().get(i);
				fileA = new File(textfield.getText());
			}
		}

		for (int i = 0; i < parentB.getChildrenUnmodifiable().size(); i++) {
			if (parentB.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentB.getChildrenUnmodifiable().get(i);
				fileB = new File(textfield.getText());
			}
		}

		Path pathFileA = Paths.get(fileA.getPath());
		Path pathFileB = Paths.get(fileB.getPath());

		byte[] bytesFileA = getBinarieFromLocation(pathFileA);
		byte[] bytesFileB = getBinarieFromLocation(pathFileB);

		if (bytesFileA.length != bytesFileB.length) {
			result.setText("not equal Files");
			return;
		} else {
			for (int i = 0; i < bytesFileA.length; i++) {
				if (bytesFileA[i] != bytesFileB[i]) {
					result.setText("not equal Files");
					return;
				}
			}
		}
		
		result.setText("Files are binary equal");
		return;

	}

	public byte[] getBinarieFromLocation(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void mainWindowController() {

	}
}
