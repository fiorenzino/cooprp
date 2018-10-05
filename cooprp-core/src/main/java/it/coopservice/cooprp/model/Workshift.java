package it.coopservice.cooprp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static it.coopservice.cooprp.model.Workshift.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Workshift implements Serializable
{

   public static final String TABLE_NAME = "crp_workshifts";

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String uuid;

   public boolean attivo = true;

   public String codiceFiscale;
   public String societa;
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Europe/Rome")
   public Date dataInizio;
   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Europe/Rome")
   public Date dataFine;

   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "locationInizio_uuid", insertable = false, updatable = false)
   public Location locationInizio;
   public String locationInizio_uuid;

   @JsonIgnore
   @ManyToOne
   @JoinColumn(name = "locationFine_uuid", insertable = false, updatable = false)
   public Location locationFine;
   public String locationFine_uuid;

   public String wbsInizio;
   public String wbsFine;

   public String descrizione;

   public Workshift()
   {
   }

}
