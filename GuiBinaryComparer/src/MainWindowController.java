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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;

public class MainWindowController {
	public boolean areFoldersInSegmentBinaryEqual(Pane parentA, Pane parentB, Pane target) {
		File directoryOrFileA = null;
		File directoryOrFileB = null;

		//get textfield from file segment
		for (int i = 0; i < parentA.getChildrenUnmodifiable().size(); i++) {
			if (parentA.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentA.getChildrenUnmodifiable().get(i);
				directoryOrFileA = new File(textfield.getText());
			}
		}

		//get textfield from file segment
		for (int i = 0; i < parentB.getChildrenUnmodifiable().size(); i++) {
			if (parentB.getChildrenUnmodifiable().get(i) instanceof TextField) {
				TextField textfield = (TextField) parentB.getChildrenUnmodifiable().get(i);
				directoryOrFileB = new File(textfield.getText());
			}
		}

		// get values from textfield and read content as files 
		
		//first case is that folders/directorys are in both segemnts
		if (directoryOrFileA != null && directoryOrFileA.exists() && directoryOrFileA.isDirectory()
				&& directoryOrFileB != null && directoryOrFileB.exists() && directoryOrFileB.isDirectory()) {
			return areFoldersEqual(directoryOrFileA, directoryOrFileB, target);
		// second case is that both values represent files
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
		System.out.println(" -- Finished mapping first directory --");
		filesFromDirectoryB = this.allFilesInFolderAndSubfolder(directoryB, filesFromDirectoryB);
		System.out.println(" -- finioshed mapping second directory --");
		
		for (int i = 0; i < filesFromDirectoryA.size(); i++) {
			// create a new horizontal line for each file in the first folder. if there is 
			//a successful binary match or not wil be drawn into the inner : for loop
			VBox newTarget = (VBox) target;
				HBox oneComparison = new HBox();
					TextField firstFile = new TextField();
					Label resultSingleFile = new Label();
					TextField secondFile = new TextField();
				oneComparison.getChildren().addAll(firstFile, resultSingleFile, secondFile);
			newTarget.getChildren().add(oneComparison);
		
			// inner for loop determening if a match is found to the current selected file in all files inside the second folder.
			inner: for (int j = 0; j < filesFromDirectoryB.size(); j++) {
				if(areFilesBinaryEqual(filesFromDirectoryA.get(i), filesFromDirectoryB.get(j))) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					resultSingleFile.setTextFill(Color.web("#7CFC00"));
					resultSingleFile.setText(" -- is binary equal to -- ");	
					secondFile.setText(filesFromDirectoryB.get(j).getName());
					break inner;
				} 
				// last run through all existing files had no match. 
				else if(j == filesFromDirectoryB.size()-1) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					resultSingleFile.setTextFill(Color.web("#FA8072"));
					resultSingleFile.setText(" -- has no binary equal match");	
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
