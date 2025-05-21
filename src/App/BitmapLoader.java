package App;

import java.io.FileInputStream;
import java.io.InputStream;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.awt.graphics.AwtBitmap;

public class BitmapLoader {
    public static Bitmap loadIcon(String path) {
        try {
            InputStream is = new FileInputStream(path);
            AwtBitmap bitmap = new AwtBitmap(is);
            final int SCALE_FACTOR = 15;
            bitmap.scaleTo(bitmap.getWidth() / SCALE_FACTOR, bitmap.getHeight() / SCALE_FACTOR);
            return bitmap;
        } catch (Exception ex) {
            return null;
        }
    }
}
