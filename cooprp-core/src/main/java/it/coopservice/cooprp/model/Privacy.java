package it.coopservice.cooprp.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

import static it.coopservice.cooprp.model.Privacy.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Privacy implements Serializable
{
   public static final String TABLE_NAME = "crp_privacies";

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String uuid;

   public boolean attivo = true;

   public String codiceFiscale;

   public boolean accettato = false;

   public Date lastUpdate;

   public Privacy()
   {
   }

}
