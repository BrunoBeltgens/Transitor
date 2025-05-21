package Routing.Graph.algorithms;

import Routing.Point;
import Routing.Graph.Vertex;

public class SpatialIndex {
    private final KDTree kdTree;
    private final DistanceCalculator distanceCalculator;

    public SpatialIndex() {
        this.kdTree = new KDTree();
        this.distanceCalculator = new DistanceCalculator();
    }

    public void insert(Vertex v) {
        kdTree.insert(v);
    }

    public Vertex nearest(Point target) {
        return kdTree.nearest(target);
    }

    private class KDTree {
        private KDNode root;

        private static class KDNode {
            private final Vertex vertex;
            private KDNode left, right;
            private final boolean vertical;

            private KDNode(Vertex vertex, boolean vertical) {
                this.vertex = vertex;
                this.vertical = vertical;
            }
        }

        public void insert(Vertex vertex) {
            root = insert(root, vertex, true);
        }

        private KDNode insert(KDNode node, Vertex vertex, boolean vertical) {
            if (node == null)
                return new KDNode(vertex, vertical);

            if (vertical) {
                if (vertex.getLon() < node.vertex.getLon())
                    node.left = insert(node.left, vertex, !vertical);
                else
                    node.right = insert(node.right, vertex, !vertical);
            } else {
                if (vertex.getLat() < node.vertex.getLat())
                    node.left = insert(node.left, vertex, !vertical);
                else
                    node.right = insert(node.right, vertex, !vertical);
            }
            return node;
        }

        public Vertex nearest(Point target) {
            return nearest(root, target, root, true).vertex;
        }

        private KDNode nearest(KDNode node, Point target, KDNode best, boolean vertical) {
            if (node == null)
                return best;

            if (distanceCalculator.haversine(node.vertex, target) < distanceCalculator.haversine(best.vertex, target))
                best = node;

            KDNode goodSide, badSide;
            if (vertical) {
                if (target.getLon() < node.vertex.getLon()) {
                    goodSide = node.left;
                    badSide = node.right;
                } else {
                    goodSide = node.right;
                    badSide = node.left;
                }
            } else {
                if (target.getLat() < node.vertex.getLat()) {
                    goodSide = node.left;
                    badSide = node.right;
                } else {
                    goodSide = node.right;
                    badSide = node.left;
                }
            }

            best = nearest(goodSide, target, best, !vertical);
            double threshold = distanceCalculator.haversine(best.vertex, target);
            if (vertical) {
                double lonDifferenceMeters = Math.abs((node.vertex.getLon() - target.getLon()) * metersPerDegreeLon(target.getLat()));
                if (lonDifferenceMeters < threshold)
                    best = nearest(badSide, target, best, !vertical);
            } else {
                double latDifferenceMeters = Math.abs((node.vertex.getLat() - target.getLat()) * metersPerDegreeLat());
                if (latDifferenceMeters < threshold)
                    best = nearest(badSide, target, best, !vertical);
            }

            return best;
        }

        private double metersPerDegreeLon(double latitude) {
            double earthRadius = 6371000;
            return Math.PI * earthRadius * Math.cos(Math.toRadians(latitude)) / 180.0;
        }
        
        private double metersPerDegreeLat() {
            double earthRadius = 6371000;
            return Math.PI * earthRadius / 180.0;
        }
    }
}
