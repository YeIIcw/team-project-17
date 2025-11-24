package data_access.Gateway.triviaapi;

import entity.Question;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Fake QuestionFetcher for testing - reads questions from JSON file that mimics API response
 */
public class FakeQuestionFetcher implements QuestionFetcher {

    private static final String JSON_FILE = "/fake_questions.json";
    private static final int SUCCESS_CODE = 0;
    private static final String RESULTS = "results";

    @Override
    public List<Question> getQuestions(String category, String difficulty, String type, int numQuestions) 
            throws QuestionNotFoundException {
        
        System.out.println("DEBUG: FakeQuestionFetcher - Loading " + numQuestions + " questions from JSON");
        System.out.println("  Category: " + category + ", Difficulty: " + difficulty + ", Type: " + type);
        
        try {
            // Read JSON file from resources
            InputStream inputStream = getClass().getResourceAsStream(JSON_FILE);
            if (inputStream == null) {
                System.out.println("ERROR: FakeQuestionFetcher - Could not find " + JSON_FILE + " in resources");
                System.out.println("DEBUG: FakeQuestionFetcher - Trying alternative path...");
                // Try alternative path
                inputStream = FakeQuestionFetcher.class.getClassLoader().getResourceAsStream("fake_questions.json");
                if (inputStream == null) {
                    System.out.println("ERROR: FakeQuestionFetcher - Could not find JSON file in classpath");
                    throw new QuestionNotFoundException();
                }
            }
            
            String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("DEBUG: FakeQuestionFetcher - Loaded JSON file (" + jsonContent.length() + " chars)");
            inputStream.close();
            
            // Parse JSON using the same logic as ApiQuestionFetcher
            List<Question> questions = parseQuestionsFromJson(jsonContent);
            
            // Filter by difficulty and type, then limit to numQuestions
            List<Question> filteredQuestions = new ArrayList<>();
            for (Question q : questions) {
                // Match type (accept all types for now, or filter if needed)
                boolean matchesType = type == null || type.isEmpty() || 
                    (type.equals("multiple") && q.getType().equals("multiple")) ||
                    (type.equals("boolean") && q.getType().equals("boolean")) ||
                    (type.equals("Multiple Choice") && q.getType().equals("multiple")) ||
                    (type.equals("True/False") && q.getType().equals("boolean"));
                
                if (matchesType && filteredQuestions.size() < numQuestions) {
                    filteredQuestions.add(q);
                }
            }
            
            // If we don't have enough questions, duplicate some
            if (filteredQuestions.isEmpty()) {
                // If no matches, just take first N questions
                filteredQuestions = questions.subList(0, Math.min(numQuestions, questions.size()));
            } else {
                while (filteredQuestions.size() < numQuestions && !questions.isEmpty()) {
                    int index = filteredQuestions.size() % questions.size();
                    filteredQuestions.add(questions.get(index));
                }
            }
            
            // Limit to requested number
            if (filteredQuestions.size() > numQuestions) {
                filteredQuestions = filteredQuestions.subList(0, numQuestions);
            }
            
            System.out.println("DEBUG: FakeQuestionFetcher - Returning " + filteredQuestions.size() + " questions");
            return filteredQuestions;
            
        } catch (IOException | JSONException e) {
            System.out.println("ERROR: FakeQuestionFetcher - Error reading JSON: " + e.getMessage());
            e.printStackTrace();
            throw new QuestionNotFoundException();
        }
    }

    // Reuse the parsing logic from ApiQuestionFetcher
    private List<Question> parseQuestionsFromJson(String json) throws JSONException {
        JSONObject responseBody = new JSONObject(json);

        int responseCode = responseBody.getInt("response_code");
        if (responseCode != SUCCESS_CODE) {
            System.out.println("DEBUG: FakeQuestionFetcher - response_code != 0. response_code = " + responseCode);
            throw new JSONException("Non-success response_code");
        }

        JSONArray questionsArray = responseBody.getJSONArray(RESULTS);
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject obj = questionsArray.getJSONObject(i);

            String qType = obj.getString("type");        // "multiple" or "boolean"
            String questionText = obj.getString("question");
            String correctAnswer = obj.getString("correct_answer");
            JSONArray incorrect = obj.getJSONArray("incorrect_answers");

            ArrayList<String> choices = new ArrayList<>();
            int correctIndex;

            if ("boolean".equals(qType)) {
                choices.add("True");
                choices.add("False");
                correctIndex = "True".equals(correctAnswer) ? 0 : 1;
            } else {
                // Add all choices first
                choices.add(correctAnswer);
                for (int j = 0; j < incorrect.length(); j++) {
                    choices.add(incorrect.getString(j));
                }
                
                // Shuffle the choices and track where the correct answer ends up
                correctIndex = shuffleChoices(choices);
            }

            int scoreValue = computeScore(obj.getString("difficulty"));

            Question question = new Question(qType, questionText, choices, correctIndex, scoreValue);
            questions.add(question);
        }

        return questions;
    }

    private int computeScore(String difficulty) {
        switch (difficulty) {
            case "easy":
                return 100;
            case "medium":
                return 200;
            case "hard":
                return 300;
            default:
                return 100;
        }
    }

    /**
     * Shuffles the choices list and returns the new index of the correct answer (first element)
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
}

