package com.sfs.test.mmo;

import java.util.List;
import java.util.Map;

import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.MMORoom;
import sfs2x.client.entities.Room;
import sfs2x.client.entities.User;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.LoginRequest;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class MMOTestAllen {
	
	  private static SmartFox sfs;
	  public static MMORoom room;
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
                      usrParam.putUtfString("sid", "TEST");
                      usrParam.putUtfString("key", "12345");
                      
                      LoginRequest request = new LoginRequest("Allen", "", "Chapatiz", usrParam);
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
  				
  				//System.out.println("Init buddy list");
  				// Init buddy list
  				//sfs.send(new InitBuddyListRequest());
  				
  		        ISFSObject params = new SFSObject();
  		        params.putUtfString("rn", "jetset.turtle");
  		        params.putInt("inst",-1);
  		        
  		        System.out.println("Room Join");
  		        sfs.send(new ExtensionRequest("user.rj", params));		
  		        
  		        try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  		        
  		        ISFSObject params1 = new SFSObject();
  		        params1.putUtfString("rn", "jetset.vip");
  		        params1.putInt("inst",-1);
  		        
  		        System.out.println("Room Join");
  		        sfs.send(new ExtensionRequest("user.rj", params1));	        
  			}
  		  });
          
          sfs.addEventListener(SFSEvent.ROOM_JOIN, new IEventListener() {
			
			@Override
			public void dispatch(BaseEvent evt) throws SFSException {
				System.out.println("Room Joined: " + evt.getArguments());
				room = (MMORoom) (Room) evt.getArguments().get("room");
				User me = sfs.getMySelf();
				
				//sfs.send(new PublicMessageRequest("Hello", new SFSObject(), (Room) evt.getArguments().get("room")));
			}
          });        
          
          sfs.addEventListener(SFSEvent.PROXIMITY_LIST_UPDATE, new IEventListener() 
          {
              public void dispatch(BaseEvent evt){
                  try{
                	  
                	  System.out.println("comming to the proximity list update in the  event listener" + evt.getArguments());
                      List<User> added = (List<User>) evt.getArguments().get("addedUsers");
                      List<User> removed = (List<User>)evt. getArguments().get("removedUsers");
   
                      // Add users that entered the proximity list
                      for (User user : added)
                      {      
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
                          System.out.println("position of the user in x coordinate=="+sfsObj.getInt("x"));
                          System.out.println("position of the user in y coordinate=="+sfsObj.getInt("y"));
                          System.out.println("------------------------------------------");
                    	 
                      }
   
                      // Remove users that left the proximity list
                      for (User user : removed)
                      {
                    	  System.out.println("**********************User Left from the area of interest ********************");
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
                          System.out.println("position of the user in x coordinate=="+sfsObj.getInt("x"));
                          System.out.println("position of the user in y coordinate=="+sfsObj.getInt("y"));
                          System.out.println("------------------------------------------");
                              
                      }
                      
                      //sfs.send(new ExtensionRequest("user.hv",new SFSObject()));
                  
	              } catch(Exception e){
	                	  e.printStackTrace();
	              } 
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
  					
  				} else if(param.get("cmd").equals("ad")) {
  					System.out.println("AVATAR DESIGN CHANGE UPDATE");
  					ISFSObject paramObj = (ISFSObject) param.get("params");
  					System.out.println(paramObj.getDump());
  					
  				} else if(param.get("cmd").equals("rj")) {
  					System.out.println("Room Joined");
  					
  					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

  				} 
  			}
  		});
                    
	  }
}


