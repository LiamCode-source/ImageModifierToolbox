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
import com.mendix.core.Core;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Rotates image clockwise/anticlockwise
 */
public class ImageRotate extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __OriginalImage;
	private system.proxies.Image OriginalImage;
	private imagemodifiertoolbox.proxies.ENUM_RotationDirection Direction;
	private java.math.BigDecimal RotationDegrees;

	public ImageRotate(IContext context, IMendixObject OriginalImage, java.lang.String Direction, java.math.BigDecimal RotationDegrees)
	{
		super(context);
		this.__OriginalImage = OriginalImage;
		this.Direction = Direction == null ? null : imagemodifiertoolbox.proxies.ENUM_RotationDirection.valueOf(Direction);
		this.RotationDegrees = RotationDegrees;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.OriginalImage = this.__OriginalImage == null ? null : system.proxies.Image.initialize(getContext(), __OriginalImage);

		// BEGIN USER CODE
		String filename = OriginalImage.getName();
		String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		
		// Checks image is in valid format for modification
		if (!extension.equals("jpg") && !extension.contentEquals("jpeg") && !extension.contentEquals("png")) {
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
				BufferedImage rotatedImage = null;
				InputStream inputStream = null;
				ByteArrayOutputStream outputStream = null;
				
				try {

					image = ImageIO.read(Core.getImage(context, __OriginalImage, false));
					
					// If no value given, assume default values
					if (RotationDegrees == null) {
						rotatedImage = rotateTransform(image, Direction.name().toLowerCase(), 0.0f);
					}
					else {
						rotatedImage = rotateTransform(image, Direction.name().toLowerCase(), RotationDegrees.floatValue());
					}
					outputStream = new ByteArrayOutputStream();
					ImageIO.write(rotatedImage, extension, outputStream);
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
		return "ImageRotate";
	}

	// BEGIN EXTRA CODE
	public BufferedImage rotateTransform(BufferedImage image, String directionLowerCase, float rotationDeg) {	
		double rads;
		switch (directionLowerCase) {
		case "clockwise":
			rads = Math.toRadians(rotationDeg);
			break;

		// Only other option is counterclockwise
		case "anticlockwise":
			rads = Math.toRadians(rotationDeg * -1);
			break;

		//  Do not rotate if no valid direction given
		default:
			rads = 0;

		}
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
		final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads, 0, 0);
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);

		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(image, rotatedImage);
		return rotatedImage;

	}
	// END EXTRA CODE
}
