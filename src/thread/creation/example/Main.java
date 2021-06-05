package thread.creation.example;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            System.out.println("We are now in thread: " + Thread.currentThread().getName());
            System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
            throw new RuntimeException("Intentional exception");
        });
        thread.setName("New Worker Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            System.out.println("A critical error happened in thread " + t.getName()
                            + " the error is: " + e.getMessage());
        });
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");
    }
}

