import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class OSM2neo4J {
	HashMap<String, Punto> nodes;
	SAXParserFirstSteep spfs;	
	public OSM2neo4J() {
		super();
		this.nodes = new HashMap<String, Punto>();
		this.spfs = new SAXParserFirstSteep(nodes);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OSM2neo4J objeto = new OSM2neo4J();
		try {
			objeto.spfs.checkInvoice(new FileInputStream(new File("cardiff.osm")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeInserter ni = new NodeInserter(objeto.nodes);
		ni.insertNodes();
		System.out.println(ni.payload);
		//System.out.println("keys en OSM2NEo4j");
		for (Punto key : objeto.nodes.values()) {
			//if (key.appears >0)
           //System.out.println("Keys de hash: "+key.toString());
    	}
		SAXParserSecondSteep sp2 = new SAXParserSecondSteep(ni.nodes);
		try {
			sp2.checkInvoice(new FileInputStream(new File("cardiff.osm")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sp2.payload);
	}

}
