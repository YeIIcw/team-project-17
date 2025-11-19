package data_access.Gateway.triviaapi;

import java.util.List;

/**
 * A minimal implementation of the BreedFetcher interface for testing purposes.
 * To avoid excessive calls to the real API, we can primarily test with a local
 * implementation that demonstrates the basic functionality of the interface.
 */
public class QuestionFetcherForLocalTesting implements QuestionFetcher {
    private int callCount = 0;

    @Override
    public List<String> getSubBreeds(String breed) throws QuestionNotFoundException {
        callCount++;
        if ("hound".equalsIgnoreCase(breed)) {
            return List.of("afghan", "basset");
        }
        throw new QuestionNotFoundException();
    }

    public int getCallCount() {
        return callCount;
    }
}
