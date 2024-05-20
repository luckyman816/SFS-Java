package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSConstants;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

public class LoginHandler extends BaseServerEventHandler{

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		trace("Login Handler entry");
        String username = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
        String password = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);

        ISession session = (ISession)event.getParameter(SFSEventParam.SESSION);
        
        // Handle output data from LOGIN
        ISFSObject outData = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
        
        //make sure there is a password before you try to use the checkSecurePassword function
        if (password.equals(""))
        {
            SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
            data.addParameter(username);
            throw new SFSLoginException("You must enter a password.", data);
        }        
        Connection conn = null;
        PreparedStatement sql = null;
        ResultSet result = null;
        try {
        	
            //get a connection to the database
            conn = getParentExtension().getParentZone().getDBManager().getConnection();

            //This will strip potential SQL injections
            sql = conn.prepareStatement("SELECT user_id, pwd FROM users WHERE name = ?");
            sql.setString(1, username);

            trace("LOGIN EXTENSION SQL: " + sql);
            
            // Obtain ResultSet
            result = sql.executeQuery();

            if(! result.first())
            {
                SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
                errData.addParameter(username);

                throw new SFSLoginException("Bad user name: "+ username, errData);
            }

            int dbId = result.getInt("user_id");
            
            // Provide a new name for the user:
            String newName = "User-" + dbId + "-" + username;
            outData.putUtfString(SFSConstants.NEW_LOGIN_NAME, newName);
            
            // Append some additional return values
            outData.putUtfString("test1", "test");
            outData.putInt("DatabaseID", dbId);
            session.setProperty("DatabaseID", dbId);

           //SFS always encrypts passwords before sending them so you need to decrypt the password
           //received from the database and compare that to what they entered in flash
           if (! getApi().checkSecurePassword(session, result.getString("pwd"), password))
           {
                SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);

                data.addParameter(username);

                throw new SFSLoginException("Login failed for user: "  + username, data);
           }

            //make sure you close the database connection when you're done with it, especially if you've
            //set a low number of maximum connections
            conn.close();

            //at this point you could trigger an joinRoom request if you wanted to, otherwise
            //this will return success to your LOGIN event listener
            trace("Login successful, joining room!");
        } catch (SQLException e) {
            trace(ExtensionLogLevel.WARN, " SQL Failed: " + e.toString());
        } finally {
        	try {
        		if (result!=null)
				result.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
        		if (sql!=null)
				sql.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
        		if (conn!=null)
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        	
		}
	}

}
