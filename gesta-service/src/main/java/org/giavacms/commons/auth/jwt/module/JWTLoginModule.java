package org.giavacms.commons.auth.jwt.module;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.jboss.logging.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.callback.ObjectCallback;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.security.acl.Group;
import java.util.List;
import java.util.Map;

public class JWTLoginModule extends AbstractServerLoginModule {
  private final static Logger logger = Logger.getLogger(JWTLoginModule.class.getName());
  private static final String SUPER_SECRET = "super-secret";
  private Principal identity;

  private String superSecret;

  List<String> roles;

  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
                         Map<String, ?> options) {
    super.initialize(subject, callbackHandler, sharedState, options);

    if (options.containsKey(SUPER_SECRET)) {
      superSecret = (String) options.get(SUPER_SECRET);
    }
  }

  @Override
  public boolean login() throws LoginException {
    String userFirstpassUsername = null;
    if (super.login() == true) {
      log.debug("super.login()==true");
      //         return true;

      userFirstpassUsername = "QUALCOSA";
    }

    // SONO IL PRIMO DELLA CATENA
    NameCallback ncb = new NameCallback("Username:");
    ObjectCallback ocb = new ObjectCallback("Password:");
    try {
      callbackHandler.handle(new Callback[]{ncb, ocb});
    } catch (Throwable e) {
      if (e instanceof RuntimeException) {
        throw (RuntimeException) e;
      }
      return false; // If the CallbackHandler can not handle the required callbacks then no chance.
    }
    String token = ncb.getName();
    Object pwd = ocb.getCredential();

    if (superSecret == null || superSecret.isEmpty()) {
      superSecret = new String((char[]) pwd);
    }

    Map<String, Object> decoded = null;
    try {
      decoded = verify(token, superSecret);
    } catch (Throwable e) {
      logger.error("Auth Error:" + e.getMessage());
//      return false;
      throw new RuntimeException("USCIRE!");
    }
    String username = (String) decoded.get("username");

    roles = (List<String>) decoded.get("roles");
    if (username != null && !username.isEmpty()) {
      identity = new SimplePrincipal(username);
      if (userFirstpassUsername != null) {
        // deve essere uguale a username ALTRIMENTI IL TOKEN NON E' DELLA STESSA IDENTITY
        if (userFirstpassUsername != username) {
          return false;
        }
      }

      if (getUseFirstPass()) {
        String userName = identity.getName();
        if (log.isDebugEnabled())
          log.debug("Storing username '" + userName + "' and empty password");
        // Add the username and an empty password to the shared state map
        sharedState.put("javax.security.auth.login.name", identity);
        sharedState.put("javax.security.auth.login.password", pwd);
      }
      loginOk = true;
      return true;
    }

    return false; // Attempted login but not successful.
  }

  private Map<String, Object> verify(String token, String superSecret) throws Exception {
    Map<String, Object> decoded;
    JWTVerifier verifier = new JWTVerifier(superSecret);
    if (token == null || token.trim().isEmpty()) {
      logger.error("token empty");
      throw new Exception("token empty");
    }
    try {
      decoded = verifier.verify(token);
      String username = (String) decoded.get("username");
      roles = (List<String>) decoded.get("roles");
      if (roles == null || roles.isEmpty()) {
        logger.error("no roles valid for application");
        throw new Exception("no roles valid for application");
      }
    } catch (SignatureException se) {
      logger.error("signature exception token");
      throw new Exception("signature exception token");
    } catch (NoSuchAlgorithmException ne) {
      logger.error("NoSuch Algorithm Exception token");
      throw new Exception("signature exception token");
    } catch (JWTVerifyException jwt) {
      logger.error("JWT Verify Exception token");
      throw new Exception("signature exception token");
    } catch (InvalidKeyException ie) {
      logger.error("Invalid Key Exception token");
      throw new Exception("signature exception token");
    } catch (IOException e) {
      logger.error("IOException token");
      throw new Exception("signature exception token");
    } catch (Throwable e) {
      logger.error("Throwable token");
      throw new Exception("signature exception token");
    }
    return decoded;
  }

  @Override
  protected Principal getIdentity() {
    return identity;
  }

  @Override
  protected Group[] getRoleSets() throws LoginException {
    Group rolesGroup = new SimpleGroup("Roles");
    Group callerPrincipal = new SimpleGroup("CallerPrincipal");
    callerPrincipal.addMember(getIdentity());
    Group[] groupsArray = new Group[2];
    groupsArray[0] = callerPrincipal;
    groupsArray[1] = rolesGroup;
    for (String role : roles) {
      rolesGroup.addMember(new SimplePrincipal(role));
    }
    return groupsArray;
  }

}