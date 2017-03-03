import org.osbot.rs07.utility.ConditionalSleep;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by LRDBLK on 2017-03-03.
 */
public class CustomTiming {


    public static boolean waitCondition (Callable<Boolean> con, int timeOut){
        return new ConditionalSleep(timeOut, 25){

            public boolean condition(){
                try {
                    return con.call();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return false;
            }


        }.sleep();
    }

    public static String formatMS(long ms)
    {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms),
                TimeUnit.MILLISECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(ms) % TimeUnit.MINUTES.toSeconds(1));
    }


}
