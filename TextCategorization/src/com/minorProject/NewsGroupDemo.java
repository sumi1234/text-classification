package com.minorProject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: Add a classifier evaluation including F-measure, Graphs and Confusion Matrix
public class NewsGroupDemo
{
	private static final String TESTING_DIR = "C:/Users/Sumedha/Desktop/fourNewsGroups/4news-test/";
	private static String[] CATEGORIES
    = { "soc.religion.christian",
        "talk.religion.misc",
        "alt.atheism",
        "misc.forsale" };
	public static void trainAndTestOnNewsGroupData(String directoryPath) throws IOException
	{
		int correct=0, incorrect=0;
		String classified;
		Classifier<String, String> bayes = new BayesClassifier<String, String>();
		String[] trainingFiles;
		List<List<String>> data = new ArrayList<List<String>>();
		for(int i=0;i<CATEGORIES.length;i++)
		{
			File classDir = new File(new File(directoryPath), CATEGORIES[i]);
			
			 if (!classDir.isDirectory())
			 {
	                String msg = "Could not find training directory="
	                    + classDir
	                    + "\nHave you unpacked 4 newsgroups?";
	                System.out.println(msg);
	                throw new IllegalArgumentException(msg);
			 }
			 trainingFiles = classDir.list();
			 
			 for(int j=0;j<trainingFiles.length;j++)
			 {
				 File file = new File(classDir,trainingFiles[j]);
				 
				 List<String> features = BayesExample.readTextFile(file.getAbsolutePath());
				 
				 data.add(features);
				 
				 System.out.println("Training on " + CATEGORIES[i] + "/" + trainingFiles[j]);
				 bayes.learn(CATEGORIES[i], features );
			 }
		}
		
		 System.out.println("Compiling DOne");
		 
		 
		 System.out.println("testing on "+ TESTING_DIR);
		 
		 for(int i=0;i<CATEGORIES.length;i++)
			{
				File classDir = new File(new File(directoryPath), CATEGORIES[i]);
				
				 if (!classDir.isDirectory())
				 {
		                String msg = "Could not find testing directory="
		                    + classDir
		                    + "\nHave you unpacked 4 newsgroups?";
		                System.out.println(msg);
		                throw new IllegalArgumentException(msg);
				 }
				 trainingFiles = classDir.list();
				 
				 for(int j=0;j<trainingFiles.length;j++)
				 {
					 File file = new File(classDir,trainingFiles[j]);
					 
					 List<String> features = BayesExample.readTextFile(file.getAbsolutePath());
					 
					 //data.add(features);
					 classified = bayes.classify(features).getCategory();
					 if(CATEGORIES[i].equalsIgnoreCase(classified))
						 correct++;
					else
						incorrect++;
					 System.out.println("Testing on " + CATEGORIES[i] + "/" + trainingFiles[j]);
					 System.out.println("Classified category for this is " + classified);
				 }
			}
		 
		 System.out.println("Evaluation ======================");
		 System.out.println("Correct : " + correct + " Incorrect   " + incorrect);
		 System.out.println("Accuracy : "+ 100.0 * (double)(correct)/(double)(correct+ incorrect));
			
		
		
	}

	public NewsGroupDemo()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		trainAndTestOnNewsGroupData("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/");
		// TODO Auto-generated method stub

	}

}
