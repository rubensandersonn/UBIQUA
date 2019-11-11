/*******************************************************************************
 * Licenced under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.coap.server.main;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import com.coap.server.skeleton.Skeleton;

import com.coap.server.model.Device;
import com.google.gson.Gson;

/**
 * The Class CoAPServer.
 * 
 * @author Yasith Lokuge, Pedro Almir
 * 
 *         Commands to start/stop this application: java -jar [YourJarPath] ps
 *         -ef | grep java sudo kill -9 <pid>
 *         
 *         Context:
 *         - recorrencia
 *         - temperatura
 *         - umidade
 *         - proximidade: metros, latitude, longitude
 *         - data e hora
 *         - rede wifi
 *         - atividade
 *         - nome do ambiente 
 */
public class Server extends CoapServer {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		try {
			/* Create and start server */
			Server server = new Server();
			server.start();

		} catch (SocketException e) {
			System.err.println("Failed to initialize server: " + e.getMessage());
		}
	}

	/**
	 * Instantiates a new CoAP server.
	 *
	 * @throws SocketException the socket exception
	 */
	public Server() throws SocketException {
		/* Provide an instance of the main resource to register clients */
		add(new RegisterClientsResource());
	}

	/**
	 * The Class RegisterClientsResource.
	 */
	class RegisterClientsResource extends CoapResource {

		/**
		 * Instantiates a new resource.
		 */
		public RegisterClientsResource() {
			// set resource identifier
			super("devices");
			// set display name
			getAttributes().setTitle("devices");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.californium.core.CoapResource#handlePOST(org.eclipse.californium.
		 * core.server.resources.CoapExchange)
		 */
		@Override
		public void handlePOST(CoapExchange exchange) {
			try {
				Device device = new Gson().fromJson(exchange.getRequestText(), Device.class);

				CoapResource newResource = new CoapResource(device.getUid(), true);
				newResource.getAttributes().addContentType(device.getContextType());
				newResource.getAttributes().addResourceType(device.getResourceType());
				newResource.getAttributes().addInterfaceDescription(device.getType());
				
				
				
				// all the context goes in this
				for (String key : device.getContext().keySet()) {
					newResource.getAttributes().addAttribute(key, device.getContext().get(key));
				}
				
				add(newResource);
				exchange.respond(ResponseCode.CREATED);
				System.out.println(device.toString());
			} catch (Exception ex) {
				exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
			}
		}
		

	    @Override
	    public void handleGET(CoapExchange exchange) {
	    	System.out.println("=======================");
	    	try {
	    		System.out.println("children: "+getChildren());
		    	
	    		System.out.println("> exchange \n");
	    		System.out.println(">RequestText: "+ exchange.getRequestText());
	    		System.out.println(">request payload: "+ exchange.getRequestPayload());
	    		System.out.println(">source address (ip):"+ exchange.getSourceAddress());
	    		System.out.println(">source port: "+ exchange.getSourcePort());
	    		System.out.println(">request code: "+ exchange.getRequestCode());
	    		System.out.println(">request options: "+ exchange.getRequestOptions());
	    		System.out.println(">request options > URI host: "+ exchange.getRequestOptions().getURIHost());
	    		
	    		
		    	List<String> uriPaths = exchange.getRequestOptions().getURIQueries();
		    	
		    	
		    	System.out.println("parames: "+uriPaths);
		    	test();
		    	
		    	if(uriPaths.size() >= 1) {
		    		// the filter is passed by the uri as "/devices/filter"
		    		

		    		// this filters by all children devices
		    		Collection<Resource> filtered = dispatchDevices(uriPaths);

		    		// marshaling to json and sending
		    		//Gson gson = new Gson();
		    		
		    		ArrayList<HashMap<String, String> > resList = new ArrayList<HashMap<String, String>>();
		    		
		    		System.out.println("Result:");
		    		for(Resource r : filtered) {
		    			
		    			HashMap<String, String> newEl = new HashMap<String, String>();
		    			
		    			System.out.println("\n---");
		    			for(String s : r.getAttributes().getAttributeKeySet()) {
		    				System.out.println(r.getAttributes().getAttributeValues(s));
		    				newEl.put(s, r.getAttributes().getAttributeValues(s).get(0));
		    			}
		    			resList.add(newEl);
		    			System.out.println("---");
		    		}
		    		
		    		exchange.respond(resList.toString());
		    	}else {
		    		exchange.respond(ResponseCode.BAD_REQUEST);
		    	}
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
	    	System.out.println("=======================");
	    	
	    }
	    
	    /*
	     * Recebe os uris e retorna os devices filtrados
	     * 
	     * 		Context:
		 *         - recorrencia
		 *         - temperatura
		 *         - umidade
		 *         - proximidade: metros, latitude, longitude
		 *         - data e hora
		 *         - rede wifi
		 *         - atividade
		 *         - nome do ambiente 
		 */
	    public Collection<Resource> dispatchDevices(List<String> uris){
	    	System.out.println("=== Dispatching ===");
	    	Collection<Resource> children = getChildren();
	    	
	    	for(String uri: uris) {
	    		
	    		// no 0 h� o tipo e nos outros h� a informa��o
	    		// ... sempre que passar por um filtro, manter o mesmo conjunto...
	    		ArrayList <String> params = handleURI(uri);
	    		String tipo = params.get(0);
	    		Skeleton skt = new Skeleton();
	    		switch(tipo) {
	    			case "type":
	    				Boolean isSensor = params.get(1).intern().equalsIgnoreCase("sensor");
	    				
	    				if(isSensor) {
	    					children = skt.sensorSkeleton(children);
	    				}else {
	    					children = skt.actuatorSkeleton(children);
	    				}
	    				
	    				break;
	    			case "proximity":
	    				System.out.println("*** Proximity ***");
	    				Double metros = Double.parseDouble(params.get(1));
	    				Double lat = Double.parseDouble(params.get(2));
	    				Double lng = Double.parseDouble(params.get(3));
	    				
	    				children = skt.proximitySkeleton(metros, lat, lng, children);
	    				break;
	    			case "temperature":
	    				System.out.println("*** Temperature ***");
	    				int temp = Integer.parseInt(params.get(1));
	    				
	    				children = skt.tempSkeleton(temp, children);
	    				break;
	    			case "umidity":
	    				System.out.println("*** Umidity ***");
	    				int umd = Integer.parseInt(params.get(1));
	    				
	    				children = skt.umiditySkeleton(umd, children);
	    				break;
	    			case "date":
	    				System.out.println("*** Date ***");
	    				String ddmmaaaa = params.get(1);
	    				String hhmmss = params.get(2);
	    				
	    				children = skt.dateSkeleton(ddmmaaaa,  hhmmss, children);
	    				
	    				break;
	    			case "network":
	    				System.out.println("*** Network ***");
	    				String network = params.get(1);
	    				
	    				children = skt.networkSkeleton(network, children);
	    				
	    				break;
	    			case "activity":
	    				System.out.println("*** Activity captured ***");
	    				String activity = params.get(1);
	    				
	    				children = skt.activitySkeleton(activity, children);
	    				break;
	    			case "env_name":
	    				System.out.println("*** Enviroment ***");
	    				String env_name = params.get(1);
	    				
	    				children = skt.envSkeleton(env_name, children);
	    				break;
	    			case "sensor":
	    				System.out.println("*** Sensors ***");
	    				
	    				
	    				children = skt.sensorSkeleton(children);
	    				break;
	    			case "actuator":
	    				System.out.println("*** Actuators ***");
	    				
	    				
	    				children = skt.actuatorSkeleton(children);
	    				break;
	    			default: 
	    				System.out.println("ERRRO: URL inv�lido: " + params);
	    				return children;
	    			
	    		}
	    		
	    	}
	    	System.out.println("=== end dispatch ===");
	    	return children;
	    }
	        
	    
	    
	    
	    
	    
	    /*
	     * Splits the URI parameters.
	     * type1=...
	     * */
	    private ArrayList<String> handleURI(String uri){
	    	System.out.println("=== on handleURI | uri: "+ uri);
	    	if(uri == null) {
	    		System.out.println("! handleURI | no valid uri: "+ uri);
	    		return null;
	    	}
	    	// devices?arg0,arg1,...
	    	String[] params = uri.split("=");
	    	System.out.println("=== params: "+ params);
	    	
	    	ArrayList<String> res = new ArrayList<String>();
	    	
	    	for(String s : params) {
	    		res.add(s);
	    	}
	    	System.out.println("=== res: "+ res);
	    	return res;
	    }
	    
	    /*
	     * Just for testing fields
	     * */
	    private Collection<Resource> test(){
	    	Collection<Resource> children = getChildren();
	    	Collection<Resource> res = new ArrayList<Resource>();
	    	
	    	System.out.println("attributes: "+getAttributes());
	    	
	    	for(Resource child : children) {
	    		System.out.println("+===");
	    		System.out.println("on filter: " + child.getName());
	    		System.out.println("> path: " + child.getPath());
	    		System.out.println("> uri: " + child.getURI());
	    		System.out.println("> class: " + child.getClass());
	    		System.out.println("> attributes>: " + child.getAttributes());
	    		for(String s : child.getAttributes().getAttributeKeySet()) {
	    			System.out.println(">> "+ s + ": " + child.getAttributes().getAttributeValues(s));
	    		}
	    		System.out.println("> attributes>resourceTypes: " + child.getAttributes().getResourceTypes());
	    		System.out.println("> attributes>contentTypes: " + child.getAttributes().getContentTypes());
	    		System.out.println("> attributes>interfaceDescriptions: " + child.getAttributes().getInterfaceDescriptions());
	    		System.out.println("> attributes>title: " + child.getAttributes().getTitle());
	    		System.out.println("> children: " + child.getChildren());
	    		
	    		
	    		System.out.println("+===");
	    	}
	    	
	    	return res;
	    }

	}

}