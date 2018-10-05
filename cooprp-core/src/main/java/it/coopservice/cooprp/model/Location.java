package it.coopservice.cooprp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

import static it.coopservice.cooprp.model.Location.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Location implements Serializable
{

   public static final String TABLE_NAME = "crp_locations";

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String uuid;

   public boolean attivo = true;

   @JsonIgnore
   public Point location;

   //   @Transient
   public String latitudine;

   //   @Transient
   public String longitudine;

   public String nome;
   public String indirizzo;
   public String comune;
   public String provincia;
   public String nazione;
   public String wbs;
   public String cantiere;
   public String commessa;

   public String societaId;

   public String mail;

   public BigInteger range;

   public Location()
   {
   }

}
