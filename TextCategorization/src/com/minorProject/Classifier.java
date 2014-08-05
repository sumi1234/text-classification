package com.minorProject;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class Classifier<T, K> implements FeatureProbabilityInterface<T, K>
{
	private static final int INITIAL_CATEGORY_DICTIONARY_CAPACITY = 16;
	
	private static final int INITIAL_FEATURE_DICTIONARY_CAPACITY = 32;
	
	private int memoryCapacity = 1000;
	
	private Dictionary<T, Integer> totalFeatureCount;
	private Dictionary<K, Dictionary<T, Integer>> featureCountPerCategory;
	
	private Dictionary<K, Integer> totalCategoryCount;
	
	private Queue<Classification<T,K>> memoryQueue;

	public Classifier()
	{
		this.reset();
		// TODO Auto-generated constructor stub
	}

	private void reset()
	//resets learned featues and category counts
	{
		this.featureCountPerCategory = new Hashtable<K, Dictionary<T,Integer>>(Classifier.INITIAL_CATEGORY_DICTIONARY_CAPACITY);
		
		this.totalFeatureCount = new Hashtable<T, Integer>(Classifier.INITIAL_FEATURE_DICTIONARY_CAPACITY);
		
		this.totalCategoryCount = new Hashtable<K, Integer>(Classifier.INITIAL_CATEGORY_DICTIONARY_CAPACITY);
		
		this.memoryQueue = new LinkedList<Classification<T,K>>();
		
		// TODO Auto-generated method stub
		
	}
	
	public Set<T> getFeatures()
	{
		return ( (Hashtable<T, Integer>) this.totalFeatureCount).keySet();
		
	}
	
	public Set<K> getCategories()
	{
		return ((Hashtable<K, Integer>)this.totalCategoryCount).keySet();
	}
	
	public int getCategoriesTotal()
	{
		int sum = 0;
		for(Enumeration<Integer> e = this.totalCategoryCount.elements();
				e.hasMoreElements();)
		{
			sum = sum+e.nextElement();
		}
		
		return sum;
		
	}
	
	public void incrementFeature(T feature, K category)
	{
		Dictionary<T, Integer> features = this.featureCountPerCategory.get(category);
		
		if(features==null)
		{
			this.featureCountPerCategory.put(category, new Hashtable<T, Integer>(Classifier.INITIAL_CATEGORY_DICTIONARY_CAPACITY));
			
			features = this.featureCountPerCategory.get(category);
		}
		
		Integer count = features.get(feature);
		
		if(count==null)
		{
			features.put(feature, 0);
			count = features.get(feature);
			
		}
		features.put(feature, ++count);
		Integer totalCount = this.totalFeatureCount.get(feature);
		
		if(totalCount==null)
		{
			this.totalFeatureCount.put(feature,0);
			totalCount = this.totalFeatureCount.get(feature);
		}
		
		this.totalFeatureCount.put(feature, ++totalCount);
		
	}
	
	public void incrementCategory(K category)
	{
		Integer count = this.totalCategoryCount.get(category);
		if(count==null)
		{
			this.totalCategoryCount.put(category, 0);
			count = this.totalCategoryCount.get(category);
		}
		this.totalCategoryCount.put(category,++count);
	}
	
	public void decrementFeature(T feature, K category)
	{
		Dictionary<T, Integer> features = this.featureCountPerCategory.get(category);
		
		if(features == null)
			return;
		
		Integer count = features.get(feature);
		if(count==null)
			return;//no instances with that feature
		
		if(count.intValue() == 1)
		{
			features.remove(feature);
			if(features.size()==0)
				this.featureCountPerCategory.remove(category);
		}
		else
		{
			features.put(feature, --count);
		}
		
		Integer totalCount = this.totalFeatureCount.get(feature);
		if(totalCount==null)
			return;
		else if(totalCount.intValue()==1)
		{
			this.totalFeatureCount.remove(feature);
		}
		else
		{
			this.totalFeatureCount.put(feature, --totalCount);
		}
		
	}
	
	public void decrementCategory(K category)
	{
		Integer count = this.totalCategoryCount.get(category);
		if(count==null)
			return;
		else if(count.intValue()==1)
		{
			this.totalCategoryCount.remove(category);
		}
		else
		{
			this.totalCategoryCount.put(category, --count);
		}
	}
	
	public int featureCount(T feature, K category)
	{
		Dictionary<T, Integer> features = this.featureCountPerCategory.get(category);
		if(features == null)
		{
			return 0;
		}
		
		Integer count = features.get(feature);
		
		if(count==null)
			return 0;
		else
			return count;
		
	}
	
	public int categoryCount(K category)
	{
		Integer count = this.totalCategoryCount.get(category);
		if(count==null)
			return 0;
		else
			return count;
		
	}
	
	public double featureProbability(T feature, K category)
	{
		if(this.categoryCount(category)==0)
			return 0;
		return (double) this.featureCount(feature, category)/(double)this.categoryCount(category);
		
	}
	
	public double featureWeightedAverage(T feature, K category)
	{
		return this.featureWeightedAverage(feature, category, null, 1.0, 5.0);	
	}
	
	public double featureWeightedAverage(T feature, K category, FeatureProbabilityInterface<T, K> calculator)
	{
		return this.featureWeightedAverage(feature, category,calculator, 1.0, 5.0);	
	}
	
	public double featureWeightedAverage(T feature, K category, FeatureProbabilityInterface<T, K> calculator, double weight)
	{
		return this.featureWeightedAverage(feature, category, calculator, weight, 0.5);
	}
	
	
	public double featureWeightedAverage(T feature, K category, FeatureProbabilityInterface<T, K> calculator,
			double weight, double assumedProbabilty)
	{
		final double basicProbability = (calculator==null)?
										this.featureProbability(feature, category):
										calculator.featureProbability(feature, category);
		
		Integer totals = this.totalFeatureCount.get(feature);
		if(totals == null)
			totals = 0;
		
		double average = (weight*assumedProbabilty + totals*basicProbability)/(weight+totals);
		return average;
	}

	public void learn(K category, Collection<T> features)
	{
		this.learn(new Classification<T,K>(features, category));
	}
	
	public void learn(Classification<T, K> classification)
	{
		for(T feature: classification.getFeatureSet())
			this.incrementFeature(feature, classification.getCategory());
		this.incrementCategory(classification.getCategory());
		
		this.memoryQueue.offer(classification);
		if(this.memoryQueue.size()>this.memoryCapacity)
		{
			Classification<T, K> toForget = this.memoryQueue.remove();
			
			for(T feature: toForget.getFeatureSet())
			{
				this.decrementFeature(feature, toForget.getCategory());
			}
			this.decrementCategory(toForget.getCategory());
		}
		
	}
	
	public abstract Classification<T, K> classify(Collection<T> features);
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

	public int getMemoryCapacity()
	{
		return memoryCapacity;
	}

	public void setMemoryCapacity(int memoryCapacity)
	{
		this.memoryCapacity = memoryCapacity;
	}

}
