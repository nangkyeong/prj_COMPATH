package flume;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import org.apache.logging.log4j.message.ParameterizedMessage;

public class FlumeRpcClient {
	private RpcClient client;
	private String hostname;
	private int port;

	public void init(String hostname, int port) {
		// Setup the RPC connection
		this.hostname = hostname;
		this.port = port;
		this.client = RpcClientFactory.getDefaultInstance(hostname, port);
		// Use the following method to create a thrift client (instead of the above
		// line):
		// this.client = RpcClientFactory.getThriftInstance(hostname, port);
	}

	public void sendEventToFlume(String logmsg) {
		this.sendEventToFlume(logmsg, null);
	}

	public void sendEventToFlume(String logmsg, Map<String, String> header) {
		// Create a Flume Event object that encapsulates the sample data
		Event event = EventBuilder.withBody(logmsg, Charset.forName("UTF-8"), header);

		// Send the event
		try {
			client.append(event);
		} catch (EventDeliveryException e) {
			// clean up and recreate the client
			client.close();
			client = null;
			client = RpcClientFactory.getDefaultInstance(hostname, port);
			// Use the following method to create a thrift client (instead of the above
			// line):
			// this.client = RpcClientFactory.getThriftInstance(hostname, port);
		}
	}
 
	//time, ip, sessionId, userid, gender, finalEdu, careerDur, keytolog, valtolog
	public String getLogMessage(String time, String ip, String sessionId, String id, String keytolog, String valtolog) {
		// 기록할 로그 목록 = time,ip,sessionID, id, keytolog, valtolog
		// org.apache.logging.log4j.message.ParameterizedMessage {}를 이용한 패턴 사용 가능
		ParameterizedMessage buildmsg = new ParameterizedMessage(
				"{\"time\" : \"{}\", \"ip\" : \"{}\", \"sessionID\" : \"{}\", \"id\": \"{}\", \"{}\" : \"{}\"}\n",
				new Object[] { time, ip, sessionId, id, keytolog, valtolog }, new Throwable());
		String message = buildmsg.getFormattedMessage();
		return message;
	}

	public void cleanUp() {
		// Close the RPC connection
		client.close();
	}

}