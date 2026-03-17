/**
 * K8s practice Java application package.
 */
package com.k8spractice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;

/**
 * Home controller for the K8s practice application.
 * Serves the main page with tech stack and system health info.
 */
@Controller
public final class HomeController {

    /** Bytes per megabyte conversion constant. */
    private static final long BYTES_PER_MB = 1024 * 1024;

    /** Percentage multiplier. */
    private static final long PERCENT = 100;

    /** Application start time used to calculate uptime. */
    private final Instant startTime = Instant.now();

    /**
     * Detects the environment where the app is running.
     *
     * @return "kubernetes", "ec2", or "local"
     */
    private String getEnv() {
        if (System.getenv("KUBERNETES_SERVICE_HOST") != null) {
            return "kubernetes";
        }
        if ("ec2".equals(System.getenv("APP_ENV"))) {
            return "ec2";
        }
        return "local";
    }

    /**
     * Renders the home page with tech stack and system health data.
     *
     * @param model the Spring MVC model
     * @return the name of the Thymeleaf template to render
     * @throws Exception if hostname or IP lookup fails
     */
    @GetMapping("/")
    public String home(final Model model) throws Exception {

        final MemoryMXBean memBean =
            ManagementFactory.getMemoryMXBean();
        final OperatingSystemMXBean osBean =
            ManagementFactory.getOperatingSystemMXBean();

        final long heapUsed =
            memBean.getHeapMemoryUsage().getUsed() / BYTES_PER_MB;
        final long heapMax =
            memBean.getHeapMemoryUsage().getMax() / BYTES_PER_MB;
        final int heapPct = (int) ((heapUsed * PERCENT) / heapMax);

        final Duration uptime = Duration.between(startTime, Instant.now());
        final long hours = uptime.toHours();
        final long minutes = uptime.toMinutesPart();
        final long seconds = uptime.toSecondsPart();

        final double cpuLoad = osBean.getSystemLoadAverage();
        final String env = getEnv();

        // Environment-aware host info
        final String hostname;
        final String podIp;
        final String envType;
        final String namespace;
        final String nodeName;

        if ("kubernetes".equals(env)) {
            envType   = "Kubernetes";
            hostname  = InetAddress.getLocalHost().getHostName();
            podIp     = InetAddress.getLocalHost().getHostAddress();
            namespace = System.getenv("POD_NAMESPACE") != null
                ? System.getenv("POD_NAMESPACE") : "default";
            nodeName  = System.getenv("NODE_NAME") != null
                ? System.getenv("NODE_NAME") : "unknown";
        } else if ("ec2".equals(env)) {
            envType   = "EC2";
            hostname  = System.getenv("EC2_HOSTNAME") != null
                ? System.getenv("EC2_HOSTNAME")
                : InetAddress.getLocalHost().getHostName();
            podIp     = System.getenv("EC2_IP") != null
                ? System.getenv("EC2_IP")
                : InetAddress.getLocalHost().getHostAddress();
            namespace = null;
            nodeName  = null;
        } else {
            envType   = "Local";
            hostname  = InetAddress.getLocalHost().getHostName();
            podIp     = InetAddress.getLocalHost().getHostAddress();
            namespace = null;
            nodeName  = null;
        }

        // Tech stack
        model.addAttribute("language", "Java 17");
        model.addAttribute("framework", "Spring Boot 3.2");
        model.addAttribute("server", "Embedded Tomcat");
        model.addAttribute("port", "8080");
        model.addAttribute(
            "javaVersion", System.getProperty("java.version"));
        model.addAttribute(
            "os",
            System.getProperty("os.name")
            + " " + System.getProperty("os.arch"));

        // Environment info
        model.addAttribute("envType", envType);
        model.addAttribute("hostname", hostname);
        model.addAttribute("podIp", podIp);
        model.addAttribute("namespace", namespace);
        model.addAttribute("nodeName", nodeName);
        model.addAttribute(
            "uptime",
            String.format("%dh %dm %ds", hours, minutes, seconds));

        // System health
        model.addAttribute("heapUsed", heapUsed);
        model.addAttribute("heapMax", heapMax);
        model.addAttribute("heapPct", heapPct);
        model.addAttribute(
            "cpuLoad", String.format("%.2f", cpuLoad));

        return "index";
    }
}
