package data_access.Gateway.triviaapi;

import entity.Question;

import java.util.List;

/**
 * Interface for the service of getting questions for a trivia game.
 */
public interface QuestionFetcher {

    /**
     * @param category the category of the questions
     * @param difficulty the difficulty of the questions
     * @param type the type of the questions
     * @param numQuestions the number of the questions
     * @return list of sub breeds for the given breed
     * @throws QuestionNotFoundException if the breed does not exist
     */
    List<Question> getQuestions(String category, String difficulty, String type, int numQuestions) throws QuestionNotFoundException;

    // a class defined in an interface is public AND static
            // extends RuntimeException
    class QuestionNotFoundException extends Exception {
        public QuestionNotFoundException () {
            super("Questions not found");
        }
    }
}