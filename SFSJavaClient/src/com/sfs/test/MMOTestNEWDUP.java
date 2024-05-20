package com.sfs.test;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSBuddyEvent;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.Buddy;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.buddylist.AddBuddyRequest;
import sfs2x.client.requests.buddylist.InitBuddyListRequest;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;


public class MMOTestNEWDUP {

	private static final class BuddyListUpdate implements IEventListener {
		@Override
		public void dispatch(BaseEvent evt) throws SFSException {
			System.out.println("-----------BUDDY UPDATES START-----------");
			System.out.println(evt.getArguments());
			
			for (Buddy buddy : sfs.getBuddyManager().getBuddyList()) {
				System.out.println("BUDDY: " + buddy.getName());
			}
			System.out.println("-----------BUDDY UPDATES END-----------");
		}
	}

	private static SmartFox sfs;
	    
	  public static void main(String args[]) {
		  sfs = new SmartFox();
    	  sfs.connect("127.0.0.1", 9933);

          sfs.addEventListener(SFSEvent.CONNECTION, new IEventListener()
          {
              public void dispatch(BaseEvent evt) throws SFSException {
                  
                  // Retrieve event parameters
                  Map<String, Object> params = evt.getArguments();
                  
                  if ((Boolean)params.get("success")) {
                      System.out.println("Connection established");
                      
                      ISFSObject usrParam = new SFSObject();
                      usrParam.putUtfString("sid", "fgdgsdgds");
                      usrParam.putUtfString("key", "12345");             
                      LoginRequest request = new LoginRequest("john", "", "Chapatiz", usrParam);
                      sfs.send(request);
                  }
                  else {
                      System.out.println("Connection failed");
                  }
              }
          });
          
          sfs.addEventListener(SFSEvent.LOGIN, new IEventListener() {
  			
  			@Override
  			public void dispatch(BaseEvent event) throws SFSException {
  				System.out.println("Login Success :)");				
  				
  				System.out.println("Init Buddy List");
  				// Init buddy list
  				sfs.send(new InitBuddyListRequest());
  				
  		        ISFSObject params = new SFSObject();
  		        params.putUtfString("rn", "jetset.turtle");
  		        params.putInt("inst",-1);
  		        
  		        sfs.send(new ExtensionRequest("user.rj", params));  
  			}
  		  });

          sfs.addEventListener(SFSEvent.ROOM_JOIN, new IEventListener() {
  			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("Room Joined: " + evt.getArguments());
			}
          });
          
          sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
  			
