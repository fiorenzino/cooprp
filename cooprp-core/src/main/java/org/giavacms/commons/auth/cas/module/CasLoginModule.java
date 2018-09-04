package org.giavacms.commons.auth.cas.module;

import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

public class CasLoginModule extends AbstractServerLoginModule {

    String targetHost = "https://cas.coopservice.it";
    String targetPathValidate = "/cas/serviceValidate";

    private Principal localIdentity;

    Logger logger = Logger.getLogger(getClass());

    @SuppressWarnings("unchecked")
    @Override
    public boolean login() throws LoginException {
        if (sharedState != null && sharedState.get("javax.security.auth.login.name") != null) {
            Object pri = sharedState.get("javax.security.auth.login.name");
            logger.info("login: enter: " + pri.toString());
            if (pri instanceof Principal) {
                localIdentity = (Principal) pri;
                logger.info("principal: " + localIdentity.getName());
                if (localIdentity != null)
                    return true;
                else {
                    logger.info("no principal!! ");
                }
            }
        } else {
            logger.debug("sharedState: " + sharedState);
        }

        try {
            logger.debug("---------------LOGIN--------------");
            NameCallback namecallback = new NameCallback("Enter username");
            PasswordCallback passwordcallback = new PasswordCallback(
                    "Enter password", false);
            CallbackHandler handler = this.callbackHandler;
            String username = "";
            String password = "";
            handler.handle(new Callback[]{namecallback, passwordcallback});
            username = namecallback.getName();
            if (username != null && !username.isEmpty()) {
                logger.info("login:" + username);
                Principal p = createIdentity(username);
                if (p != null) {
                    sharedState.put("javax.security.auth.login.name", p);
                    sharedState.put("javax.security.auth.login.password", password);
                    localIdentity = p;
                    return true;
                }
            }
//            logger.warn("not returning true");
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        logger.info("getRoleSets: enter");
        Group[] groups = {};
        return groups;
    }

    @Override
    public boolean logout() throws LoginException {
//        logger.info("---------------LOGOUT--------------");
        if (this.localIdentity != null) {
            this.localIdentity = null;
            return true;
        } else {
            return super.logout();
        }
    }

    @Override
    protected Principal getIdentity() {
//        logger.info("---------------GET IDENTITY--------------");
        return localIdentity != null ? localIdentity : null;
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
                           Map<String, ?> options) {
//        logger.info("initialize");
        if (options.get("casServerUrlPrefix") != null) {
            this.targetHost = (String) options.get("casServerUrlPrefix");
        }
        if (options.get("/cas/serviceValidate") != null) {
            this.targetPathValidate = (String) options.get("/cas/serviceValidate");
        }
        super.initialize(subject, callbackHandler, sharedState, options);
    }
}
