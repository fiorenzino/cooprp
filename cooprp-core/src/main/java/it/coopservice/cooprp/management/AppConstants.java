package it.coopservice.cooprp.management;

public class AppConstants
{

   public static final String APP_NAME = "cooprp";

   public static final String API_PATH = "/api";
   public static final String SEPARATOR = "_";

   public static final String IDENTITY_PATH = "/v1/identity";
   public static final String OPERAZIONI_PATH = "/v1/operations";
   public static final String LOCATIONS_PATH = "/v1/locations";
   public static final String NOTIFICATIONS_PATH = "/v1/notifications";
   public static final String WORKSHIFTS_PATH = "/v1/workshifts";
   public static final String COMPANY_CONFIGURATIONS_PATH = "v1/companyconfigurations";
   public static final String LANGUAGES_PATH = "/v1/languages";
   public static final String UTENTI_PATH = "/v1/user";

   public static final String I18N_PATH = "/v1/i18n";

   public static final String LOGIN_PATH = "/v1/login";

   public static final String OPERATIONS_QUEUE = "jms/queue/operationsqueue";
   public static final String DEFAULT_TIMEZONE = "Europe/Rome";

   public static final String GMT_TIMEZONE = "GMT";

   public static final String ISO_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss";

   public static final String EGGS_SCHEMA = "cooprp";
   public static final String ARGO_SCHEMA = "cooprp";

   public AppConstants()
   {
   }
}
