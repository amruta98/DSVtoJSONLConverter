package com.demo.test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.demo.test.solution.DSVFileConverter;
import com.google.gson.Gson;


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
	public void checkInputFile1(){
		try{
			String outputFilePath=System.getProperty("user.dir")+File.separator+"input"+File.separator+"JSONL output.jsonl";
			List<String> expectedOutput=loadOutput(outputFilePath);
			String inputFile=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 1.txt" ;
			String output1=DSVFileConverter.createJSONL(inputFile);
			List<String> actualOutput=loadOutput(output1);
			Assert.assertTrue("Output is incorrect:"+actualOutput, actualOutput.equals(expectedOutput));
		}catch(Exception e){
			System.out.println("Unexpected Exception:"+e.getMessage());
			Assert.assertFalse("Unexpected exception:"+e.getMessage(), true);
		}
	}
	
	@Test
	public void checkInputFile2(){
		try{
			String outputFilePath=System.getProperty("user.dir")+File.separator+"input"+File.separator+"JSONL output.jsonl";
			List<String> expectedOutput=loadOutput(outputFilePath);
			String inputFile=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 2.txt" ;
			String output1=DSVFileConverter.createJSONL(inputFile);
			List<String> actualOutput=loadOutput(output1);
			Assert.assertTrue("Output is incorrect:"+actualOutput, actualOutput.equals(expectedOutput));
		}catch(Exception e){
			System.out.println("Unexpected Exception:"+e.getMessage());
			Assert.assertFalse("Unexpected exception:"+e.getMessage(), true);
		}
	}
	
	@Test
	public void CheckInvalidFilePath(){
		try{
			String inputFile="config";
			DSVFileConverter.createJSONL(inputFile);
			Assert.assertFalse("Exception expected for invalid input file path.",true);
		}catch(Exception e){
			System.out.println("Expected Exception:"+e.getMessage());
			Assert.assertFalse("Expected exception:"+e.getMessage(), false);
		}
	}
	
	
	@Test
	public void CheckDifferentDelimiter(){
		try{
			String outputFilePath=System.getProperty("user.dir")+File.separator+"input"+File.separator+"JSONL output.jsonl";
			List<String> expectedOutput=loadOutput(outputFilePath);
			String inputFile=System.getProperty("user.dir")+File.separator+"input"+File.separator+"DSV input 3.txt" ;
			String output1=DSVFileConverter.createJSONL(inputFile);
			List<String> actualOutput=loadOutput(output1);
			Assert.assertTrue("Output is incorrect: "+actualOutput, actualOutput.equals(expectedOutput));
		}catch(Exception e){
			System.out.println("Unexpected Exception:"+e.getMessage());
			Assert.assertFalse("Unexpected exception:"+e.getMessage(), true);
		}
	}
	
	public List<String> loadOutput(String outputFilePath) throws Exception{
		Gson gson=new Gson();
		List<String> lines=new ArrayList<>();
		Path path=Paths.get(outputFilePath);
		Stream<String> stream=Files.lines(path);
		stream.forEach(line->lines.add(gson.fromJson(line, Map.class).toString()));
		stream.close();
		return lines;
	}

}
