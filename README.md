# jNetPulse Daemon

A lightweight, Java-based Linux daemon designed for real-time network observability. It actively monitors target infrastructure via TCP socket connections, tracks network latency, and logs uptime status natively using Linux `systemd`.

## 🚀 Problem & Solution

Manual infrastructure checks are inefficient. This project automates uptime and latency monitoring by running as a continuous background process, simulating the edge-probing techniques used by enterprise observability platforms.

## 🛠 Tech Stack

- **Language:** Java 11+
- **Concurrency:** `java.util.concurrent.ScheduledExecutorService`
- **Networking:** TCP Sockets (`java.net.Socket`)
- **OS / Infrastructure:** Linux `systemd`, `systemctl`

## ⚙️ Deployment Instructions (Linux)

1. **Compile the probe:**
   ```bash
   javac NetProbe.java
   sudo mkdir -p /opt/netprobe
   sudo mv NetProbe.class /opt/netprobe/
   ```

2. **Prepare the log directory:**
   ```bash
   sudo touch /var/log/net_probe.log
   sudo chmod 666 /var/log/net_probe.log
   ```

3. **Register and start the daemon:**
   ```bash
   sudo cp netprobe.service /etc/systemd/system/
   sudo systemctl daemon-reload
   sudo systemctl enable netprobe.service
   sudo systemctl start netprobe.service
   ```

## 📊 Sample Output

Viewing the live telemetry via `tail -f /var/log/net_probe.log`:

```text
Jun 29, 2026 4:55:00 PM NetProbe lambda$main$0
INFO: [UP] Google DNS is reachable. Latency: 12 ms
Jun 29, 2026 4:55:00 PM NetProbe lambda$main$0
INFO: [UP] GitHub Web is reachable. Latency: 45 ms
Jun 29, 2026 4:56:00 PM NetProbe lambda$main$0
SEVERE: [DOWN] Internal Server is UNREACHABLE.
```
