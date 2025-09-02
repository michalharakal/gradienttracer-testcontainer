package ai.tribit.gradienttracer;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.time.Duration;

public class GradientTracerContainer extends GenericContainer<GradientTracerContainer> {

    public GradientTracerContainer(ImageFromDockerfile image) {
        super(image);
        withStartupTimeout(Duration.ofMinutes(5));
        // keep container running; command is defined in Dockerfile
    }
}
