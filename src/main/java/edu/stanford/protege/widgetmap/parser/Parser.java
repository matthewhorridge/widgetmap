package edu.stanford.protege.widgetmap.parser;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2013
 */
public class Parser {
//
//    public Node parse(InputStream is) {
//        JsonReader reader = Json.createReader(is);
//        JsonStructure structure = reader.read();
//        JsonObject object = (JsonObject) structure;
//        return parseNode(object, null);
//    }
//
//    private Node parseNode(JsonObject object, ParentNode parentNode) {
//        double weight = parseWeight(object);
//        if(object.containsKey("children")) {
//            Direction direction = parseDirection(object);
//            ParentNode childNode = new ParentNode(direction);
//            if (parentNode != null) {
//                parentNode.addChild(childNode, weight);
//            }
//            JsonArray array = (JsonArray) object.get("children");
//            for (Object anArray : array) {
//                JsonObject val = (JsonObject) anArray;
//                parseNode(val, childNode);
//            }
//
//            return childNode;
//        }
//        else {
//            NodeProperties.Builder builder = NodeProperties.builder();
//            for(String key : object.keySet()) {
//                if(!key.equals("weight") && !key.equals("direction") && !key.equals("children")) {
//                    builder.setValue(key, object.get(key).toString());
//                }
//            }
//            TerminalNode.Builder nodeBuilder = TerminalNode.builder();
//            TerminalNode tn = nodeBuilder.withProperties(builder.build()).build();
//            if (parentNode != null) {
//                parentNode.addChild(tn, weight);
//            }
//            return tn;
//        }
//    }
//
//    private Direction parseDirection(JsonObject object) {
//        JsonString dir = (JsonString) object.get("direction");
//        if(dir == null) {
//            return Direction.ROW;
//        }
//        return Direction.valueOf(dir.getString().toUpperCase());
//    }
//
//    private double parseWeight(JsonObject object) {
//        JsonNumber weight = object.getJsonNumber("weight");
//        if(weight == null) {
//            return 1.0;
//        }
//        else {
//            return weight.doubleValue();
//        }
//    }
//
//    public static void main(String[] args) throws Exception{
//        URL url = Parser.class.getResource("/example.json");
//        InputStream is = new FileInputStream(new File(url.toURI()));
//        Parser p = new Parser();
//        Node n = p.parse(is);
//        is.close();
//        NodeDumper d = new NodeDumper();
//        n.accept(d);
//    }

}
