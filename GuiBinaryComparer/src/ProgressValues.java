import java.util.ArrayList;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProgressValues {
	private static double ProgressValue = 0;
	private static ArrayList<HBox> listWithAllHBoxToAdd = new ArrayList<>();
	private static boolean finishedMappingA = false;
	private static boolean finishedMappingB = false;
	
	public static synchronized boolean isFinishedMappingA() {
		return finishedMappingA;
	}

	public static synchronized void setFinishedMappingA(boolean finishedMappingA) {
		ProgressValues.finishedMappingA = finishedMappingA;
	}

	public static synchronized boolean isFinishedMappingB() {
		return finishedMappingB;
	}

	public static synchronized void setFinishedMappingB(boolean finishedMappingB) {
		ProgressValues.finishedMappingB = finishedMappingB;
	}

	public static synchronized ArrayList<HBox> getFolderComparisonResult() {
		return listWithAllHBoxToAdd;
	}

	public static synchronized void clearList() {
		listWithAllHBoxToAdd.clear();
	}
	
	public static synchronized void addHBoxToGuiQueue(HBox folderComparisonResult) {
		listWithAllHBoxToAdd.add(folderComparisonResult);
	}

	public static synchronized double getProgressValue() {
		return ProgressValue;
	}

	public static synchronized void setProgressValue(double progressValue) {
		ProgressValue = progressValue;
	}
}
