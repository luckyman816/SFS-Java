package com.sfs.test.buddy;

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
import sfs2x.client.core.SFSBuddyEvent;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Buddy;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.entities.invitation.Invitation;
import sfs2x.client.entities.invitation.InvitationReply;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.PublicMessageRequest;
import sfs2x.client.requests.buddylist.GoOnlineRequest;
import sfs2x.client.requests.buddylist.InitBuddyListRequest;
import sfs2x.client.requests.game.InvitationReplyRequest;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class InvitedUser
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
				else if(param.get("cmd").equals("bi"))
				{	
					System.out.println("BI response");
					ISFSObject biObj = (ISFSObject) param.get("params");
					System.out.println(biObj.getUtfString("errmsg"));

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
                    usrParam.putUtfString("sid", "TEST");
                    usrParam.putUtfString("key", "12345");                    
       
                    LoginRequest request = new LoginRequest("Madhusmita-Test-21", "", "Chapatiz", usrParam);
    				
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

				System.out.println("Init Buddy List");
  				
  				// Init buddy list
  				sfs.send(new InitBuddyListRequest());

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
        
        sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {

			@Override
			public void dispatch(BaseEvent evnt) throws SFSException {
				// TODO Auto-generated method stub
				System.out.println("getting response from the server"); 
				Map param=evnt.getArguments();
				System.out.println(evnt.getArguments());
				System.out.println("getting response "+param );
				SFSObject obj=(SFSObject) param.get("params");
				if(param.get("cmd").equals("bi"))
				{	
					System.out.println("BI response");
					ISFSObject biObj = (ISFSObject) param.get("params");
					System.out.println(biObj.getUtfString("errmsg"));
				}
			}
		});        
        
        
        // Listener for LOGIN error if any during "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				System.out.println("Login falied :(");
			}
		});

        sfs.addEventListener(SFSBuddyEvent.BUDDY_ONLINE_STATE_UPDATE, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
		         // As the state change event is dispatched to me too,
		         // I have to check if I am the one who changed his state
		         System.out.println("BUDDY_ONLINE_STATE_UPDATE");
		         ISFSObject paramsObj = (ISFSObject) evt.getArguments().get("params");
		         boolean isItMe = paramsObj.getBool("isItMe");
		         
		         if (isItMe)
		             System.out.println("I'm now"+(sfs.getBuddyManager().getMyOnlineState() ? "online" : "offline"));
		         else {
		        	 Buddy buddy = (Buddy) paramsObj.getSFSObject("buddy");
		        	 System.out.println("My buddy " + paramsObj.getSFSObject("buddy") + " is now"+ (buddy.isOnline() ? "online" : "offline"));
		         }
			}
		});
        
    	final class BuddyListUpdate implements IEventListener {
    		@Override
    		public void dispatch(BaseEvent evt) throws SFSException {
    			
    			System.out.println(evt.getType() + "-----------BUDDY UPDATES START-----------");
		        
    			System.out.println(evt.getArguments());
				
			     // Put myself offline in the Buddy List system
			     //sfs.send(new GoOnlineRequest(true));
    			for (Buddy buddy : sfs.getBuddyManager().getBuddyList()) {
    				System.out.println("STATE: " + buddy.getState());
    				System.out.println("BUDDY: " + buddy.getName());
    			}
    			System.out.println("-----------BUDDY UPDATES END-----------");
    		}
    	}
        
	     sfs.addEventListener(SFSBuddyEvent.BUDDY_ONLINE_STATE_UPDATE, new IEventListener() {
	         public void dispatch(BaseEvent evt) throws SFSException {
	             // As the state change event is dispatched to me too,
	             // I have to check if I am the one who changed his state
	             boolean isItMe = (Boolean) evt.getArguments().get("isItMe");
	             
	             if (isItMe)
	                 System.out.println("I'm now" + (sfs.getBuddyManager().getMyOnlineState() ? "online" : "offline"));
	             else
	                 System.out.println("My buddy " + ((Buddy)evt.getArguments().get("buddy")).getName() + " is now" + (((Buddy)evt.getArguments().get("buddy")).isOnline() ? "online" : "offline" ));
	         }
	     });
	     

    	
    	sfs.addEventListener(SFSBuddyEvent.BUDDY_LIST_INIT, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evtParams) throws SFSException {
				System.out.println("The following error occurred while executing a buddy-related request: " 
						+ evtParams.getArguments());
			}
        });
        sfs.addEventListener(SFSBuddyEvent.BUDDY_ONLINE_STATE_UPDATE, new BuddyListUpdate());
        //sfs.addEventListener(SFSBuddyEvent.BUDDY_VARIABLES_UPDATE, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_ADD, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_REMOVE, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_BLOCK, new BuddyListUpdate());        
        
        // Connect to SFS based on TCP set-up done at server
        sfs.connect("127.0.0.1", 9933);
        System.out.println("After connect");
        
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}