         	@Override
  			public void dispatch(BaseEvent evnt) throws SFSException {
  				System.out.println("getting response from the server"); 
  				Map param = evnt.getArguments();
  				
  				System.out.println(evnt.getArguments());
  				
  				System.out.println("getting response "+param );
  				
  				SFSObject obj = (SFSObject) param.get("params");
        				
  				if(param.get("cmd").equals("rl"))
  				{
  					List<String> roomNames=(List<String>) obj.get("roomList").getObject();
  					//System.out.println(roomNames);
  					
  				} else if(param.get("cmd").equals("rj")) {
  					System.out.println("Room Joined");
  					
  					
  					// Add Buddy list
  					sfs.send(new AddBuddyRequest("mike"));
  					
  					/*ISFSObject isfparam = new SFSObject();
					isfparam.putInt("x", 5);
					isfparam.putInt("y", 3);
					sfs.send(new ExtensionRequest("user.mv", isfparam));*/
  					
  				} else if(param.get("cmd").equals("mv")) {
  					
  					System.out.println("move response caught");
  					System.out.println("-------------------response for move request---for mike-------------");
					System.out.println("============"+obj.getInt("x"));
					System.out.println("============"+obj.getInt("y"));
					
  				}
  				
  			}
  		});

          sfs.addEventListener(SFSEvent.PROXIMITY_LIST_UPDATE, new IEventListener() 
          {
              public void dispatch(BaseEvent evt){
                  try{
                	  
                	  System.out.println("comming to the proximity list update in the  event listener");
                      List<User> added = (List<User>) evt.getArguments().get("addedUsers");
                      List<User> removed = (List<User>)evt. getArguments().get("removedUsers");
   
                      // Add users that entered the proximity list
                      for (User user : added)
                      {
                          System.out.println("PLAYER IN ROOM: " + user.isPlayerInRoom((Room) evt.getArguments().get("room")));       
                    	  System.out.println("**********************new User appeared in the area of interest ********************");
                    	  System.out.println(evt.getArguments());
                          /*Room room = (Room) evt.getArguments().get("room");
                          User user = (User) evt.getArguments().get("user");*/
                         
                          Map map=user.getProperties();
                          System.out.println(map);
                          System.out.println(user.getVariable("uv"));
                          UserVariable obj= user.getVariable("uv");
                          
                          ISFSObject sfsObj=obj.getSFSObjectValue();
                          System.out.println("mid of the user =="+sfsObj.getInt("mid"));
                          System.out.println("nick of the user=="+sfsObj.getUtfString("nick"));
                          System.out.println("pet of the user=="+sfsObj.getSFSArray("pet"));
                          System.out.println("user is vip =="+sfsObj.getBool("vip"));
                          System.out.println("user ck params=="+sfsObj.getUtfString("ck"));
                          System.out.println("user ad params=="+sfsObj.getUtfString("ad"));
                          System.out.println("user at params=="+sfsObj.getInt("at"));
                          System.out.println("followers of the user=="+sfsObj.getUtfStringArray("fl"));
                          System.out.println("position of the user in x coordinate=="+sfsObj.getInt("x"));
                          System.out.println("position of the user in y coordinate=="+sfsObj.getInt("y"));
                          System.out.println("------------------------------------------");
                    	 
                      }
   
                      // Remove users that left the proximity list
                      for (User user : removed)
                      {
                    	  System.out.println("PLAYER IN ROOM: " + user.isPlayerInRoom((Room) evt.getArguments().get("room")));
                    	  System.out.println("**********************User Left from the area of interest ********************");
                    	  System.out.println(evt.getArguments());
                          /*Room room = (Room) evt.getArguments().get("room");
                          User user = (User) evt.getArguments().get("user");*/
                         
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
                      
                      sfs.send(new ExtensionRequest("user.hv",new SFSObject()));
                  
	              } catch(Exception e){
	                	  e.printStackTrace();
	              } 
              }
          });

          
          sfs.addEventListener(SFSBuddyEvent.BUDDY_LIST_INIT, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent arg0) throws SFSException {
				System.out.println("INIT BUDDY LIST");
				
				// Retrieve my buddies list
				System.out.println(sfs.getBuddyManager().getBuddyList().size());
				
			}
          });
          
          sfs.addEventListener(SFSBuddyEvent.BUDDY_ERROR, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evtParams) throws SFSException {
				System.out.println("The following error occurred while executing a buddy-related request: " 
						+ evtParams.getArguments());
			}
          });
          
          sfs.addEventListener(SFSBuddyEvent.BUDDY_ONLINE_STATE_UPDATE, new BuddyListUpdate());
          sfs.addEventListener(SFSBuddyEvent.BUDDY_VARIABLES_UPDATE, new BuddyListUpdate());
          sfs.addEventListener(SFSBuddyEvent.BUDDY_ADD, new BuddyListUpdate());
          sfs.addEventListener(SFSBuddyEvent.BUDDY_REMOVE, new BuddyListUpdate());
          sfs.addEventListener(SFSBuddyEvent.BUDDY_BLOCK, new BuddyListUpdate());
          //sfs.addEventListener(SFSBuddyEvent.BUDDY_MESSAGE, onBuddyMessage);
          
          sfs.disconnect();     
	    }
}


