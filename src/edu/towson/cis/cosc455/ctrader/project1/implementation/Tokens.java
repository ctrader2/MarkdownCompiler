package edu.towson.cis.cosc455.ctrader.project1.implementation;
//This class stores all of the legal tokens that will be accepted within the markdown languge

import java.util.ArrayList;

/*The naming convention I will use for token variables is lowercase 's' or 'e' for opening or closing html tags 
followed by the html tag they are describing in all caps*/ 

public class Tokens {
	
	//<html and </html>
	public static final String sHTML = "#BEGIN";
	public static final String eHTML = "#END";
	
	//<head> and </head>
	public static final String sHEAD = "^";
	public static final String eHEAD = "^";
	
	//<title> and </title>
	public static final String sTITLE = "<";
	public static final String eTITLE = ">";
	
	//<p> and </p>
	public static final String sPARAGRAPH = "{";
	public static final String ePARAGRAPH = "}";
	
	//<strong and </strong>
	public static final String sBOLD = "**";
	public static final String eBOLD = "**";
	
	//<i> and </i>
	public static final String sITALIC = "*";
	public static final String eITALIC = "*";
	
	//<li> and </li>
	public static final String sLIST = "+";
	public static final String eLIST = ";";
	
	//<br/>
	public static final String sBREAK = "~";
	
	//Link with Description, <ahref = link>text</a>
	public static final String sLINKDESCRIPTION = "[";
	public static final String eLINKDESCRIPTION = "]";
	
	//Used to signal source addresses for links, audio, and addresses
	public static final String sADDRESS = "(";
	public static final String eADDRESS = ")";
	
	//<audio controls><source src></audio>
	public static final String sAUDIO = "@";
	
	//<iframe/>
	public static final String sVIDEO = "%";
	
	//Variables
	public static final String sVARIABLE = "$DEF";
	public static final String eVARIABLE = "$END";
	public static final String uVARIABLE = "$USE";
	public static final String dVARIABLE = "=";
			
	
	//Declare Array of Legal Characters
	public static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789,.'\":?_!/".toCharArray();
	public static final String TAB = "\t";
	public static final String NEWLINE = "\n";
	
	//Declare Array of Legal Tokens
	public static final String[] TOKENS = new String[]{sHTML, eHTML, sHEAD, eHEAD, sTITLE, eTITLE, sPARAGRAPH, ePARAGRAPH, sBOLD, eBOLD, 
			sITALIC, eITALIC, sLIST, eLIST, sBREAK, sLINKDESCRIPTION, eLINKDESCRIPTION, sADDRESS, eADDRESS, sAUDIO, sVIDEO, sVARIABLE, eVARIABLE, uVARIABLE, dVARIABLE};
	}
	

