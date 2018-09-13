package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.pojo.Societa;
import org.giavacms.commons.tracer.TracerInterceptor;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@LocalBean
@Interceptors({ TracerInterceptor.class })
public class OperatoriAttiviRepository implements Serializable
{

   private static final long serialVersionUID = 1L;

   @PersistenceContext
   EntityManager em;

   public String matricolaProvvOperatoreValido(String codiceFiscale) throws Exception
   {
      String query = ""
               + " SELECT OP.MATRPROVV "
               + " FROM " + AppConstants.EGGS_SCHEMA + ".operatori_attivi OP "
               + " WHERE UPPER(codicefiscale) = :CODICE_FISCALE ";

      Query nativequery = em.createNativeQuery(query).setParameter("CODICE_FISCALE", codiceFiscale.toUpperCase());

      List<Object> matricolaProvvList = new ArrayList<>();
      List<Object> objectList = nativequery.getResultList();

      if (objectList == null || objectList.isEmpty())
      {
         throw new Exception("Non esiste un operatore attivo con codice fiscale " + codiceFiscale);
      }
      objectList.forEach(object ->
      {
         if (object != null)
         {
            matricolaProvvList.add(object.toString());
         }
      });

      if (matricolaProvvList.isEmpty())
      {
         throw new Exception("Non esiste un operatore attivo con matricola provvisoria e codice fiscale " +
                  codiceFiscale);
      }

      return matricolaProvvList.stream()
               .map(Object::toString)
               .collect(Collectors.joining(";"));
   }



   public List<Societa> societaPagaOperatore(String codiceFiscale) throws Exception
   {
      String query = ""
               + " SELECT OP.CODICE_SOCIETA, OP.DESCRIZIONE_SOCIETA"
               + " FROM " + AppConstants.EGGS_SCHEMA + ".operatori_attivi OP "
               + " WHERE UPPER(codicefiscale) = :CODICE_FISCALE "
               + " GROUP BY OP.CODICE_SOCIETA, OP.DESCRIZIONE_SOCIETA";

      Query nativequery = em.createNativeQuery(query).setParameter("CODICE_FISCALE", codiceFiscale.toUpperCase());

      List<Societa> matricolaProvvList = new ArrayList<>();
      List<Object> objectList = nativequery.getResultList();

      if (objectList == null || objectList.isEmpty())
      {
         throw new Exception("Non esiste un operatore attivo con codice fiscale " + codiceFiscale);
      }
      objectList.forEach(object ->
      {
         if (object != null)
         {
            Object[] array = (Object[]) object;
            Societa societa = new Societa();
            societa.societaId = (String)array[0];
            societa.nome = (String)array[1];
            matricolaProvvList.add(societa);
         }
      });

      if (matricolaProvvList.isEmpty())
      {
         throw new Exception("Non esiste un operatore attivo con matricola provvisoria e codice fiscale " +
                  codiceFiscale);
      }

      return matricolaProvvList;
   }
}
