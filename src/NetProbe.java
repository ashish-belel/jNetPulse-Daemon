import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetProbe {
    private static final Logger logger = Logger.getLogger("NetProbe");
    private static final String LOG_FILE = "/var/log/net_probe.log";

    static class Target {
        String host; int port; String name;
        Target(String host, int port, String name) {
            this.host = host; this.port = port; this.name = name;
        }
    }

    public static void main(String[] args) {
        try {
            FileHandler fh = new FileHandler(LOG_FILE, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            return;
        }

        List<Target> targets = Arrays.asList(
            new Target("8.8.8.8", 53, "Google DNS"),
            new Target("github.com", 443, "GitHub Web")
        );

        logger.info("Network Observability Daemon Started.");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        Runnable probeTask = () -> {
            for (Target t : targets) {
                long start = System.currentTimeMillis();
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(t.host, t.port), 3000);
                    long latency = System.currentTimeMillis() - start;
                    logger.info(String.format("[UP] %s is reachable. Latency: %d ms", t.name, latency));
                } catch (Exception e) {
                    logger.severe(String.format("[DOWN] %s is UNREACHABLE.", t.name));
                }
            }
        };

        scheduler.scheduleAtFixedRate(probeTask, 0, 60, TimeUnit.SECONDS);
    }
}