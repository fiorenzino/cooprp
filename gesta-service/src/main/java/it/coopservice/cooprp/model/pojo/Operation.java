package it.coopservice.cooprp.model.pojo;

import java.io.Serializable;

public class Operation implements Serializable
{
   public String uuid;
   public String operationStatus;
   public String operationType;
   public String location_uuid;
   public String companyConfiguration_uuid;
   public String dataOra;
   public String codiceFiscale;
   public String latitudine;
   public String longitudine;
   public String dataRicezione;
   public String dataNotifica;
   public String timezone;
   public String societaId;

   public Operation()
   {

   }
}
