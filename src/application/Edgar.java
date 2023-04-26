package application;
// package Software1;
import java.io.*;
import java.util.*;
import java.sql.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.geometry.*;
/**
 * To count all the number of words in a poem loaded from a file and sort them from greatest to least
 * @author rashaadswanson
 *+
 *
 */

public class Edgar extends Application {
	static Connection conn;
	static String file_contents;
	static File file;
		/**
		 * The main entry point for the application. 
		 * @param args command line arguments
		 */
		public static void main(String[] args) {
			launch(args);
			return;
			/*
			 
			
			
		*/	
			
		}
		
		/**
		 * Sorts the words from greatest to least occurance 
		 * @param map words and their occurence counts 
		 * @return map of the words and thier occurances sorted
		 */
		public static Map<String, Integer> sortWords (Map<String, Integer> map) {
			TreeMap<String, Integer> treeMap= new TreeMap<String, Integer>((a,b)->{
				if(map.get(a)!=map.get(b))
					return -Integer.compare(map.get(a),map.get(b));
				return a.compareTo(b);
			});
			treeMap.putAll(map);
			return treeMap;
		}
		/**
		 * It loads a file into a String
		 * @param file a file to be loaded
		 * @return a string containing the files contents
		 */
		public static String loadfile(File file) {
			
			StringBuilder builder = new StringBuilder();
			try {
				BufferedReader buffer= new BufferedReader( new FileReader(file));
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
		
		/**
		 * Trims the file of html header
		 * @param s the string contents of the file 
		 * @return a string of the file without the header 
		 */
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
		/**
		 * Removes any html flags from a string
		 * @param s the string with flags
		 * @return the string without flags 
		 */
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
		/**
		 * Counts the number of times each word is used
		 * @param s A string to be counted 
		 * @return A sorted map of the words and their occurrence counts
		 */
		public static SortedMap<String,Integer> countwords(String s) {
			String[] words =s.split("\\s+");
			SortedMap<String,Integer> wordCount=new TreeMap <String, Integer>();
			for(String w:words) {
				
					try {
				//wordCount.put(w, wordCount.getOrDefault(w, 0)+ 1);
					String query = "INSERT INTO word (word,count) VALUES ('"+w+"',1) ON DUPLICATE KEY UPDATE count = count +1;" ;
					Statement stmt = conn.createStatement();
					 int cnt = stmt.executeUpdate(query);
					}catch(SQLException e) {
						throw new Error("failed",e);
					}
					
			}
			return wordCount;
		}
		 public static void connectdb() {
			
	
			String url ="jdbc:mysql://localhost:3306/word%20occurrences";
			try{
				 conn=DriverManager.getConnection(url, "root", "");
				 Statement stmt =conn.createStatement();
				 String query = "DELETE FROM word;" ;
					 int cnt = stmt.executeUpdate(query);
			}catch(SQLException e) {
				throw new  Error("failed",e);
			}
			}
	public void init() {
		return;
	}
	@Override
    public void start(Stage stage) {
            try {
            		Button connectbutton = new Button();
            		connectbutton.setText("Connect to DB");
            		connectbutton.setVisible(false);
                    Button filebutton = new Button();
                    filebutton.setText("Load File");
                    Label label = new Label("No file selected");
                    Label wordlabel = new Label("");
                    wordlabel.setVisible(false);
                    
                    FileChooser file_chooser = new FileChooser();
                    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent e) {
                                            file = file_chooser.showOpenDialog(stage);
                                            if(file != null){
                                                    label.setText(file.getAbsolutePath() + " selected");
                                                 file_contents=   loadfile(file);
                                                    connectbutton.setVisible(true);
                                            }
                                    }
                    };
                    filebutton.setOnAction(event);
                    EventHandler<ActionEvent> DBevent = new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                         connectdb();
                         if(conn != null) {
                        	 String poem =file_contents;
                 			poem=trimfile(poem);
                 			poem= removeflags(poem);
                 			
                 			poem=poem.replaceAll("[^a-zA-Z ]", " ").toLowerCase();
                 			SortedMap <String, Integer> wordCount= countwords(poem);
                 			try{
                 				 Statement stmt =conn.createStatement();
                 				 ResultSet rs= stmt.executeQuery("SELECT word,count FROM word ORDER BY count DESC LIMIT 20;");
                 				 String wl="";
                 				 while(rs.next()) {

                 					 wl+=""+rs.getInt("count")+" "+rs.getString("word")+"\n";
                 				 }
                 				 wordlabel.setText(wl);
                 				 wordlabel.setVisible(true);
                 			}catch(SQLException se) {
                 				throw new  Error("failed",se);
                 			} 
                         }
                      
                        }
        };
        connectbutton.setOnAction(DBevent);
                    VBox vbox = new VBox(30, label, filebutton, connectbutton,wordlabel);
                    vbox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(vbox,400,400);
                    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
            } catch(Exception e) {
                    e.printStackTrace();
            }
    }
}