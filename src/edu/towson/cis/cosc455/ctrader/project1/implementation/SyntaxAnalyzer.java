package edu.towson.cis.cosc455.ctrader.project1.implementation;
import java.util.Arrays;

import Exceptions.CompilerException;

public class SyntaxAnalyzer implements edu.towson.cis.cosc455.ctrader.project1.interfaces.SyntaxAnalyzer {

	/**
	 * Begins at first entry in inputQueue, checks to make sure .mkd file begins with #BEGIN and ends with #END
	 * If the file beings with #BEGIN, checks syntax of head and body of the document 
	 * 
	 * @throws CompilerException if .mkd file does not being with #BEGIN or end with #END
	 */
	public void markdown() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sHTML)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
				variableDefine();
				head();
				body();
				if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eHTML)){
					Compiler.outputQueue.add(Compiler.inputQueue.remove());
				}else throw new CompilerException("Syntax Error: .mkd file does not end with #END");
		}else throw new CompilerException("Syntax Error: .mkd file does not start with #BEGIN");
		return;
	}

	/**
	 * Checks to ensure syntax of head (begins and ends with '^', and checks to see if a title exists
	 * The head portion of the document is optional
	 * 
	 * @throws CompilerException if head section does not end with '^', if applicable 
	 */
	public void head() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sHEAD)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			title();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eHEAD)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Syntax Error: head block is not closed with '^'");
		}return;
	
	}

	/**
	 * Checks to ensure syntax of title block (begins with '<' and ends with '>') is correct
	 * The title portion of the document is optional
	 * 
	 * If a title block exists, title() checks for text between opening and closing title tags
	 * 
	 * @throws CompilerException if title block does not end with '>', if applicable 
	 */
	public void title() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sTITLE)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eTITLE)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Syntax Error: title block is not closed with '>'");
		}return;
	}

	
	/**
	 * Begins by checking to see if variable is defined immediately following head section, if there is one
	 * Then, while there are still tags left in the inputqueue to be analyzed for syntax, it checks and processes each one 
	 * through paragraph() and inneritem()
	 * 
	 * After all valid tags have been consumed and analyzed, checks for leftover elements inputqueue that may be invalid
	 * 
	 * @throws CompilerException if any of the methods body() calls throws a CompilerException
	 */
	public void body() throws CompilerException {
		variableDefine();
		while(onceMore()){
			paragraph(); 
			innerItem();
		}
			checkSyntax();
	}


	/**
	 *If the next token in the inputqueue is the open paragraph tag, '{', then removes from inputqueue and 
	 *moves it to the output queue. Then checks for any variable definitions followed by valid tags that could
	 *be contained within a paragraph, until the close paragraph tag is found, '}'
	 * 
	 * @throws CompilerException if paragraph is not properly closed with '}' tag
	 */
	public void paragraph() throws CompilerException {
		
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sPARAGRAPH)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			variableDefine();
			while(onceMore())
				innerItem();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.ePARAGRAPH)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Syntax Error: Invalid Text -  " + Compiler.inputQueue.peek());
		}return;
	}

	/**
	 * Checks for text between Markdown tags. Begins when called and ends when another valid Markdown tag is encountered
	 */
	public void innerText()  {
			while(!Arrays.asList(Tokens.TOKENS).contains(Compiler.inputQueue.peek().toUpperCase()))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
	}

	/**
	 * When variable definition is encountered, it checks that the syntax '$DEF varName = value $END' is upheld
	 * If proper syntax is found, pushes each individual token to the output queue
	 * 
	 * @throws CompilerException if there is no variable name, value or definition is not closed with '$USE'
	 */
	public void variableDefine() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sVARIABLE)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.dVARIABLE)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Error: No Variable Definition.");
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eVARIABLE)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Error: Variable Definition: " + Compiler.inputQueue.peek());
		}
		
	}

	/**
	 * When $USE token is encountered in inputQueue, checks to make sure syntax of variable use is upheld:
	 * $USE varName $END ;
	 * 
	 * @throws CompilerException if there is no variable name or is not closed with '$USE'
	 */
	public void variableUse() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.uVARIABLE)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eVARIABLE))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			else
				throw new CompilerException("Syntax Error: Invalid Use of Variable - " + Compiler.inputQueue.peek());
		}

	}

	/**
	 *When Markdown bold tag is encountered, **, makes sure syntax of bolding text is upheld:
	 * ** bold text**
	 * 
	 * @throws CompilerException if bold text started with not closing tag
	 */
	public void bold() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sBOLD)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eBOLD)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: Bold tag not closed");
		}
		else return;

	}

	/**
	 *When Markdown italic tag is encountered, *, makes sure syntax of italic text is upheld:
	 * * italic text*
	 * 
	 * @throws CompilerException if italic text started with not closing tag
	 */
	public void italics() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sITALIC)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eITALIC)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: italic tag not closed");
		}
		else return;

	}

	/**
	 *When Markdown list tag is encountered, ensures syntax of list items is upheld:
	 * + text ;
	 * 
	 * @throws CompilerException if list item is opened but not closed correctly with ';'
	 */
	public void listitem() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sLIST)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			while(onceMore())
				innerItem();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eLIST)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: List item not closed");
		}
		else return;

	}

	/**
	 *Checks for valid Markdown tags that occur within paragraph and list item tags, as well as text
	 * 
	 * 
	 * @throws CompilerException if any of called methods throw CompilerException
	 */
	public void innerItem() throws CompilerException {
		if(Compiler.inputQueue.peek().equals(Tokens.sAUDIO))
			audio();
		else if(Compiler.inputQueue.peek().equals(Tokens.sBOLD))
			bold();
		else if(Compiler.inputQueue.peek().equals(Tokens.sITALIC))
			italics();
		else if(Compiler.inputQueue.peek().equals(Tokens.sLINKDESCRIPTION))
			link();
		else if(Compiler.inputQueue.peek().equals(Tokens.sLIST))
			listitem();
		else if(Compiler.inputQueue.peek().equals(Tokens.uVARIABLE))
			variableUse();
		else if(Compiler.inputQueue.peek().equals(Tokens.sBREAK))
			newline();
		else if(Compiler.inputQueue.peek().equals(Tokens.sVIDEO))
			video();
		else if(!Arrays.asList(Tokens.TOKENS).contains(Compiler.inputQueue.peek().toUpperCase()))
			innerText();		
		else { }
		
			
	}


	/**
	 *If link tag is encountered, '[' ensures syntax for linked phrases is upheld:
	 * [linked phrase](address)
	 * 
	 * @throws CompilerException if either linked phrase or address tags are not closed
	 */
	public void link() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sLINKDESCRIPTION)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		innerText();
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eLINKDESCRIPTION))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		else throw new CompilerException("Syntax Error: Linked Phrased Not Closed with ']'");
		
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		else throw new CompilerException("Syntax Error: Address of Linked Phrase not closed with ')'");
		}
	  else return;

	}

	/**
	 *If audio tag is encountered, '@' ensures syntax for linked phrases is upheld:
	 * @(address)
	 * 
	 * @throws CompilerException if audio address tags are not closed
	 */
	public void audio() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sAUDIO)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
				innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Syntax Error: Address of audio file not closed with ')'");
		}
	}

	/**
	 *If video tag is encountered, '%' ensures syntax for linked phrases is upheld:
	 * %(address)
	 * 
	 * @throws CompilerException if video address tags are not closed
	 */
	public void video() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sVIDEO)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
				innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("Syntax Error: Video address not closed with ')'");
		}

	}

	/**
	 * If Markdown tag newline is encountered, remove from inputqueue and add to output queue
	 * 
	 */
	public void newline() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sBREAK))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
	}
	
	/**
	 * Checks to see if next lexeme in inputQueue is a valid Markdown tag
	 * 
	 */
	public boolean onceMore() {
		innerText();
		String next = Compiler.inputQueue.peek();
		return next.equalsIgnoreCase(Tokens.sAUDIO) || next.equalsIgnoreCase(Tokens.sBOLD) || next.equalsIgnoreCase(Tokens.sITALIC) ||
				next.equalsIgnoreCase(Tokens.sLINKDESCRIPTION) || next.equals(Tokens.sBREAK)|| next.equalsIgnoreCase(Tokens.sLIST) || next.equalsIgnoreCase(Tokens.uVARIABLE) 
				|| next.equalsIgnoreCase(Tokens.sPARAGRAPH) || next.equalsIgnoreCase(Tokens.sVARIABLE)|| next.equalsIgnoreCase(Tokens.sVIDEO);
				
	}
	
	/**
	 * Checks to see if leftover lexemes in inputQueue match any valid, if so, throw and error, because they are inserted in incorrect order
	 * @throws CompilerException if anything is left in the inputQueue
	 */
	public void checkSyntax() throws CompilerException {
		if(!Arrays.asList(Tokens.TOKENS).contains(Compiler.inputQueue.peek().toUpperCase()) || !Compiler.inputQueue.peek().equals(Tokens.eHTML))
			throw new CompilerException("Syntax Error - Unexpected token: " + Compiler.inputQueue.peek());
	}

}
