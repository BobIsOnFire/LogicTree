import java.time.Duration;
import java.time.Instant;

public class Stopwatch {
    private Instant startTime;
    private long time;
    private boolean paused;
    private String name;

    public static Stopwatch start() {
        return start("Stopwatch time");
    }

    public static Stopwatch start(String name) {
        Stopwatch w = new Stopwatch();
        w.name = name;
        w.startTime = Instant.now();
        return w;
    }

    public Stopwatch resume() {
        if (!paused) return this;
        startTime = Instant.now();
        paused = false;
        return this;
    }

    public Stopwatch stop() {
        if (paused) return this;
        time += Duration.between(startTime, Instant.now()).toMillis();
        paused = true;
        return this;
    }

    public Stopwatch clear() {
        stop();
        time = 0;
        return this;
    }

    private long getTime() {
        if (paused) return time;
        return time + Duration.between(startTime, Instant.now()).toMillis();
    }

    public void stopAndReport() {
        stop();
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return name + ": " + getTime() + " ms";
    }
}
