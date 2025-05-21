package App;

// SETUP MAP DATA
// GO TO: https://download.mapsforge.org/maps/v5/europe/
// Download what maps you want (probably netherlands.map)
// Put it in the folder resources/tiles/ (create this folder if it doesn't already exist)

// SOURCES
// https://stackoverflow.com/questions/54945117/javafx-and-mapsforge
// https://repo1.maven.org/maven2/
// https://github.com/mapsforge/mapsforge
// https://github.com/mapsforge/mapsforge/blob/master/docs/Integration.md
// https://github.com/mapsforge/mapsforge/issues/1017
// https://pixabay.com/vectors/google-map-marker-red-peg-309740/

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.awt.util.AwtUtil;
import org.mapsforge.map.awt.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class MapPanel extends MapView {
    private final int tileSize = 512;

    public MapPanel() {
        getMapScaleBar().setVisible(true);

        AwtUtil.createTileCache(
                tileSize,
                getModel().frameBufferModel.getOverdrawFactor(),
                1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        ArrayList<String> args = new ArrayList<>();

        // More maps can be added if necessary:
        args.add("resources/tiles/maastricht.map");
        ArrayList<File> mapFiles = getMapFiles(args);

        getModel().mapViewPosition.setMapPosition(new MapPosition(new LatLong(50.8385, 5.7217), (byte) 11.5));
        addLayers(mapFiles);
    }

    public int getTileSize() {
        return tileSize;
    }

    private void addLayers(ArrayList<File> mapFiles) {
        Layers layers = getLayerManager().getLayers();
        int tileSize = 512;

        // Tile cache
        TileCache tileCache = AwtUtil.createTileCache(
                tileSize,
                getModel().frameBufferModel.getOverdrawFactor(),
                1024,
                new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString()));

        {
            getModel().displayModel.setFixedTileSize(tileSize);
            MultiMapDataStore mapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
            if (mapFiles != null)
                for (File file : mapFiles)
                    mapDataStore.addMapDataStore(new MapFile(file), false, false);
            TileRendererLayer tileRendererLayer = createTileRendererLayer(tileCache, mapDataStore,
                    getModel().mapViewPosition);
            layers.add(tileRendererLayer);
            mapDataStore.boundingBox();
        }
    }

    private ArrayList<File> getMapFiles(ArrayList<String> args) {
        if (args.isEmpty())
            System.out.println("missing argument: <mapFile>");
        ArrayList<File> result = new ArrayList<>();
        for (String arg : args) {
            File mapFile = new File(arg);
            if (!mapFile.exists())
                System.out.println("file does not exist: " + mapFile);
            else if (!mapFile.isFile())
                System.out.println("not a file: " + mapFile);
            else if (!mapFile.canRead())
                System.out.println("cannot read file: " + mapFile);
            result.add(mapFile);
        }
        return result;
    }

    private TileRendererLayer createTileRendererLayer(TileCache tileCache, MapDataStore mapDataStore,
            IMapViewPosition mapViewPosition) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore, mapViewPosition, false,
                true, false, AwtGraphicFactory.INSTANCE, null) {
            @Override
            public boolean onTap(LatLong tapLatLong, org.mapsforge.core.model.Point layerXY,
                    org.mapsforge.core.model.Point tapXY) {
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
        return tileRendererLayer;
    }
}
