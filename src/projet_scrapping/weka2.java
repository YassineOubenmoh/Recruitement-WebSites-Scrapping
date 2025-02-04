package projet_scrapping;

import weka.core.converters.CSVLoader;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.Randomize;
import weka.core.stopwords.AbstractFileBasedStopwords;
import weka.core.tokenizers.WordTokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class weka2 {

    public static void main(String[] args) throws Exception {
        // Remove commas from the CSV file
        removeCommasFromCSV("C:\\Users\\apple\\Desktop\\emploiFile.csv", "emploi_no_commas.csv");

        // Load the modified dataset
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("emploi_no_commas.csv"));
        Instances dataset = loader.getDataSet();

        //This line of code is responsible for setting the class index of the dataset.
        // In Weka, the class index is the index of the attribute that represents the class
        // or the target variable.
        //this part of the code sets the class index to the index of the "niveauEtude" attribute.
        if (dataset.classIndex() == -1) {
            dataset.setClassIndex(dataset.attribute("niveaudetude").index());
        }

        // Create a list to store indices of instances to be deleted
        ArrayList<Integer> indicesToDelete = new ArrayList<>();

        // Iterate through each instance to check for missing values
        for (int i = 0; i < dataset.numInstances(); i++) {
            if (dataset.instance(i).hasMissingValue()) {
                indicesToDelete.add(i);
            }
        }

        // Delete instances with missing values
        for (int i = indicesToDelete.size() - 1; i >= 0; i--) {
            dataset.delete(indicesToDelete.get(i));
        }

        // Convert nominal attributes to string
        //This conversion might be necessary for certain machine 
        //learning algorithms that require input data to be in a specific format
        NominalToString nominalToStringFilter = new NominalToString();
        nominalToStringFilter.setInputFormat(dataset);
        Instances filteredData = Filter.useFilter(dataset, nominalToStringFilter);

        // Apply StringToWordVector filter
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(filteredData);
        filter.setWordsToKeep(1800); // You can experiment with this value
        filter.setDoNotOperateOnPerClassBasis(false); // Set to true if you want global word frequencies
        filter.setOutputWordCounts(true); // Display the number of occurrences of a word/feature
        filter.setTFTransform(true); // Set the term frequency transform
        filter.setIDFTransform(true); // Set the inverse document frequency transform
        Instances vectorizedDataset = Filter.useFilter(filteredData, filter);

        // Set stopwords if needed
        AbstractFileBasedStopwords stopwords = new AbstractFileBasedStopwords() {
            @Override
            public String stopwordsTipText() {
                return null;
            }

            @Override
            public String globalInfo() {
                return null;
            }

            @Override
            protected boolean is(String s) {
                return false;
            }
        };

        // Set tokenizer
        //Tokenization is the process of breaking a text into individual units called tokens.
        //preparing text data for analysis or machine learning.
        WordTokenizer wordTokenizer = new WordTokenizer();
        filter.setTokenizer(wordTokenizer);

        // Set minimum term frequency and periodic pruning if needed
        filter.setMinTermFreq(3);
        filter.setPeriodicPruning(2);

        // Save the vectorized dataset to a file (optional)
        instancesToCSV(vectorizedDataset, "vectorized_dataset.csv");

        // Split the data into train and test splits
        //Randomize randomize = new Randomize();
        //randomize.setSeed(42);
        //randomize.setInputFormat(vectorizedDataset);
        //Instances randomizedData = Filter.useFilter(vectorizedDataset, randomize);

        Randomize randomize = new Randomize();
        randomize.setInputFormat(vectorizedDataset);
        randomize.setRandomSeed(42); // Set the seed for randomization
        Instances randomizedData = Filter.useFilter(vectorizedDataset, randomize);
        
        
        int trainSize = (int) Math.round(randomizedData.numInstances() * 0.7);
        int testSize = randomizedData.numInstances() - trainSize;

        Instances trainingData = new Instances(randomizedData, 0, trainSize);
        Instances testData = new Instances(randomizedData, trainSize, testSize);

        // Train a naive bayes classifier
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(trainingData);

        String modelFilePath = "NaiveBayes_model.model";

        // Save the trained naive bayes classifier to a file
        SerializationHelper.write(modelFilePath, nb);

        // Evaluate the classifier using cross-validation
        Evaluation eval = new Evaluation(trainingData);
        Random randomSeed = new Random(42);
        int folds = 5;
        eval.crossValidateModel(nb, trainingData, folds, randomSeed);
        System.out.println(eval.toSummaryString("Cross-Validated Evaluation Metrics: \n", false));

        // Evaluate the classifier on test data
        Evaluation eval1 = new Evaluation(trainingData);
        eval1.evaluateModel(nb, testData);
        System.out.println(eval1.toSummaryString());

        // Print the confusion matrix
        Evaluation eval2 = new Evaluation(trainingData);
        eval2.evaluateModel(nb, testData);
        System.out.println("Confusion Matrix: \n");
        System.out.println(eval2.toMatrixString());
    }

    // Helper method to save instances to a CSV file
    public static void instancesToCSV(Instances data, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);

        // Write header
        for (int i = 0; i < data.numAttributes(); i++) {
            writer.append(data.attribute(i).name());
            if (i < data.numAttributes() - 1) {
                writer.append(",");
            }
        }
        writer.append("\n");

        // Write data
        for (int j = 0; j < data.numInstances(); j++) {
            for (int i = 0; i < data.numAttributes(); i++) {
                writer.append(String.valueOf(data.instance(j).value(i)));
                if (i < data.numAttributes() - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");
        }

        writer.flush();
        writer.close();
    }

    // Helper method to remove commas from a CSV file, excluding those within quotes
    public static void removeCommasFromCSV(String inputFilePath, String outputFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        String line;
        while ((line = reader.readLine()) != null) {
            // Remove commas from the line, excluding those within quotes
            String[] parts = line.split(",(?=([^\"]\"[^\"]\")[^\"]$)");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].replaceAll(",", "");
            }

            // Join the modified parts and write to the new CSV file
            String lineWithoutCommas = String.join(",", parts);
            writer.write(lineWithoutCommas);
            writer.newLine();
        }

        reader.close();
        writer.close();
    }
}