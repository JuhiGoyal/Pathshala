package com.pathshala.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ExternalApiClient {

    private final String GEMINI_API_KEY = "AIzaSyCOO_PFTRWFtTfhJjaPVzUoMmumbu4-Czs";
    private final String PLAGIARISM_API_KEY = "0I0wG3TO7r6oxwv7D6MTNzBk4J5MGY0c";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> generateQuestionsForCourse(Integer courseId, int count) {
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.startsWith("YOUR")) {
            log.warn("Gemini API key not provided; using fallback dummy questions");
            return generateDummyQuestions(courseId, count);
        }
        return generateDummyQuestions(courseId, count); // replace with real API call
    }

    private List<String> generateDummyQuestions(Integer courseId, int count) {
        List<String> out = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            out.add("Q" + i + " for course " + courseId + " — Explain concept " + i + " in detail.");
        }
        return out;
    }

    public double checkPlagiarism(String answerText) {
        if (answerText == null || answerText.trim().isEmpty()) return 0.0;
        if (PLAGIARISM_API_KEY == null || PLAGIARISM_API_KEY.startsWith("YOUR")) {
            double score = (answerText.hashCode() & 0xff) % 30;
            return Math.max(0.0, Math.min(100.0, score));
        }
        return 5.0; // placeholder
    }
}
