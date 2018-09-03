package it.coopservice.cooprp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.coopservice.cooprp.model.pojo.LanguageKey;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static it.coopservice.cooprp.model.Language.TABLE_NAME;

@Table(name = TABLE_NAME)
@Entity
public class Language implements Serializable
{
   public final static String TABLE_NAME = "crp_languages";

   @EmbeddedId
   public LanguageKey id;

   public String value;

   @Column(insertable = false, updatable = false)
   public String key;

   @Column(insertable = false, updatable = false)
   public String language;

   @Temporal(TemporalType.TIMESTAMP)
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Rome")
   public Date lastUpdate;
}
