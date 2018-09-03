package it.coopservice.cooprp.model.pojo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LanguageKey implements Serializable
{

   public String key;

   public String language;
}
