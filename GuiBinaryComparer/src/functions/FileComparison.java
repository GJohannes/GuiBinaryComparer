package functions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileComparison {

	public boolean areFilesBinaryEqual(File fileA, File fileB) {
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
			e.printStackTrace();
		}
		return null;
	}	
}
