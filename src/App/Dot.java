package App;

import static org.mapsforge.core.util.MercatorProjection.*;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.layer.Layer;

public class Dot extends Layer {
    private final LatLong center;
    private final Paint paint;
    private final int tileSize;

    public Dot(LatLong center, int tileSize) {
        this.center = center;
        this.tileSize = tileSize;
        Paint paint = AwtGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Style.FILL);
        this.paint = paint;
    }

    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        final int circleRadius = 5;
        long mapSize = getMapSize(zoomLevel, tileSize);
        int x = (int) (longitudeToPixelX(center.longitude, mapSize) - topLeftPoint.x);
        int y = (int) (latitudeToPixelY(center.latitude, mapSize) - topLeftPoint.y);
        canvas.drawCircle(x, y, circleRadius, paint);
    }
}
