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

@Controller
public class HomeController {

    private final Instant startTime = Instant.now();

    @GetMapping("/")
    public String home(Model model) throws Exception {

        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        long heapUsed = memBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMax  = memBean.getHeapMemoryUsage().getMax()  / (1024 * 1024);
        int heapPct   = (int) ((heapUsed * 100) / heapMax);

        Duration uptime = Duration.between(startTime, Instant.now());
        long hours   = uptime.toHours();
        long minutes = uptime.toMinutesPart();
        long seconds = uptime.toSecondsPart();

        String hostname  = InetAddress.getLocalHost().getHostName();
        String podIp     = InetAddress.getLocalHost().getHostAddress();
        String namespace = System.getenv("POD_NAMESPACE") != null
                           ? System.getenv("POD_NAMESPACE") : "default";
        String nodeName  = System.getenv("NODE_NAME") != null
                           ? System.getenv("NODE_NAME") : "unknown";

        double cpuLoad = osBean.getSystemLoadAverage();

        model.addAttribute("language",   "Java 17");
        model.addAttribute("framework",  "Spring Boot 3.2");
        model.addAttribute("server",     "Embedded Tomcat");
        model.addAttribute("port",       "8080");

        model.addAttribute("hostname",   hostname);
        model.addAttribute("podIp",      podIp);
        model.addAttribute("namespace",  namespace);
        model.addAttribute("nodeName",   nodeName);
        model.addAttribute("heapUsed",   heapUsed);
        model.addAttribute("heapMax",    heapMax);
        model.addAttribute("heapPct",    heapPct);
        model.addAttribute("cpuLoad",    String.format("%.2f", cpuLoad));
        model.addAttribute("uptime",     String.format("%dh %dm %ds", hours, minutes, seconds));
        model.addAttribute("javaVersion",System.getProperty("java.version"));
        model.addAttribute("os",         System.getProperty("os.name") + " " + System.getProperty("os.arch"));

        return "index";
    }
}
