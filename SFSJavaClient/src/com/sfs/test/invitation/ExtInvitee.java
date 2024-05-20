package com.sfs.test.invitation;

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
import sfs2x.client.requests.buddylist.AddBuddyRequest;
import sfs2x.client.requests.game.InvitationReplyRequest;
import sfs2x.client.requests.game.InviteUsersRequest;

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
					List<Object> invitedUsrList = new ArrayList<Object>();
					invitedUsrList.add(sfs.getUserManager().getUserByName("Madhusmita-Test-1"));
					
					// Set the custom invitation details
			         ISFSObject invtParam= new SFSObject();
			         //invtParam.putUtfString("msg", "Would you like to be my friend?");
			         //invtParam.putBool("buddyreq", true);
			         invtParam.putUtfString("invitee", "Madhusmita-Test-1");
			         sfs.send(new ExtensionRequest("user.bi", invtParam));
			         
			         //sfs.send(new InviteUsersRequest(invitedUsrList, 10, invtParam));
				}	
			}
		});
 /*         
          sfs.addEventListener(SFSEvent.INVITATION, new IEventListener() {
              public void dispatch(BaseEvent evt) throws SFSException {
                  // Let's accept this invitation
            	  System.out.println("Invitation request received");

                  //sfs.send(new InvitationReplyRequest((Invitation) evt.getArguments().get("invitation"), InvitationReply.ACCEPT));
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
				System.out.println(paramBaseEvent.getArguments().get("data"));
				ISFSObject invtReplyData = (ISFSObject) paramBaseEvent.getArguments().get("data");
				
				if (invtReplyData.getBool("buddyreq")) {
					if (Integer.parseInt(paramBaseEvent.getArguments().get("reply").toString()) == 0) {
						System.out.println("Buddy request has been accepted");
						
	  					// Add Buddy list
	  					sfs.send(new AddBuddyRequest("mike"));
						
					} else {
						System.out.println("Buddy request has been denied");
					}
				}
			}
          });*/
          
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

               //	 sfs.send(new JoinRoomRequest("animation", ""));           
                 LoginRequest request = new LoginRequest("Madhusmita-Test-2", "", "Chapatiz", usrParam);
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
        
        
        // Listener for handle after successfully join the room
        sfs.addEventListener(SFSEvent.ROOM_JOIN, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("room join triggered------------------ in the ROOM JOIN EVENT HANDLER");
				System.out.println(evt.getArguments().size());
				System.out.println("Joined Room: " + evt.getArguments().get("room"));
				
				System.out.println("event type=   "+evt.getType());
				System.out.println("event targete=   "+evt.getTarget());
				
				Map<String, Object> mRoomList = evt.getArguments();
				System.out.println(evt);
				Room oRoomDetail = (Room) mRoomList.get("room");
			    
				//ISFSObject obj=evt.
				
				Iterator itr=mRoomList.keySet().iterator();
				while(itr.hasNext())
				{
					System.out.println(itr.next());
				}
				System.out.println("Room Details-> Id: " + oRoomDetail.getId()
						+ " , GroupId: " + oRoomDetail.getGroupId() 
						+ " , Capacity: " + oRoomDetail.getCapacity()
						+"mapObject: " + oRoomDetail.getProperties()+ "size="+oRoomDetail.getProperties().size()
						
						);
				System.out.println("After new room join successfully: " + sfs.getRoomList());
				Room testRoom = sfs.getRoomByName("TestRoom");
				//
			
			}
		});
        
        
        
        // Listener for handle after error while join the room
        sfs.addEventListener(SFSEvent.ROOM_JOIN_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("Room joined failed-------------------------");
				System.out.println(evt.getArguments());
				System.out.println("Couldn't able to join the room :( \n ERROR: " + evt.getArguments().get("errorMessage"));
				System.out.println("After new room join unsuccessfully: " + sfs.getRoomList());
			}
		});
        
        
        sfs.addEventListener(SFSEvent.ROOM_ADD, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent paramBaseEvent) throws SFSException {
				System.out.println("New room has been created: " + paramBaseEvent.getArguments());
				sfs.send(new JoinRoomRequest("Room2Name", "mindfire"));
			}
		});
        
        sfs.addEventListener(SFSEvent.ROOM_CREATION_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent paramBaseEvent) throws SFSException {
				System.out.println("Error while creating dynamic room :(" + paramBaseEvent.getArguments());
			}
		});
        
        sfs.addEventListener(SFSEvent.USER_ENTER_ROOM, new IEventListener() {
            public void dispatch(BaseEvent evt) throws SFSException {
          	  
          	  System.out.println("user enter  triggered from server side******************************************");
          	  System.out.println(evt.getArguments());
                Room room = (Room) evt.getArguments().get("room");
                User user = (User) evt.getArguments().get("user");
               
                Map map=user.getProperties();
                System.out.println(map);
                System.out.println(user.getVariable("uv"));
                UserVariable obj= user.getVariable("uv");
                
                ISFSObject sfsObj=obj.getSFSObjectValue();
                System.out.println(sfsObj.getInt("mid"));
                System.out.println(sfsObj.getUtfString("nick"));
                System.out.println(sfsObj.getSFSArray("pet"));
                System.out.println(sfsObj.getBool("vip"));
                System.out.println(sfsObj.getUtfString("ck"));
                System.out.println(sfsObj.getUtfString("ad"));
                System.out.println(sfsObj.getInt("at"));
                System.out.println(sfsObj.getUtfStringArray("fl"));
                System.out.println(sfsObj.getInt("x"));
                System.out.println(sfsObj.getInt("y"));
                System.out.println("------------------------------------------");
        
            }
        });
        // Connect to SFS based on TCP set-up done at server
        sfs.connect("127.0.0.1", 9933);
        System.out.println("After connect");
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}