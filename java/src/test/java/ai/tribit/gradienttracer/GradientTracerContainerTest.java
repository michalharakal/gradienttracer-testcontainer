package ai.tribit.gradienttracer;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class GradientTracerContainerTest {

    @Test
    void canStartAndRunPython() throws IOException, InterruptedException {
        ImageFromDockerfile image = new ImageFromDockerfile()
                .withFileFromPath("Dockerfile", Path.of("../docker/Dockerfile").toAbsolutePath())
                .withBuildArg("GT_REF", "main");

        try (GradientTracerContainer container = new GradientTracerContainer(image)) {
            container.withStartupTimeout(Duration.ofMinutes(10));
            container.start();

            Container.ExecResult r = container.execInContainer("python", "-c", "import sys; print(sys.version.split()[0])");
            assertEquals(0, r.getExitCode(), () -> "Non-zero exit: " + r.getStderr());
            assertTrue(r.getStdout().trim().startsWith("3."));
        }
    }
}
