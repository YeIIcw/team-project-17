package data_access.Gateway.triviaapi;

import entity.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiQuestionFetcherTest {

    @Test
    void testFetchesQuestionsFromOpenTdb() {
        ApiQuestionFetcher fetcher = new ApiQuestionFetcher();

        String url = fetcher.buildTriviaUrl("9", "easy", "multiple", 10);
        System.out.println("DEBUG - Calling URL: " + url);

        try {
            List<Question> questions = fetcher.getQuestions("9", "easy", "multiple", 10);

            assertNotNull(questions, "Questions list should not be null");
            assertFalse(questions.isEmpty(), "Questions list should not be empty");
            assertEquals(10, questions.size(), "Should return the requested number of questions");

            for (Question q : questions) {
                assertNotNull(q.getText(), "Question text should not be null");
                assertFalse(q.getText().isEmpty(), "Question text should not be empty");
                assertNotNull(q.getChoices(), "Choices should not be null");
                assertTrue(q.getChoices().size() >= 2, "Should have at least 2 choices");
            }

        } catch (QuestionFetcher.QuestionNotFoundException e) {
            // If we hit 429 or other external issue, SKIP instead of failing
            Assumptions.assumeTrue(false,
                    "OpenTDB not reachable / rate-limited (e.g. HTTP 429). " +
                            "Check DEBUG logs if you need more detail.");
        }
    }
}
