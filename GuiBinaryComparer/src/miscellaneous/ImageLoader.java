package miscellaneous;
import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader {
	public ImageView getGreenCheckIcon() {
		InputStream input = getClass().getResourceAsStream("/greenCheck.png");
		return this.convertStreamToImageView(input);
	}
	
	public ImageView getWaitingIcon() {
		InputStream input = getClass().getResourceAsStream("/waiting.gif");
		return this.convertStreamToImageView(input);
	}
	
	public ImageView getRedUnCheck() {
		InputStream input = getClass().getResourceAsStream("/redUnCheck.png");
		return this.convertStreamToImageView(input);
	}
	
	public ImageView getTitleImage() {
		InputStream input = getClass().getResourceAsStream("/firstTry.gif");
		return this.convertStreamToImageView(input);
	}

	private ImageView convertStreamToImageView(InputStream input) {
		Image image = new Image(input);
		ImageView view = new ImageView(image);
		return view;
	}
	
}
