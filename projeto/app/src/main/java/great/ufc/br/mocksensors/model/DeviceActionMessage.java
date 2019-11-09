/**
 * 
 */
package great.ufc.br.mocksensors.model;

import java.util.List;

/**
 * @author PedroAlmir
 *
 */
public class DeviceActionMessage {
	/** Communication token */
	private String cToken;
	/** List of actions to execute in device */
	private List<DeviceAction> actions;
	
	/** Default constructor */
	public DeviceActionMessage() {}
	
	/**
	 * @param cToken
	 * @param actions
	 */
	public DeviceActionMessage(String cToken, List<DeviceAction> actions) {
		this.cToken = cToken;
		this.actions = actions;
	}
	/**
	 * @return the cToken
	 */
	public String getcToken() {
		return cToken;
	}
	/**
	 * @param cToken the cToken to set
	 */
	public void setcToken(String cToken) {
		this.cToken = cToken;
	}
	/**
	 * @return the actions
	 */
	public List<DeviceAction> getActions() {
		return actions;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<DeviceAction> actions) {
		this.actions = actions;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceActionMessage [cToken=" + cToken + ", actions=" + actions + "]";
	}
}
