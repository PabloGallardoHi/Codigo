package weupnp;

import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/*
 * Clase que utiliza la librería Weupnp para
 * hacer el port mapping
 */
public class SelectorPuertoNat {

	private int SAMPLE_PORT;
	private static boolean LIST_ALL_MAPPINGS = false;
	private static GatewayDevice activeGW;

	public SelectorPuertoNat(int puerto) throws Exception{

		SAMPLE_PORT = puerto;
		
		addLogLine("Starting weupnp");

		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		addLogLine("Looking for Gateway Devices...");

		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();

		if (gateways.isEmpty()) {
			addLogLine("No gateways found");
			addLogLine("Stopping weupnp");
			return;
		}
		addLogLine(gateways.size()+" gateway(s) found\n");

		int counter=0;
		for (GatewayDevice gw: gateways.values()) {
			counter++;
			addLogLine("Listing gateway details of device #" + counter+
					"\n\tFriendly name: " + gw.getFriendlyName()+
					"\n\tPresentation URL: " + gw.getPresentationURL()+
					"\n\tModel name: " + gw.getModelName()+
					"\n\tModel number: " + gw.getModelNumber()+
					"\n\tLocal interface address: " + gw.getLocalAddress().getHostAddress()+"\n");
		}

		// choose the first active gateway for the tests
		activeGW = gatewayDiscover.getValidGateway();

		if (null != activeGW) {
			addLogLine("Using gateway: " + activeGW.getFriendlyName());
		} else {
			addLogLine("No active gateway device found");
			addLogLine("Stopping weupnp");
			return;
		}


		// testing PortMappingNumberOfEntries
		Integer portMapCount = activeGW.getPortMappingNumberOfEntries();
		addLogLine("GetPortMappingNumberOfEntries: " + (portMapCount!=null?portMapCount.toString():"(unsupported)"));

		// testing getGenericPortMappingEntry
		PortMappingEntry portMapping = new PortMappingEntry();
		if (LIST_ALL_MAPPINGS) {
			int pmCount = 0;
			do {
				if (activeGW.getGenericPortMappingEntry(pmCount,portMapping))
					addLogLine("Portmapping #"+pmCount+" successfully retrieved ("+portMapping.getPortMappingDescription()+":"+portMapping.getExternalPort()+")");
				else{
					addLogLine("Portmapping #"+pmCount+" retrieval failed"); 
					break;
				}
				pmCount++;
			} while (portMapping!=null);
		} else {
			if (activeGW.getGenericPortMappingEntry(0,portMapping))
				addLogLine("Portmapping #0 successfully retrieved ("+portMapping.getPortMappingDescription()+":"+portMapping.getExternalPort()+")");
			else
				addLogLine("Portmapping #0 retrival failed");        	
		}

		InetAddress localAddress = activeGW.getLocalAddress();
		addLogLine("Using local address: "+ localAddress.getHostAddress());
		String externalIPAddress = activeGW.getExternalIPAddress();
		addLogLine("External address: "+ externalIPAddress);
			
		addLogLine("Querying device to see if a port mapping already exists for port "+ SAMPLE_PORT);
		
		for(int i = 0; activeGW.getSpecificPortMappingEntry(SAMPLE_PORT,"TCP",portMapping) && i<=10; SAMPLE_PORT++){
			if(i==10){
				addLogLine("Port "+SAMPLE_PORT+" is already mapped. Aborting test.");
				return;
			} else{
				addLogLine("Port "+SAMPLE_PORT+" is already mapped. Traying next port");
			}
		}
		
		addLogLine("Mapping free. Sending port mapping request for port "+SAMPLE_PORT);

		// test static lease duration mapping
		if (activeGW.addPortMapping(SAMPLE_PORT,SAMPLE_PORT,localAddress.getHostAddress(),"TCP","test")) {
			addLogLine("Mapping SUCCESSFUL.");
		}
		
	}

	private static void addLogLine(String line) {

		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp+": "+line+"\n";
		System.out.print(logline);
	}
	
	public void releasePort() throws Exception{
		
		addLogLine("Stopping weupnp");
		
		if (activeGW.deletePortMapping(SAMPLE_PORT,"TCP")) {
			addLogLine("Port mapping removed, test SUCCESSFUL");
        } else {
			addLogLine("Port mapping removal FAILED");
        }
	}

	public int getPortUsed(){
		return SAMPLE_PORT;
	}
}
