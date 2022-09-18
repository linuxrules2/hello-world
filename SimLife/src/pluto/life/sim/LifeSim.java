package pluto.life.sim;

public class LifeSim {

    private long mLiftCount = 0;

    private void start() throws InterruptedException{
        mLiftCount = 1;

        LivingThing lt = new LivingThing();
        Thread thread = new Thread(lt);
        thread.start();

        while(Thread.activeCount() > 2) {
            Thread.sleep(10000);
            System.out.println("Tick..." + mLiftCount++ + ", Running..." + Thread.activeCount());
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Start...");

        LifeSim sim = new LifeSim();
        sim.start();

        System.out.println("The End");
    }
}
