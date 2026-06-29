import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class NetProbe {
    static class Target {
        String host; int port; String name;
        Target(String host, int port, String name) {
            this.host = host; this.port = port; this.name = name;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Target> targets = Arrays.asList(
            new Target("8.8.8.8", 53, "Google DNS"),
            new Target("github.com", 443, "GitHub Web")
        );

        System.out.println("Starting network probe...");

        while (true) {
            for (Target t : targets) {
                long start = System.currentTimeMillis();
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(t.host, t.port), 3000);
                    long latency = System.currentTimeMillis() - start;
                    System.out.printf("[UP] %s is reachable. Latency: %d ms%n", t.name, latency);
                } catch (Exception e) {
                    System.out.printf("[DOWN] %s is UNREACHABLE.%n", t.name);
                }
            }
            Thread.sleep(60000);
        }
    }
}