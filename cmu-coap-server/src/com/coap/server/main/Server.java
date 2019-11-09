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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;

import com.coap.server.model.Device;
import com.google.gson.Gson;

/**
 * The Class CoAPServer.
 * 
 * @author Yasith Lokuge, Pedro Almir
 * 
 *	Commands to start/stop this application: java -jar [YourJarPath] 
 *	ps -ef | grep java sudo kill -9 <pid>
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
				newResource.getAttributes().addAttribute("ip", device.getIp());
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
	    		LinkedHashMap<String, String> filters = new LinkedHashMap<>();
		    	List<String> params = exchange.getRequestOptions().getURIQueries();
		    	
		    	for(String uriParam : params){
		    		String[] keyValue = uriParam.split("=");
		    		filters.put(keyValue[0].trim(), keyValue[1].trim());
		    	}
		    	
		    	System.out.println("filters: " + filters);
		    	
		    	// This filters by all children devices
	    		Collection<Resource> filtered = filterDevices(filters);
	    		exchange.respond(ResponseCode.VALID, new Gson().toJson(convertResourceToDevice(filtered)), MediaTypeRegistry.APPLICATION_JSON);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		exchange.respond(ResponseCode.BAD_REQUEST);
	    	}
	    	
	    	System.out.println("=======================");
	    	
	    }
	    
	    private List<Device> convertResourceToDevice(Collection<Resource> resources){
	    	List<Device> devices = new ArrayList<>();
	    	Iterator<Resource> iterator = resources.iterator();
	    	while (iterator.hasNext()) {
				Resource resource = (Resource) iterator.next();
				Iterator<String> attrKeySet = resource.getAttributes().getAttributeKeySet().iterator();
				
				Device device = new Device();
				device.setUid(resource.getName());
				LinkedHashMap<String, String> context = new LinkedHashMap<>();
				while (attrKeySet.hasNext()) {
					String key = (String) attrKeySet.next();
					String value = resource.getAttributes().getAttributeValues(key).get(0).trim();
					
					if(!value.isEmpty()){
						if(key.equals("if")){
							device.setType(value);
						}else if(key.equals("ct")){
							device.setContextType(Integer.valueOf(value));
						}else if(key.equals("rt")){
							device.setResourceType(value);
						}else if(key.equals("ip")){
							device.setIp(value);
						}else{
							context.put(key, value);
						}
					}
				}
				device.setContext(context);
				devices.add(device);
			}
	    	return devices;
	    }
	    
	    private Collection<Resource> filterDevices(HashMap<String, String> filters){
	    	Collection<Resource> res = new ArrayList<Resource>(getChildren());
	    	
	    	Iterator<Resource> iterator = res.iterator();
	    	while (iterator.hasNext()) {
				Resource resource = (Resource) iterator.next();
				Iterator<String> attrKeySet = resource.getAttributes().getAttributeKeySet().iterator();
				boolean flagRemove = false;
				while (attrKeySet.hasNext()) {
					String key = (String) attrKeySet.next();
					String value = resource.getAttributes().getAttributeValues(key).get(0).trim();
					if(filters.get(key) != null && !filters.get(key).equals(value)){
						flagRemove = true;
						break;
					}
				}
				if(flagRemove) iterator.remove();
			}
	    	
	    	return res;
	    }

	}

}