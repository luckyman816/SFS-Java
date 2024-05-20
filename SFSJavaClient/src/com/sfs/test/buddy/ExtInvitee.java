package com.sfs.test.buddy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import sfs2x.client.requests.buddylist.AddBuddyRequest;
import sfs2x.client.requests.buddylist.GoOnlineRequest;
import sfs2x.client.requests.buddylist.RemoveBuddyRequest;
import sfs2x.client.requests.buddylist.InitBuddyListRequest;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class ExtInvitee
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
					//List<Object> invitedUsrList = new ArrayList<Object>();
					//invitedUsrList.add(sfs.getUserManager().getUserByName("Madhusmita-Test-11"));
					
					// Set the custom invitation details
			         ISFSObject invtParam= new SFSObject();
/*			         invtParam.putUtfString("msg", "Would you like to be my friend?");
			         invtParam.putBool("buddyreq", true);*/
			         invtParam.putUtfString("invitee", "Madhusmita-Test-61");
			         sfs.send(new ExtensionRequest("user.bi", invtParam));
			         
			         //sfs.send(new InviteUsersRequest(invitedUsrList, 10, invtParam));
				}
				else if(param.get("cmd").equals("bi"))
				{	
					System.out.println("BI response");
					ISFSObject biObj = (ISFSObject) param.get("params");
					System.out.println("ERROR MSG: " + biObj.getUtfString("errmsg"));
					
			         ISFSObject invtParam= new SFSObject();
			         invtParam.putUtfString("invitee", "Madhusmita-Test-61");
			         sfs.send(new ExtensionRequest("user.br", invtParam));
			         
			         //sfs.send(new RemoveBuddyRequest("Madhusmita-Test-51"));
				}
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
      
                    LoginRequest request = new LoginRequest("Madhusmita-Test-62", "", "Chapatiz", usrParam);
    				//LoginRequest LoginRequest = new LoginRequest("sunil","","chapatiz");
    				
    				System.out.println("Logging in");
    				sfs.send(request);

    				System.out.println("");
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
        
        
        // Listener for LOGIN error if any during "LoginRequest"
        sfs.addEventListener(SFSEvent.LOGIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				System.out.println("Login falied :(");
			}
		});

        final class BuddyListUpdate implements IEventListener {
    		@Override
    		public void dispatch(BaseEvent evt) throws SFSException {
    			
    			System.out.println(evt.getType() + "-----------BUDDY UPDATES START-----------");
		        
    			System.out.println(evt.getArguments());
    			
    			for (Buddy buddy : sfs.getBuddyManager().getBuddyList()) {
    				System.out.println("STATE: " + buddy.getState());
    				System.out.println(buddy.isOnline());
    				System.out.println("BUDDY: " + buddy.getName());
    			}
    			System.out.println("-----------BUDDY UPDATES END-----------");
    		}
    	}    

        sfs.addEventListener(SFSBuddyEvent.BUDDY_ONLINE_STATE_UPDATE, new IEventListener() {
            public void dispatch(BaseEvent evt) throws SFSException {
            	System.out.println(evt.getArguments());
                // As the state change event is dispatched to me too,
                // I have to check if I am the one who changed his state
                boolean isItMe = (Boolean) evt.getArguments().get("isItMe");
                
                if (isItMe)
                    System.out.println("I'm now" + (sfs.getBuddyManager().getMyOnlineState() ? "online" : "offline"));
                else
                    System.out.println("My buddy " + ((Buddy)evt.getArguments().get("buddy")).getName() + " is now" + (((Buddy)evt.getArguments().get("buddy")).isOnline() ? "online" : "offline" ));
            }
        });
    	     
	     // Put myself offline in the Buddy List system
	    // sfs.send(new GoOnlineRequest(true));
  
    	sfs.addEventListener(SFSBuddyEvent.BUDDY_LIST_INIT, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_ONLINE_STATE_UPDATE, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_VARIABLES_UPDATE, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_ADD, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_REMOVE, new BuddyListUpdate());
        sfs.addEventListener(SFSBuddyEvent.BUDDY_BLOCK, new BuddyListUpdate());  
        sfs.addEventListener(SFSBuddyEvent.BUDDY_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evtParams) throws SFSException {
				System.out.println("The following error occurred while executing a buddy-related request: " 
						+ evtParams.getArguments());
			}
        });
        
        // Connect to SFS based on TCP set-up done at server
        sfs.connect("127.0.0.1", 9933);
        System.out.println("After connect");
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}