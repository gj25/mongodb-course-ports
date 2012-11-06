package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import play.Logger;

public class Users {

    public static String makeSalt() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String makePwHash(String pw, String salt) {
        if (salt == null) {
            salt = makeSalt();
        }

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update((pw + salt).getBytes());
            byte[] digest = sha.digest();
            
            //convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
    	    for (int i=0;i<digest.length;i++) {
    	       hexString.append(Integer.toHexString(0xFF & digest[i]));
    	    }
            return hexString + "," + salt;
        }
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Invalid MessageDigest algotithm", ex);
        }
    }
    
    private static final String USER_RE = "^[a-zA-Z0-9_-]{3,20}$";
    private static final String PASS_RE = "^.{3,20}$";
    private static final String EMAIL_RE = "^[\\S]+@[\\S]+\\.[\\S]+$";

    public boolean validateSignup(String username, String password, String verify, String email, HashMap<String, String> errors) {
        errors.put("username_error", "");
        errors.put("password_error", "");
        errors.put("verify_error", "");
        errors.put("email_error", "");

        if (username == null || !username.matches(USER_RE)) {
            errors.put("username_error", "invalid username. try just letters and numbers");
            return false;
        }

        if (password == null || !password.matches(PASS_RE)) {
            errors.put("password_error", "invalid password.");
            return false;
        }

        if (!password.equals(verify)) {
            errors.put("verify_error", "password must match");
            return false;
        }

        if (email != null && !email.equals("") && !email.matches(EMAIL_RE)) {
            errors.put("email_error", "invalid email address");
            return false;
        }

        return true;
    }

    // userRecord from user.py is not used by the caller. Not sure about the purpose. Will add it when needed.
    public boolean validateLogin(String username, String password) {
        // *** Homework
        DBCollection users = MongoDB.getCollection("users");
        DBObject query = new BasicDBObject("_id", username);
        DBObject user = users.findOne(query);
        // ***

        if (user == null) {
            Logger.info("User not in database");
            return false;
        }

        String pwdField = (String) user.get("password");      
        String salt = pwdField.split(",")[1];
        String newPsdHash = makePwHash(password, salt);
        if (!pwdField.equals(newPsdHash)) {
            Logger.info("user password is not a match");
            return false;
        }

        return true;
    }

    public String startSession(String username) {
        DBObject session = new BasicDBObject("username", username);
        MongoDB.getCollection("sessions").insert(session, WriteConcern.SAFE);

        Logger.info("Created new session for user: " + username);
        return session.get("_id").toString();
    }

    public void endSession(String sessionId) {
        DBObject idObj = new BasicDBObject("_id", new ObjectId(sessionId));
        MongoDB.getCollection("sessions").remove(idObj, WriteConcern.SAFE);

        Logger.info("ended session : " + sessionId);
    }

    // returns username associated with the session
    public String getSession(String sessionId) {
        DBObject idObj = new BasicDBObject("_id", new ObjectId(sessionId));
        DBObject session = MongoDB.getCollection("sessions").findOne(idObj);

        return (session != null) ? (String) session.get("username") : null;
    }
    
    public boolean newUser(String username, String password, String email)
    {
        /// *** Homework
        DBObject user = new BasicDBObject();
        user.put("_id", username);
        user.put("password", makePwHash(password, null));
        if (email != null && !email.equals(""))
        {
            user.put("email", email);
        }
        
        try {
            MongoDB.getCollection("users").insert(user, WriteConcern.SAFE);
        } 
        // retunrn bool in case of failure
        catch(MongoException e)
        {
            Logger.error("Failed to create new user: " + username, e);
            return false;
        }
        // ***
        Logger.info("Created new user: " + username);
        
        return true;
    }
    
    private static final String SECRET = "thisisnotsecret";
    private static String hashStr(String stringToHash) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(SECRET.getBytes(), "HmacSHA1");
            mac.init(secret);

            byte[] rawHmac = mac.doFinal(stringToHash.getBytes());
            return new String(Base64.encodeBase64(rawHmac));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RuntimeException("Problems calculating HMAC", ex);
        }
    }
    
    public String makeSecureVal(String s)
    {
        return s + "|" + hashStr(s);
    }
    
    public String checkSecureVal(String h)
    {
        String val = h.split("\\|")[0];
        
        if (h.equals(makeSecureVal(val)))
        {
            return val;
        }
        return null;
    }
}
