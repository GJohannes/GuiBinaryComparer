import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class MainWindowController {
	public boolean areFoldersInSegmentBinaryEqual(Parent parentA, Parent parentB, Parent target) {
		File directoryOrFileA = null;
		File directoryOrFileB = null;

		for (int i = 0; i < parentA.getChildrenUnmodifiable().size(); i++) {
			if (parentA.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentA.getChildrenUnmodifiable().get(i);
				directoryOrFileA = new File(textfield.getText());
			}
		}

		for (int i = 0; i < parentB.getChildrenUnmodifiable().size(); i++) {
			if (parentB.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentB.getChildrenUnmodifiable().get(i);
				directoryOrFileB = new File(textfield.getText());
			}
		}

		if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isDirectory()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isDirectory()) {
			return areFoldersEqual(directoryOrFileA, directoryOrFileB, target);
		} else if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isFile()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isFile()) {
			return areFilesBinaryEqual(directoryOrFileA, directoryOrFileB);
		} else {
			return false;
		}
	}

	public boolean areFoldersEqual(File directoryA, File directoryB, Parent target) {
		ArrayList<File> filesFromDirectoryA = new ArrayList<>();
		ArrayList<File> filesFromDirectoryB = new ArrayList<>();
		filesFromDirectoryA = this.allFilesInFolderAndSubfolder(directoryA, filesFromDirectoryA);
		filesFromDirectoryB = this.allFilesInFolderAndSubfolder(directoryB, filesFromDirectoryB);

		
		for (int i = 0; i < filesFromDirectoryA.size(); i++) {
			inner: for (int j = 0; j < filesFromDirectoryB.size(); j++) {
				if(areFilesBinaryEqual(filesFromDirectoryA.get(i), filesFromDirectoryB.get(j))) {
					VBox newTarget = (VBox) target;
					Label resultSingleFile = new Label();
					 resultSingleFile.setTextFill(Color.web("#7CFC00"));
					resultSingleFile.setText(filesFromDirectoryA.get(i).getName() + " is binary equal to " + filesFromDirectoryB.get(j).getName());	
					newTarget.getChildren().add(resultSingleFile);
					filesFromDirectoryA.set(i, null);
					break inner;
					
				} 
				else if(j == filesFromDirectoryB.size()-1) {
					VBox newTarget = (VBox) target;
					Label resultSingleFile = new Label();
					resultSingleFile.setText(filesFromDirectoryA.get(i).getName() + "has no binary equal match");
					resultSingleFile.setTextFill(Color.web("#FA8072"));
					newTarget.getChildren().add(resultSingleFile);
					break inner;
				}
			}

		}

		for(int i = 0; i < filesFromDirectoryA.size(); i++) {
			if(filesFromDirectoryA.get(i) != null) {
				return false;
			}
		}
		
		return true;
	}

	private boolean areFilesBinaryEqual(File fileA, File fileB) {
		Path pathFileA = Paths.get(fileA.getPath());
		Path pathFileB = Paths.get(fileB.getPath());

		byte[] bytesFileA = getBinarieFromLocation(pathFileA);
		byte[] bytesFileB = getBinarieFromLocation(pathFileB);

		if (bytesFileA.length != bytesFileB.length) {
			return false;
		} else {
			for (int i = 0; i < bytesFileA.length; i++) {
				if (bytesFileA[i] != bytesFileB[i]) {

					return false;
				}
			}
		}

		return true;
	}

	private byte[] getBinarieFromLocation(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<File> allFilesInFolderAndSubfolder(File directory, ArrayList<File> files) {
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isFile()) {
				files.add(fList[i]);
			} else if (fList[i].isDirectory()) {
				allFilesInFolderAndSubfolder(fList[i], files);
			}
		}
		return files;
	}

}
