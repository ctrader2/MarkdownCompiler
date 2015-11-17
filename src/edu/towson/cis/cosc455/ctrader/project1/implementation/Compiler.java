package edu.towson.cis.cosc455.ctrader.project1.implementation;
//Imports Used in Compiler.java
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import Exceptions.BadFileExtension;
import Exceptions.CompilerException;

public class Compiler {
	
	//Used for Reading File
	static String file;
	static String extension;
	static File markdownFile;
	static Scanner fileReader;
	static final String MKD = "mkd";
	
	//Stacks Used by Lexical Analyzer and Syntax Analyzer
	static Queue<String> inputQueue = new LinkedList<String>();
	static Queue<String> outputQueue = new LinkedList<String>();
	
	//Final String Used for Storing HTML translated Markdown Language 
	static StringBuilder htmlSB = new StringBuilder();
	
	
	public static void main(String[] args) throws BadFileExtension, CompilerException {
		//Get File  Check Extension
		file = ("Test8.mkd");
		try{
			extension = file.split("\\.")[1];  //Store file extension in string 'extension'
			if(!extension.equals(MKD)) throw new BadFileExtension("Error - file extension must be '.mkd' - Exiting Program"); //check to ensure correct file extension '.mkd' is used		
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Bad File Name - Exiting Program"); //throw error if no file extension is specified
			e.printStackTrace();
		}
		try{
			fileReader = new Scanner(new FileReader(file));
			markdownFile = new File(file);
		}
		catch(FileNotFoundException e){
			System.out.println("File Not Found. Exiting Program"); //throw error if file does not exist
			e.printStackTrace();
		}
		
		
		//Now that file is verified, begin compilation..
		//Lexical Analysis
		LexicalAnalyzer lexer = new LexicalAnalyzer();
		lexer.getNextToken();
		//Syntax Analysis
		SyntaxAnalyzer sexer = new SyntaxAnalyzer();
		sexer.markdown();
		//Semantic Analysis
		SemanticAnalyzer mexer = new SemanticAnalyzer();
		mexer.runMexer();
		
		//Convert StringBuilder Object into valid HTML file 
		try {
				String filename = file + ".html";
				File file = new File(filename);

				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(htmlSB.toString());
				bw.close();

				openHTMLFileInBrowser(filename);
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		
		}

	
	/**
	 * Opens an HTML file in the default browser. Requires the following imports: 
	 * 		import java.awt.Desktop;
	 * 		import java.io.File;
	 * 		import java.io.IOException;
	 * @param htmlFileStr the String of an HTML file.
	 */
	static void openHTMLFileInBrowser(String htmlFileStr){
		File file= new File(htmlFileStr.trim());
		if(!file.exists()){
			System.err.println("File "+ htmlFileStr +" does not exist.");
			return;
		}
		try{
			Desktop.getDesktop().browse(file.toURI());
		}
		catch(IOException ioe){
			System.err.println("Failed to open file");
			ioe.printStackTrace();
		}
		return ;
	}

}
