// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package imagemodifiertoolbox.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import com.mendix.core.Core;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Converts jpg, jpeg, and WEBP images to PNG
 */
public class ImageConvertToPNG extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __OriginalImage;
	private system.proxies.Image OriginalImage;

	public ImageConvertToPNG(IContext context, IMendixObject OriginalImage)
	{
		super(context);
		this.__OriginalImage = OriginalImage;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.OriginalImage = this.__OriginalImage == null ? null : system.proxies.Image.initialize(getContext(), __OriginalImage);

		// BEGIN USER CODE
		String filename = OriginalImage.getName();
		String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		
		IContext context = this.getContext();

		BufferedImage image = null;
		BufferedImage opacityImage = null;
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		boolean result = false;

		try {
			image = ImageIO.read(Core.getImage(context, __OriginalImage, false));
			
			outputStream = new ByteArrayOutputStream();
			// Writes image to png
			result = ImageIO.write(image, "png", outputStream);
			
			if (result) {
				 // Update the file metadata
                IMendixObject updatedImage = Core.instantiate(context, "System.Image");
				// Changes file name to match the changed format
				
				__OriginalImage.setValue(context, "Name", OriginalImage.getName().replace("."+extension, ".png"));
				Core.storeImageDocumentContent(context, updatedImage, new ByteArrayInputStream(outputStream.toByteArray()), image.getWidth(), image.getHeight());
				return true;
			}
			

		} catch (NullPointerException e) {
			Core.getLogger("ImageModifier").error(e.getMessage(), e);
			return false;
		} catch (Exception e) {
			Core.getLogger("ImageModifier").error(e.getMessage(), e);
			return false;
		} finally {
			//inputStream.close();
			outputStream.close();
		}
		return false;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "ImageConvertToPNG";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
