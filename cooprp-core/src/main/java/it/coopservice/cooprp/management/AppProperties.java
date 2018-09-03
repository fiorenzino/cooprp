package it.coopservice.cooprp.management;

import org.giavacms.core.util.SystemPropertiesUtils;
import org.jboss.logging.Logger;

public enum AppProperties {
    // jwt
    jwtMobileSecret,
    jwtSecret,
    jwtExpireTime,
    jwtMobileExpireTime,
    jwtRoles,
    fileUploadPath,


    // ldap
    ldapHosts,
    ldapPort,
    ldapWithAuth,
    ldapWithSsl,
    ldapBase,
    ldapBindDN,
    ldapBindCredential,
    ldapGroupMembership,
    ldapBaseTech,

    defaultSchema,
    ;

    Logger logger = Logger.getLogger(getClass());

    private AppProperties() {
    }

    public static String getProperty(String prefix, String name) {
        return System.getProperty(prefix + "." + name);
    }

    @SuppressWarnings("unchecked")
    public <T> T cast(Class<T> clazz) {
        if (Long.class.equals(clazz)) {
            return (T) new Long(value());
        }
        if (Integer.class.equals(clazz)) {
            return (T) new Integer(value());
        }
        if (Boolean.class.equals(clazz)) {
            return (T) new Boolean(value());
        }
        return (T) value();
    }

    public String value() {
        String value = getProperty(AppConstants.APP_NAME, name());
        if (value == null) {
            String message = AppConstants.APP_NAME
                    + " - ATTENZIONE SISTEMA NON CONFIGURATO: MANCANO LE SYSTEM PROPERTIES IN STANDALONE.XML"
                    + "- " + name();
            throw new RuntimeException(
                    message);

            // SystemPropertiesUtils.setProperty(
            // getClass().getPackage().getName(), name(), predefinito);
        }
        return value;
    }

    public String replace(String[] placeholders, Object[] values) {
        String value = value();
        if (placeholders != null && values != null
                && placeholders.length == values.length) {
            for (int i = 0; i < placeholders.length; i++) {
                value = value.replaceAll("\\{" + placeholders[i] + "\\}",
                        (values[i] == null ? "" : values[i].toString()));
            }
        }
        return value;
    }

    public String[] split(String string) {
        return value().split(string);
    }

    public String optionalValue() {
        return SystemPropertiesUtils.getProperty(AppConstants.APP_NAME,
                name());
    }

}
