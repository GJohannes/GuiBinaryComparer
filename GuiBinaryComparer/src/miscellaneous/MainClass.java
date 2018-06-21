package miscellaneous;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import gui.MainWindow;
import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public class MainClass extends Application {

	public static void main(String[] args) throws AWTException, InterruptedException {
		launch(args);
		// readFile();
	}

	@Override
	public void start(Stage stage) {

//		HBox root = new HBox();
//		for (int i = 0; i < 100; i++) {
//			Button b1 = new Button("asdasd");
//			root.getChildren().add(b1);
//		}
//		ScrollPane scroll = new ScrollPane();
//		scroll.setContent(root);
//		Scene scene = new Scene(scroll, 650, 500, Color.BLACK);
//		stage.setScene(scene);
//		stage.show();

		 MainWindow window = new MainWindow();
		 
		 stage.setOnCloseRequest(e -> {
				System.exit(0);
				Platform.exit();
		 });
		 
		 window.start(stage);
		 
	}

	public void fxSameParentAsButton(Button button) {
		Pane pane = (Pane) button.getParent();
		TextField field = new TextField();
		pane.getChildren().add(field);
	}

	public void fxElement(HBox root) {
		for (int i = 0; i < root.getChildren().size(); i++) {
			Node node = root.getChildren().get(i);
			if (node instanceof Pane) {
				// if(node instanceof HBox && node.getUserData() != null &&
				// node.getUserData().equals("xxx")) {
				Pane pane = (Pane) node;
				TextField field = new TextField();
				pane.getChildren().add(field);
			}
		}
	}

//	public static void readFile() {
//		File file = new File("C:\\Users\\vp5cmew\\Desktop\\wasd.txt");
//
//		try {
//			System.out.println(Files.readAllLines(file.toPath()));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		FileInputStream stream = null;
//
//		try {
//			stream = new FileInputStream(file);
//
//			StringBuffer s = new StringBuffer();
//
//			int content;
//			while ((content = stream.read()) != -1) {
//				// convert to char and display it
//				System.out.print((char) content);
//			}
//
//			System.out.println(s);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
