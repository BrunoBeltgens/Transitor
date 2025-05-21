package App;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.layer.overlay.Marker;

public class RouteMarker extends Marker {
    private static final Bitmap icon = BitmapLoader.loadIcon("resources/marker.png");

    public RouteMarker(LatLong p) {
        super(p, icon, 0, - (icon != null ? icon.getHeight() / 2 : 0));
    }
}
