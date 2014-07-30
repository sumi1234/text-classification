package com.minorProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class BayesExample
{
	
	public static void trainOnNewsGroupData(String directoryPath)
	{
		
		
	}
	
	public static List<String> readTextFile(String file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<String> tokens = new ArrayList<String>();
		String[] in = new String[1000];
		String input;
		while((input=reader.readLine())!=null)
		{
			in = input.split("\\s");
			tokens.addAll(Arrays.asList(in));
		}
		
		reader.close();
		
		//return tokens.toArray(new String[tokens.size()]);
		return tokens;
	}
	
	

	public BayesExample()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		Classifier<String, String> bayes = new BayesClassifier<String, String>();
		
		// TODO Auto-generated method stub
//		final String[] positiveText = "I love sunny days".split("\\s");
//		bayes.learn("positive", Arrays.asList(positiveText));
//		
//		final String[] negativeText = "I hate rain".split("\\s");
//		bayes.learn("negative", Arrays.asList(negativeText));
//		
//		
//		final String[] ut1 = "today is a sunny day".split("\\s");
//		final String[] ut2 = "there will be rain".split("\\s");
//		
//		System.out.println(bayes.classify(Arrays.asList(ut1)).getCategory());
//		System.out.println(bayes.classify(Arrays.asList(ut2)).getCategory());
		
		//now train data on 4 news group
		
		final List<String> alt_atheism = readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/alt.atheism/53302");
		bayes.learn("alt.atheism", alt_atheism );
		bayes.learn("alt.atheism", readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/alt.atheism/53319"));
		bayes.learn("alt.atheism", readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/alt.atheism/53327"));
		bayes.learn("alt.atheism", readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/alt.atheism/53328"));

		final List<String> misc_forsale = readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/misc.forsale/74756");
		bayes.learn("misc.forsale", misc_forsale );
		
		final List<String> misc_forsale_test = readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/misc.forsale/74759");
		bayes.learn("misc.forsale", misc_forsale_test);
		System.out.println(bayes.classify(readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/misc.forsale/74758")).getCategory());
		
		System.out.println(bayes.classify(readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/alt.atheism/53339")).getCategory());
//		((BayesClassifier<String, String>)bayes).classifyDetailed(readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/misc.forsale/74758"));
		
		System.out.println(((BayesClassifier<String, String>) bayes).classifyDetailed(readTextFile("C:/Users/Sumedha/Desktop/fourNewsGroups/4news-train/misc.forsale/74758")));
//		System.out.println(cl.toString());
		bayes.setMemoryCapacity(500);
	}

}
