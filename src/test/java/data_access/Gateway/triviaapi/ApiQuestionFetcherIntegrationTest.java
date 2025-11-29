package data_access.Gateway.triviaapi;

import entity.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiQuestionFetcherIntegrationTest {

  @Test
  void apiCallReturnsTenQuestions() throws Exception {
    ApiQuestionFetcher fetcher = new ApiQuestionFetcher();

    // category 9 = General Knowledge
    List<Question> questions = fetcher.getQuestions("9", "easy", "multiple", 10);

    assertNotNull(questions, "API returned null list");
    assertEquals(10, questions.size(), "API did not return exactly 10 questions");

    for (Question q : questions) {
      assertNotNull(q.getText(), "Question text missing");
      assertNotNull(q.getType(), "Type missing");
      assertTrue(q.getChoices().size() >= 2, "Choices missing");
    }
  }
}
