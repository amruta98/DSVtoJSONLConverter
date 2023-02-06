package com.demo.test;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.demo.test.solution.DSVFileConverter;


/**
 * Unit test for simple App.
 */
public class DSVFileConverterTest 
{


	@Rule
    public TestName testName;
	protected String testCaseName;
	public DSVFileConverterTest() {
		this.testName=new TestName();
	}
	@Before
	public void setUp(){
		  this.testCaseName = this.testName.getMethodName();
	      System.out.println(this.testCaseName + ": *** start ***");
	}
	
	@After
	public void tearDown(){
		  System.out.println(this.testCaseName + ": *** end ***");
	}

	@Test
	public void checkDSVFileReader(){
		try{
			String inputFile=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 1.txt" ;
			DSVFileConverter.createJSONL(inputFile,",");
			String inputFile2=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 2.txt" ;
			DSVFileConverter.createJSONL(inputFile2,"|");
		}catch(Exception e){
			System.out.println("Unexpected Exception:"+e.getMessage());
			Assert.assertFalse("Unexpected exception:"+e.getMessage(), true);
		}
	}
	
	@Test
	public void CheckInvalidFilePath(){
		try{
			String inputFile="config";
			DSVFileConverter.createJSONL(inputFile,",");
		}catch(Exception e){
			System.out.println("Expected Exception:"+e.getMessage());
			Assert.assertFalse("Expected exception:"+e.getMessage(), false);
		}
	}
	@Test
	public void CheckInvalidDelimiter(){
		try{
			String inputFile=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 1.txt";
			DSVFileConverter.createJSONL(inputFile,null);
		}catch(Exception e){
			System.out.println("Expected Exception:"+e.getMessage());
			Assert.assertFalse("Expected exception:"+e.getMessage(), false);
		}
	}
	
	@Test
	public void CheckDifferentDelimiter(){
		try{
			String inputFile=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 3.txt";
			DSVFileConverter.createJSONL(inputFile,".");
		}catch(Exception e){
			System.out.println("Unexpected Exception:"+e.getMessage());
			Assert.assertFalse("Unexpected exception:"+e.getMessage(), false);
		}
	}
}
