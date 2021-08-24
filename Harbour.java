package Lab_Lista12;

import java.security.KeyStore;
import java.util.Random;    
import java.util.concurrent.Semaphore;

public final class Harbour
{
    class Boat extends Thread
    {
        private final int helpingTugboats;

        private Boat(int number)
        {
            super("Boat #" + (number + 1));
            helpingTugboats = number % 3 + 1;
        }

        private void arrive() throws InterruptedException
        {
            System.out.println(getName() + " is ready to arrive to the harbour.");

            spots.acquire();
            tugboats.acquire(helpingTugboats);

            sleep(1500);

            System.out.println(getName() + Main.ANSI_YELLOW+ " moored. " + Main.ANSI_RESET);
            tugboats.release(helpingTugboats);

            sleep(new Random().nextInt(5)*1000);
        }

        private void setSail() throws InterruptedException
        {
            System.out.println(getName() + " is ready to set sail.");

            tugboats.acquire(helpingTugboats);

            spots.release();
            sleep(1000);
            System.out.println(getName() + Main.ANSI_CYAN + " sailed out" + Main.ANSI_RESET + " into the open sea.");

            tugboats.release(helpingTugboats);

            sleep((new Random().nextInt(5)+7)*1000);
        }

        @Override
        public void run()
        {
            while(true)
            {
                try {
                    setSail();
                    arrive();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Semaphore spots;
    private Semaphore tugboats;
    private Boat [] boats;


    public Harbour()
    {
        spots = new Semaphore(10,true);
        tugboats = new Semaphore(10);
        boats = createBoats(15);
    }

    public Harbour(int boats)
    {
        spots = new Semaphore((boats*2)/3,true);
        tugboats = new Semaphore(boats*2/3);
        this.boats = createBoats(boats);
    }

    public void start()
    {
        for (int i = 0; i < boats.length; i++)
            boats[i].start();
    }

    private Boat[] createBoats(int number)
    {
        Boat [] allBoats = new Boat[number];

        for (int i = 0; i < allBoats.length; i++)
        {
            allBoats[i] = new Boat(i);
        }
        return allBoats;
    }

}
class Main
{
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args)
    {
        Harbour harbour = new Harbour(15);
        harbour.start();
    }
}