package com.yahoo.basava.testng.reporters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlSuite;

public class SummaryReporter implements IReporter
{
	private String fileName = "briefSummary.properties";
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) 
	{
		int passed = 0, totalPassedMethods = 0, failed = 0, totalFailedMethods = 0, skipped = 0, totalSkippedMethods = 0, total = 0;
		StringBuffer includedGroupsBuf = new StringBuffer();
		StringBuffer excludedGroupsBuf = new StringBuffer();
		for ( ISuite su : suites )
		{
			for (ISuiteResult sr : su.getResults().values()) 
			{
		        ITestContext testContext = sr.getTestContext();
		        passed += testContext.getPassedTests().size();
		        failed += testContext.getFailedTests().size();
		        skipped += testContext.getSkippedTests().size();
		        
		        String[] includedGroups = testContext.getIncludedGroups();
		        String[] excludedGroups = testContext.getExcludedGroups();
		        for ( String s : includedGroups )
		        {
		        	includedGroupsBuf.append(s).append(",");
		        }
		        for ( String s : excludedGroups )
		        {
		        	excludedGroupsBuf.append(s).append(",");
		        }
		        
		        IResultMap passedTests = testContext.getPassedTests();
				int passedMethods = getMethodSet(passedTests).size();
				totalPassedMethods += passedMethods;
				
				int failedMethods = getMethodSet(testContext.getFailedTests()).size();
				totalFailedMethods += failedMethods;
				
				int skippedMethods = getMethodSet(testContext.getSkippedTests()).size();
				totalSkippedMethods += skippedMethods;
		    }
		}
		total = passed + failed + skipped ;
		
		
		
		StringBuffer buf  = new StringBuffer();
		buf.append("IncludedGroups = " + includedGroupsBuf.toString()).append("\n");
		buf.append("ExcludedGroups = " + excludedGroupsBuf.toString()).append("\n\n");
				
		buf.append("passed=" + passed + "\n");
		buf.append("failed=" + failed + "\n");
		buf.append("skipped="+ skipped + "\n");
		buf.append("total="+total + "\n\n");
		
		buf.append("passedMethods=" + totalPassedMethods + "\n");
		buf.append("failedMethods=" + totalFailedMethods + "\n");
		buf.append("skippedMethods=" + totalSkippedMethods + "\n");
		buf.append("totalMethodCount=" + ( totalPassedMethods + totalFailedMethods + totalSkippedMethods ) + "\n");
		
		float overallFailPercentage = 0, methodFailPercentage = 0;
		overallFailPercentage = ( ( ( failed + skipped )* 100 )/ total );
		methodFailPercentage = ( ( totalFailedMethods + totalSkippedMethods ) * 100 ) / ( totalPassedMethods + totalFailedMethods + totalSkippedMethods );

		buf.append("OverallFailurePercentage = " + overallFailPercentage ).append("\n");
		buf.append("MethodFailurePercentage = " + methodFailPercentage ).append("\n"); 
		
		
		writeToFile( outputDirectory + "/" + fileName, buf.toString() );
	}

	private Collection<ITestNGMethod> getMethodSet(IResultMap tests) {
	    Set<ITestNGMethod> r = new TreeSet<ITestNGMethod>(new TestSorter<ITestNGMethod>());
	    r.addAll(tests.getAllMethods());
	    return r;
	}	
	
	private class TestSorter<T extends ITestNGMethod> implements Comparator<T> {
		/** Arranges methods by classname and method name */
		public int compare(T o1, T o2) {
			int r = ((T) o1).getTestClass().getName().compareTo(((T) o2).getTestClass().getName());
			if (r == 0) {
				r = ((T) o1).getMethodName().compareTo(((T) o2).getMethodName());
			}
			return r;
		}
	}
	
	/**
	 * @param fileToWrite - in which file to write - filename with location
	 * @param string - what to write
	 */
	public static void writeToFile(String fileToWrite, String stringToWrite)
	{
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter( new FileWriter(fileToWrite, false));
			writer.write(stringToWrite);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not write report! " + e.getLocalizedMessage());
			System.exit(-1);
		}
	}
}
