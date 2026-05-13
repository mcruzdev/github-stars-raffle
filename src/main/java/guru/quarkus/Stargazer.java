package guru.quarkus;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record Stargazer(User user, @JsonProperty("starred_at") LocalDateTime starredAt) {
    public record User(
            String url,
            @JsonProperty("avatar_url") String avatarUrl) {
    }
}
