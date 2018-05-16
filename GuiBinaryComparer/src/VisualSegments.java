import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VisualSegments {

	public VBox getFileComparisonSegment() {

		VBox fileBox = new VBox();
		Label titleFile = new Label();
		titleFile.setText("Insert File path Below");
		TextField fileLocation = new TextField();
		Button chooseFile = new Button();

		chooseFile.setOnAction(e -> {
			this.chooseFileA(fileLocation);
		});

		
		fileBox.getChildren().addAll(titleFile,fileLocation,chooseFile);
		return fileBox;
	}

	private void chooseFileA(TextField field) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(new Stage());
		if (file != null) {
			field.setText(file.getAbsolutePath());
		} else {
			return;
		}
	}
}
