package data_access.Gateway.triviaapi;

import entity.Question;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiQuestionFetcherParsingTest {

  @Test
  void parsesBooleanQuestionCorrectly() throws JSONException {
    ApiQuestionFetcher fetcher = new ApiQuestionFetcher();

    String json = "{\n" + "  \"response_code\": 0,\n" + "  \"results\": [\n" + "    {\n"
        + "      \"type\": \"boolean\",\n" + "      \"difficulty\": \"easy\",\n"
        + "      \"category\": \"General Knowledge\",\n" + "      \"question\": \"Pluto is a planet.\",\n"
        + "      \"correct_answer\": \"False\",\n" + "      \"incorrect_answers\": [\"True\"]\n" + "    }\n" + "  ]\n"
        + "}";

    List<Question> questions = fetcher.parseQuestionsFromJson(json);

    assertEquals(1, questions.size());
    Question q = questions.get(0);

    assertEquals("boolean", q.getType());
    assertEquals("Pluto is a planet.", q.getText());

    // Boolean questions should be ["True", "False"]
    assertEquals(2, q.getChoices().size());
    assertEquals("True", q.getChoices().get(0));
    assertEquals("False", q.getChoices().get(1));

    // Correct answer is "False" -> index 1
    assertEquals(1, q.getCorrectChoiceIndex());

    // Difficulty "easy" -> score 1 (per computeScore)
    assertEquals(100, q.getScoreValue());
  }

  @Test
  void parsesMultipleChoiceQuestionCorrectly() throws JSONException {
    ApiQuestionFetcher fetcher = new ApiQuestionFetcher();

    String json = "{\n" + "  \"response_code\": 0,\n" + "  \"results\": [\n" + "    {\n"
        + "      \"type\": \"multiple\",\n" + "      \"difficulty\": \"medium\",\n"
        + "      \"category\": \"Entertainment: Japanese Anime & Manga\",\n"
        + "      \"question\": \"Who wrote and directed 'Spirited Away'?\",\n"
        + "      \"correct_answer\": \"Hayao Miyazaki\",\n" + "      \"incorrect_answers\": [\n"
        + "        \"Isao Takahata\",\n" + "        \"Mamoru Hosoda\",\n" + "        \"Hidetaka Miyazaki\"\n"
        + "      ]\n" + "    }\n" + "  ]\n" + "}";

    List<Question> questions = fetcher.parseQuestionsFromJson(json);

    assertEquals(1, questions.size());
    Question q = questions.get(0);

    assertEquals("multiple", q.getType());
    assertEquals("Who wrote and directed 'Spirited Away'?", q.getText());

    // multiple: correct first, then incorrects
    assertEquals(4, q.getChoices().size());
    assertEquals("Hayao Miyazaki", q.getChoices().get(0));
    assertEquals("Isao Takahata", q.getChoices().get(1));
    assertEquals("Mamoru Hosoda", q.getChoices().get(2));
    assertEquals("Hidetaka Miyazaki", q.getChoices().get(3));

    // correct at index 0
    assertEquals(0, q.getCorrectChoiceIndex());

    // Difficulty "medium" -> score 2
    assertEquals(200, q.getScoreValue());
  }
}
