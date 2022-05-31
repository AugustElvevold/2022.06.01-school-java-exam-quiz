package com.kristiania;

import static java.lang.String.format;

public record UserScore(String username, int score, int time, String topic) {
    @Override
    public String toString() {
        return format("%-20s %5d %5d%s", username, score, time, "s");
    }

    public String toStringWithTopic() {
        return format("%-20s %5d %5ds %s", username, score, time, topic);
    }
}
