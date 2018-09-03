package it.coopservice.cooprp.model;

//tab_operatori

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

import static it.coopservice.cooprp.model.CompanyConfiguration.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class CompanyConfiguration implements Serializable
{

   static final String TABLE_NAME = "crp_companyconfigurations";

   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "uuid", unique = true)
   public String uuid;

   public String nome;
   public String mail;
   public Long retentionPeriod;
   public String wsOperazioni;
   public String wsTurni;
   public Long tolleranzaMinuti;

   public CompanyConfiguration()
   {
   }

}
