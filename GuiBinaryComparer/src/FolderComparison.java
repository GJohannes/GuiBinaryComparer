import java.io.File;
import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FolderComparison implements Runnable{
	private File directoryA;
	private File directoryB;
	
	public FolderComparison(File directoryA, File directoryB) {
		System.out.println(directoryA + "Upper call");
		this.directoryA = directoryA;
		this.directoryB = directoryB;
	}
	
	
	public boolean areFoldersEqual(File directoryA, File directoryB) {
		ProgressValues values = new ProgressValues();
		FileComparison fileComparison = new FileComparison();
		ArrayList<File> filesFromDirectoryA = new ArrayList<>();
		ArrayList<File> filesFromDirectoryB = new ArrayList<>();

		filesFromDirectoryA = this.allFilesInFolderAndSubfolder(directoryA, filesFromDirectoryA);
		values.setFinishedMappingA(true);
		filesFromDirectoryB = this.allFilesInFolderAndSubfolder(directoryB, filesFromDirectoryB);
		values.setFinishedMappingB(true);
		
		for (int i = 0; i < filesFromDirectoryA.size(); i++) {
			double percentProgressPerFile = 100.0/filesFromDirectoryA.size(); 
			
			// create a new horizontal line for each file in the first folder. if there is 
			//a successful binary match or not wil be drawn into the inner : for loop
			//VBox newTarget = values.getFolderComparisonResult();
				HBox oneComparison = new HBox();
					TextField firstFile = new TextField();
					Label resultSingleFile = new Label();
					TextField secondFile = new TextField();
				oneComparison.getChildren().addAll(firstFile, resultSingleFile, secondFile);

			
	
			
			// inner for loop determening if a match is found to the current selected file in all files inside the second folder.
			inner: for (int j = 0; j < filesFromDirectoryB.size(); j++) {
				if(fileComparison.areFilesBinaryEqual(filesFromDirectoryA.get(i), filesFromDirectoryB.get(j))) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					resultSingleFile.setTextFill(Color.web("#7CFC00"));
					resultSingleFile.setText(" -- is binary equal to -- ");	
					secondFile.setText(filesFromDirectoryB.get(j).getName());
					values.addHBoxToGuiQueue(oneComparison);
					values.setProgressValue(values.getProgressValue() + percentProgressPerFile);
					break inner;
				} 
				// last run through all existing files had no match. 
				else if(j == filesFromDirectoryB.size()-1) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					resultSingleFile.setTextFill(Color.web("#FA8072"));
					resultSingleFile.setText(" -- has no binary equal match");	
					values.addHBoxToGuiQueue(oneComparison);
					values.setProgressValue(values.getProgressValue() + percentProgressPerFile);
					break inner;
				}
			}
		}
		
		HBox doneMessageBox = new HBox();
			Label doneMessage = new Label(); doneMessage.setText("Finished Comparing Folders");
			doneMessageBox.getChildren().add(doneMessage);
		values.addHBoxToGuiQueue(doneMessageBox);
			
		
//		for(int i = 0; i < filesFromDirectoryA.size(); i++) {
//			if(filesFromDirectoryA.get(i) != null) {
//				return false;
//			}
//		}
		
		return true;
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

	@Override
	public void run() {
		 this.areFoldersEqual(directoryA, directoryB);
		 
	}


}
