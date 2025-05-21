package Routing.Graph.generator;

import java.io.File;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Routing.Graph.Graph;
import Routing.Graph.Vertex;
import Routing.Graph.Edge.Edge;
import Routing.Graph.Edge.Target.IndexedTarget;

public class JSONGraph extends Generator {
    private final String filePath;

    public JSONGraph(Graph g, String filePath) {
        super(g);
        this.filePath = filePath;
    }

    @Override
    public Graph build() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            Iterator<JsonNode> elements = rootNode.elements();
            while (elements.hasNext()) {
                JsonNode vertexNode = elements.next();
                double latitude = vertexNode.get("la").asDouble();
                double longitude = vertexNode.get("lo").asDouble();

                Vertex vertex = new Vertex(latitude, longitude);
                g.addVertex(vertex);

                JsonNode edgesArray = vertexNode.get("e");
                for (JsonNode edgeNode : edgesArray) {
                    int index = edgeNode.get("i").asInt();
                    int weight = edgeNode.get("w").asInt();

                    vertex.getEdges().add(new Edge(new IndexedTarget(index), weight));
                }
            }
        } catch (Exception e) {
            System.out.println("Could not read the json file");
        }

        return g;
    }
}
