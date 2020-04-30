public interface DatasetAnalysis {
    void startEpoch();
    void analyzeLayer(float[] aLayer, float[] correctOutput);
    void printResults();
    void saveToFile(String aFileName);
    String appendToReadMe();

}
