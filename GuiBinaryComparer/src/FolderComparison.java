import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FolderComparison implements Runnable {
	private File directoryA;
	private File directoryB;

	public FolderComparison(File directoryA, File directoryB) {

		this.directoryA = directoryA;
		this.directoryB = directoryB;
	}

	public boolean areFoldersEqual(File directoryA, File directoryB) {
		GuiAndWorkerSharedValues sharedValuesWorkerGui = new GuiAndWorkerSharedValues();
		FileComparison fileComparison = new FileComparison();
		ArrayList<File> filesFromDirectoryA = new ArrayList<>();
		ArrayList<File> filesFromDirectoryB = new ArrayList<>();

		filesFromDirectoryA = this.allFilesInFolderAndSubfolder(directoryA, filesFromDirectoryA);
		sharedValuesWorkerGui.setFinishedMappingA(true);
		filesFromDirectoryB = this.allFilesInFolderAndSubfolder(directoryB, filesFromDirectoryB);
		sharedValuesWorkerGui.setFinishedMappingB(true);

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
					// important to create new file becuase reference from list
					// is gone when button is clicked
					String path = filesFromDirectoryA.get(i).getAbsolutePath();

					pathToFirstFile.setOnMouseClicked(e -> {
						System.out.println(System.getProperty("os.name"));

						if (System.getProperty("os.name").equals("Windows")) {
							try {
								Runtime.getRuntime().exec("explorer.exe /select," + path);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} else if (System.getProperty("os.name").equals("Linux")) {
							System.out.println(1);
							File file = new File(path);
							File parentFile = new File(file.getParent());
							System.out.println(file);
							System.out.println(parentFile);

							System.out.println(2);
							System.out.println(parentFile);
							if (parentFile.exists() && parentFile.isDirectory()) {
								Desktop desktop = Desktop.getDesktop();

								System.out.println("OPen Now");
								System.out.println(desktop.isSupported(Desktop.Action.OPEN) + "open desktop");
								try {
									System.out.println("try topo");
									File fileXYZ = new File("/home/guenthjs/Desktop/BA");
									System.out.println("try middle");
									Desktop.getDesktop().open(fileXYZ);
									System.out.println("try bottom");
									//desktop.open(fileXYZ);
								} catch (Exception e1) {
									System.out.println("Catched exception");
									e1.printStackTrace();
								}
								System.out.println("Done");

							}
							System.out.println(3);
						} else {
							// TODO open dialog box not supported os
							System.out.println("not suported os");
						}
						System.out.println("Done");
					});

					resultSingleFile.setTextFill(Color.web("#7CFC00"));
					resultSingleFile.setText(" -- is binary equal to -- ");

					secondFile.setText(filesFromDirectoryB.get(j).getName());
					String secondPath = filesFromDirectoryB.get(j).getAbsolutePath();
					pathToSecondFile.setOnMouseClicked(e -> {
						try {
							Runtime.getRuntime().exec("explorer.exe /select," + secondPath);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					});
					sharedValuesWorkerGui.addHBoxToGuiQueue(oneComparison);
					sharedValuesWorkerGui
							.setProgressValue(sharedValuesWorkerGui.getProgressValue() + percentProgressPerFile);
					break inner;
				}
				// last run through all existing files had no match.
				else if (j == filesFromDirectoryB.size() - 1) {
					firstFile.setText(filesFromDirectoryA.get(i).getName());
					resultSingleFile.setTextFill(Color.web("#FA8072"));
					resultSingleFile.setText(" -- has no binary equal match");
					String path = filesFromDirectoryA.get(i).getAbsolutePath();
					pathToFirstFile.setOnMouseClicked(e -> {
						try {
							System.out.println("open windows second button");
							Runtime.getRuntime().exec("explorer.exe /select," + path);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					});

					sharedValuesWorkerGui.addHBoxToGuiQueue(oneComparison);
					sharedValuesWorkerGui
							.setProgressValue(sharedValuesWorkerGui.getProgressValue() + percentProgressPerFile);
					break inner;
				}
			}
		}

		HBox doneMessageBox = new HBox();
		Label doneMessage = new Label();
		doneMessage.setText("Finished Comparing Folders");
		doneMessageBox.getChildren().add(doneMessage);
		sharedValuesWorkerGui.addHBoxToGuiQueue(doneMessageBox);
		sharedValuesWorkerGui.setWorkerRunning(false);

		// for(int i = 0; i < filesFromDirectoryA.size(); i++) {
		// if(filesFromDirectoryA.get(i) != null) {
		// return false;
		// }
		// }

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
		GuiAndWorkerSharedValues sharedValuesWorkerGui = new GuiAndWorkerSharedValues();
		sharedValuesWorkerGui.setWorkerRunning(false);
	}

}
