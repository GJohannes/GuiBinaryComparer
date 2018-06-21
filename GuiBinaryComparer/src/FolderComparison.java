import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.parsers.CachingParserPool.SynchronizedGrammarPool;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FolderComparison extends Task<ArrayList<HBox>> {
	private File directoryA;
	private File directoryB;
	private MainWindow main;
	private ArrayList<HBox> boxList = new ArrayList<>();

	public FolderComparison(File directoryA, File directoryB, MainWindow main) {
		this.directoryA = directoryA;
		this.directoryB = directoryB;
		this.main = main;
	}

	public boolean areFoldersEqual(File directoryA, File directoryB) {
		double currentProgress = 0;

		FileComparison fileComparison = new FileComparison();
		ArrayList<File> filesFromDirectoryA = new ArrayList<>();
		ArrayList<File> filesFromDirectoryB = new ArrayList<>();

		filesFromDirectoryA = this.allFilesInFolderAndSubfolder(directoryA, filesFromDirectoryA);
		this.updateMessage(STATES.FINISHED_FIRST_COMPARISON.toString());
		filesFromDirectoryB = this.allFilesInFolderAndSubfolder(directoryB, filesFromDirectoryB);
		this.updateMessage(STATES.FINISHED_SECOND_COMPARISON.toString());

		for (int i = 0; i < filesFromDirectoryA.size(); i++) {
			double percentProgressPerFile = 100.0 / filesFromDirectoryA.size();

			// create a new horizontal line for each file in the first folder.
			// if there is
			// a successful binary match or not will be drawn into the inner :
			// for loop
			// VBox newTarget = values.getFolderComparisonResult();
			HBox oneComparison = new HBox();
			oneComparison.setAlignment(Pos.CENTER);
			Button pathToFirstFile = new Button("Show");
			TextField firstFile = new TextField();
			Label resultSingleFile = new Label();
			TextField secondFile = new TextField();
			Button pathToSecondFile = new Button("Show");
			oneComparison.getChildren().addAll(pathToFirstFile, firstFile, resultSingleFile, secondFile,
					pathToSecondFile);
			// inner for loop determening if a match is found to the current
			// selected file in all files inside the second folder.
			inner: for (int j = 0; j < filesFromDirectoryB.size(); j++) {
				if (fileComparison.areFilesBinaryEqual(filesFromDirectoryA.get(i), filesFromDirectoryB.get(j))) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					Path path = Paths.get(filesFromDirectoryA.get(i).getAbsolutePath());
					pathToFirstFile.setOnMouseClicked(this.showFileInFileSystem(path));

					resultSingleFile.setTextFill(Color.web("#7CFC00"));
					resultSingleFile.setText(" -- is binary equal to -- ");

					secondFile.setText(filesFromDirectoryB.get(j).getName());
					Path secondPath = Paths.get(filesFromDirectoryB.get(j).getAbsolutePath());
					pathToSecondFile.setOnMouseClicked(this.showFileInFileSystem(secondPath));

					currentProgress = this.updateGuiThread(oneComparison, currentProgress, percentProgressPerFile);
					break inner;
				}
				// last run through all existing files had no match.
				else if (j == filesFromDirectoryB.size() - 1) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					resultSingleFile.setTextFill(Color.web("#FA8072"));
					resultSingleFile.setText(" -- has no binary equal match");
					Path path = Paths.get(filesFromDirectoryA.get(i).getAbsolutePath());
					pathToFirstFile.setOnMouseClicked(this.showFileInFileSystem(path));

					currentProgress = this.updateGuiThread(oneComparison, currentProgress, percentProgressPerFile);
					break inner;
				}
			}
		}
		return true;
	}

	private double updateGuiThread(HBox oneComparison, double currentProgress, double percentProgressPerFile) {
		//create new list with old values so that this.updateValue(boxList); works. 
		//the update value seems to only work if a new object is set as the updated object
		ArrayList<HBox> newList = new ArrayList<>();
		newList.addAll(boxList);
		boxList = newList;
		
		boxList.add(oneComparison);
		this.updateValue(boxList);
		currentProgress = currentProgress + percentProgressPerFile;
		this.updateProgress(currentProgress, 100);
		return currentProgress;
	}
	
	private EventHandler<MouseEvent> showFileInFileSystem(Path path) {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (System.getProperty("os.name").contains("Windows")) {
					try {
						Runtime.getRuntime().exec("explorer.exe /select," + path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if (System.getProperty("os.name").contains("Linux")) {
					File file = new File(path.toString());
					File parentFile = new File(file.getParent());
					if (parentFile.exists() && parentFile.isDirectory()) {
						Desktop desktop = Desktop.getDesktop();
						try {
							// this linux file system opening seems to be bugged
							Desktop.getDesktop().open(parentFile);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} else {
					Stage stage = new Stage();
					VBox box = new VBox();
					box.setAlignment(Pos.CENTER);
					box.setSpacing(15);
					Scene scene = new Scene(box, 300, 100, Color.BLACK);
					Label lable = new Label();
					lable.setText("Not Supported OS to show file system");
					Button closePopUp = new Button("OK");
					closePopUp.setOnMouseClicked(e -> {
						stage.close();
					});
					box.getChildren().addAll(lable, closePopUp);
					stage.setScene(scene);
					stage.show();
				}
			}
		};
		return eventHandler;
	}

	private ArrayList<File> allFilesInFolderAndSubfolder(File directory, ArrayList<File> files) {
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isFile()) {
				files.add(fList[i]);
				GuiAndWorkerSharedValues.incrementTotalFiles();
			} else if (fList[i].isDirectory()) {
				allFilesInFolderAndSubfolder(fList[i], files);
			}
		}
		return files;
	}

	@Override
	protected ArrayList<HBox> call() throws Exception {
		this.areFoldersEqual(directoryA, directoryB);
		
		HBox finishedMessageBox = new HBox();
		Label label = new Label();
		label.setText("Finished");
		finishedMessageBox.getChildren().add(label);
		boxList.add(finishedMessageBox);
		return boxList;
	}

}
