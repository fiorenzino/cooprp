package it.coopservice.cooprp.service.timer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class ScavengerTimer {


    @Schedule(hour = "0", minute = "*", persistent = false)
    public void exec() {

    }
}
