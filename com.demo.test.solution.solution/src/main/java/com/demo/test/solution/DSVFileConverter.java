package com.demo.test.solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DSVFileConverter {

    private static Gson gson = new GsonBuilder().create();
    private static Map<String,Object> map=null;
    private static SimpleDateFormat outputFormat=new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat inputFormat1=new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat inputFormat2=new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = Logger.getLogger(DSVFileConverter.class.getName());	

	public static void main(String[] args){
		try {
			if(args.length>=1){
				String filePath=args[0].split("=",2)[1];
				String delimiter=" ";
				for(int i=1;i<args.length;i++){
					if(args[i]!=null)
						filePath = filePath+" "+args[i] ;
				}
				System.out.println();
				System.out.println("FilePath : ["+filePath+"]");
				System.out.println();
				Scanner sc=new Scanner(System.in);
				System.out.print("Enter delimeter :");
				delimiter=sc.nextLine();
				sc.close();
				System.out.println();
				if(delimiter.isEmpty())
					delimiter=" ";
				createJSONL(filePath, delimiter);
			}else
				log.error("Please provide args like  java -jar jarname.jar file=E:\\input 1.txt");
		}catch(IOException e){
			log.error("Please check args it should like java -jar jarname.jar file=E:\\input 1.txt");
    		log.error(getStackTrace(e));
    	}catch(Exception e){
			log.error("Please check args it should like java -jar jarname.jar file=E:\\input 1.txt");
    		log.error(getStackTrace(e));
    	}
	}
    
    public static void createJSONL(String filePath,String delimiter) throws Exception{
    	log.info("Conversion started for :["+filePath+ "] with delimiter :"+delimiter);
		String encoding = "UTF-8";
    	String outputFilePath=System.getProperty("user.dir")+File.separator+"output"+File.separator+"output_"+System.currentTimeMillis()+".jsonl";
    	Stream<String> stream=null;
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), encoding));
		try {
			if(filePath==null||!new File(filePath).exists())
				throw new Exception("Invalid input filePath:["+filePath+"]");
			Path path=Paths.get(filePath);
			stream=Files.lines(path);
			if (stream != null && writer != null && delimiter!=null&& !delimiter.isEmpty()) {
				Optional<String> keysValue = Files.lines(path).limit(1).findFirst();
				if (keysValue.isPresent()) {
					String keysLine = keysValue.get();
					String[] keys = keysLine.split(Pattern.quote(delimiter));
					stream.skip(1).map(val -> getJsonObject(val, delimiter, keys)).forEach(val -> {
						try {
							writer.write(val + "\n");
						} catch (IOException e) {
							log.error(getStackTrace(e));
						}
					});
					log.info("Conversion completed output jsonl filepath :[" + outputFilePath + "]");
				}else{
					log.warn("No lines present in input file.");
				}
			}else{
				log.error("Invalid input:["+ filePath+ "] delimiter:"+ delimiter);
				throw new Exception("Invalid input:["+ filePath+ "] \n\tdelimiter:"+ delimiter);
			}
		} finally {
			if(writer!=null)
				writer.close();
			if(stream!=null)
				stream.close();
		}

    }
	private static String getJsonObject(String line,String delimiter,String[] keys) {
		map=new LinkedHashMap<>();
		try {
			String[] values = line.split(Pattern.quote(delimiter));
			if (keys.length != values.length) {
				List<String> valueList = new ArrayList<>();
				StringBuilder builder = new StringBuilder();
				int temp = 0;
				boolean skip = false;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == '"') {
						builder = new StringBuilder();
						for (int j = i + 1; j < line.length(); j++) {
							temp = j;
							if (j==line.lastIndexOf('"', j)){
								if(j+1<line.length()&&!delimiter.equals(new String(""+line.charAt(j+1)))){
									builder.append(line.charAt(j));
									continue;
								}
								else
									break;
							}
							
							builder.append(line.charAt(j));
						}
						
						valueList.add(builder.toString());
						i = temp;
						skip = true;
					} else {
						if ((i <= temp && skip) || delimiter.equals(new String(line.charAt(i) + ""))) {
							continue;
						} else {
							if (line.indexOf(delimiter, i) == -1) {
								valueList.add(line.substring(i, line.length()));
								temp = line.length();
							} else {
								valueList.add(line.substring(i, line.indexOf(delimiter, i)));
								temp = line.indexOf(delimiter, i);
							}
							skip = true;
						}
					}
				}
				if (valueList.size() != keys.length){
					if(valueList.size()<keys.length){
						for(int k=valueList.size()-1;k<keys.length;k++){
							valueList.add("");
						}
						values = valueList.toArray(new String[0]);
					}
				}else
					values = valueList.toArray(new String[0]);
			}
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].equals("dateOfBirth")) {
					try{
						values[i] = outputFormat.format(inputFormat1.parse(values[i]));
					}catch(Exception e){
						if(values[i].length()>3&&values[i].charAt(2)=='-')
							values[i] = outputFormat.format(inputFormat2.parse(values[i]));
					}
				}
				if(values[i]!=null&&values[i].matches("[0-9]+")){
					map.put(keys[i],Integer.parseInt(values[i]));
				}else{
					if(values[i]!=null&&!values[i].isEmpty()){
						map.put(keys[i], values[i]);
					}
				}
			}
		}catch(Exception e){
			log.error(getStackTrace(e));
		}
		return gson.toJson(map);
	}
	private static String getStackTrace(final Throwable throwable) {
	     final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
	static{
		String outputFolder=System.getProperty("user.dir")+File.separator+"output";
		File file=new File(outputFolder);
		if(!file.exists())
			file.mkdir();
	}
}
