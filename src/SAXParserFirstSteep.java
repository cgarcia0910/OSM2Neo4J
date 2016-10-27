

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Check SkatesTown invoice totals using a SAX parser.
 */
public class SAXParserFirstSteep
    extends DefaultHandler
    implements InvoiceChecker
{
    public SAXParserFirstSteep(HashMap<String, Punto> nodes) {
		super();
		this.nodes = nodes;
	}

	// Class-level data
    // invoice running total	
	HashMap<String, Punto> nodes = new HashMap<String, Punto>();
    boolean inWay = false;
    boolean isStreet = false;
    ArrayList<String> refs;

    /**
     * Check invoice totals.
     * @param     invoiceXML    Invoice XML document
     * @exception Exception     Any exception returned during checking
     */
    public void checkInvoice(InputStream invoiceXML) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(invoiceXML, this);
    }
    public void startElement(String namespaceURI,
                             String localName,
                             String qualifiedName,
                             Attributes attrs) throws SAXException {
    	if (qualifiedName.equals("node")) {
    		//System.out.println(attrs.getValue(namespaceURI, "id"));
    		nodes.put(attrs.getValue(namespaceURI, "id").toString().replace("\"", ""), new Punto(attrs.getValue(namespaceURI, "lat"), attrs.getValue(namespaceURI, "lon"), 0));
    		//System.out.println("PRUEBA"+attrs.getValue(namespaceURI, "id").toString().substring(1, attrs.getValue(namespaceURI, "id").length()));
    	}
        if (qualifiedName.equals("way"))
        {
        	refs = new ArrayList<String>();
        	//System.out.println("start way");
        	inWay = true;
        }
        if (qualifiedName.equals("nd") && attrs.getValue(namespaceURI, "ref") != null){
        	refs.add(attrs.getValue(namespaceURI, "ref").toString().replace("\"", ""));
        }
        if (inWay && qualifiedName.equals("tag") && attrs.getValue(namespaceURI, "k").equals("highway")  && !attrs.getValue(namespaceURI, "v").equals("pedestrian"))
        {
        	isStreet = true;
        	//System.out.println(attrs.getValue(namespaceURI,"v")); 
        }
        if (inWay && qualifiedName.equals("tag") && attrs.getValue(namespaceURI, "k").equals("maxspeed"))
        {
        	//isStreet = true;
        	System.out.println("limite vel");
        	//System.out.println(attrs.getValue(namespaceURI,"v")); 
        }
    }

    public void endElement(String namespaceURI,
                           String localName,
                           String qualifiedName) throws SAXException {
    	
        if (inWay && qualifiedName.equals("way")){
        	inWay = false;
        	if (isStreet) {
        		isStreet = false;
	        	for (int i=0; i<refs.size(); i++) {
	        		nodes.put(refs.get(i).toString(), new Punto(nodes.get(refs.get(i)).lat, nodes.get(refs.get(i)).lng, nodes.get(refs.get(i)).appears+1));        		
	        	}
        	}
        	
        }
    }
    
    public static void main(String[] args) {
    	HashMap<String, Punto> nodes = new HashMap<String, Punto>();
    	SAXParserFirstSteep ic = new SAXParserFirstSteep(nodes);
    	
    	try {
			ic.checkInvoice(new FileInputStream(new File("invoiceExample.xml")));
			for (String key : nodes.keySet()) {
				//if (key.appears >1)
	            //System.out.println(key.toString());
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}