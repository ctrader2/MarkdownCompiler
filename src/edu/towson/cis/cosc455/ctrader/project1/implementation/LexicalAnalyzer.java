package edu.towson.cis.cosc455.ctrader.project1.implementation;
import Exceptions.CompilerException;

public class LexicalAnalyzer implements edu.towson.cis.cosc455.ctrader.project1.interfaces.LexicalAnalyzer {
	static String currentToken = "";
	static StringBuilder tokenBuilder = new StringBuilder();
	static char nextCharacter;
	static int currentPosition;

	@Override
	public void getNextToken() throws CompilerException {
		if(Compiler.fileReader.hasNext()){
			currentToken = Compiler.fileReader.next();
			currentPosition = 0;
			//System.out.println(currentToken);
			//System.out.println(currentToken.length());
			while(currentPosition < currentToken.length()){
				getCharacter();
				addCharacter();
				currentPosition++;
			}	
			lookupToken();
			Compiler.inputQueue.add(tokenBuilder.toString());
			tokenBuilder.setLength(0);
			getNextToken();
		}
	}

	@Override
	public void getCharacter() {
		nextCharacter = currentToken.charAt(currentPosition);
		//System.out.println(nextCharacter);
	}

	@Override
	public void addCharacter() {
		//System.out.println("next: " + nextCharacter);
		tokenBuilder.append(nextCharacter);
		System.out.println(tokenBuilder);
	}

	@Override
	public boolean isSpace () {
		if(tokenBuilder.charAt(currentPosition) == ' ')
			return true;
		return false;
	}

	@Override
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
			
	
	
	
	


