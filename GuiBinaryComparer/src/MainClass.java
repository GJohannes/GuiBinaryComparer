import java.awt.AWTException;
import java.awt.Robot;
import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
//		Robot a = new Robot();
//
//		System.out.println("hello world");
//		for (int i = 0; i < 7; i++) {
//			System.out.println("test.1234");
//			a.mouseMove(220, 330);
//			Thread.sleep(100);
//		}
//		System.out.println("Done");

	}
	
	
	
	@Override
	public void start(Stage stage) {
		MainWindow window = new MainWindow();
		window.start(stage);
		
//		Button button = new Button("test12343");
//		HBox root = new HBox();
//		VBox box = new VBox();
//		VBox box2 = new VBox();
//		VBox box3 = new VBox();
//		
//		button.setOnMouseClicked(event -> 	{	
//											fxSameParentAsButton(button);
//											fxElement(root);
//											});
//		button.setUserData("zzz");
//		box2.setUserData("xxx");
//		
//		root.getChildren().add(button);
//		root.getChildren().add(box);
//		root.getChildren().add(box2);
//		root.getChildren().add(box3);
//	    
//	   
//	    Scene scene = new Scene(root, 500, 500, Color.BLACK);
//		
//		
//		stage.setTitle("TestFX");
//		stage.setScene(scene);
//		stage.show();
	}

	
	public void fxSameParentAsButton(Button button) {
		Pane pane = (Pane) button.getParent();
		TextField field = new TextField();
		pane.getChildren().add(field);
	}
	
	
	public void fxElement(HBox root) {
		
		for(int i = 0; i < root.getChildren().size(); i++) {
			Node node = root.getChildren().get(i);
	    	if(node instanceof Pane) {
	    	//if(node instanceof HBox && node.getUserData() != null && node.getUserData().equals("xxx")) {
	    		Pane pane = (Pane) node;
	    		TextField field = new TextField();
	    		pane.getChildren().add(field);
	    	}
	    }
	}
}
