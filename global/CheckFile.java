package imagemodifiertoolbox.global;

public class CheckFile {	
	public static boolean checkFileExt(String fileExt, String[] validExtensions) {
		if (fileExt == null) {
			return false;
		}
		
		for (String validExt : validExtensions) {
			if (fileExt.equals(validExt)) {
				return true;
			}
		}
		return false;	
	}
	
	public static String getFIleExt(String fileName) {
		if (fileName == null || !fileName.contains(".")) {
			return "";
		}
		
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		return extension;
	}
}
