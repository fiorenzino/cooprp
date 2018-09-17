package it.coopservice.cooprp.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class Operation implements Serializable
{
   public String uuid;
   public String operationStatus;
   public OperationType operationType;
   public String location_uuid;
   public String companyConfiguration_uuid;
   public String dataOra;
   public String codiceFiscale;
   public String matricolaProvvisoria;
   public String latitudine;
   public String longitudine;
   public String timezone;
   public String societaId;

   public Date dataRicezione;
   public Date dataNotifica;
   public Date realDate;

   public Operation()
   {

   }
}
