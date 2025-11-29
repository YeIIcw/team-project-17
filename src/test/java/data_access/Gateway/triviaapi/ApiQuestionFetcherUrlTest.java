package data_access.Gateway.triviaapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiQuestionFetcherUrlTest {

  @Test
  void buildTriviaUrlBuildsExpectedString() {
    ApiQuestionFetcher fetcher = new ApiQuestionFetcher();

    String url = fetcher.buildTriviaUrl("9", "easy", "multiple", 10);

    String expected = "https://opentdb.com/api.php" + "?amount=10" + "&category=9" + "&difficulty=easy"
        + "&type=multiple";

    assertEquals(expected, url);
  }
}
