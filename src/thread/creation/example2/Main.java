package thread.creation.example2;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static final int MAX_PASSWORD = 4999;

    public static void main(String[] args) {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
        var threads = new ArrayList<Thread>();
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());

        threads.forEach(t -> t.start());
    }


    public static class Vault{
        private int password;
        public Vault(int password){
            this.password = password;
        }

        public boolean isCorrectPassword(int password){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password == password;
        }
    }

    public static abstract class HackerThread extends Thread{
        protected Vault vault;

        public HackerThread(Vault vault){
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            System.out.println("Starting thread " + this.getName());
            super.start();
        }
    }

    public static class AscendingHackerThread extends HackerThread{

        public AscendingHackerThread(final Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for(int guess = 0; guess < MAX_PASSWORD; guess++){
                if(vault.isCorrectPassword(guess)){
                    System.out.println(this.getName() + " guessed the password " + guess);
                    System.exit(0);
                }

            }
        }
    }

    public static class DescendingHackerThread extends HackerThread{

        public DescendingHackerThread(final Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int guess = MAX_PASSWORD; guess >= 0; guess--){
                if(vault.isCorrectPassword(guess)){
                    System.out.println(this.getName() + " guessed the password " + guess);
                    System.exit(0);
                }
            }
        }
    }

    public static class PoliceThread extends Thread{
        @Override
        public void run() {
            for (int i = 10; i > 0; i--){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
            System.out.println("Game over for you hackers");
            System.exit(0);
        }
    }
}
