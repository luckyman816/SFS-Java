package com.sfs.test;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.SFSUser;
import sfs2x.client.entities.User;
import sfs2x.client.entities.UserPrivileges;
import sfs2x.client.entities.match.UserProperties;
import sfs2x.client.entities.variables.SFSUserVariable;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.CreateRoomRequest;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.RoomSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CopyOfChapaTest
{
    private static SmartFox sfs;
    
    public static void main(String args[]) {
    	
    	sfs = new SmartFox(true);
        
        // Add event handler for connection
        sfs.addEventListener(SFSEvent.CONNECTION, new IEventListener()
        {
            public void dispatch(BaseEvent evt) throws SFSException {
                

		        System.out.println("------------------------------");
		        System.out.println((sfs.getMySelf() != null) ? sfs.getMySelf(): "");
		        System.out.println("------------------------------");            	
            	
                // Retrieve event parameters
                Map<String, Object> params = evt.getArguments();
                System.out.println(params);
                if ((Boolean)params.get("success")) {
                    System.out.println("Connection established");
                    
    		        // for user authentication
    		        ISFSObject usrParam = new SFSObject();
    		        usrParam.putUtfString("sid", "123451");
    		        usrParam.putUtfString("key", "12345");                    
                    
    				LoginRequest request = new LoginRequest("madhusmita", "", "Chapatiz", usrParam);
    				System.out.println("Logging in");
    				sfs.send(request); 				
                }
                else {
                    System.out.println("Connection failed");
                }
            }
        });
        
        sfs.addEventListener(SFSEvent.CONNECTION_LOST, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("Connection LOST :(");
			}
		});
        
        // Listener for LOGIN, get called just after "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {				
				System.out.println("Login Success :)");	
				System.out.println(evt.getArguments().toString());
				System.out.println(evt.getArguments().get("data").toString());
				ISFSObject usrData = (ISFSObject) (evt.getArguments().get("data"));
				System.out.println(usrData.getUtfString("ad"));
				
				/*				
                // Retrieve event parameters
                Map<String, Object> params = evt.getArguments();
                SFSObject data = (SFSObject) params.get("data");
				System.out.println(data.getDump());
				
                SFSUser usr = (SFSUser) params.get("user");
				System.out.println(usr.toString());*/
				
		        // Send two integers to the Zone extension and get their sum in return
		        ISFSObject adParams = new SFSObject();
		        adParams.putInt("mid", 10);
		        adParams.putUtfString("ad", "Ad status");
		        adParams.putInt("at", 6);
		        sfs.send(new ExtensionRequest("user.ad", adParams));
		        
		        // SPELL
		        ISFSObject spellParam = new SFSObject();
		        spellParam.putInt("mid", 11);
		        
		        sfs.send(new ExtensionRequest("consumable.sl", spellParam));
		        
/*		        ISFSObject uuvParam = new SFSObject();
		        
		        ISFSObject seObj = new SFSObject();
		        seObj.putBool("speed", true);
		        seObj.putBool("levitate", false);
		        
		        uuvParam.putSFSObject("se", seObj);
		        
				ISFSObject seTest = new SFSObject();

				if (uuvParam.containsKey("se")) {
					ISFSObject oldSeObj = uuvParam.getSFSObject("se");
					seTest = oldSeObj;
				}
				
				seTest.putBool("levitate1", false);		        
		        
		        System.out.println(uuvParam.getDump());*/
			}
		});
        
        // Listener for LOGIN error if any during "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println(evt.getArguments());
				System.out.println("Login falied :(");
			}
		});
        
    	// Listener for handling extension response
        sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("extension call");
				System.out.println("evt.getArguments().get: " + evt.getArguments().get("cmd"));
				if ("uuv".equals(evt.getArguments().get("cmd"))) {
					 ISFSObject responseParams = (SFSObject)evt.getArguments().get("params");
					 System.out.println(responseParams.getDump());
				}
			}
		});         
        
        // Connect to SFS based on TCP set-up done at server
        //sfs.connect("sfs.chapatiz.com", 9933);
        sfs.connect("127.0.0.1", 9933);
        //sfs.connect("192.168.9.88", 9933);
        System.out.println("After connect");
        //sfs.disconnect();
        System.out.println("After disconnect");
    }
}