package org.giavacms.commons.model.pojo;

import it.coopservice.cooprp.model.pojo.Societa;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class Utente implements Serializable
{
   private static final long serialVersionUID = -2548444688311424091L;

   private String username;
   private String nome;
   private String cognome;
   private List<String> ruoli;
   private String email;
   public String matricola;

   public List<Societa> societa;
   private Map<String, String> attributi;

   public List<String> getRuoli()
   {
      if (ruoli == null)
      {
         ruoli = new ArrayList<String>();
      }
      return ruoli;
   }

   public void setRuoli(List<String> ruoli)
   {
      this.ruoli = ruoli;
   }

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public String getNome()
   {
      return nome;
   }

   public void setNome(String nome)
   {
      this.nome = nome;
   }

   public String getCognome()
   {
      return cognome;
   }

   public void setCognome(String cognome)
   {
      this.cognome = cognome;
   }

   public Map<String, Object> toMap()
   {
      HashMap<String, Object> claims = new HashMap<String, Object>();
      claims.put("username", username);
      claims.put("name", nome);
      claims.put("mail", email);
      claims.put("surname", cognome);
      claims.put("roles", getRuoli().toArray());
      claims.putAll(getAttributi());
      return claims;

   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public Map<String, String> getAttributi()
   {
      if (attributi == null)
      {
         attributi = new HashMap<>();
      }
      return attributi;
   }

   public void setAttributi(Map<String, String> attributi)
   {
      this.attributi = attributi;
   }
}
