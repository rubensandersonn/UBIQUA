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
 *         Commands to start/stop this application: java -jar [YourJarPath] ps
 *         -ef | grep java sudo kill -9 <pid>
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
		    	
		    	List<String> uriPaths = exchange.getRequestOptions().getURIQueries();
		    	
		    	System.out.println("parames: "+uriPaths);
		    	
		    	if(uriPaths.size() >= 1) {
		    		// the filter is passed by the uri as "/devices/filter"
		    		String filter = uriPaths.get(0);

		    		// this filters by all children devices
		    		Collection<Resource> filtered = filterDevices(filter);

		    		// marshaling to json and sending
		    		Gson gson = new Gson();
		    		exchange.respond(filtered.toString());
		    	}else {
		    		exchange.respond(ResponseCode.BAD_REQUEST);
		    	}
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
	    	System.out.println("=======================");
	    	
	    }
	    
	    private Collection<Resource> filterDevices(String filter){
	    	Collection<Resource> children = getChildren();
	    	Collection<Resource> res = new ArrayList<Resource>();
	    	
	    	System.out.println("attributes: "+getAttributes());
	    	
	    	for(Resource child : children) {
	    		System.out.println("+===");
	    		System.out.println("on filter: " + child.getName());
	    		System.out.println(": " + child.getPath());
	    		System.out.println(": " + child.getURI());
	    		System.out.println(": " + child.getClass());
	    		System.out.println(": " + child.getAttributes().getResourceTypes());
	    		System.out.println(": " + child.getAttributes().getContentTypes());
	    		System.out.println(": " + child.getAttributes().getInterfaceDescriptions());
	    		
	    		if(child.getAttributes().getInterfaceDescriptions().contains(filter)) {
	    			System.out.println("---ahoy---");
	    			res.add(child);
	    		}
	    		System.out.println("+===");
	    	}
	    	
	    	return res;
	    }

	}

}