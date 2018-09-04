package it.coopservice.cooprp.service;

import com.novell.ldap.LDAPConnection;
import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.pojo.LdapParameters;
import it.coopservice.cooprp.util.LdapOperations;
import org.giavacms.commons.model.pojo.Utente;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.io.Serializable;
import java.util.List;

@Stateless
@Interceptors({ TracerInterceptor.class })
public class LdapService implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger logger = Logger.getLogger(getClass());

    public Utente login(String username, String password) {
        LDAPConnection loginConnection = null;
        try {
            Utente utente = find(username);
            if (utente != null) {
                String keystore = null;
                LdapParameters loginParameters = new LdapParameters(AppProperties.ldapHosts.value(),
                         AppProperties.ldapPort.cast(Integer.class),
                         //utente.getDn(),
                         "",
                         password,
                         AppProperties.ldapWithAuth.cast(Boolean.class),
                         AppProperties.ldapWithSsl.cast(Boolean.class),
                         keystore);
                loginConnection = LdapOperations.connect(loginParameters);
                if (loginConnection != null && loginConnection.isBound() && loginConnection.isConnected()) {
                    return utente;
                }
            }
            return null;
        } finally {
            if (loginConnection != null) {
                LdapOperations.disconnect(loginConnection);
            }

        }
    }

    public Utente find(String username) {
        LDAPConnection ldapConnection = null;
        try {
            String keystore = null;
            LdapParameters ldapParameters = new LdapParameters(AppProperties.ldapHosts.value(),
                     AppProperties.ldapPort.cast(Integer.class),
                     AppProperties.ldapBindDN.value(),
                     AppProperties.ldapBindCredential.value(),
                     AppProperties.ldapWithAuth.cast(Boolean.class),
                     AppProperties.ldapWithSsl.cast(Boolean.class),
                     keystore);
            ldapConnection = LdapOperations.connect(ldapParameters);
            Utente utente = new Utente();
            utente.setUsername(username);
            List<Utente> utenti = LdapOperations.users(ldapConnection, "csIDVuid=" + username, new String[]{"fullName",
                     "csIDVuid",
                     "mail",
                     "groupMembership"},
                     AppProperties.ldapBaseTech.value());
            if (utenti != null && utenti.size() == 1) {
                return utenti.get(0);
            }
            return null;
        } finally {
            if (ldapConnection != null) {
                LdapOperations.disconnect(ldapConnection);
            }
        }
    }

    public Utente loginCodiceFiscale(String codiceFiscale, String password) {
        LDAPConnection loginConnection = null;
        try {
            Utente utente = findByCodiceFiscale(codiceFiscale);
            if (utente != null) {
                String keystore = null;
                LdapParameters loginParameters = new LdapParameters(AppProperties.ldapHosts.value(),
                         AppProperties.ldapPort.cast(Integer.class),
                         //utente.getDn(),
                         "",
                         password,
                         AppProperties.ldapWithAuth.cast(Boolean.class),
                         AppProperties.ldapWithSsl.cast(Boolean.class),
                         keystore);
                loginConnection = LdapOperations.connect(loginParameters);
                if (loginConnection != null && loginConnection.isBound() && loginConnection.isConnected()) {
                    return utente;
                }
            }
            return null;
        } finally {
            if (loginConnection != null) {
                LdapOperations.disconnect(loginConnection);
            }

        }
    }

    public Utente findByCodiceFiscale(String codiceFiscale) {
        logger.info("Login LDAP per codice fiscale");
        LDAPConnection ldapConnection = null;
        try {
            String keystore = null;
            LdapParameters ldapParameters = new LdapParameters(AppProperties.ldapHosts.value(),
                    AppProperties.ldapPort.cast(Integer.class),
                    AppProperties.ldapBindDN.value(),
                    AppProperties.ldapBindCredential.value(),
                    AppProperties.ldapWithAuth.cast(Boolean.class),
                    AppProperties.ldapWithSsl.cast(Boolean.class),
                    keystore);
            ldapConnection = LdapOperations.connect(ldapParameters);
            List<Utente> utenti = LdapOperations.users(ldapConnection, "uid=" + codiceFiscale, new String[]{"fullName",
                    "csIDVuid",
                    "mail",
                    "groupMembership"});
            if (utenti != null && utenti.size() == 1) {
                logger.info("logging user: " + utenti.get(0).getUsername());
                return utenti.get(0);
            }
            return null;
        } finally {
            if (ldapConnection != null) {
                LdapOperations.disconnect(ldapConnection);
            }
        }
    }


}