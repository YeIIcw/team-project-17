package data_access.Gateway.triviaapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class ApiQuestionFetcher implements QuestionFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://opentdb.com/api.php";
    private static final int SUCCESS_CODE = 0;
    private static final String RESULTS = "results";

    /**
     * @param category the category of the questions
     * @param difficulty the difficulty of the questions
     * @param type the type of the questions
     * @param numQuestions the number of the questions
     * @return list of sub breeds for the given breed
     * @throws QuestionNotFoundException if the breed does not exist
     */
    @Override
    public List<String> getQuestions(String category, String difficulty, String type, int numQuestions) throws QuestionNotFoundException {
        final Request request = new Request.Builder()
                .url(String.format(buildTriviaUrl(category, difficulty, type, numQuestions)))
                .method("GET", null)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            // string.equals()
            // int == int
            if (responseBody.getString("response_code").equals(SUCCESS_CODE)) {
                final JSONArray questionsArray = responseBody.getJSONArray(RESULTS);
                final List<String> questions = new ArrayList<>();

                for (int i = 0; i < questionsArray.length(); i++) {
                    questions.add(questionsArray.getString(i));
                };

                return questions;
            }
            else {
                // But if the error is already a BreedNotFoundException,
                // rethrowing a new one just loses the stack trace and adds no value.
                throw new QuestionNotFoundException();
            }
        }
        // | BNFException no need
        catch (IOException | JSONException event) {
            throw new QuestionNotFoundException();
        }

    }

    public String buildTriviaUrl(String category, String difficulty, String type, int numQuestions) {
        String baseUrl = API_URL;

        return baseUrl +
                "?amount=" + numQuestions +
                "&category=" + category +
                "&difficulty=" + difficulty +
                "&type=" + type;
    }

}



