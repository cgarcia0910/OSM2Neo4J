import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

public class NodeInserter {
	public HashMap<String, Punto> nodes;
	private String url;
	private String idNodoN4j;
	private String idNodoOSM;
	public String payload;
	private static final String USER_AGENT = null;
	private class keysPair {
		
		public String OSM;
		public String N4J;
		public keysPair(String oSM, String n4j) {
			super();
			OSM = oSM;
			N4J = n4j;
		}
		@Override
		public String toString() {
			return "keysPair [OSM=" + OSM + ", N4J=" + N4J + "]";
		}
		
	}
	private ArrayList<keysPair> parcial;
	
	public NodeInserter(HashMap<String, Punto> nodes) {
		super();
		this.nodes = nodes;
		//System.out.println("keys en nodeInserter");
		for (String key : this.nodes.keySet()) {
	           //System.out.println("Keys de hash: "+key.toString());
	    	}
		this.url = "http://neo4j:tfg@localhost:7474";
		parcial = new ArrayList<keysPair>();
	}
	public void processJsonResponse(JsonElement elemento, HashMap<String, Punto> nodes) {
		//System.out.println("Nodos dentro de procesarJSonResponse");
//		for (String key : this.nodes.keySet()) {
//	           System.out.println("Keys de hash: "+key.toString());
//	    	}
		  if (elemento.isJsonObject()) {
		        JsonObject obj = elemento.getAsJsonObject();
		        java.util.Set<java.util.Map.Entry<String,JsonElement>> entradas = obj.entrySet();
		        java.util.Iterator<java.util.Map.Entry<String,JsonElement>> iter = entradas.iterator();
		        while (iter.hasNext()) {
		            java.util.Map.Entry<String,JsonElement> entrada = iter.next();
		            if (entrada.getKey().equals("id")) {
		            	idNodoN4j = entrada.getValue().toString().replace("\"", "");
		            //System.out.println("id");
		            }
		            	
		            //	System.out.println( "IDN4J: "+idNodoN4j);
		            if (entrada.getKey().equals("idOSM")) {
		            	idNodoOSM = entrada.getValue().toString().replace("\"", "");		            	
		            	//System.out.println("Nodos dentro de procesarJSonResponse");
		        		for (String key : this.nodes.keySet()) {
		        	           //System.out.println("Keys de hash: "+key.toString());
		        	    	}
		        		//System.out.println(entrada.getValue());
//		        		System.out.println("Nodo q tiene q buscar");
		            	//System.out.println("IDOSM: "+idNodoOSM);
		        		//System.out.println(idNodoOSM);
		            	//System.out.println(nodes.get(idNodoOSM));
		        		//this.nodes.put(this.nodes.get(idNodoOSM), value)
		            	//this.parcial.add(new keysPair(idNodoOSM, idNodoN4j));
		            	//System.out.println("InsertarEnMap: idOSM: "+idNodoOSM+" idN4j: "+idNodoN4j);
		            	//System.out.println(this.nodes.get(idNodoOSM));
		        
		            	this.nodes.put(idNodoOSM, new Punto(this.nodes.get(idNodoOSM).lat, this.nodes.get(idNodoOSM).lng, this.nodes.get(idNodoOSM).appears, idNodoN4j));
		            	//this.nodes.get(idNodoOSM).setIdN4J(idNodoN4j);
		            }		           
		            processJsonResponse(entrada.getValue(), nodes);
		        }
		        
		    } else if (elemento.isJsonArray()) {
		        JsonArray array = elemento.getAsJsonArray();
		        java.util.Iterator<JsonElement> iter = array.iterator();
		        while (iter.hasNext()) {
		            JsonElement entrada = iter.next();
		            processJsonResponse(entrada, nodes);
		        }
		    }
		    else if (elemento.isJsonPrimitive()) {
		    	//System.out.println(elemento.getAsJsonPrimitive().getAsString());
		    }
	}
	public void insertNodes() {
		boolean fistLoop = true;
		payload = "[";
		Set entrySet = nodes.entrySet();		 
		Iterator<Punto> it =  entrySet.iterator();
		while (it.hasNext()) {
			Entry pt = (Entry) it.next();
		    if (((Punto)pt.getValue()).appears > 0 && fistLoop) {
		    	payload = payload +"\n{\"method\":\"POST\", \"to\":\"/node\", \"body\":{\"idOSM\":\""+pt.getKey()+"\", \"lat\":\"" + ((Punto)pt.getValue()).lat.toString()+ "\", \"lng\":\"" + ((Punto)pt.getValue()).lng.toString() + "\"}}";
		    	fistLoop = false;
		    } else if (((Punto)pt.getValue()).appears > 0 && !fistLoop) {
		    	payload = payload +",\n{\"method\":\"POST\", \"to\":\"/node\", \"body\":{\"idOSM\":\""+pt.getKey()+"\", \"lat\":\"" + ((Punto)pt.getValue()).lat.toString()+ "\", \"lng\":\"" + ((Punto)pt.getValue()).lng.toString() + "\"}}";
		    }
		}
		payload = payload + "\n]";
		//System.out.print(payload);
//		for (Punto key : nodes.values()) {
//			if (key.appears >0 && firstLoop) {
//				payload = payload +"\"method\":\"POST\", \"TO\":\"\node\", \"body\":\"{\"lat\":\"" + key.lat.toString()+ "\", \"lng\":\"" + key.lng.toString() + "\"},";
//			}            
//    	}
		
		URL obj;
		String urlParameters = "/db/data/batch";
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
        writer.write(payload);
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
	JsonParser  parser = new JsonParser();
	JsonElement datos = parser.parse(jsonString.toString());
	
	processJsonResponse(datos, nodes);
    ///System.out.println(this.parcial.toString());
    for (int i=0; i<parcial.size(); i++) {
    	//System.out.println(nodes.get(parcial.get(i).OSM.toString()));
    }
    //String prueba = parcial.get(0).OSM;
    //System.out.println(prueba);
    //System.out.println("dsfs" + nodes.get(prueba.substring(1, prueba.length()-1)));
	}
}
