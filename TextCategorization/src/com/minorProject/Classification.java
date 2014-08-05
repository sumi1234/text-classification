package com.minorProject;

import java.util.Collection;

public class Classification<T, K>//t is the feature class, k is the category class
{
	private Collection<T> featureSet;
	
	private K category;
	
	private double probability;
	
	public Classification()
	{
		// TODO Auto-generated constructor stub
	}
	
	public Classification(Collection<T> featureSet, K category)
	{
		this(featureSet, category, 1.0);
	}

	public Classification(Collection<T> featureSet, K category, double probability)
	{
		this.setFeatureSet(featureSet);
		this.setCategory(category);
		this.setProbability(probability);
		// TODO Auto-generated constructor stub
	}
	
	public String toString()
	{
		return "Classification [category = " + this.category
				+ " , probabilty = " + this.probability + ", featurset = " +
				featureSet + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

	public Collection<T> getFeatureSet()
	{
		return featureSet;
	}

	public void setFeatureSet(Collection<T> featureSet)
	{
		this.featureSet = featureSet;
	}

	public K getCategory()
	{
		return category;
	}

	public void setCategory(K category)
	{
		this.category = category;
	}

	public double getProbability()
	{
		return probability;
	}

	public void setProbability(double probability)
	{
		this.probability = probability;
	}

}
