package com.sfs.test;

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
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.PublicMessageRequest;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class MyTestAdvMP
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
    	
    	sfs = new SmartFox(true);
        
    	
          sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener() {
			
        	 
        	  
			@Override
			public void dispatch(BaseEvent evnt) throws SFSException {
				// TODO Auto-generated method stub
				System.out.println("getting response from the server"); 
				Map param=evnt.getArguments();
				System.out.println(evnt.getArguments());
				System.out.println("getting response "+param );
				SFSObject obj=(SFSObject) param.get("params");
      				
				
				
				if(param.get("cmd").equals("rl"))
				{
					List<String> roomNames=(List<String>) obj.get("roomList").getObject();
					System.out.println(roomNames);	
				}
				else if(param.get("cmd").equals("uuv"))
				{
					System.out.println("----------------UUV------------------------------------");
					System.out.println(obj.getDump());
			        // CUV
			        sfs.send(new ExtensionRequest("user.cuv", null));	
					System.out.println("----------------------------------------------------");
				}	
				else if(param.get("cmd").equals("cuv"))
				{
					System.out.println("----------------CUV------------------------------------");
					System.out.println(obj.getDump());
					System.out.println("----------------------------------------------------");
				}					
				else if(param.get("cmd").equals("mv"))
				{
					System.out.println("-------------------response for move request----------------");
					System.out.println("============"+obj.getInt("x"));
					System.out.println("============"+obj.getInt("y"));
					System.out.println("Z: " + obj.getInt("z"));
				}
				else if(param.get("cmd").equals("rj"))
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
					
					try {
					System.out.println(obj.getUtfString("name"));
					//System.out.println(obj.getUtfString("map"));
					System.out.println(obj.getInt("X"));
					System.out.println(obj.getInt("Y"));
					System.out.println(obj.getUtfString("Type"));
					System.out.println("in JoinRoom------------------------------------------");
					byte mapData[]=obj.getByteArray("map");
					
					FileOutputStream fos=new FileOutputStream("map.zip");
					fos.write(mapData);
					
			        // UUV
			        ISFSObject uuvParam = new SFSObject();
			        uuvParam.putUtfString("key", "speed");
			        uuvParam.putBool("value", false);
			        sfs.send(new ExtensionRequest("user.uuv", uuvParam));					
					
					ISFSObject params = new SFSObject();
					params.putInt("x", 12);
					params.putInt("y", 15);
					params.putInt("z", -12);
					sfs.send(new ExtensionRequest("user.mv", params));
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ISFSObject params1 = new SFSObject();
					params1.putInt("x", 12);
					params1.putInt("y", 16);
					params1.putInt("z", 0);
					sfs.send(new ExtensionRequest("user.mv", params1));					
					/*
					    ObjectOutputStream  oos=new ObjectOutputStream(new FileOutputStream(file));
						oos.writeObject(obj.getClass("mapObject"));*/
					
					
					/*
					 * Testing the user public message system
					 * 
					 */
					
					ISFSObject talkParams =new SFSObject();
					talkParams.putUtfString("msg","hi all");
					talkParams.putBool("me", true);
					//talkParams.putInt("mid", 11);
						
					sfs.send(new ExtensionRequest("user.tk",talkParams));
					ISFSObject publicMessageParam=new SFSObject();
					publicMessageParam.putBool("me", false);
					//sfs.send(new PublicMessageRequest("Indian Post replaced by SmartFox for message transportation", params));
					
					sfs.send(new ExtensionRequest("user.hv", null));
						
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(param.get("cmd").equals("ad") || param.get("cmd").equals("pc"))
				{
					Map param1=evnt.getArguments();
					System.out.println(evnt.getArguments());
					ISFSObject obj1=(SFSObject) param1.get("params");
					System.out.println(obj1.getDump());
				}				
				else if(param.get("cmd").equals("tk"))
				{
					System.out.println("user talk response retrieved");
				    System.out.println(obj.getUtfString("msg")+" from the server by the user"+obj.getUtfString("user"));
				    System.out.println("the mid of the user is======="+obj.getInt("mid"));
				}
				
				
			}
		});
          
          sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE,new IEventListener() {
  			
  			@Override
  			public void dispatch(BaseEvent event) throws SFSException {
  			 System.out.println("=======================receiving the public message============================");
  				System.out.println(event.getArguments());
  			}
  		});
          
          sfs.addEventListener(SFSEvent.USER_EXIT_ROOM, new IEventListener() {
              public void dispatch(BaseEvent evt) throws SFSException {
            	  System.out.println("user exited*************** from the current room");
            	  System.out.println(evt.getArguments());
                  Room room = (Room) evt.getArguments().get("room");
                  User user = (User) evt.getArguments().get("user");
                  System.out.println(user.getVariable("mid"));
                  System.out.println(user.getVariable("nick"));
                  System.out.println(user.getVariable("vip"));
                  System.out.println("User " + user.getName() + " just joined Room " + room.getName());
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
                    usrParam.putUtfString("sid", "123451");
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
/*		        ISFSObject params1 = new SFSObject();
		        params1.putUtfString("n1", "billyard");
		        params1.putUtfString("n2", "TrickShot");*/
		       // sfs.send(new ExtensionRequest("JoinRoom", params1));
		        // sfs.send(new ExtensionRequest("user.getRoomList", params1));
		        //sfs.send(new JoinRoomRequest("animation"));  
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
                user.getVariable("uv").getSFSObjectValue().putUtfString("ad", "MODIFIED-AD");
                
            }
        });
        // Connect to SFS based on TCP set-up done at server
        sfs.connect("127.0.0.1", 9933);
        System.out.println("After connect");
        sfs.disconnect();
        System.out.println("After disconnect");
    }
}