package it.coopservice.cooprp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.coopservice.cooprp.model.pojo.LanguageKey;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static it.coopservice.cooprp.model.Language.TABLE_NAME;

@Table(name = TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { "key", "language" }) })
@Entity
public class Language implements Serializable
{
   public final static String TABLE_NAME = "crp_languages";

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String uuid;

   public boolean attivo = true;

   public String value;

   public String key;

   public String language;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Rome")
   public Date lastUpdate;
}
