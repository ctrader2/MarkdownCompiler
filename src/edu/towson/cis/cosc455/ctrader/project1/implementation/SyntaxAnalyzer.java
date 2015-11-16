package edu.towson.cis.cosc455.ctrader.project1.implementation;

import java.util.Arrays;

import Exceptions.CompilerException;

public class SyntaxAnalyzer implements edu.towson.cis.cosc455.ctrader.project1.interfaces.SyntaxAnalyzer {

	@Override
	public void markdown() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sHTML)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
				head();
				body();
				//System.out.print(Compiler.inputQueue.peek());
				if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eHTML)){
					System.out.println("Syntax Check complete");
				}else throw new CompilerException("Syntax Error: .mkd file does not end with #END");
		}else throw new CompilerException("Syntax Error: .mkd file does not start with #BEGIN");
		return;
	}

	@Override
	public void head() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sHEAD)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			title();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eHEAD)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: head");
		}
		else return;
	}

	@Override
	public void title() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sTITLE)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eTITLE)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: title");
		}
		else return;
	}

	
	@Override
	public void body() throws CompilerException {
		while(onceMore()){
			variableDefine();
			paragraph(); 
			innerItem();
		}
			checkSyntax();
	}


	@Override
	public void paragraph() throws CompilerException {
		
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sPARAGRAPH)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			variableDefine();
			while(onceMore())
				innerItem();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.ePARAGRAPH)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: paragraph " + Compiler.inputQueue.peek());
		}
		else return;

	}

	@Override
	public void innerText() throws CompilerException {
			while(!Arrays.asList(Tokens.TOKENS).contains(Compiler.inputQueue.peek().toUpperCase()))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
	}

	@Override
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
			}else throw new CompilerException("Error: Variable Definition");
		}
		
	}

	@Override
	public void variableUse() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.uVARIABLE)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eVARIABLE))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			else
				throw new CompilerException("Syntax Error: Variable use");
		}

	}

	@Override
	public void bold() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sBOLD)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eBOLD)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: bold");
		}
		else return;

	}

	@Override
	public void italics() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sITALIC)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eITALIC)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: italic");
		}
		else return;

	}

	@Override
	public void listitem() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sLIST)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			while(onceMore())
				innerItem();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eLIST)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}
			else throw new CompilerException("Syntax Error: list item");
		}
		else return;

	}

	@Override
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
		else if(!Arrays.asList(Tokens.TOKENS).contains(Compiler.inputQueue.peek().toUpperCase()))
			innerText();		
		else { }
		
			
	}


	@Override
	public void link() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sLINKDESCRIPTION)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		innerText();
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eLINKDESCRIPTION))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		else throw new CompilerException("Syntax Error: list item");
		
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		innerText();
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS))
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
		else throw new CompilerException("Syntax Error: list item");
		}
	  else return;

	}

	@Override
	public void audio() throws CompilerException {
		if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sAUDIO)){
			Compiler.outputQueue.add(Compiler.inputQueue.remove());
			innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.sADDRESS)){
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
				innerText();
			if(Compiler.inputQueue.peek().equalsIgnoreCase(Tokens.eADDRESS))
				Compiler.outputQueue.add(Compiler.inputQueue.remove());
			}else throw new CompilerException("error: address");
		}
	}

	@Override
	public void video() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void newline() throws CompilerException {
		// TODO Auto-generated method stub

	}
	
	public boolean onceMore() throws CompilerException{
		innerText();
		String next = Compiler.inputQueue.peek();
		return next.equalsIgnoreCase(Tokens.sAUDIO) || next.equalsIgnoreCase(Tokens.sBOLD) || next.equalsIgnoreCase(Tokens.sITALIC) ||
				next.equalsIgnoreCase(Tokens.sLINKDESCRIPTION) || next.equalsIgnoreCase(Tokens.sLIST) || next.equalsIgnoreCase(Tokens.uVARIABLE) 
				|| next.equalsIgnoreCase(Tokens.sPARAGRAPH) || next.equalsIgnoreCase(Tokens.sVARIABLE);
				
	}
	
	public void checkSyntax() throws CompilerException {
		if(!Arrays.asList(Tokens.TOKENS).contains(Compiler.inputQueue.peek().toUpperCase()) || !Compiler.inputQueue.peek().equals(Tokens.eHTML))
			throw new CompilerException("Syntax Error - Unexpected token: " + Compiler.inputQueue.peek());
	}

}
