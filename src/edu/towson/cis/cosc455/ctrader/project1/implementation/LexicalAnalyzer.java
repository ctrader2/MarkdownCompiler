package edu.towson.cis.cosc455.ctrader.project1.implementation;
import Exceptions.CompilerException;

public class LexicalAnalyzer implements edu.towson.cis.cosc455.ctrader.project1.interfaces.LexicalAnalyzer {
	static String currentToken = ""; //used for getting next token element from input file
	static StringBuilder tokenBuilder = new StringBuilder(); //used for building token to compare to valid Markdown lexemes
	static char nextCharacter; //stores character of current token for lexical analysis
	static int currentPosition; //counter for position inside current token during lexical analysis

	/**
	 * Reads each individual string from .mkd file, then compares each character in string to valid Markdown characters
	 * 
	 * If the string equals a Markdown tag, adds string to inputqueue, signifying that it is a valid tag in the language
	 * If the string does not equal a Markdown tag, then each character is examined and if all are valid characters the
	 * 		string is added as a legal word in the language.
	 * 
	 * White space can be ignored with exception to words as well as audio, video, and link tags. When these Markdown tags are found,
	 *   they are spliced out of the currentToken and inserted into the inputqueue as their own tokens.
	 * 
	 * @throws a CompilerException if current token being examined is not a valid Markdown tag or legal text within the language
	 */
	public void getNextToken() throws CompilerException {
		if(Compiler.fileReader.hasNext()){
			currentToken = Compiler.fileReader.next();
			currentPosition = 0;
			
			while(currentPosition < currentToken.length()){
				getCharacter();
				addCharacter();
				if(isAudio()){
					Compiler.inputQueue.add(tokenBuilder.toString());
					tokenBuilder.deleteCharAt(0);
				}
				else if(isVideo()){
					Compiler.inputQueue.add(tokenBuilder.toString());
					tokenBuilder.deleteCharAt(0);
				}
				else if(isAddressBeg()){
					Compiler.inputQueue.add(tokenBuilder.toString());
					tokenBuilder.deleteCharAt(0);
				}
				else if(isAddressEnd()){
					Compiler.inputQueue.add(tokenBuilder.substring(0, tokenBuilder.length()-1));
					tokenBuilder.delete(0, tokenBuilder.length()-1);
				}
				else if(isLinkBeg()){
					Compiler.inputQueue.add(tokenBuilder.toString());
					tokenBuilder.deleteCharAt(0);
				}
				else if(isLinkEnd()){
					Compiler.inputQueue.add(tokenBuilder.substring(0, tokenBuilder.length()-1));
					tokenBuilder.delete(0, tokenBuilder.length()-1);
					Compiler.inputQueue.add(tokenBuilder.substring(0));
					tokenBuilder.deleteCharAt(0);
				}
				currentPosition++;
			}	
			lookupToken();
			Compiler.inputQueue.add(tokenBuilder.toString());
			tokenBuilder.setLength(0);
			getNextToken();
		}
	}

	/**
	 * Gets a character from incrementing positions within the current token being lexically analyzed
	 */
	public void getCharacter() {
		nextCharacter = currentToken.charAt(currentPosition);
	}

	/**
	 * Adds the next character of current token being examined to stringbuilder for lexical processing
	 * 
	 */
	public void addCharacter() {
		tokenBuilder.append(nextCharacter);
	}

	/**
	 * Checks to see whether the current character being examined is a space
	 * 
	 * @return true if current character being examined within current token is a space
	 */
	public boolean isSpace () {
		if(tokenBuilder.charAt(currentPosition) == ' ')
			return true;
		return false;
	}
	
	/**
	 * Compares current character being examined to valid Markdown audio tag, to signify whether splicing currentToken may need to occur
	 *
	 *@return true if beginning of current token being examined is opening audio tag
	 */
	public boolean isAudio(){
		if(tokenBuilder.charAt(0) == '@')
			return true;
		return false;
	}

	/**
	 * Compares current character being examined to valid Markdown video tag, to signify whether splicing currentToken may need to occur
	 *
	 *@return true if beginning of current token being examined is opening video tag
	 */
	public boolean isVideo(){
		if(tokenBuilder.charAt(0) == '%')
			return true;
		return false;
	}
	/**
	 * Compares current character being examined to valid Markdown address tag, to signify whether splicing currentToken may need to occur
	 *
	 *@return true if beginning of current token being examined is opening address tag
	 */
	public boolean isAddressBeg(){
		if(tokenBuilder.charAt(0) == '(')
			return true;
		return false;
	}
	
	/**
	 * Compares current character being examined to valid Markdown address tag, to signify whether splicing currentToken may need to occur
	 *
	 *@return true if end of current token being examined is closing address tag
	 */
	public boolean isAddressEnd(){
		if(tokenBuilder.charAt(tokenBuilder.length() - 1) == ')')
			return true;
		return false;
	}
	/**
	 * Compares current character being examined to valid Markdown link tag, to signify whether splicing currentToken may need to occur
	 *
	 *@return true if beginning of current token being examined is opening of link description
	 */
	public boolean isLinkBeg(){
		if(tokenBuilder.charAt(0) == '[')
			return true;
		return false;
	}
	
	/**
	 * Compares current character being examined to valid Markdown link tag, to signify whether splicing currentToken may need to occur
	 *
	 *@return true if end of the current token being examined is closing of link description
	 */
	public boolean isLinkEnd(){
		if(tokenBuilder.charAt(tokenBuilder.length() - 1) == ']')
			return true;
		return false;
	}
	
	/**
	 * Looks up current token and verifies it as either a valid Markdown tag or valid text within the language definition
	 * 
	 * @return true if current token being examined is a legal Markdown tag or legal text within the language
	 * @throws CompilerException if the current token being examined is not a valid Markdown tag or legal text
	 */
	public boolean lookupToken() throws CompilerException {
			
			for(int x=0; x < Tokens.TOKENS.length; x++)
				if(Tokens.TOKENS[x].equals(tokenBuilder.toString()))
					return true;
			for(int x=0; x < tokenBuilder.length(); x++)
				for(int y=0; y < Tokens.CHARACTERS.length; y++)
					if(tokenBuilder.charAt(x) == Tokens.CHARACTERS[y])
						return true;
				
			throw new CompilerException("Lexical Error - Illegal Lexeme Found: " + tokenBuilder);
	}
}
			
	
	
	
	


