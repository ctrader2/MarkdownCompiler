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

	@Override
	public void getCharacter() {
		nextCharacter = currentToken.charAt(currentPosition);
	}

	@Override
	public void addCharacter() {
		tokenBuilder.append(nextCharacter);
	}

	@Override
	public boolean isSpace () {
		if(tokenBuilder.charAt(currentPosition) == ' ')
			return true;
		return false;
	}
	
	public boolean isAudio(){
		if(tokenBuilder.charAt(0) == '@')
			return true;
		return false;
	}
	
	public boolean isVideo(){
		if(tokenBuilder.charAt(0) == '%')
			return true;
		return false;
	}
	
	public boolean isAddressBeg(){
		if(tokenBuilder.charAt(0) == '(')
			return true;
		return false;
	}
	
	public boolean isAddressEnd(){
		if(tokenBuilder.charAt(tokenBuilder.length() - 1) == ')')
			return true;
		return false;
	}
	
	public boolean isLinkBeg(){
		if(tokenBuilder.charAt(0) == '[')
			return true;
		return false;
	}
	
	public boolean isLinkEnd(){
		if(tokenBuilder.charAt(tokenBuilder.length() - 1) == ']')
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
			
	
	
	
	


