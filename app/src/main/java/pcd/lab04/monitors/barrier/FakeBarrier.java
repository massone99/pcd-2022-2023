package pcd.lab04.monitors.barrier;

/*
 * Barrier - to be implemented
 */
public class FakeBarrier implements Barrier {

    private int numThreads;

    public FakeBarrier(int numThreads) {
        this.numThreads = numThreads;
    }

    @Override
    public synchronized void hitAndWaitAll() throws InterruptedException {
        numThreads--;
        while (numThreads > 0) {
            wait();
        }
        notifyAll();
    }


}
