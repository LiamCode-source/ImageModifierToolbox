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
import java.math.BigDecimal;
import imagemodifiertoolbox.global.CheckFile;

/**
 * Increases/decreases opacity of image
 */
public class ImageOpacity extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __OriginalImage;
	private system.proxies.Image OriginalImage;
	private java.math.BigDecimal Opacity;

	public ImageOpacity(IContext context, IMendixObject OriginalImage, java.math.BigDecimal Opacity)
	{
		super(context);
		this.__OriginalImage = OriginalImage;
		this.Opacity = Opacity;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.OriginalImage = this.__OriginalImage == null ? null : system.proxies.Image.initialize(getContext(), __OriginalImage);

		// BEGIN USER CODE
		String filename = OriginalImage.getName();
		String[] validExt = {"png", "PNG", "jpeg", "jpg", "JPG"};
		
		String extension = CheckFile.getFIleExt(filename);		
		boolean isValidExt = CheckFile.checkFileExt(extension, validExt);
		
		// Checks image is in valid format for modification
		if (!isValidExt) {
			Core.getLogger("ImageModifier").error("The file must be a jpg, jpeg or png file.");
		}
		else {	
			if (__OriginalImage == null) {
				throw new NullPointerException("No image provided.");
			} 
			else if (OriginalImage.getHasContents() != true) {
				Core.getLogger("ImageModifier").error("The input image has no contents.");
				return false;
			} 
			else {
				IContext context = this.getContext();

				BufferedImage image = null;
				BufferedImage opacityImage = null;
				InputStream inputStream = null;
				ByteArrayOutputStream outputStream = null;
				
				try {
					
					image = ImageIO.read(Core.getImage(context, __OriginalImage, false));					
					
					// If no valud value given, assume default values
					if (Opacity == null || Opacity.compareTo(BigDecimal.ZERO) < 0 || Opacity.compareTo(BigDecimal.ONE) > 0) {
						Core.getLogger("ImageModifier").warn("Invalid opacity value. Reverting opacity to 1.0.");
						opacityImage = opacityTransform(image, 1.0f);
					}
					else {
						opacityImage = opacityTransform(image, Opacity.floatValue());
					}
					
					outputStream = new ByteArrayOutputStream();
					ImageIO.write(opacityImage, extension, outputStream);
					inputStream = new ByteArrayInputStream(outputStream.toByteArray());
					// Stores modified image
					Core.storeImageDocumentContent(context, __OriginalImage, inputStream, 100, 100);
					return true;

				} catch (NullPointerException e) {
					Core.getLogger("ImageModifier").error(e.getMessage(), e);
					return false;
				} catch (Exception e) {
					Core.getLogger("ImageModifier").error(e.getMessage(), e);
					return false;
				} finally {
					inputStream.close();
					outputStream.close();
					}
				}
			}
			return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "ImageOpacity";
	}

	// BEGIN EXTRA CODE
	public BufferedImage opacityTransform(BufferedImage image, float opacity) {
		BufferedImage modImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = modImage.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();
		return modImage;
	}
	// END EXTRA CODE
}
