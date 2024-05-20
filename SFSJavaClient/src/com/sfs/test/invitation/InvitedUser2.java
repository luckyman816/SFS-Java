package com.sfs.test.invitation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.entities.invitation.Invitation;
import sfs2x.client.entities.invitation.InvitationReply;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.PublicMessageRequest;
import sfs2x.client.requests.game.InvitationReplyRequest;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class InvitedUser2
{
    private static SmartFox sfs;
    
    public static void main(String args[]) {
    	
    	List<Number> numbers = new ArrayList<Number>();
    	ArrayList<Integer> integers = new ArrayList<Integer>();
    	ArrayList<Long> longs = new ArrayList<Long>();
    	ArrayList<Float> floats = new ArrayList<Float>();
    	numbers.addAll(integers);
    	numbers.addAll(longs);
    	numbers.addAll(floats);
    	
    	sfs = new SmartFox();
        
    	
          sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
			
        	 
        	  
			@Override
			public void dispatch(BaseEvent evnt) throws SFSException {
				// TODO Auto-generated method stub
				System.out.println("getting response from the server"); 
				Map param=evnt.getArguments();
				System.out.println(evnt.getArguments());
				System.out.println("getting response "+param );
				SFSObject obj=(SFSObject) param.get("params");

				if(param.get("cmd").equals("rj"))
				{
					System.out.println("****************************************************************");
					Map param1=evnt.getArguments();
					System.out.println(evnt.getArguments());
					System.out.println("getting response "+param1 );
					ISFSObject obj1=(SFSObject) param1.get("params");
					System.out.println(obj1.getDump());
					System.out.println("****************************************************************");
					//User user = (User) evnt.getArguments().get("user");
	                //Map<String, Object> prop = user.getProperties();
	                //System.out.println("USER PROPERTIES: " + prop.get("test"));
				}
				}
			});
          
          sfs.addEventListener(SFSEvent.INVITATION, new IEventListener() {
              public void dispatch(BaseEvent evt) throws SFSException {
            	  System.out.println("Invitation request received");
            	  System.out.println((Invitation) evt.getArguments().get("invitation"));
            	  
            	  Invitation invitation = (Invitation) evt.getArguments().get("invitation");
            	  ISFSObject invtData =invitation.getParams();
            	  System.out.println(invtData.getDump());
            	  
            	  System.out.println("MESSAGE: " + invtData.getUtfString("msg"));
            	  if (invtData.getBool("buddyreq")) {
            		  sfs.send(new InvitationReplyRequest((Invitation) evt.getArguments().get("invitation"), 
            				  InvitationReply.ACCEPT,
            				  invtData));
            	  }
              }
          });
          sfs.addEventListener(SFSEvent.INVITATION_REPLY_ERROR, new IEventListener() {
              public void dispatch(BaseEvent evt) throws SFSException {
                  System.out.println("Failed to reply to invitation due to the following problem: " + evt.getArguments().get("errorMessage"));
              }
          });
          sfs.addEventListener(SFSEvent.INVITATION_REPLY, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent paramBaseEvent) throws SFSException {
				System.out.println("Invitation has been accepted");
				System.out.println(paramBaseEvent.getArguments());
			}
          });          
    	
        // Add event handler for connection
        sfs.addEventListener(SFSEvent.CONNECTION, new IEventListener()
        {
            public void dispatch(BaseEvent evt) throws SFSException {
                
                // Retrieve event parameters
                Map<String, Object> params = evt.getArguments();
                System.out.println(params);
                if ((Boolean)params.get("success")) {
                    System.out.println("Connection established");
                    
                    // for user authentication
                    ISFSObject usrParam = new SFSObject();
                    usrParam.putUtfString("sid", "fgdgsdgds");
                    usrParam.putUtfString("key", "12345");                    
       
                    LoginRequest request = new LoginRequest("Madhusmita-Test-1", "", "Chapatiz", usrParam);
    				
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
			public void dispatch(BaseEvent event) throws SFSException {
				System.out.println("Login Success :)");				
				
				Map<String, Object> mLoginOutputData = event.getArguments();
				System.out.println(mLoginOutputData.size());
				Iterator  itr=mLoginOutputData.keySet().iterator();
				while(itr.hasNext())
				{
					System.out.println(mLoginOutputData.get(itr.next()));
				}
				
		        ISFSObject params = new SFSObject();
		        params.putUtfString("rn", "central.hall");
		        params.putInt("inst",-1);
		        
		      sfs.send(new ExtensionRequest("user.rj", params));
		      
			}
		});
        
        
        // Listener for LOGIN error if any during "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				System.out.println("Login falied :(");
			}
		});

        // Connect to SFS based on TCP set-up done at server
        sfs.connect("127.0.0.1", 9933);
        System.out.println("After connect");
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}