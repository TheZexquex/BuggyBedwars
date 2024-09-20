package club.devcord.gamejam.timer;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Stopwatch {
    private ScheduledExecutorService executorService;
    private boolean running;
    private boolean paused;
    private Duration elapsedDuration = Duration.ZERO;

    public void start(int stepAmount, TimeUnit timeUnit, Consumer<Duration> runAtPause, Consumer<Duration> runPerStep,  Runnable runOnShutdown) {
        running = true;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        step(stepAmount, timeUnit, runAtPause, runPerStep, runOnShutdown);
    }

    public void start(long stepAmount, TimeUnit timeUnit, Consumer<Duration> runAtPause, Consumer<Duration> runPerStep, Runnable runOnShutdown) {
        running = true;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        step(stepAmount, timeUnit, runAtPause, runPerStep, runOnShutdown);
    }

    private void step(long stepAmount, TimeUnit timeUnit, Consumer<Duration> runAtPause, Consumer<Duration> runPerStep, Runnable runOnShutdown) {
        if (!running) {
            runOnShutdown.run();
            executorService.shutdown();
            return;
        }
        if (paused) {
            runAtPause.accept(elapsedDuration);
        } else {
            runPerStep.accept(elapsedDuration);
            elapsedDuration = elapsedDuration.plus(Duration.of(stepAmount, timeUnit.toChronoUnit()));
        }
        executorService.schedule(() -> {
            step(stepAmount, timeUnit, runAtPause, runPerStep, runOnShutdown);
        }, stepAmount, timeUnit);
    }

    public void setPaused(boolean pause) {
        paused = pause;
    }

    public void abort() {
        running = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunning() {
        return running;
    }
}