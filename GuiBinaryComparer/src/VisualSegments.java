import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VisualSegments {

	public VBox getFileOrFolderComparisonSegment(FileOrDirectory fileOrDirectory) {
		VBox fileBox = new VBox();
		Label titleFile = new Label();
		titleFile.setText("Insert " + fileOrDirectory.toString().toLowerCase() + " path below");
		TextField setLocation = new TextField();
		Button chooseLocation = new Button("Select a " + fileOrDirectory.toString().toLowerCase());
		
		if(fileOrDirectory.equals(FileOrDirectory.FILE)) {
			chooseLocation.setOnAction(e -> {
				this.chooseAFile(setLocation);
			});
		} else {
			chooseLocation.setOnAction(e -> {
				this.chooseADirectory(setLocation);
			});
		}
	
		fileBox.getChildren().addAll(titleFile,setLocation,chooseLocation);
		return fileBox;
	}

	private void chooseAFile(TextField field) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(new Stage());
		if (file != null) {
			field.setText(file.getAbsolutePath());
		} else {
			return;
		}
	}
	
	private void chooseADirectory(TextField field) {
		DirectoryChooser chooser = new DirectoryChooser();
		File file = chooser.showDialog(new Stage());
		if (file != null) {
			field.setText(file.getAbsolutePath());
		} else {
			return;
		}
	}
}
