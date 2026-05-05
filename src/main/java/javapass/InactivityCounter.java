package javapass;
import java.util.Timer;
import java.util.TimerTask;

// on utilise cette class comme un servie demarable ce qui la rend plus fonctionnelle.

public class InactivityCounter {

    private static final long INACTIVITY_DELAY = 5 * 60 * 1000L;
    //private static final long INACTIVITY_DELAY = 10 * 1000L;
    private Services services;
    private static Timer inactivityTimer = new Timer(true); // true = daemon
    private static TimerTask currentTask;

    // Plus de main(), on expose start() à la place
    public void start(Services services) {
        this.services = services;
        resetTimer();
    }

    public synchronized void resetTimer() {
        if (currentTask != null) {
            currentTask.cancel();
        }

        currentTask = new TimerTask() {
            @Override
            public void run() {
                System.out.print(Interface.RED + "\n[Securite] " + Interface.GREEN +
                    "5 minutes d'inactivité détectées. Fermeture de l'application ...");
                    //"10 secondes d'inactivité détectées. Fermeture de l'application ");
                for (int k = 3; k != 0; k--) {
                    services.wait(2000);
                    System.out.print((" ... " + k ));
                }
                services.deconnectionDB();
                System.exit(0);
            }
        };

        inactivityTimer.schedule(currentTask, INACTIVITY_DELAY);
    }

    public static String waitMs(int millisecond) {
        try {
            Thread.sleep(millisecond);
            return "Done";
        } catch (InterruptedException e) {
            return e.getMessage();
        }
    }
}


