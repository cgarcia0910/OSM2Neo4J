//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.xml.sax.helpers.DefaultHandler;
//
///**
// * Check SkatesTown invoice totals using a SAX parser.
// */
//public class SAXParserSecondSteep
//    extends DefaultHandler
//    implements InvoiceChecker
//{    
//	public SAXParserSecondSteep(HashMap<String, Punto> nodes) {
//		super();
//		this.nodes = nodes;
//		for (Punto key : nodes.values()) {
//			//if (key.appears >0)
//           //System.out.println("Keys de SAX2: "+key.toString());
//    	}
//	}
//
//
//	HashMap<String, Punto> nodes = new HashMap<String, Punto>();
//    double runningTotal = 0.0;
//    // invoice total
//    double total = 0.0;
//    boolean inWay = false;
//    // Utility data for extracting money amounts from content
//    boolean isMoneyContent = false;
//    double amount = 0.0;
//    ArrayList<String> refs;
//    public String payload = "";
//    boolean isStreet = false;
//
//    /**
//     * Check invoice totals.
//     * @param     invoiceXML    Invoice XML document
//     * @exception Exception     Any exception returned during checking
//     */
//    public void checkInvoice(InputStream invoiceXML) throws Exception {
//        // Use the default (non-validating) parser
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        SAXParser saxParser = factory.newSAXParser();
//
//        // Parse the input; we are the handler of SAX events
//        saxParser.parse(invoiceXML, this);
//    }
//
//    // SAX DocumentHandler methods
//    /*
//    public void startDocument() throws SAXException {
//        runningTotal = 0.0;
//        total = 0.0;
//        isMoneyContent = false;
//        System.out.println("empieza");
//    }*/
///*
//    public void endDocument() throws SAXException {
//        // Use delta equality check to prevent cumulative
//        // binary arithmetic errors. In this case, the delta
//        // is one half of one cent
//        if (Math.abs(runningTotal - total) >= 0.005) {
//            throw new SAXException(
//                "Invoice error: total is " + Double.toString(total) +
//                " while our calculation shows a total of " +
//                Double.toString(Math.round(runningTotal * 100) / 100.0));
//        }
//    
//        else System.out.println("InvoiceChecker correct !");
//    }*/
//
//    public void startElement(String namespaceURI,
//                             String localName,
//                             String qualifiedName,
//                             Attributes attrs) throws SAXException {
//    	/*
//    	if (qualifiedName.equals("node")) {
//    		
//    		//refs.add(attrs.getValue(namespaceURI, "id"));
//    		//System.out.println(attrs.getValue(namespaceURI, "id"));
//    		//nodes.put(attrs.getValue(namespaceURI, "id"), new Punto(attrs.getValue(namespaceURI, "lat"), attrs.getValue(namespaceURI, "lon"), 0));
//    	}
//        if (qualifiedName.equals("way"))
//        {
//        	refs = new ArrayList<String>();
//        	//System.out.println("start way");
//        	inWay = true;
//        }
//        if (qualifiedName.equals("nd") && attrs.getValue(namespaceURI, "ref") != null){
//        	refs.add(attrs.getValue(namespaceURI, "ref"));
//        }
//        if (inWay && qualifiedName.equals("tag") && attrs.getValue(namespaceURI, "k").equals("name"))
//        {
//        	//System.out.println(attrs.getValue(namespaceURI,"v")); 
//        }*/
//    	if (qualifiedName.equals("node")) {
//    		//System.out.println(attrs.getValue(namespaceURI, "id"));
//    		//nodes.put(attrs.getValue(namespaceURI, "id").toString().replace("\"", ""), new Punto(attrs.getValue(namespaceURI, "lat"), attrs.getValue(namespaceURI, "lon"), 0));
//    		//System.out.println("PRUEBA"+attrs.getValue(namespaceURI, "id").toString().substring(1, attrs.getValue(namespaceURI, "id").length()));
//    	}
//        if (qualifiedName.equals("way"))
//        {
//        	refs = new ArrayList<String>();
//        	//System.out.println("start way");
//        	inWay = true;
//        }
//        if (qualifiedName.equals("nd") && attrs.getValue(namespaceURI, "ref") != null){
//        	refs.add(attrs.getValue(namespaceURI, "ref").toString().replace("\"", ""));
//        }
//        if (inWay && qualifiedName.equals("tag") && attrs.getValue(namespaceURI, "k").equals("highway")  && !attrs.getValue(namespaceURI, "v").equals("pedestrian"))
//        {
//        	isStreet = true;
//        	//System.out.println(attrs.getValue(namespaceURI,"v")); 
//        }
//        if (inWay && qualifiedName.equals("tag") && attrs.getValue(namespaceURI, "k").equals("maxspeed"))
//        {
//        	//isStreet = true;
//        	System.out.println("limite vel");
//        	//System.out.println(attrs.getValue(namespaceURI,"v")); 
//        }
//    }
//
//    public void endElement(String namespaceURI,
//                           String localName,
//                           String qualifiedName) throws SAXException {
//        /*
//        if (inWay && qualifiedName.equals("way")){
//	        	inWay = false;
//	        	this.payload = "[";
//	        	if(isStreet) {
//		        	//System.out.println(nodes.toString());
//		        	//HACER LA PRIMERA REALCION
//		        	System.out.println("KKK"+nodes.get(refs.get(0)).toString());
//		        	
//		        	if (refs.size() > 1) {
//		        		this.payload = this.payload + "{\"method\" : \"POST\", \"to\": \""+nodes.get(refs.get(0).replace("\"", "")).getIdN4J()+"/relactionships\", \"body\" : {\"to\" : \""+nodes.get(refs.get(1).replace("\"", "")).getIdN4J()+"\"}}";
//		        	}
//		        	if (refs.size() > 2) {	
//			        	for (int i=2; i<refs.size(); i++) {
//		//	        		System.out.println(refs.get(i));
//		//	        		System.out.println(nodes.get(refs.get(i)).lat);
//		//	        		System.out.println(nodes.get(refs.get(i)).lng);
//			        		//this.payload = this.payload + "{\"method\" : \"POST\", }";
//			        		/*if (nodes.get(Double.valueOf(refs.get(i))) == null) {
//			        			nodes.put(refs.get(i), new Punto(nodes.get(Double.valueOf(refs.get(i))).lat, nodes.get(Double.valueOf(refs.get(i))).lng, 1));
//			        		}
//			        		else {
//			        			//nodes.put(refs.get(i), new Punto(nodes.get(refs.get(i)).lat, nodes.get(refs.get(i)).lng, nodes.get(refs.get(i)).appears+1));
//			        		//}
//			        		
//			        		this.payload = this.payload + ",{\"method\" : \"POST\", \"to\": \""+nodes.get(refs.get(i-1).replace("\"", "")).getIdN4J()+"/relactionships\", \"body\" : {\"to\" : \""+nodes.get(refs.get(i).replace("\"", "")).getIdN4J()+"\"}}";
//			        	}
//		        	}
//		        	
//		        	//System.out.println(refs.toString());
//		        	
//		        	Iterable it = (Iterable)nodes.entrySet().iterator();
//		        	while (((Iterator<Entry<String, Punto>>) it).hasNext()) {
//		        		Map.Entry e = (Map.Entry)((Iterator<Entry<String, Punto>>) it).next();
//		        	System.out.println(e.getKey() + " " + e.getValue().toString());
//		        	}
//		
//		        	System.out.println(nodes.toString());
//		        	
//		        	for (Punto key : nodes.values()) {
//		                //System.out.println(key.toString());
//		        	}
//		        }
//		        this.payload = this.payload + "]";
//        }
//        */
//        if (inWay && qualifiedName.equals("way")){        	
//        	inWay = false;
//        	if (isStreet) {
//        		isStreet = false;
//        		System.out.println("way");
//	        	for (int i=0; i<refs.size(); i++) {
//	        		System.out.println("KKK"+nodes.get(refs.get(0)).toString());        		
//	        	}
//        	}
//        	
//        }
//    }
//
//    public void characters(char buf[], int offset, int len)
//        throws SAXException {
//        if (isMoneyContent) {
//            String value = new String(buf, offset, len);
//            amount = Double.valueOf(value).doubleValue();
//        }
//    }
//    
//    
//    public static void main(String[] args) {
//    //	SAXParserSecondSteep ic = new SAXParserSecondSteep();
//    	try {
////			ic.checkInvoice(new FileInputStream(new File("invoiceExample.xml")));
//	//		System.out.println(ic.payload);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//}




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class SAXParserSecondSteep
    extends DefaultHandler
    implements InvoiceChecker
{
    public SAXParserSecondSteep(HashMap<String, Punto> nodes) {
		super();
		this.nodes = nodes;
	}

	// Class-level data
    // invoice running total	
	HashMap<String, Punto> nodes; // = new HashMap<String, Punto>();
    boolean inWay = false;
    boolean isStreet = false;
    ArrayList<String> refs;
    String payload = "[";

    /**
     * Check invoice totals.
     * @param     invoiceXML    Invoice XML document
     * @exception Exception     Any exception returned during checking
     */
    public void checkInvoice(InputStream invoiceXML) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(invoiceXML, this);
        this.payload = this.payload.substring(0, payload.length()-1);
        this.payload = this.payload+"]";
    }
    public void startElement(String namespaceURI,
                             String localName,
                             String qualifiedName,
                             Attributes attrs) throws SAXException {
    	if (qualifiedName.equals("node")) {
    		//System.out.println(attrs.getValue(namespaceURI, "id"));
    		//nodes.put(attrs.getValue(namespaceURI, "id").toString().replace("\"", ""), new Punto(attrs.getValue(namespaceURI, "lat"), attrs.getValue(namespaceURI, "lon"), 0));
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
        		//System.out.println("street");
        		//HACER LA PRIMERA REALCION
	        	//System.out.println("KKK"+nodes.get(refs.get(0)).toString());
	        	
	        	if (refs.size() > 1) {
	        		//this.payload = this.payload + "{\"method\" : \"POST\", \"to\": \"/"+nodes.get(refs.get(0).replace("\"", "")).getIdN4J()+"/relationships\", \"body\" : {\"to\" : \""+nodes.get(refs.get(1).replace("\"", "")).getIdN4J()+"\"}},";
	        		insertarRelationship(nodes.get(refs.get(0).replace("\"", "")).getIdN4J(), nodes.get(refs.get(1).replace("\"", "")).getIdN4J());
	        	}
	        	if (refs.size() > 2) {	
		        	for (int i=2; i<refs.size(); i++) {
	//	        		System.out.println(refs.get(i));
	//	        		System.out.println(nodes.get(refs.get(i)).lat);
	//	        		System.out.println(nodes.get(refs.get(i)).lng);
		        		//this.payload = this.payload + "{\"method\" : \"POST\", }";
		        		/*if (nodes.get(Double.valueOf(refs.get(i))) == null) {
		        			nodes.put(refs.get(i), new Punto(nodes.get(Double.valueOf(refs.get(i))).lat, nodes.get(Double.valueOf(refs.get(i))).lng, 1));
		        		}
		        		else {
		        			//nodes.put(refs.get(i), new Punto(nodes.get(refs.get(i)).lat, nodes.get(refs.get(i)).lng, nodes.get(refs.get(i)).appears+1));
		        		//}
		        		*/
		        		//this.payload = this.payload + "{\"method\" : \"POST\", \"to\": \"/"+nodes.get(refs.get(i-1).replace("\"", "")).getIdN4J()+"/relationships\", \"body\" : {\"to\" : \""+nodes.get(refs.get(i).replace("\"", "")).getIdN4J()+"\"}},";
		        		insertarRelationship(nodes.get(refs.get(i-1).replace("\"", "")).getIdN4J(), nodes.get(refs.get(i).replace("\"", "")).getIdN4J());
		        	}
		        	//this.payload = this.payload + ",{\"method\" : \"POST\", \"to\": \""+nodes.get(refs.get(refs.size()-1).replace("\"", "")).getIdN4J()+"/relactionships\", \"body\" : {\"to\" : \""+nodes.get(refs.get(refs.size()).replace("\"", "")).getIdN4J()+"\"}}";
	        	}
//	        	this.payload = this.payload + "]";
	        	
        	}
        	
        }
    }
    public void insertarRelationship(String src, String dst) {
    	String url = "http://localhost:7474";
    	String relPayload = "{\"to\":\"http://localhost:7474/db/data/node/"+dst+"\", \"type\" : \"STREET\"}";
    	URL obj;
		String urlParameters = "/db/data/node/"+src+"/relationships";
		StringBuffer jsonString;
		try {
			obj = new URL(url.concat(urlParameters));
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

		
		connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("authorization", "Basic bmVvNGo6dGZn");
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write(relPayload);
        writer.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        jsonString = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
                jsonString.append(line);
        }
        //System.out.print(jsonString);
        br.close();
        connection.disconnect();
    } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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