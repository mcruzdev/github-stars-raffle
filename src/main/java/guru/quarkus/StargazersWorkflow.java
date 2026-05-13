package guru.quarkus;

import io.quarkiverse.flow.Flow;
import io.serverlessworkflow.api.types.Workflow;
import io.serverlessworkflow.fluent.func.FuncWorkflowBuilder;
import io.serverlessworkflow.fluent.func.dsl.FuncDSL;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.serverlessworkflow.fluent.func.dsl.FuncDSL.function;

@ApplicationScoped
public class StargazersWorkflow extends Flow {

    @ConfigProperty(name = "github.quarkiverse.quarkus-flow.url")
    String quarkusFlowUrl;

    @Override
    public Workflow descriptor() {
        return FuncWorkflowBuilder.workflow("github-stars-raffle", "quarkus.io")
                .tasks(
                        FuncDSL.http()
                        .header("Accept", "application/vnd.github.star+json")
                        .header("X-GitHub-Request-Time", "2022-11-28")
                        .GET()
                        .uri(quarkusFlowUrl),
                        function("filterByRecentStars", stargazers -> Arrays.stream(stargazers).filter(item -> {
                            LocalDateTime last1Hour = LocalDateTime.now().minusHours(1);
                            return item.starredAt().isAfter(last1Hour);
                        }).collect(Collectors.toSet()), Stargazer[].class)
                )
                .build();
    }
}
