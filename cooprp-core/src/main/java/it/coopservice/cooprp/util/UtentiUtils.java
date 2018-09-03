package it.coopservice.cooprp.util;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPEntry;
import it.coopservice.cooprp.management.AppProperties;
import org.giavacms.commons.model.pojo.Utente;
import org.jboss.logging.Logger;

import java.util.Iterator;

public class UtentiUtils
{
    static Logger logger = Logger.getLogger(UtentiUtils.class);


    public static Utente createUtenteFromLdapEntry(LDAPEntry entry) {
        Utente utente = new Utente();
        utente.setDn(entry.getDN());
        popolateUser(entry.getAttributeSet(), utente);
        return utente;
    }

    private static void popolateUser(LDAPAttributeSet attributes, Utente utente) {
        try {
            if (attributes != null) {
                @SuppressWarnings("unchecked")
                Iterator<LDAPAttribute> iterator = attributes.iterator();
                while (iterator.hasNext()) {
                    LDAPAttribute attribute = (LDAPAttribute) iterator.next();
                    if ("groupMembership".equals(attribute.getName())) {
                        String[] ldapRolesDN = null;
                        if (attribute.size() > 1) {
                            ldapRolesDN = attribute.getStringValueArray();
                        } else {
                            ldapRolesDN = new String[]{attribute.getStringValue()};
                        }
                        if (ldapRolesDN != null) {
                            for (String ldapRoleDN : ldapRolesDN) {
                                String appRole = makeRole(ldapRoleDN);
                                if (appRole != null) {
                                    utente.getRuoli().add(appRole);
                                }
                            }
                        }
                    }
                    if ("fullName".equals(attribute.getName())) {
                        utente.setNome(attribute.getStringValue());
                    }
                    if ("csIDVuid".equals(attribute.getName())) {
                        utente.setUsername(attribute.getStringValue());
                    }
//                    if ("mail".equals(attribute.getName())) {
//                        utente.setMail(attribute.getStringValue());
//                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * cn=GestioneDotazioni_PR_Operativi,ou=GestioneDotazioni,ou=Groups,o=Coopservice
     *
     * @param ldapRoleDN
     * @return
     */
    private static String makeRole(String ldapRoleDN) {
        if (ldapRoleDN == null) {
            return null;
        }
        try {

            // GestioneDotazioni_PR_Operativi,ou=GestioneDotazioni,ou=Groups,o=Coopservice
            ldapRoleDN = ldapRoleDN.substring(ldapRoleDN.indexOf("cn=") + "cn=".length());

            // GestioneDotazioni_PR_Operativi
            ldapRoleDN = ldapRoleDN.substring(0, ldapRoleDN.indexOf(","));

            String groupMembership = AppProperties.ldapGroupMembership.optionalValue();
            if (groupMembership == null || groupMembership.trim().isEmpty()) {
                // PR_Operativi
                if (ldapRoleDN.indexOf("_") >= 0) {
                    ldapRoleDN = ldapRoleDN.substring(ldapRoleDN.indexOf("_"));
                    if (ldapRoleDN.length() > 0) {
                        ldapRoleDN = ldapRoleDN.substring(1).trim();
                    }
                }
            } else if (ldapRoleDN.startsWith(groupMembership) && ldapRoleDN.length() > groupMembership.length()) {
                ldapRoleDN = ldapRoleDN.substring(groupMembership.length() + 1).trim();
            } else {
                ldapRoleDN = null;
            }
            return ldapRoleDN;
        } catch (Exception e) {// e.printStackTrace();
            logger.error("Error getting role from " + ldapRoleDN);
            return null;
        }

    }

}
