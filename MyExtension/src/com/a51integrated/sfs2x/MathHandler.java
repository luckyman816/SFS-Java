package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class MathHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		int n1 = params.getInt("n1");
		int n2 = params.getInt("n2");
		
		SFSObject rtn = new SFSObject();
		rtn.putInt("sum", n1 + n2);
		
		MyExtension parents = (MyExtension) getParentExtension();
		
		parents.send("math", rtn, player);
	}

}
