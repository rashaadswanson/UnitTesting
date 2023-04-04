package App;
// package Software1;
import java.io.*;
import java.util.*;


public class Application {
	
		public static void main(String[] args) {
			String poem = loadfile("Poe.html");
			poem=trimfile(poem);
			poem= removeflags(poem);
			
			poem=poem.replaceAll("[^a-zA-Z ]", " Pr").toLowerCase();
			SortedMap <String, Integer> wordCount= countwords(poem);
			Map<String, Integer> sortedMap= sortWords(wordCount);
			for(Map.Entry<String,Integer> entry: sortedMap.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue().toString());
			}
			
			
			
		}
		public static Map<String, Integer> sortWords (Map<String, Integer> map) {
			TreeMap<String, Integer> treeMap= new TreeMap<String, Integer>((a,b)->{
				if(map.get(a)!=map.get(b))
					return -Integer.compare(map.get(a),map.get(b));
				return a.compareTo(b);
			});
			treeMap.putAll(map);
			return treeMap;
		}
		public static String loadfile(String filename) {
			
			StringBuilder builder = new StringBuilder();
			try {
				BufferedReader buffer= new BufferedReader( new FileReader(filename));
				String str;
				while((str=buffer.readLine())!= null){
					builder.append(str).append("\n");
				}
			 return builder.toString();
			} catch (Exception e) {
				return "";
				// TODO Auto-generated catch block
				
				
			}
		}
		public static String trimfile (String s )
		{
			int startindex=s.indexOf("<h1>");
			int endindex=s.indexOf("</div><!--end chapter-->");
			if (startindex==-1 || endindex==-1) {
				return "";
			}
			s=s.substring(startindex, endindex);
			return s;
		}
		public static String removeflags(String s) {
			while(s.contains("<")) {
				//Find the first open bracket
				int fob = s.indexOf("<");
				//Find the first closed bracket
				int fcb = s.indexOf(">");
			   //If < index is 0
				if(fob==0)
					//poem= poem after first closed bracket
				{
					s=s.substring(fcb+1);
					
				}
				//else 
					//poem= poem (zero,first open) + " " + poem(first close +1)
				else {
					s=s.substring(0,fob)+ " " + s.substring(fcb+1);
				}
			}
			return s;
		}
		public static SortedMap<String,Integer> countwords(String s) {
			String[] words =s.split("\\s+");
			SortedMap<String,Integer> wordCount=new TreeMap <String, Integer>();
			for(String w:words) {
					wordCount.put(w, wordCount.getOrDefault(w, 0)+ 1);

			}
			return wordCount;
		}
	}
