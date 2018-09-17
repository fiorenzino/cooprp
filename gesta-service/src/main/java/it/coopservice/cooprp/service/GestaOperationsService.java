package it.coopservice.cooprp.service;

import it.coopservice.cooprp.model.pojo.Operation;
import it.coopservice.cooprp.model.pojo.OperationType;

import javax.ejb.Stateless;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*
12962;0004851233;2018;08;28;08;10;E
12962;0004851305;2018;08;28;08;11;E

I primi 5 caratteri non hanno importanza

I 10 caratteri successivi attualmente sono il numero Badge (Cosa ci mettiamo?) => mi sembra di ricordare la matricola provvisoria [DA VERIFICARE]

Anno

Mese

Giorno

Ora

Minuti

Verso (E: Entrata, U: Uscita)
 */
@Stateless
public class GestaOperationsService
{
   public void writeToFile(Operation operation) throws IOException
   {
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(operation.realDate);
      StringBuilder msg = new StringBuilder("12962");
      String separetor = ";";
      msg.append(separetor);

      msg.append(operation.matricolaProvvisoria);
      msg.append(separetor);

      msg.append(calendar.get(Calendar.YEAR));
      msg.append(separetor);
      msg.append(calendar.get(Calendar.MONTH));
      msg.append(separetor);
      msg.append(calendar.get(Calendar.DAY_OF_MONTH));
      msg.append(separetor);
      msg.append(calendar.get(Calendar.HOUR_OF_DAY));
      msg.append(separetor);
      msg.append(calendar.get(Calendar.MINUTE));
      msg.append(separetor);
      String type = operation.operationType.equals(OperationType.OPEN) ? "E" : "U";
      msg.append(type);
      msg.append(separetor);

      Files.write(Paths.get("/home/jboss/Timbrature.txt"), msg.toString().getBytes());
   }
}
