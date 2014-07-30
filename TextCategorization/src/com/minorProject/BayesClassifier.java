package com.minorProject;
//Java-naive-bayes-classsifier github
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class BayesClassifier<T, K> extends Classifier<T, K>
{

	/**
	 * @param args
	 */
	
	private double featuresProbabilityProduct(Collection<T> features, K category)
	{
		double product = 1.0;
		for(T feature : features)
			product = product * this.featureWeightedAverage(feature, category);
		return product;
	}
	
	private double categoryProbability(Collection<T> features, K category)
	{
		double prob = (featuresProbabilityProduct(features, category))*((double)this.categoryCount(category)/(double)this.getCategoriesTotal());
		return prob;
	}
	
	private SortedSet<Classification<T, K>> categoryProbabilities(Collection<T> features)
	{
		SortedSet<Classification<T, K>> probabilities = new TreeSet<Classification<T,K>>
		(
				new Comparator<Classification<T, K>>()
				{
					public int compare(Classification<T,K> o1, Classification<T, K> o2)
					{
						int temp = Double.compare(o1.getProbability(), o2.getProbability());
						
						if((temp == 0) && (!o1.getCategory().equals(o2.getCategory())))
							temp = -1;
						return temp;
					}
						
				}
				);
		
		for(K category: this.getCategories())
			probabilities.add(new Classification<T, K>
			(
				features, category,this.categoryProbability(features, category)
			));
		
		return probabilities;
	}
	
	@Override
	public Classification<T, K> classify(Collection<T> features)
	{
		SortedSet<Classification<T,K>> probabilities = this.categoryProbabilities(features);
		
		if(probabilities.size()>0)
			return probabilities.last();
		else
			return null;
	}
	
		
	public Collection<Classification<T, K>> classifyDetailed(Collection<T> features)
	{
		return this.categoryProbabilities(features);
	}
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
