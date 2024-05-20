package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExtension extends SFSExtension {

	@Override
	public void init() {		
		try {
			trace("Start creating dynamic room");
			// Create new dynamic room
	        CreateRoomSettings roomSetting = new CreateRoomSettings();
	        roomSetting.setGroupId("Group 2");
	        roomSetting.setName("Room2Name");
	        roomSetting.setPassword("mindfire");
	        getParentZone().createRoom(roomSetting);
	        trace("End creating dynamic room");
			//getApi().createRoom(getParentZone(), roomSetting, null);
		} catch (SFSCreateRoomException e) {
			e.printStackTrace();
		}
		
		trace("Starting event handler starting.");
		
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginHandler.class);
		
		trace("Math handler extension starting.");
		this.addRequestHandler("math", MathHandler.class);
	}

	@Override
	public void destroy() {
		trace("Login extension stopped.");
		super.destroy();
	}
	
}
