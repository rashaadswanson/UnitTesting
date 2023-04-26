package application;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

public class Test_load {

	@Test
	@DisplayName("To see if the loaded correctly")
	public void test_loadfile() {
		assertNotEquals("poe html should return a non empty string","",Edgar.loadfile(null));
		assertEquals("Empty file names return empty strings","",Edgar.loadfile(null));
		





	}
	@Test
	@DisplayName("Testing to see if file trimming works")
	public void test_trimfile() {

		String teststring ="<h1>test</div><!--end chapter-->";
		assertEquals("Trims the flags and leaves the word test","<h1>test",Edgar.trimfile(teststring));

	}
	@Test
	@DisplayName("Testing to see if html flags are removed")
	public void test_removeflags() {
		String teststring ="<h1>test</div><!--end chapter-->";
		assertEquals("Returns a non empty string test","test  ",Edgar.removeflags(teststring));

	}
	@Test
    @DisplayName("Testing if string can be broken down to a tree of all the word counts")
	public void test_countwords() {
		assertNotEquals("Returns a map of all the word counts","",Edgar.countwords("poe.html"));


	}
	@Test
	@DisplayName("Testing the occurence for each number of words")

	public void test_sortwords() {

		assertNotEquals("Returns the number of occurence of each word in the poem","",Edgar.sortWords(new TreeMap<String,Integer>()));
	}

}
