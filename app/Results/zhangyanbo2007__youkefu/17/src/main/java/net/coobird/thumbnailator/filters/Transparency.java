package net.coobird.thumbnailator.filters;
 import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
public class Transparency implements ImageFilter{

 private  AlphaComposite composite;

/**
 * Instantiates a {@link Transparency} filter with the specified opacity.
 *
 * @param alpha		The opacity of the resulting image. The value should be
 * 					between {@code 0.0f} (transparent) to {@code 1.0f}
 * 					(opaque), inclusive.
 * @throws IllegalArgumentException	If the specified opacity is outside of
 * 									the range specified above.
 */
public Transparency(float alpha) {
    super();
    if (alpha < 0.0f || alpha > 1.0f) {
        throw new IllegalArgumentException("The alpha must be between 0.0f and 1.0f, inclusive.");
    }
    this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
}/**
 * Instantiates a {@link Transparency} filter with the specified opacity.
 * <p>
 * This is a convenience constructor for the
 * {@link Transparency#Transparency(float)} constructor.
 *
 * @param alpha		The opacity of the resulting image. The value should be
 * 					between {@code 0.0f} (transparent) to {@code 1.0f}
 * 					(opaque), inclusive.
 * @throws IllegalArgumentException	If the specified opacity is outside of
 * 									the range specified above.
 */
public Transparency(double alpha) {
    this((float) alpha);
}
public BufferedImage apply(BufferedImage img){
    int width = img.getWidth();
    int height = img.getHeight();
    BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = finalImage.createGraphics();
    g.setComposite(composite);
    g.drawImage(img, 0, 0, null);
    g.dispose();
    return finalImage;
}


public float getAlpha(){
    return composite.getAlpha();
}


}