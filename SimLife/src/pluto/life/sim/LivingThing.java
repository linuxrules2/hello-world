package pluto.life.sim;

import java.util.Random;

public class LivingThing implements Runnable {

    static final public int SIM_LIFT_EXPECTATION = 10;      // seconds
    private long mLifeStart;    // ms
    private long mExp;          // second

    public LivingThing() {
        mLifeStart = System.currentTimeMillis();
        mExp = SIM_LIFT_EXPECTATION; //new Random().nextInt(SIM_LIFT_EXPECTATION);

        //System.out.println("+New... " + mLifeStart);
    }

    @Override
    public void run() {

        final long expDie = mLifeStart + mExp * 1000;
        while(expDie > System.currentTimeMillis()) {

            final long currTime = System.currentTimeMillis();
            final long age = (currTime - mLifeStart);   // age of ms
            if((age > SIM_LIFT_EXPECTATION * 200) && (age < SIM_LIFT_EXPECTATION * 600)) {
                reproduce();
            }

            try {
                Thread.sleep(1000);
            }
            catch(Exception e) {
                // ignore for now
            }
        }

        //System.out.println("-End... " + mLifeStart);
    }

    private void reproduce() {

        Random rand = new Random();

        if(rand.nextInt(10) < 7) {
            return;
        }

        LivingThing lt = new LivingThing();
        Thread thread = new Thread(lt);
        thread.start();
    }
}
