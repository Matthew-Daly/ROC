package using_runnable_interface;

public class MyRunnable implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            System.out.println("Printing from " + Thread.currentThread().getName() + " Value of i = " + i);
        }
    }

}
