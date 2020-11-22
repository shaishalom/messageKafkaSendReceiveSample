package com.example.restservice.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class StringUtils {

	

    
    public static List<String> stringArrToStringList(String[] arrList){
    	List<String> list= Arrays.stream(arrList).collect(Collectors.toList());
    	return list;
    	
    }

    public static List<Object> objectArrToObjectList(Object[] arrList){
    	if (arrList==null) {
    		return null;
    	}
    	List<Object> list= Arrays.stream(arrList).collect(Collectors.toList());
    	return list;
    	
    }
    
    
    
    public static String printArrayList(List<String> list){
    	StringBuilder sb = new StringBuilder();
    	list.forEach(item-> { sb.append(item).append("\n"); });
    	String output = sb.toString();
    	return output;
    	
    }
    
   public static int countLineLevelByPipes(String _line)
    {
          StringTokenizer strTok = new StringTokenizer(_line, "|");
          int pipes = strTok.countTokens();
          if (!_line.contains("|")) {
        	  pipes=0;
          }
          return pipes;
    }
    

   	public static String toJson (Object obj) {
//   		ObjectMapper mapper = new ObjectMapper();
   		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
   		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		return jsonStr;
   	}
    
   	public static Boolean sendToFile(String filePath,String str) {
		Boolean isSuccess = false;
   		try {
   				//do not append, create only
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
				writer.append(str);
				writer.close();
				isSuccess = true;
		} catch (IOException e) {
			System.err.println("file not sound:"+filePath);
			//e.printStackTrace();
		}
   		return isSuccess;
   	}
   	
   	public static int getRandomNumber() {
   		return getRandomNumber(1000);
   	}

   	public static int getRandomNumber(int maxNum) {
		int rand = new Random().nextInt(maxNum);
		return rand;
   	}

   	public static String getDateAsString() { 
   		//hours comes 0-23. need to increase by hour
   		LocalDateTime nowDate = LocalDateTime.now().plusHours(1L);
   		return getDateAsString(nowDate);
   	}

   	public static String getDateAsStringUsingPattern(String pattern) {
   		//hours comes 0-23.need to increase by hour
   		LocalDateTime currentTime = LocalDateTime.now().plusHours(1L); 
		DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern(pattern);
		String currentTimeStr = currentTime.format(dateTimeFormatter);
		return currentTimeStr;
   	}
   	
   	public static String getDateAsString(LocalDateTime currentTime) {
		DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String currentTimeStr = currentTime.format(dateTimeFormatter);
		return currentTimeStr;
   	}
   	
   	public static String getDateAsString(Date date) {
   		if (date==null) return null;
   		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
   		return getDateAsString(ldt);
   	}
   	
   	public static Date getDate(String dateTimeStr) {
   		if (dateTimeStr==null) return null;
   		//dateTimeStr = dateTimeStr.replace("T", " ").replace("Z", " ");
		DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
   		LocalDateTime ldt = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
   		Date outDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
   		return outDate;
   	}
   	
	public static String getStringBetween(String str,String strFrom,String strTo) {
		Pattern p = Pattern.compile(strFrom+"(.*?)"+strTo);
		Matcher matcher = p.matcher(str);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	public static String getFromString(String str,String strFrom) {
		return getStringBetween(str,strFrom,"$");
	}

	public static String getFromStartToString(String str,String strTo) {
		return getStringBetween(str,"^",strTo);
	}
	
	public static boolean hasMatch(String str,String patternMatch) {
		Pattern p = Pattern.compile(patternMatch);
		Matcher matcher = p.matcher(str);
		return matcher.find();
	}

    public static boolean createFolder(String dir){
    	File f = new File(dir);
    	if (!f.exists()) {
    		boolean b=f.mkdir();
    		return b;
    	}
    	return false;
    }

    public static boolean createEmptyFile(String fullFileName){
    	Path path = Paths.get(fullFileName);
    	try (BufferedWriter writer = Files.newBufferedWriter(path,Charset.defaultCharset())){ //override if exists
    	}catch (IOException ioe)	{
    		System.err.format("IOException: %s%n",ioe);
    		return false;
    	}
    	return true;
    	
    }
    
    public static List<String> listElementToLowerCase(List<String> list){
    	if (list==null) {
    		return null;
    	}
    	List<String> newList = list.stream().map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
    	return newList;
    }
    
    public static List<String> listElementToUpperCase(List<String> list){
    	if (list==null) {
    		return null;
    	}
    	List<String> newList = list.stream().map(String::trim).map(String::toUpperCase).collect(Collectors.toList());
    	return newList;
    }
    
    

}
