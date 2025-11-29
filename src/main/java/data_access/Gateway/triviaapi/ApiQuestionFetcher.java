package data_access.Gateway.triviaapi;

import entity.Question;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiQuestionFetcher implements QuestionFetcher {

  private final OkHttpClient client = new OkHttpClient();
  private static final String API_URL = "https://opentdb.com/api.php";
  private static final int SUCCESS_CODE = 0;
  private static final String RESULTS = "results";

  // @Override
  // public List<Question> getQuestions(String category,
  // String difficulty,
  // String type,
  // int numQuestions) throws QuestionNotFoundException {
  //
  // String url = buildTriviaUrl(category, difficulty, type, numQuestions);
  //
  // Request request = new Request.Builder()
  // .url(url)
  // .get()
  // .build();
  //
  // try (Response response = client.newCall(request).execute()) {
  // if (!response.isSuccessful()) {
  // throw new QuestionNotFoundException();
  // }
  //
  // String bodyString = response.body().string();
  // return parseQuestionsFromJson(bodyString);
  //
  // } catch (IOException | JSONException e) {
  // throw new QuestionNotFoundException();
  // }
  // }

  @Override
  public List<Question> getQuestions(String category, String difficulty, String type, int numQuestions)
      throws QuestionNotFoundException {

    String url = buildTriviaUrl(category, difficulty, type, numQuestions);
    System.out.println("DEBUG - ApiQuestionFetcher.getQuestions URL = " + url);

    Request request = new Request.Builder().url(url).get().build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        System.out
            .println("DEBUG - HTTP not successful. Code: " + response.code() + ", message: " + response.message());
        throw new QuestionNotFoundException();
      }

      String bodyString = response.body().string();
      System.out.println("DEBUG - Raw JSON from API: " + bodyString);

      return parseQuestionsFromJson(bodyString);

    } catch (IOException | JSONException e) {
      System.out.println("DEBUG - Exception while calling API or parsing JSON:");
      e.printStackTrace();
      System.out.println("DEBUG - URL was: " + url);
      throw new QuestionNotFoundException();
    }
  }

  List<Question> parseQuestionsFromJson(String json) throws JSONException {
    JSONObject responseBody = new JSONObject(json);

    int responseCode = responseBody.getInt("response_code");
    if (responseCode != SUCCESS_CODE) {
      System.out.println("DEBUG - OpenTDB response_code != 0. response_code = " + responseCode);
      System.out.println("DEBUG - Full JSON: " + json);
      throw new JSONException("Non-success response_code");
    }

    JSONArray questionsArray = responseBody.getJSONArray(RESULTS);
    List<Question> questions = new ArrayList<>();

    for (int i = 0; i < questionsArray.length(); i++) {
      JSONObject obj = questionsArray.getJSONObject(i);

      String qType = obj.getString("type");
      String questionText = decodeHtmlEntities(obj.getString("question"));
      String correctAnswer = decodeHtmlEntities(obj.getString("correct_answer"));
      JSONArray incorrect = obj.getJSONArray("incorrect_answers");

      ArrayList<String> choices = new ArrayList<>();
      int correctIndex;

      if ("boolean".equals(qType)) {
        // True/False questions - don't shuffle, standard order
        choices.add("True");
        choices.add("False");
        correctIndex = "True".equals(correctAnswer) ? 0 : 1;
      } else {
        // Multiple choice - add all choices, then shuffle
        choices.add(correctAnswer);
        for (int j = 0; j < incorrect.length(); j++) {
          choices.add(decodeHtmlEntities(incorrect.getString(j)));
        }

        // Shuffle and get new correct index
        correctIndex = shuffleChoices(choices);
      }

      int scoreValue = computeScore(obj.getString("difficulty"));

      Question question = new Question(qType, questionText, choices, correctIndex, scoreValue);
      questions.add(question);
    }

    return questions;
  }

  private int computeScore(String difficulty) {
    // simple example: scale by difficulty
    switch (difficulty) {
      case "easy":
        return 100;
      case "medium":
        return 200;
      case "hard":
        return 300;
      default:
        return 1;
    }
  }

  public String buildTriviaUrl(String category, String difficulty, String type, int numQuestions) {
    return API_URL + "?amount=" + numQuestions + "&category=" + category + // category should be an ID string like "9",
                                                                           // "10", "31", ...
        "&difficulty=" + difficulty + // "easy", "medium", "hard"
        "&type=" + type; // "multiple" or "boolean"
  }

  /**
   * Shuffles the choices list and returns the new index of the correct answer
   * (first element)
   */
  private int shuffleChoices(ArrayList<String> choices) {
    if (choices.size() <= 1) {
      return 0;
    }

    // Store the correct answer (first element)
    String correctAnswer = choices.get(0);

    // Shuffle the list
    java.util.Collections.shuffle(choices);

    // Find the new index of the correct answer
    return choices.indexOf(correctAnswer);
  }

  /**
   * Decodes HTML entities to normal characters
   */
  private String decodeHtmlEntities(String text) {
    return text.replace("&quot;", "\"").replace("&#039;", "'").replace("&amp;", "&").replace("&lt;", "<")
        .replace("&gt;", ">").replace("&apos;", "'").replace("&rsquo;", "'").replace("&lsquo;", "'")
        .replace("&rdquo;", "\"").replace("&ldquo;", "\"");
  }
}
