package interface_adapter.Preferences;

public class PreferencesViewModel {

    private String category;
    private String difficulty;
    private String type;
    private int numQuestions;
    private boolean success;
    private String message;

    public String getCategory()   { return category; }
    public String getDifficulty() { return difficulty; }
    public String getType()       { return type; }
    public int getNumQuestions()  { return numQuestions; }
    public boolean isSuccess()    { return success; }
    public String getMessage()    { return message; }

    public void setCategory(String category)       { this.category = category; }
    public void setDifficulty(String difficulty)   { this.difficulty = difficulty; }
    public void setType(String type)               { this.type = type; }
    public void setNumQuestions(int numQuestions)  { this.numQuestions = numQuestions; }
    public void setSuccess(boolean success)        { this.success = success; }
    public void setMessage(String message)         { this.message = message; }
}
