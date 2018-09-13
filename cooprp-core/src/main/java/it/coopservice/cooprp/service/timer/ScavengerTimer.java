package it.coopservice.cooprp.service.timer;

import it.coopservice.cooprp.repository.OperationsRepository;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ScavengerTimer
{

   @Inject OperationsRepository operationsRepository;

   @Schedule(hour = "0", minute = "*", persistent = false)
   public void exec()
   {
   }

}
