package miscellaneous;
import java.util.ArrayList;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GuiAndWorkerSharedValues {
	private static int totalFiles;

	public static synchronized int getTotalFiles() {
		return totalFiles;
	}

	public static synchronized void setTotalFiles(int totalFiles) {
		GuiAndWorkerSharedValues.totalFiles = totalFiles;
	}
	
	public static synchronized void incrementTotalFiles() {
		totalFiles++;
	}
	
//	private static double ProgressValue = 0;
//	private static ArrayList<HBox> listWithAllHBoxToAdd = new ArrayList<>();
//	private static boolean finishedMappingA = false;
//	private static boolean finishedMappingB = false;
//	private static boolean workerRunning = false;
//	
//	public static synchronized boolean isWorkerRunning() {
//		return workerRunning;
//	}
//
//	public static synchronized void setWorkerRunning(boolean workerRunning) {
//		GuiAndWorkerSharedValues.workerRunning = workerRunning;
//	}
//
//	public static synchronized boolean isFinishedMappingA() {
//		return finishedMappingA;
//	}
//
//	public static synchronized void setFinishedMappingA(boolean finishedMappingA) {
//		GuiAndWorkerSharedValues.finishedMappingA = finishedMappingA;
//	}
//
//	public static synchronized boolean isFinishedMappingB() {
//		return finishedMappingB;
//	}
//
//	public static synchronized void setFinishedMappingB(boolean finishedMappingB) {
//		GuiAndWorkerSharedValues.finishedMappingB = finishedMappingB;
//	}
//
//	public static synchronized ArrayList<HBox> getFolderComparisonResult() {
//		return listWithAllHBoxToAdd;
//	}
//
//	public static synchronized void clearList() {
//		listWithAllHBoxToAdd.clear();
//	}
//	
//	public static synchronized void addHBoxToGuiQueue(HBox folderComparisonResult) {
//		listWithAllHBoxToAdd.add(folderComparisonResult);
//	}
//
//	public static synchronized double getProgressValue() {
//		return ProgressValue;
//	}
//
//	public static synchronized void setProgressValue(double progressValue) {
//		ProgressValue = progressValue;
//	}
}
