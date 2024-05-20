package test;

import java.util.Map;
import java.util.UUID;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.LoginRequest;

/**
 * @author madhusmitap
 *
 */
public class SimpleUserMove extends Thread {
	//private static final Logger logger = Logger.getLogger(SimpleUserMove.class);
	private SmartFox sfs;
	private String userName;
	
	@Override
	public void run() {

		sfs = new SmartFox();
		userName = IChapaConstant.USER_PREFIX + UUID.randomUUID().toString();
		
		// connect to the server
		
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		// Add connection listener
		sfs.addEventListener(SFSEvent.CONNECTION, new IEventListener() {

			public void dispatch(BaseEvent evt) throws SFSException {
				Map<String, Object> params = evt.getArguments();
				System.out.println(params);

				if ((Boolean) params.get("success")) {

					System.out.println("Connection established");
					ISFSObject usrParam = new SFSObject();
					usrParam.putUtfString("sid", IChapaConstant.SESSION_ID);
					usrParam.putUtfString("key", IChapaConstant.CONNECTION_KEY);
					LoginRequest request = new LoginRequest(userName, "",
							"Chapatiz", usrParam);
					System.out.println("Logging in: " + userName);
					sfs.send(request);
				} else {
					System.out.println("Connection failed for " + userName);
				}
			}
		});

		// Add login event listener
		sfs.addEventListener(SFSEvent.LOGIN, new IEventListener() {

			@Override
			public void dispatch(BaseEvent event) throws SFSException {

				System.out.println("Login Success :) " + userName);
				ISFSObject params = new SFSObject();
				params.putUtfString("rn", "jetset.turtle");
				params.putInt("inst", -1);
				sfs.send(new ExtensionRequest("user.rj", params));
			}
		});

		// Add Extension response listener
		sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
			@Override
			public void dispatch(BaseEvent evnt) throws SFSException {
				System.out.println("extension response for the user=" + userName);
				Map<String,Object> param = evnt.getArguments();
				SFSObject obj = (SFSObject) param.get("params");

				if (param.get("cmd").equals("rj")) {					
					System.out.println("room: " + obj.getUtfString("id") + 
							" xCoord: " + obj.getInt("X") + " yCoord: " + obj.getInt("Y"));
					
					ISFSObject params = new SFSObject();
					params.putInt("x", IChapaConstant.X_TP);
					params.putInt("y", IChapaConstant.Y_TP);
					
					// Send user move request
					sfs.send(new ExtensionRequest("user.mv", params));
					
				} else if (param.get("cmd").equals("mv")) {
					System.out.println("User Moved: " + userName);
				}

			}
		});
		sfs.connect(IChapaConstant.SFS_SERVER_IP, IChapaConstant.SFS_SERVER_TCP_PORT);
		System.out.println("After connect");
		try {
		Thread.sleep(1000);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
		
		// Remove the event listeners
		sfs.removeAllEventListeners();
				
		// Close the connection to server
		sfs.disconnect();
	}

}
