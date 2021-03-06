package edu.towson.cis.cosc455.ctrader.project1.implementation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import Exceptions.CompilerException;

public class SemanticAnalyzer {
	//Boolean variables to mark whether semantic analyzer is currently working inside certain Markdown tags
	boolean head = false;
	boolean title = false;
	boolean bold = false;
	boolean italic = false;
	boolean list = false;
	boolean paragraph = false;
	boolean variableUse = false;
	
	//Used for Creating HTML output string
	StringBuilder text = new StringBuilder();
	
	//Used to define variable and value pairs
	static Map<String, String> scopedVariable = new LinkedHashMap<String, String>();
	static Map<String, String> unscopedVariable = new LinkedHashMap<String, String>();
	boolean scoped = false;
	

	/**
	 * While output queue generated by syntax analyzer is not empty, convert Markdown tags to HTML tags
	 * 
	 * @throws CompilerException if any of methods used throw compiler exception 
	 */
	public void runMexer() throws CompilerException{
		while(!Compiler.outputQueue.isEmpty()){
			if(Compiler.outputQueue.peek().equals(Tokens.sVARIABLE)){
				defineVariable();
			}
			convert();
		}
	}
	

	/**
	 * Checks for valid markdown tags, converts them to HTML tags and adds to HTML output string
	 * 
	 * @throws CompilerException if any of methods used throw compiler exception 
	 */
	public void convert() throws CompilerException{
		if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sHTML)){
			Compiler.htmlSB.append("<html>" + " ");
		    Compiler.outputQueue.remove();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eHTML)){
			Compiler.htmlSB.append("</html>");
			Compiler.outputQueue.remove();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sHEAD) && head == false){
			Compiler.htmlSB.append("<head>" + " ");
			Compiler.outputQueue.remove();
			head = true;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eHEAD)){
			Compiler.htmlSB.append("</head>" + " ");
			Compiler.outputQueue.remove();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sTITLE) && title == false){
			Compiler.htmlSB.append("<title>" + " ");
			Compiler.outputQueue.remove();
			title = true;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eTITLE)){
			Compiler.htmlSB.append("</title>" + " ");
			Compiler.outputQueue.remove();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sPARAGRAPH)){
			Compiler.htmlSB.append("<p>" + " ");
			Compiler.outputQueue.remove();
			paragraph = true;
			if(Compiler.outputQueue.peek().equals(Tokens.sVARIABLE)){
				defineScopedVariable();
				scoped = true;
			}
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.ePARAGRAPH)){
			Compiler.htmlSB.append("</p>" + " ");
			Compiler.outputQueue.remove();
			paragraph = false;
			disgardScopedVariable();
			scoped = false;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sBOLD) && bold == false){
			Compiler.htmlSB.append("<strong>" + " ");
			Compiler.outputQueue.remove();
			bold = true;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sBOLD) && bold == true){
			Compiler.htmlSB.append("</strong>" + " ");
			Compiler.outputQueue.remove();
			bold = false;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eBOLD) && bold == true){
			Compiler.htmlSB.append("</strong>" + " ");
			Compiler.outputQueue.remove();
			bold = false;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sITALIC) && italic == false){
			Compiler.htmlSB.append("<i>" + " ");
			Compiler.outputQueue.remove();
			italic = true;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eITALIC) && italic == true){
			Compiler.htmlSB.append("</i>" + " ");
			Compiler.outputQueue.remove();
			italic = false;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sLIST) && list == false){
			Compiler.htmlSB.append("<ul> <li>" + " ");
			Compiler.outputQueue.remove();
			list = true;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sLIST) && list == true){
			Compiler.htmlSB.append("<li>" + " ");
			Compiler.outputQueue.remove();
			list = true;
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eLIST)){
			Compiler.htmlSB.append("</li>" + " ");
			Compiler.outputQueue.remove();
			list = true;
			if(!Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sLIST)){
				Compiler.htmlSB.append("</ul>" + " ");
				list = false;
			}
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sBREAK)){
			Compiler.htmlSB.append("<br/>" + " ");
			Compiler.outputQueue.remove();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sBREAK)){
			Compiler.htmlSB.append("<br/>" + " ");
			Compiler.outputQueue.remove();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sLINKDESCRIPTION)){
			Compiler.htmlSB.append("<a href=\"");
			Compiler.outputQueue.remove();
			holdLinkDescription();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eLINKDESCRIPTION)){
			Compiler.outputQueue.remove();
			sLinkAddress();
			eLinkAddress();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sAUDIO)){
			Compiler.htmlSB.append("<audio controls>");
			Compiler.outputQueue.remove();
			sAudioAddress();
			eAudioAddress();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sVIDEO)){
			Compiler.htmlSB.append("<iframe ");
			Compiler.outputQueue.remove();
			sVideoAddress();
			eVideoAddress();
		}
		else if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.uVARIABLE)){
			Compiler.outputQueue.remove();
			useVariable();
			if(isText())
				Compiler.outputQueue.remove();
			if(Compiler.outputQueue.peek().equals(Tokens.eVARIABLE))
				Compiler.outputQueue.remove();
		}
		else if(isText())
			Compiler.htmlSB.append(Compiler.outputQueue.remove() + " ");
	
		

	}
	

	/**
	 * Holds descriptions of links while address is being added to HTML output string
	 * 
	 */
	private void holdLinkDescription(){
		while(isText())
			text.append(Compiler.outputQueue.remove() + " ");
	}
	

	/**
	 * @return true if next value in outputQueue is text
	 */
	private boolean isText(){
		if(Arrays.asList(Tokens.TOKENS).contains(Compiler.outputQueue.peek()))
				return false;
		return true;
	}
	

	/**
	 * Processes link addresses, adds to HTML string 
	 * 
	 * @throws CompilerException if there is no link address
	 */
	private void sLinkAddress() throws CompilerException{
		if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS)){
			Compiler.outputQueue.remove();
			while(isText())
				Compiler.htmlSB.append(Compiler.outputQueue.remove());
		}
		else throw new CompilerException("Semantic Error: No Link Address");
		
	}
	

	/**
	 * Processes link address, converts to HTML and adds to HTML output string
	 * 
	 * @throws CompilerException if there is no link address
	 */
	private void eLinkAddress() throws CompilerException{
		 if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS)){
			Compiler.outputQueue.remove();
			Compiler.htmlSB.append("\"> " + text.toString() + "</a> ");
			text = null;
		}else throw new CompilerException("Semantic Error: No Link Address");
	}
	

	/**
	 * Converts Markdown audio tags and links to HTML audio tags and addresses
	 * 
	 * @throws CompilerException if there is no address for audio file 
	 */
	private void sAudioAddress() throws CompilerException{
		if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS)){
			Compiler.outputQueue.remove();
			Compiler.htmlSB.append("<source src=\"");
			while(isText())
				Compiler.htmlSB.append(Compiler.outputQueue.remove());
		}
		else throw new CompilerException("Semantic Error: No Audio Address");	
	}
	
	/**
	 * Converts Markdown audio tags and links to HTML audio tags and addresses
	 * 
	 * @throws CompilerException if there is no address for audio file 
	 */
	private void eAudioAddress() throws CompilerException{
		 if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS)){
			Compiler.outputQueue.remove();
			Compiler.htmlSB.append("\"> </audio>");

		}else throw new CompilerException("Semantic Error: No Link Address");
	}
	
	/**
	 * Converts Markdown video tags and links to HTML video tags and addresses
	 * 
	 * @throws CompilerException if there is no address for video file 
	 */
	private void sVideoAddress() throws CompilerException{
			
			if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS)){
				Compiler.outputQueue.remove();
				Compiler.htmlSB.append("src=\"");
				while(isText())
					Compiler.htmlSB.append(Compiler.outputQueue.remove());
			}
			else throw new CompilerException("Semantic Error: No Audio Address");	
		}

	/**
	 * Converts Markdown video tags and links to HTML video tags and addresses
	 * 
	 * @throws CompilerException if there is no address for video file 
	 */
	private void eVideoAddress() throws CompilerException{
		 if(Compiler.outputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS)){
			Compiler.outputQueue.remove();
			Compiler.htmlSB.append("\"/>");
	
		}else throw new CompilerException("Semantic Error: No Video Address");
	}

	/**
	 * If next token in outputQueue generated by syntax analyzer is definition of a variable that is not scoped within
	 * other Markdown tags, assigns and stores key/value pairs to be used later in the Markdown file
	 * 
	 * @throws CompilerException if definition of variable is not correct (more for readability, should already be checks by syntax analyzer)
	 */
	private void defineVariable() throws CompilerException{
		String variableName, variableValue;
		if(Compiler.outputQueue.peek().equals(Tokens.sVARIABLE)){
			Compiler.outputQueue.remove();
			if(isText()){
				variableName = Compiler.outputQueue.remove();
			}else throw new CompilerException("Semantic Error: No variable name");
			if(Compiler.outputQueue.peek().equals(Tokens.dVARIABLE)){
				Compiler.outputQueue.remove();
			}else throw new CompilerException("Semantic Error: Variable definition must contain '='");
			if(isText()){
				variableValue = Compiler.outputQueue.remove();
			}else throw new CompilerException("Semantic Error: No variable value.");
			if(Compiler.outputQueue.peek().equals(Tokens.eVARIABLE))
				Compiler.outputQueue.remove();
		}else throw new CompilerException("Semantic Error: Variable Definition");
			
			unscopedVariable.put(variableName, variableValue);		
	}
	
	/**
	 * If next token in outputQueue generated by syntax analyzer is definition of a variable that is scoped within
	 * other Markdown tags, assigns and stores key/value pairs to be used later in the Markdown file, within the scame scope
	 * 
	 * @throws CompilerException if definition of variable is not correct (more for readability, should already be checks by syntax analyzer)
	 */
	private void defineScopedVariable() throws CompilerException{
		String variableName, variableValue;
		if(Compiler.outputQueue.peek().equals(Tokens.sVARIABLE)){
			Compiler.outputQueue.remove();
			if(isText()){
				variableName = Compiler.outputQueue.remove();
			}else throw new CompilerException("Semantic Error: No variable name");
			if(Compiler.outputQueue.peek().equals(Tokens.dVARIABLE)){
				Compiler.outputQueue.remove();
			}else throw new CompilerException("Semantic Error: Variable definition must contain '='");
			if(isText()){
				variableValue = Compiler.outputQueue.remove();
			}else throw new CompilerException("Semantic Error: No variable value.");
			if(Compiler.outputQueue.peek().equals(Tokens.eVARIABLE))
				Compiler.outputQueue.remove();
		}else throw new CompilerException("Semantic Error: Variable Definition");
			
			scopedVariable.put(variableName, variableValue);		
	}
	
	
		
	/**
	 * If variable is used later in program, retrives value from key/value pairing, inserts into HTML output string
	 * 
	 * @throws CompilerException if variable is undefined
	 */
	private void useVariable() throws CompilerException{
		if(scoped==true){
			if(scopedVariable.containsKey(Compiler.outputQueue.peek()))
				Compiler.htmlSB.append(scopedVariable.get(Compiler.outputQueue.peek()) + " ");
			else throw new CompilerException("Semantic Error - Undefined Variable: " + Compiler.outputQueue.peek());
		}
		else if(!unscopedVariable.isEmpty() && unscopedVariable.containsKey(Compiler.outputQueue.peek())){
			Compiler.htmlSB.append(unscopedVariable.get(Compiler.outputQueue.peek()) + " ");
		}
		else
			throw new CompilerException("Semantic Error - undefined variable: " + Compiler.outputQueue.peek());
	}
	
	/**
	 * Removes key/value pairs of scoped variables once the semantic anaylzer exits local scope where it was defined
	 */
	private void disgardScopedVariable(){
		scopedVariable.clear();
	}
		
}