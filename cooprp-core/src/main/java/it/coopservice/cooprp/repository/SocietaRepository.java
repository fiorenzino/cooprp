package it.coopservice.cooprp.repository;

import it.coopservice.cooprp.management.AppConstants;
import it.coopservice.cooprp.model.pojo.Societa;
import org.giavacms.commons.tracer.TracerInterceptor;
import org.jboss.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@LocalBean
@Interceptors({ TracerInterceptor.class })
public class SocietaRepository implements Serializable
{

   private static final long serialVersionUID = 1L;

   Logger logger = Logger.getLogger(getClass());

   @PersistenceContext
   private EntityManager em;

   protected EntityManager getEm()
   {
      return em;
   }

   public void setEm(EntityManager em)
   {
      this.em = em;
   }

   @SuppressWarnings("unchecked")
   public List<Societa> getAllList() throws Exception
   {
      // private Long id;
      // private String nome;
      // private String societaId;
      //
      // private String archidocCodiceSocieta;
      // private String archidocDescrizioneSocieta;
      // private String codiceSoggettoPrefisso;
      //
      // private Date sapDataInizio;
      //
      // private Short archidocArchive = new Short("0");
      // private Short archidocDocumentTypeBusta = new Short("107");
      // private Short archidocDocumentTypeCud = new Short("131");
      //
      // private Integer archidocFirstYearOfCuds = 2011;

      List resultSet =
               getEm().createNativeQuery(
                        "SELECT "
                                 + " ID, NOME, SOCIETAID, CODICESOGGETTOPREFISSO, SAPDATAINIZIO, "
                                 + " ARCHIDOCARCHIVE, ARCHIDOCCODICESOCIETA, ARCHIDOCDESCRIZIONESOCIETA, "
                                 + " ARCHIDOCDESCSOCIETACUD, ARCHIDOCDOCUMENTTYPEBUSTA, ARCHIDOCDOCUMENTTYPECUD, "
                                 + " ARCHIDOCFIRSTYEAROFCUDS, SOCIETAPAGHE "
                                 + " FROM " + AppConstants.ARGO_SCHEMA + Societa.TABLE_NAME +
                                 " WHERE ID > 0 ").getResultList();
      List<Societa> result = mapResultSetToSocieta((List<Object[]>) resultSet);
      return result;
   }

   public Societa fetchSocietaPerCodicePaga(String codiceSocieta) throws Exception
   {
      // private Long id;
      // private String nome;
      // private String societaId;
      //
      // private String archidocCodiceSocieta;
      // private String archidocDescrizioneSocieta;
      // private String codiceSoggettoPrefisso;
      //
      // private Date sapDataInizio;
      //
      // private Short archidocArchive = new Short("0");
      // private Short archidocDocumentTypeBusta = new Short("107");
      // private Short archidocDocumentTypeCud = new Short("131");
      //
      // private Integer archidocFirstYearOfCuds = 2011;
      List resultSet = getEm()
               .createNativeQuery(
                        "SELECT "
                                 + " ID, NOME, SOCIETAID, ARCHIDOCCODICESOCIETA, ARCHIDOCDESCRIZIONESOCIETA, "
                                 + " ARCHIDOCDESCSOCIETACUD, CODICESOGGETTOPREFISSO, SAPDATAINIZIO, "
                                 + " ARCHIDOCARCHIVE, ARCHIDOCDOCUMENTTYPEBUSTA, ARCHIDOCDOCUMENTTYPECUD, "
                                 + " ARCHIDOCFIRSTYEAROFCUDS,SOCIETAPAGHE "
                                 + " FROM " + AppConstants.ARGO_SCHEMA + Societa.TABLE_NAME
                                 + " WHERE ID > 0 "
                                 + " AND SOCIETAPAGHE = :CODICE_SOCIETA")
               .setParameter("CODICE_SOCIETA", codiceSocieta)
               .getResultList();

      List<Societa> result = mapResultSetToSocieta((List<Object[]>) resultSet);
      return result.get(0);
   }

   public List<Societa> mapResultSetToSocieta(List<Object[]> result)
   {
      List<Societa> societaList = new ArrayList<>();
      result.forEach(object ->
      {
         Societa societa = new Societa();
         if (object[0] != null && object[0] instanceof Number)
         {
            Number number = (Number) object[0];
            societa.id = number.longValue();
         }
         if (object[1] != null)
         {
            societa.nome =  object[1].toString();
         }
         if (object[2] != null)
         {
            societa.societaId = object[2].toString();
         }
         if (object[3] != null)
         {
            societa.archidocCodiceSocieta = object[3].toString();
         }
         if (object[4] != null)
         {
            societa.archidocDescrizioneSocieta = object[4].toString();
         }
         if (object[5] != null)
         {
            societa.archidocDescrizioneSocieta = object[5].toString();
         }
         if (object[6] != null)
         {
            societa.codiceSoggettoPrefisso = object[6].toString();
         }
         if (object[7] != null && object[7] instanceof Date)
         {
            societa.sapDataInizio = (Date) object[7];
         }
         if (object[8] != null && object[7] instanceof Short)
         {
            societa.archidocArchive = (Short) object[8];
         }
         if (object[9] != null && object[9] instanceof Short)
         {
            societa.archidocDocumentTypeBusta = (Short) object[9];
         }
         if (object[10] != null && object[10] instanceof Short)
         {
            societa.archidocDocumentTypeCud = (Short) object[10];
         }
         if (object[11] != null && object[11] instanceof Number)
         {
            Number number = (Number) object[11];
            societa.archidocFirstYearOfCuds = number.intValue();
         }
         if (object[12] != null)
         {
            societa.societaPaghe = object[12].toString();
         }
         societaList.add(societa);
      });

      return societaList;
   }
}
