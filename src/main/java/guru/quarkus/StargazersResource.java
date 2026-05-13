package guru.quarkus;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/quarkus-flow/stargazers")
public class StargazersResource {

    @Inject
    StargazersWorkflow workflow;

    @GET
    public Uni<Set<Stargazer>> getStars() {
        return workflow.startInstance()
                .onItem()
                .transform(model -> model
                        .asCollection()
                        .stream()
                        .map(item -> item.as(Stargazer.class).orElseThrow())
                        .collect(Collectors.toSet()));
    }
}
