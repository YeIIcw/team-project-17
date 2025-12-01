package use_case.levelup;

public class LevelUpInputData {

    private final String statToIncrease; //"damage" or "health"

    public LevelUpInputData(String statToIncrease) {
        this.statToIncrease = statToIncrease;
    }

    public String getStatToIncrease() {
        return statToIncrease;
    }

}
