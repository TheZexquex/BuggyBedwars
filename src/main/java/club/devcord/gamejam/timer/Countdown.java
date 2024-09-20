package club.devcord.gamejam.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Countdown {
    private ScheduledExecutorService executorService;
    private boolean running;

    public void start(int timeSpan, TimeUnit timeUnit, Consumer<Long> runPerStep, Runnable runAfter) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        running = true;
        step(timeSpan, timeUnit, runPerStep, runAfter);
    }

    private void step(long timeSpan, TimeUnit timeUnit, Consumer<Long> runPerStep, Runnable runAfter) {
        if (!running) {
            executorService.shutdown();
            return;
        }

        if (timeSpan <= 0) {
            runAfter.run();
            executorService.schedule(() -> runPerStep.accept(timeSpan), 50, TimeUnit.MILLISECONDS);
            executorService.shutdown();
            return;
        }

        runPerStep.accept(timeSpan);

        executorService.schedule(() -> step(timeSpan - 1, timeUnit, runPerStep, runAfter), 1, timeUnit);
    }

    public void abort() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
