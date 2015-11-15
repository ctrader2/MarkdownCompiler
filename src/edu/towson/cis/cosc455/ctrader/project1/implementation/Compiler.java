package edu.towson.cis.cosc455.ctrader.project1.implementation;
//Imports Used in Compiler.java
import java.awt.Desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import Exceptions.BadFileExtension;
import Exceptions.CompilerException;

public class Compiler {
	
	//Used for Reading File
	static String file;
	static String extension;
	static File markdownFile;
	static Scanner fileReader;
	static final String MKD = "mkd";
	
	//Stacks Used by Lexer and Sexer
	static Queue<String> inputQueue = new LinkedList<String>();
	static Queue<String> outputQueue = new LinkedList<String>();
	
	
	public static void main(String[] args) throws BadFileExtension, CompilerException {
		//Get File  Check Extension
		file = ("Test3.mkd");
		try{
			extension = file.split("\\.")[1];
			if(!extension.equals(MKD)) throw new BadFileExtension("Error - file extension must be '.mkd' - Exiting Program");			
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Bad File Name - Exiting Program");
			e.printStackTrace();
		}
		try{
			fileReader = new Scanner(new FileReader(file));
			markdownFile = new File(file);
		}
		catch(FileNotFoundException e){
			System.out.println("File Not Found. Exiting Program");
			e.printStackTrace();
		}
		
		
		//Now that file is verified, begin compilation..
		LexicalAnalyzer lexer = new LexicalAnalyzer();
		lexer.getNextToken();
		System.out.println("Lexer Complete");
		
		SyntaxAnalyzer sexer = new SyntaxAnalyzer();
		sexer.markdown();
		System.out.println("Sexer Complete");

	}
	
	/**
	 * Opens an HTML file in the default browswer. Requires the following imports: 
	 * 		import java.awt.Desktop;
	 * 		import java.io.File;
	 * 		import java.io.IOException;
	 * @param htmlFileStr the String of an HTML file.
	 */
	void openHTMLFileInBrowswer(String htmlFileStr){
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
