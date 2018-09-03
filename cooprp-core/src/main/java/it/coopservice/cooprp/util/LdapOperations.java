package it.coopservice.cooprp.util;

import com.novell.ldap.*;
import it.coopservice.cooprp.management.AppProperties;
import it.coopservice.cooprp.model.pojo.LdapParameters;
import org.giavacms.commons.model.pojo.Utente;
import org.jboss.logging.Logger;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class LdapOperations
{
  static Logger logger = Logger.getLogger(LdapOperations.class.getName());

  public static LDAPConnection connect(LdapParameters ldap) {
    LDAPConnection ld = null;
    try {
      if (ldap.isWithSsl()) {
        logger.debug("with ssl");
        // Security.addProvider(new Provider());
        if (ldap.getKeystore() != null
          && !ldap.getKeystore().trim().isEmpty()) {
          System.setProperty("javax.net.ssl.trustStore",
            ldap.getKeystore());
        }

        LDAPConnection
          .setSocketFactory(new LDAPJSSESecureSocketFactory());
      }
      ld = new LDAPConnection();
      if (ldap.getHost() != null && ldap.getHost().contains(";")) {
        logger.debug("FAILOVER - USING HOSTS: " + ldap.getHost());
        String[] splitH = ldap.getHost().split(";");
        for (String host : splitH) {

          try {
            if (host.contains(":")) {
              String[] hostPort = host.split(":");
              if (hostPort.length == 2 && hostPort[0].trim().length() > 0 && hostPort[1].trim().length() > 0)
                ld.connect(hostPort[0], Integer.valueOf(hostPort[1]));
            } else {
              ld.connect(host, ldap.getPort());
            }

            if (ldap.isWithAuth()) {
              logger.warn("FAILOVER - with auth");
              ld.bind(3, ldap.getUsername(),
                ldap.getPassword().getBytes("UTF8"));
            } else {
              logger.debug("FAILOVER - anonymous connection!!!");
            }
            if (ld != null) {
              logger.debug("FAILOVER - LDAP CONNECTION ON: " + host);
              return ld;
            }
          } catch (LDAPException e) {
            logger.error("FAILOVER - ERROR IN LDAP CONNECTION ON: " + host);
            logger.error("FAILOVER - Error: " + e.toString());
          }

        }

      } else {
        try {
          ld.connect(ldap.getHost(), ldap.getPort());
          if (ldap.isWithAuth()) {
            logger.debug("with auth");
            ld.bind(3, ldap.getUsername(),
              ldap.getPassword().getBytes("UTF8"));
          } else {
            logger.debug("anonymous connection!!!");
          }
          if (ld != null) {
            logger.debug("LDAP CONNECTION ON: " + ldap.getHost());
            return ld;
          }
        } catch (LDAPException e) {
          logger.error("ERROR IN LDAP CONNECTION ON: " + ldap.getHost());
          logger.error("Error: " + e.toString());
          return null;
        }
      }

    } catch (UnsupportedEncodingException e) {
      logger.error("Error: " + e.toString());
      return null;

    }
    return ld;
  }

  public static boolean disconnect(LDAPConnection ld) {
    try {
      ld.disconnect();
    } catch (Throwable exception) {
      return false;
    }
    return true;
  }

  public static LDAPSearchResults search(LDAPConnection ld, String base,
                                         int scope, String filter, String[] attributes) {
    try {
      if (attributes == null)
        attributes = new String[]{LDAPConnection.ALL_USER_ATTRS};

      LDAPSearchResults risultati = ld.search(base, scope, filter,
        attributes, false);
      if (risultati.hasMore()) {
        return risultati;
      }
      return null;
    } catch (Throwable exception) {
      // logger.debug("ERROR: ATTRIB - " + Arrays.toString(attributes));
      // logger.debug("ERROR: filter - " + filter);
      logger.error("LdapUtils.search:" + exception.getMessage());
      // exception.getStackTrace();
    }
    return null;
  }

  public static List<Utente> users(LDAPConnection connection, String filter, String[] returnAttributes) {
    return users(connection, filter, returnAttributes, AppProperties.ldapBase.value());
  }

  public static List<Utente> users(LDAPConnection connection, String filter, String[] returnAttributes, String base) {
    List<Utente> returnResult = new ArrayList<Utente>();

    LDAPSearchConstraints searchConstraints = new LDAPSearchConstraints(
      connection.getSearchConstraints());
    searchConstraints.setMaxResults(20000);
    connection.setConstraints(searchConstraints);
    LDAPSearchResults searchResults = LdapOperations.search(
      connection, base, LDAPConnection.SCOPE_SUB, filter, returnAttributes);
    if (searchResults != null) {
      Object[] ordered = LdapOperations.orderBy(searchResults,
        new String[]{"cn"});
      LDAPEntry entry = null;
      for (int i = 0; i < ordered.length; ++i) {
        entry = (LDAPEntry) ordered[i];
        returnResult.add(UtentiUtils.createUtenteFromLdapEntry(entry));
      }
    }
    return returnResult;
  }

  @SuppressWarnings("unchecked")
  public static Object[] orderBy(LDAPSearchResults searchResults,
                                 String[] namesToSortBy) {
    boolean[] sort = new boolean[]{true};
    TreeSet<Object> sortedResults = new TreeSet<Object>();
    while (searchResults.hasMore()) {
      try {
        sortedResults.add(searchResults.next());
      } catch (Throwable localLDAPException) {
        logger.error(localLDAPException.getMessage());
      }
    }
    LDAPCompareAttrNames myComparator = null;
    try {
      myComparator = new LDAPCompareAttrNames(namesToSortBy, sort);
    } catch (Throwable exc) {
      return null;
    }
    Object[] sortedSpecial = sortedResults.toArray();
    Arrays.sort(sortedSpecial, myComparator);
    return sortedSpecial;
  }

  public static boolean changeDn(LDAPConnection ld, String dn, String cn,
                                 String newParentdn) {
    logger.info("changeDn, dn = " + dn);
    logger.info("changeDn, cn = " + cn);
    logger.info("changeDn, newParentDn = " + newParentdn);
    // ESEMPIO: cambio dn: cn=pasini12 samuele12,ou=P,ou=Soci,ou=Utentes,o=SD
    // in dn: cn=pasini12 samuele12,ou=Q,ou=Soci,ou=Utentes,o=SD
    // parametri da passare
    // dn: cn=pasini12 samuele12,ou=P,ou=Soci,ou=Utentes,o=SD
    // cn: cn=pasini12 samuele12
    // newParentdn: ou=Q,ou=Soci,ou=Utentes,o=SD
    try {
      ld.rename(dn, cn, newParentdn, true);
      // ld.rename(oldDn, newDn, true);
    } catch (Exception e) {
      logger.error("changeDn: " + dn, e);
      return false;
    }
    return true;
  }

  public static List<String> groupMembership(LDAPConnection c, String base,
                                             String value) {
    try {
      LDAPSearchConstraints searchConstraints = new LDAPSearchConstraints(
        c.getSearchConstraints());
      searchConstraints.setMaxResults(20000);
      c.setConstraints(searchConstraints);
      List<String> groupMemberships = new ArrayList<String>();
      String filtro = "objectClass=inetOrgPerson";
      String[] attributi = new String[]{"groupMembership"};
      LDAPSearchResults risultati = search(c, base, LDAPConnection.SCOPE_SUB, filtro, attributi);
      value = value.toUpperCase();
      while (risultati.hasMore()) {
        LDAPEntry risultato = risultati.next();
        if (risultato.getAttribute("groupMembership") != null) {
          String[] gs = risultato.getAttribute("groupMembership")
            .getStringValueArray();
          if (gs != null) {
            for (String g : gs) {
              if (value.equals(g.toUpperCase())) {
                groupMemberships.add(risultato.getDN());
              }
            }

          }
        }
      }
      Collections.sort(groupMemberships);
      StringBuffer sb = new StringBuffer();
      for (String groupMembershipValue : groupMemberships) {
        sb.append(groupMembershipValue.trim()).append("\n");
      }
      return groupMemberships;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
  }

  public static List<String> member(LDAPConnection c, String base) {
    try {
      LDAPSearchConstraints searchConstraints = new LDAPSearchConstraints(
        c.getSearchConstraints());
      searchConstraints.setMaxResults(20000);
      c.setConstraints(searchConstraints);
      String filtro = "(!(objectClass=inetOrgPerson))";
      String[] attributi = new String[]{"member"};
      LDAPSearchResults risultati = search(c, base, LDAPConnection.SCOPE_SUB, filtro, attributi);
      if (risultati.getCount() == 0) {
        return new ArrayList<String>();
      }
      LDAPEntry risultato = risultati.next();
      if (risultato == null) {
        return new ArrayList<String>();
      }
      LDAPAttribute memberAttribute = risultato.getAttributeSet()
        .getAttribute("member");
      if (memberAttribute == null) {
        return new ArrayList<String>();
      }
      String[] memberValues = memberAttribute.getStringValueArray();
      List<String> members = new ArrayList<String>();
      Arrays.sort(memberValues);
      StringBuffer sb = new StringBuffer();
      for (String memberValue : memberValues) {
        sb.append(memberValue.trim()).append("\n");
        members.add(memberValue);
      }
      return members;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
  }

  public static List<String> equivalentToMe(LDAPConnection c, String base) {
    try {
      LDAPSearchConstraints searchConstraints = new LDAPSearchConstraints(
        c.getSearchConstraints());
      searchConstraints.setMaxResults(20000);
      c.setConstraints(searchConstraints);
      String filtro = "(!(objectClass=inetOrgPerson))";
      String[] attributi = new String[]{"equivalentToMe"};
      LDAPSearchResults risultati = search(c, base, LDAPConnection.SCOPE_SUB, filtro, attributi);
      if (risultati.getCount() == 0) {
        return new ArrayList<String>();
      }
      LDAPEntry risultato = risultati.next();
      if (risultato == null) {
        return new ArrayList<String>();
      }
      LDAPAttribute memberAttribute = risultato.getAttributeSet()
        .getAttribute("equivalentToMe");
      if (memberAttribute == null) {
        return new ArrayList<String>();
      }
      String[] memberValues = memberAttribute.getStringValueArray();
      List<String> members = new ArrayList<String>();
      Arrays.sort(memberValues);
      StringBuffer sb = new StringBuffer();
      for (String memberValue : memberValues) {
        sb.append(memberValue.trim()).append("\n");
        members.add(memberValue);
      }
      return members;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
  }

  public static String ids(LDAPConnection c, String base) {
    try {
      String filtro = "objectClass=inetOrgPerson";
      String[] attributi = new String[]{"csIDVuid", "workforceID"};
      LDAPSearchResults risultati = search(c, base, 2, filtro, attributi);
      LDAPEntry risultato = risultati.next();
      String csIDVuid = risultato.getAttribute("csIDVuid") == null ? null
        : risultato.getAttribute("csIDVuid").getStringValue();
      String workforceID = risultato.getAttribute("workforceID") == null ? null
        : risultato.getAttribute("workforceID").getStringValue();
      return base + ",csIDVuid=" + csIDVuid + ",workforceID="
        + workforceID;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return null;
    }
  }

}