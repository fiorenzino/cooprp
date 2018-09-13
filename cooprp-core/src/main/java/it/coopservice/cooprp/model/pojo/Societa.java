package it.coopservice.cooprp.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class Societa implements Serializable
{

   public static final String TABLE_NAME = "societa";
   private static final long serialVersionUID = 1L;
   public Long id;
   public String nome;
   public String societaId;

   public String archidocCodiceSocieta;
   public String archidocDescrizioneSocieta;
   public String archidocDescrizioneSocietaCud;
   public String codiceSoggettoPrefisso;

   public Date sapDataInizio;

   public Short archidocArchive = new Short("0");
   public Short archidocDocumentTypeBusta = new Short("107");
   public Short archidocDocumentTypeCud = new Short("131");

   public Integer archidocFirstYearOfCuds = 2011;

   //questo campo viene mappato sul campo codice_societa della tabella operatori_attivi / operatori (entity Operatore)
   public String societaPaghe;

   public Societa()
   {
   }

   public Societa(String archidocDescrizioneSocieta)
   {
      this.archidocDescrizioneSocieta = archidocDescrizioneSocieta;
   }

   public Societa(Long id, String nome, String societaId, String codiceSoggettoPrefisso, Date sapDataInizio,
            Short archidocArchive, String archidocCodiceSocieta,
            String archidocDescrizioneSocieta,
            String archidocDescrizioneSocietaCud,
            Short archidocDocumentTypeBusta, Short archidocDocumentTypeCud,
            Integer archidocFirstYearOfCuds, String societaPaghe)
   {
      this.id = id;
      this.nome = nome;
      this.societaId = societaId;
      this.archidocCodiceSocieta = archidocCodiceSocieta;
      this.archidocDescrizioneSocieta = archidocDescrizioneSocieta;
      this.archidocDescrizioneSocietaCud = archidocDescrizioneSocietaCud;
      this.codiceSoggettoPrefisso = codiceSoggettoPrefisso;
      this.sapDataInizio = sapDataInizio;
      this.archidocArchive = archidocArchive;
      this.archidocDocumentTypeBusta = archidocDocumentTypeBusta;
      this.archidocDocumentTypeCud = archidocDocumentTypeCud;
      this.archidocFirstYearOfCuds = archidocFirstYearOfCuds;
      this.societaPaghe = societaPaghe;
   }

   @Override public String toString()
   {
      return "Societa{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", societaId='" + societaId + '\'' +
               ", archidocCodiceSocieta='" + archidocCodiceSocieta + '\'' +
               ", archidocDescrizioneSocieta='" + archidocDescrizioneSocieta + '\'' +
               ", archidocDescrizioneSocietaCud='" + archidocDescrizioneSocietaCud + '\'' +
               ", codiceSoggettoPrefisso='" + codiceSoggettoPrefisso + '\'' +
               ", sapDataInizio=" + sapDataInizio +
               ", archidocArchive=" + archidocArchive +
               ", archidocDocumentTypeBusta=" + archidocDocumentTypeBusta +
               ", archidocDocumentTypeCud=" + archidocDocumentTypeCud +
               ", archidocFirstYearOfCuds=" + archidocFirstYearOfCuds +
               ", societaPaghe='" + societaPaghe + '\'' +
               '}';
   }
}
