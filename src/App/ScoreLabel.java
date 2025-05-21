package App;

import static org.mapsforge.core.util.MercatorProjection.*;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.core.graphics.Bitmap;

public class ScoreLabel extends Layer {
    private final LatLong position;
    private final double score;
    private final int tileSize;
    public final Bitmap icon;

    private static final Bitmap iconNormal = BitmapLoader.loadIcon("resources/score-label.png");
    private static final Bitmap iconGreen = BitmapLoader.loadIcon("resources/score-label-green.png");
    private static final Bitmap iconRed = BitmapLoader.loadIcon("resources/score-label-red.png");

    private static final int BAD_SCORE_THRESHOLD = 100;
    private static final int GOOD_SCORE_THRESHOLD = 200;

    public ScoreLabel(LatLong position, double score, int tileSize) {
        this.position = position;
        this.score = score;
        this.tileSize = tileSize;
        if (score >= GOOD_SCORE_THRESHOLD)
            icon = iconGreen;
        else if (score <= BAD_SCORE_THRESHOLD)
            icon = iconRed;
        else
            icon = iconNormal;
    }

    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        if (icon != null) {
            long mapSize = getMapSize(zoomLevel, tileSize);
            int x = (int) (longitudeToPixelX(position.longitude, mapSize) - topLeftPoint.x);
            int y = (int) (latitudeToPixelY(position.latitude, mapSize) - topLeftPoint.y);

            canvas.drawBitmap(icon, x, y);

            int textSize = 12;

            String scoreText = String.format("%.1f", score);
            Paint paintText = AwtGraphicFactory.INSTANCE.createPaint();
            paintText.setColor(org.mapsforge.core.graphics.Color.WHITE);
            paintText.setTextSize(textSize);

            int textX = x + (icon.getWidth() - scoreText.length() * textSize / 2) / 2;
            int textY = y + icon.getHeight() / 2 + 4;

            canvas.drawText(scoreText, textX, textY, paintText);
        }
    }
}
