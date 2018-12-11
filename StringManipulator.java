package net.ddns.tuxchen;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.security.*;
import java.math.*;

/**
 * With this tool you can your text convert to binary texts, or you can make forwarding texts to reversing texts and you can convert your normal texts to 1337 texts.<br>
 * The tabs are: <ul>
 * <li><b>binary Text</b><br>
 * <li><b>reverse Text</b><br>
 * <li><b>1337 Text</b>
 * </ul>
 * @author jmueller developed by J. Mueller
 * @version 1.0
 */

public class StringManipulator extends JFrame implements Runnable {
	private Container c;
	// Tab 1
	private JTextArea binCode;
	private JScrollPane scrollBinCode;
	private JButton binToAscii, asciiToBin, clearBoth;
	private JTextArea asciiCode;
	private JScrollPane scrollAsciiCode;
	// Tab 2
	private JTextArea forwardText;
	private JScrollPane scrollForwardText;
	private JButton forwardToReverse, clearFRTexts;
	private JTextArea reverseText;
	private JScrollPane scrollReverseText;
	// Tab 3
	private JTextArea normalText;
	private JScrollPane scrollNormalText;
	private JButton normalToLeet, clearNLTexts;
	private JTextArea leetText;
	private JScrollPane scrollLeetText;
	// Tab 4
	private JTextArea decryptedText;
	private JScrollPane scrollDecryptedText;
	private JButton encryptText, clearDETexts;
	private JTextArea encryptedText;
	private JScrollPane scrollEncryptedText;
	// Tab 5
	private JTextArea hexcode;
	private JScrollPane scrollHexcode;
	private JComboBox choice;
	private JButton convertToHex, convertToAscii, clearHexAndAscii;
	private JTextArea decryptedHexCode;
	private JScrollPane scrollDecryptedHexCode;
	// Tab 6
	private JTextArea clearText;
	private JScrollPane scrollClearText;
	private JLabel lbl_exp;
	private JLabel lbl_mod;
	private JTextField exponent;
	private JTextField modul;
	private JButton enc, dec, clearRSA;
	private JTextArea encText;
	private JScrollPane scrollEncText;
	// Menu
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem copy;
	// MenuBar bottom
	private JToolBar info;
	private JLabel decrypt;
	private JLabel encrypt;
	private Thread count;
	private boolean run;
	// utilities
	private JToolBar tb;
	private JTabbedPane tp;
	private JPanel panel1; // binary to ascii and the other way around
	private JPanel panel2; // reverse texts
	private JPanel panel3; // 1337 texts
	private JPanel panel4; // MD5 texts
	private JPanel panel5; // Hex code
	private JPanel panel6; // RSA
	private final Font font = new Font("Courier New", Font.PLAIN, 16);
	
	/**
	 * Rules the events which called by clicking on a button
	 * 
	 * @author jmueller developed by J. Mueller
	 */
	private class ClickButton implements ActionListener {
		
		/**
		 * It's called when a button clicked
		 * 
		 * @param e ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == asciiToBin) {
				binCode.setText(convertToBinary(readFromAsciiCode()));
			}
			else if(e.getSource() == binToAscii) {
				asciiCode.setText(convertToAscii(readFromBinCode()));
			}
			else if(e.getSource() == clearBoth) {
				writeInBinCode("");
				writeInAsciiCode("");
			}
			else if(e.getSource() == forwardToReverse) {
				writeInReverseText(reverseText(readFromForwardText()));
			}
			else if(e.getSource() == clearFRTexts) {
				reverseText.setText("");
				forwardText.setText("");
			}
			else if(e.getSource() == normalToLeet) {
				leetText.setText(convertToLeet(readFromNormalText()));
			}
			else if(e.getSource() == clearNLTexts) {
				leetText.setText("");
				normalText.setText("");
			}
			else if(e.getSource() == encryptText) {
				encryptedText.setText(md5(readFromDecryptedText()));
			}
			else if(e.getSource() == clearDETexts) {
				encryptedText.setText("");
				decryptedText.setText("");
			}
			else if(e.getSource() == convertToHex) {
				
				hexcode.setText("");
				String text = decryptedHexCode.getText();
				String hex = "";
				
				for(int i = 0; i < text.length(); i++) {
					hexcode.append(convertToHexCode(decryptedHexCode.getText().charAt(i) + ""));
					hexcode.append(" ");
					hexcode.setCaretPosition(hexcode.getText().length());
				}
			}
			else if(e.getSource() == convertToAscii) {
				String hex = hexcode.getText();
				String[] hexSplit = hex.split(" ");
				String text = "";
				
				for(String i : hexSplit) {
					text += hexToAscii(i);
				}
				
				decryptedHexCode.setText(text);
			}
			else if(e.getSource() == clearHexAndAscii) {
				hexcode.setText("");
				decryptedHexCode.setText("");
			}
			else if(e.getSource() == copy) {
				if(tp.getSelectedIndex() == 0) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(binCode.getText()), null);
				}
				else if(tp.getSelectedIndex() == 1) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(reverseText.getText()), null);
				}
				else if(tp.getSelectedIndex() == 2) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(leetText.getText()), null);
				}
				else if(tp.getSelectedIndex() == 3) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(encryptedText.getText()), null);
				}
				else if(tp.getSelectedIndex() == 4) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(hexcode.getText()), null);
				}
				else if(tp.getSelectedIndex() == 5) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(encText.getText()), null);
				}
			}
			else if(e.getSource() == enc) {
				try {
					String text = clearText.getText();
					String encryptedText = "";
					BigInteger exp = new BigInteger(exponent.getText());
					BigInteger mod = new BigInteger(modul.getText());
					BigInteger zahl;
				
					for(int i = 0; i < text.length(); i++) {
						zahl = new BigInteger((int)text.charAt(i)+"");
						encryptedText += zahl.modPow(exp, mod) + " ";
					}
				
					encText.setText(encryptedText);
				}
				catch(NumberFormatException exp) {
					JOptionPane.showMessageDialog(c, "Enter a number in the 'exponent' and 'modul' textfield!");
				}
			}
			else if(e.getSource() == dec) {
				try {
					BigInteger exp = new BigInteger(exponent.getText());
					BigInteger mod = new BigInteger(modul.getText());
					long m = Long.parseLong(modul.getText());
					long phiM = ((long) fermat((double)m)-1) * ((long) (m/fermat((double)m)-1));
					BigInteger phiModul = new BigInteger(phiM+"");
					BigInteger key = exp.modInverse(phiModul);
					
					StringTokenizer st = new StringTokenizer(encText.getText());
					
					String decryptedText = "";
					String temp;
					BigInteger zahl;
					
					while(st.hasMoreTokens()) {
						temp = st.nextToken();
						zahl = new BigInteger(temp);
						
						decryptedText += (char) zahl.modPow(key, mod).intValue();
					}
					
					clearText.setText(decryptedText);
				}
				catch(NumberFormatException exp) {
					JOptionPane.showMessageDialog(c, "Enter a number in the 'exponent' and 'modul' and 'decrypted text' textfield!");
				}
				catch(ArithmeticException exp) {
					JOptionPane.showMessageDialog(c, "'exponent' do not to be a divisor of phi(modul)!");
				}
			}
			else if(e.getSource() == clearRSA) {
				encText.setText("");
				clearText.setText("");
			}
		}
		
		/**
		 * Encrypt the transfered text with the md5 algorithm
		 * 
		 * @param text String
		 * @return String
		 */
		public String md5(String text) {
			StringBuffer hexString = new StringBuffer();
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(text.getBytes());
				
				byte byteData[] = md.digest();
				
				//convert the byte to hex format
				for(int i = 0; i < byteData.length; i++) {
					String hex = Integer.toHexString(0xff & byteData[i]);
					if(hex.length() == 1) {
						hexString.append('0');
					}
					hexString.append(hex);
				}
				
			}
			catch(NoSuchAlgorithmException e) {
			}
			
			return hexString.toString();
		}
		
		/**
		 * Converts the transfered text to 1337
		 * 
		 * @param text String
		 * @return String
		 */
		public String convertToLeet(String text) {
			String leetText = "";
			
			for(int i = 0; i < text.length(); i++) {
				if(text.charAt(i) == 'A' || text.charAt(i) == 'a') {
					leetText += "4";
				}
				else if(text.charAt(i) == 'B' || text.charAt(i) == 'b') {
					leetText += "8";
				}
				else if(text.charAt(i) == 'C' || text.charAt(i) == 'c') {
					leetText += "<";
				}
				else if(text.charAt(i) == 'D' || text.charAt(i) == 'd') {
					leetText += "|)";
				}
				else if(text.charAt(i) == 'E' || text.charAt(i) == 'e') {
					leetText += "3";
				}
				else if(text.charAt(i) == 'H' || text.charAt(i) == 'h') {
					leetText += "|-|";
				}
				else if(text.charAt(i) == 'I' || text.charAt(i) == 'i') {
					leetText += "!";
				}
				else if(text.charAt(i) == 'K' || text.charAt(i) == 'k') {
					leetText += "|<";
				}
				else if(text.charAt(i) == 'L' || text.charAt(i) == 'l') {
					leetText += "1";
				}
				else if(text.charAt(i) == 'M' || text.charAt(i) == 'm') {
					leetText += "|\\/|";					
				}
				else if(text.charAt(i) == 'N' || text.charAt(i) == 'n') {
					leetText += "|\\|";
				}
				else if(text.charAt(i) == 'O' || text.charAt(i) == 'o') {
					leetText += "<>";
				}
				else if(text.charAt(i) == 'S' || text.charAt(i) == 's') {
					leetText += "5";
				}
				else if(text.charAt(i) == 'T' || text.charAt(i) == 't') {
					leetText += "7";
				}
				else if(text.charAt(i) == 'U' || text.charAt(i) == 'u') {
					leetText += "v";
				}
				else if(text.charAt(i) == 'V' || text.charAt(i) == 'v') {
					leetText += "\\/";
				}
				else if(text.charAt(i) == 'W' || text.charAt(i) == 'w') {
					leetText += "\\/\\/";
				}
				else if(text.charAt(i) == 'X' || text.charAt(i) == 'x') {
					leetText += "><";
				}
				else {
					leetText += text.charAt(i) + "";
				}
			}
			
			return leetText;
		}
		
		public boolean isInteger(double zahl) {
			int iZahl = (int) zahl;
			
			if((zahl - iZahl) != 0.0) {
				return false;
			}
			
			return true;
		}
		
		public boolean isEven(double zahl) {
					
			int iZahl = (int) zahl;
			
			if((iZahl % 2) == 0) {
				return true;
			}
			
			return false;
		}
		
		public double fermat(double n) {
			double a = Math.ceil(Math.sqrt(n));
			double bSquared = Math.pow(a, 2) - n;
			
			if(isEven(n)) {
				return 2.0;
			}
			else {
				while(!isInteger(Math.sqrt(bSquared))) {
					a++;
					bSquared = Math.pow(a, 2) - n;
				}
			}
			
			return (a - Math.sqrt(bSquared));
		}
		
		/**
		 * Gets the reversed text of the transfered text
		 * 
		 * @param text String
		 * @return String
		 */
		public String reverseText(String text) {
			String reverseText = "";
			for(int i = text.length()-1; i >= 0; i--) {
				reverseText += text.charAt(i) + "";
			}
			
			return reverseText;
		}
		
		/**
		 * Converts the transfered text to binary code
		 * 
		 * @param text String
		 * @return String
		 */
		public String convertToBinary(String text) {
			String binaryCode = "";
			String temp = "";
			int dezimal;
			int helper;
			
			for(int i = 0; i < text.length(); i++) {
				dezimal = (int) text.charAt(i);
				
				do {
					helper = dezimal / 2;
					if(dezimal % 2 == 0) {
						temp += "0";
					}
					else {
						temp += "1";
					}
					
					dezimal = helper;
				} while(dezimal != 0);
				
				while(temp.length() < 8) {
					temp += "0";
				}
				
				for(int j = temp.length()-1; j >= 0; j--) {
					binaryCode += temp.charAt(j);
					
				}
				
				binaryCode += " ";
				temp = "";
				
			}
			
			return binaryCode;
		}
		
		/**
		 * Converts the transfered binary code to an ASCII code
		 * 
		 * @param text String
		 * @return String
		 */
		public String convertToAscii(String text) {
			int stelle;
			int dezimal = 0;
			String asciiCode = "";
			String temp = "";
			StringTokenizer st = new StringTokenizer(text);
			
			while(st.hasMoreTokens()) {
				temp = st.nextToken();
				stelle = 1;
				dezimal = 0;
				for(int i = temp.length()-1; i >= 0; i--) {
					if(temp.charAt(i) == '1') {
						dezimal += stelle;
					}
					
					stelle *= 2;
				}
				
				asciiCode += ((char) dezimal);
			}
			
			return asciiCode;
		}
		
		public String convertToHexCode(String text) {
			String hex = "";
			String binary = convertToBinary(text);
			String[] temp = new String[2];
			
			temp[0] = binary.substring(0, 4);
			temp[1] = binary.substring(4, 8);
			
			temp[0] = convertToDecimal(temp[0]) + "";
			temp[1] = convertToDecimal(temp[1]) + "";
			
			for(int i = 0; i < 2; i++) {
				if(Integer.parseInt(temp[i]) == 10) {
					hex += (choice.getSelectedIndex() == 0 ? "A" : "a");
				}
				else if(Integer.parseInt(temp[i]) == 11) {
					hex += (choice.getSelectedIndex() == 0 ? "B" : "b");
				}
				else if(Integer.parseInt(temp[i]) == 12) {
					hex += (choice.getSelectedIndex() == 0 ? "C" : "c");
				}
				else if(Integer.parseInt(temp[i]) == 13) {
					hex += (choice.getSelectedIndex() == 0 ? "D" : "d");
				}
				else if(Integer.parseInt(temp[i]) == 14) {
					hex += (choice.getSelectedIndex() == 0 ? "E" : "e");
				}
				else if(Integer.parseInt(temp[i]) == 15) {
					hex += (choice.getSelectedIndex() == 0 ? "F" : "f");
				}
				else {
					hex += temp[i];
				}
			}
			
			return hex;
		}
		
		public int convertToDecimal(String text) {
			int zahl = 0;
			int stelle = 1;
			
			for(int i = text.length()-1; i >= 0; i--) {
				if(text.charAt(i) == '1') {
					zahl += stelle;
				}
				
				stelle *= 2;
			}
			
			return zahl;
		}
		
		public char hexToAscii(String hex) {
			String t1 = hex.charAt(0) + "";
			String t2 = hex.charAt(1) + "";
			
			t1 = t1.replace("A", "10");
			t1 = t1.replace("B", "11");
			t1 = t1.replace("C", "12");
			t1 = t1.replace("D", "13");
			t1 = t1.replace("E", "14");
			t1 = t1.replace("F", "15");
			
			t1 = t1.replace("a", "10");
			t1 = t1.replace("b", "11");
			t1 = t1.replace("c", "12");
			t1 = t1.replace("d", "13");
			t1 = t1.replace("e", "14");
			t1 = t1.replace("f", "15");
			
			t2 = t2.replace("A", "10");
			t2 = t2.replace("B", "11");
			t2 = t2.replace("C", "12");
			t2 = t2.replace("D", "13");
			t2 = t2.replace("E", "14");
			t2 = t2.replace("F", "15");
			
			t2 = t2.replace("a", "10");
			t2 = t2.replace("b", "11");
			t2 = t2.replace("c", "12");
			t2 = t2.replace("d", "13");
			t2 = t2.replace("e", "14");
			t2 = t2.replace("f", "15");
			
			t1 = binCon(Integer.parseInt(t1));
			t2 = binCon(Integer.parseInt(t2));
			
			while(t2.length() < 4) {
				t2 = "0" + t2;
			}
			
			String bin = t1 + t2;
			
			int dec = convertToDecimal(bin);
			
			return (char) dec;
		}
		
		public String binCon(int dec) {
			String helper = "";
			String bin = "";
			
			while(dec != 0) {
				helper += dec % 2;
				dec /= 2;
			}
			
			for(int i = helper.length()-1; i >= 0; i--) {
				bin += helper.charAt(i) + "";
			}
			
			return bin;
		}
	}
	
	

	/**
	 * The constructor build up the GUI
	 */
	public StringManipulator() {
		c = this.getContentPane();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(c, e.getMessage());
		}
		
		tb = new JToolBar("RSA Settings");
		
		exponent = new JTextField("", 10);
		exponent.setFont(font);
		
		modul = new JTextField("", 10);
		modul.setFont(font);
		
		lbl_exp = new JLabel("Exponent: ");
		lbl_exp.setHorizontalAlignment(JLabel.RIGHT);
		lbl_exp.setFont(font);
		
		lbl_mod = new JLabel("Modul: ");
		lbl_mod.setHorizontalAlignment(JLabel.RIGHT);
		lbl_mod.setFont(font);
		
		tb.add(lbl_exp);
		tb.add(exponent);
		tb.add(lbl_mod);
		tb.add(modul);
		
		tp = new JTabbedPane();
		
		panel1 = new JPanel(new BorderLayout());
		
		// MenuBar initialized
		menuBar = new JMenuBar();
		menuBar.add(Box.createGlue());
		
		menu = new JMenu("Copy");
		menu.setMnemonic(KeyEvent.VK_C);
		
		copy = new JMenuItem("Copy");
		copy.setMnemonic(KeyEvent.VK_C);
		copy.addActionListener(new ClickButton());
		
		menu.add(copy);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
		// textarea for binary code
		binCode = new JTextArea();
		binCode.setLineWrap(true);
		binCode.setWrapStyleWord(true);
		binCode.setFont(font);
		binCode.setRows(10);
		scrollBinCode = new JScrollPane(binCode);
		scrollBinCode.setBorder(new TitledBorder("binary code"));
		
		panel1.add(BorderLayout.NORTH, scrollBinCode);
		
		JPanel center = new JPanel(new FlowLayout());
		
		// button for binary to ASCII
		binToAscii = new JButton("Binary to ASCII");
		binToAscii.setHorizontalAlignment(JLabel.CENTER);
		binToAscii.setFont(font);
		binToAscii.addActionListener(new ClickButton());
		
		// button for ASCII to binary
		asciiToBin = new JButton("ASCII to binary");
		asciiToBin.setHorizontalAlignment(JLabel.CENTER);
		asciiToBin.setFont(font);
		asciiToBin.addActionListener(new ClickButton());
		
		// button for clear both
		clearBoth = new JButton("Clear");
		clearBoth.setHorizontalAlignment(JLabel.CENTER);
		clearBoth.setFont(font);
		clearBoth.addActionListener(new ClickButton());
		
		center.add(binToAscii);
		center.add(asciiToBin);
		center.add(clearBoth);
		
		panel1.add(BorderLayout.CENTER, center);
		
		asciiCode = new JTextArea();
		asciiCode.setLineWrap(true);
		asciiCode.setWrapStyleWord(true);
		asciiCode.setFont(font);
		asciiCode.setRows(10);
		scrollAsciiCode = new JScrollPane(asciiCode);
		scrollAsciiCode.setBorder(new TitledBorder("ASCII code"));
		
		panel1.add(BorderLayout.SOUTH, scrollAsciiCode);
		
		panel2 = new JPanel(new BorderLayout());
		
		forwardText = new JTextArea();
		forwardText.setLineWrap(true);
		forwardText.setWrapStyleWord(true);
		forwardText.setFont(font);
		forwardText.setRows(10);
		scrollForwardText = new JScrollPane(forwardText);
		scrollForwardText.setBorder(new TitledBorder("normal Text"));
		
		JPanel centerReverse = new JPanel(new FlowLayout());
		
		forwardToReverse = new JButton("Reverse Text");
		forwardToReverse.setFont(font);
		forwardToReverse.addActionListener(new ClickButton());
		
		clearFRTexts = new JButton("Clear");
		clearFRTexts.setFont(font);
		clearFRTexts.addActionListener(new ClickButton());
		
		centerReverse.add(forwardToReverse);
		centerReverse.add(clearFRTexts);
		
		reverseText = new JTextArea();
		reverseText.setLineWrap(true);
		reverseText.setWrapStyleWord(true);
		reverseText.setFont(font);
		reverseText.setRows(10);
		scrollReverseText = new JScrollPane(reverseText);
		scrollReverseText.setBorder(new TitledBorder("reverse Text"));
		
		panel2.add(BorderLayout.NORTH, scrollForwardText);
		panel2.add(BorderLayout.CENTER, centerReverse);
		panel2.add(BorderLayout.SOUTH, scrollReverseText);
		
		panel3 = new JPanel(new BorderLayout());
		
		normalText = new JTextArea();
		normalText.setLineWrap(true);
		normalText.setWrapStyleWord(true);
		normalText.setFont(font);
		normalText.setRows(10);
		scrollNormalText = new JScrollPane(normalText);
		scrollNormalText.setBorder(new TitledBorder("normal Text"));
		
		JPanel centerLeet = new JPanel(new FlowLayout());
		
		normalToLeet = new JButton("Convert to 1337");
		normalToLeet.setFont(font);
		normalToLeet.addActionListener(new ClickButton());
		
		clearNLTexts = new JButton("Clear");
		clearNLTexts.setFont(font);
		clearNLTexts.addActionListener(new ClickButton());
		
		centerLeet.add(normalToLeet);
		centerLeet.add(clearNLTexts);
		
		leetText = new JTextArea();
		leetText.setLineWrap(true);
		leetText.setWrapStyleWord(true);
		leetText.setFont(font);
		leetText.setRows(10);
		scrollLeetText = new JScrollPane(leetText);
		scrollLeetText.setBorder(new TitledBorder("1337 Text"));
		
		panel3.add(BorderLayout.NORTH, scrollNormalText);
		panel3.add(BorderLayout.CENTER, centerLeet);
		panel3.add(BorderLayout.SOUTH, scrollLeetText);
		
		panel4 = new JPanel(new BorderLayout());
		
		decryptedText = new JTextArea();
		decryptedText.setLineWrap(true);
		decryptedText.setWrapStyleWord(true);
		decryptedText.setFont(font);
		decryptedText.setRows(10);
		scrollDecryptedText = new JScrollPane(decryptedText);
		scrollDecryptedText.setBorder(new TitledBorder("decrypted Text"));
		
		JPanel centermd5 = new JPanel(new FlowLayout());
		
		encryptText = new JButton("Encrypt with MD5");
		encryptText.setFont(font);
		encryptText.addActionListener(new ClickButton());
		
		clearDETexts = new JButton("Clear");
		clearDETexts.setFont(font);
		clearDETexts.addActionListener(new ClickButton());
		
		centermd5.add(encryptText);
		centermd5.add(clearDETexts);
		
		encryptedText = new JTextArea();
		encryptedText.setLineWrap(true);
		encryptedText.setWrapStyleWord(true);
		encryptedText.setFont(font);
		encryptedText.setRows(10);
		scrollEncryptedText = new JScrollPane(encryptedText);
		scrollEncryptedText.setBorder(new TitledBorder("encrypted Text"));
		
		panel4.add(BorderLayout.NORTH, scrollDecryptedText);
		panel4.add(BorderLayout.CENTER, centermd5);
		panel4.add(BorderLayout.SOUTH, scrollEncryptedText);
		
		panel5 = new JPanel(new BorderLayout());
		
		hexcode = new JTextArea();
		hexcode.setLineWrap(true);
		hexcode.setWrapStyleWord(true);
		hexcode.setFont(font);
		hexcode.setRows(10);
		scrollHexcode = new JScrollPane(hexcode);
		scrollHexcode.setBorder(new TitledBorder("Hex Code"));
		
		panel5.add(BorderLayout.NORTH, scrollHexcode);
		
		JPanel centerHex = new JPanel(new FlowLayout());
		
		convertToHex = new JButton("Convert to Hex");
		convertToHex.setFont(font);
		convertToHex.addActionListener(new ClickButton());
		
		choice = new JComboBox();
		choice.addItem("A-F");
		choice.addItem("a-f");
		choice.setFont(font);
		
		convertToAscii = new JButton("Convert to ASCII");
		convertToAscii.setFont(font);
		convertToAscii.addActionListener(new ClickButton());
		
		clearHexAndAscii = new JButton("Clear");
		clearHexAndAscii.setFont(font);
		clearHexAndAscii.addActionListener(new ClickButton());
		
		centerHex.add(convertToHex);
		centerHex.add(choice);
		centerHex.add(convertToAscii);
		centerHex.add(clearHexAndAscii);
		
		panel5.add(BorderLayout.CENTER, centerHex);
		
		decryptedHexCode = new JTextArea();
		decryptedHexCode.setLineWrap(true);
		decryptedHexCode.setWrapStyleWord(true);
		decryptedHexCode.setFont(font);
		decryptedHexCode.setRows(10);
		scrollDecryptedHexCode = new JScrollPane(decryptedHexCode);
		scrollDecryptedHexCode.setBorder(new TitledBorder("ASCII Code"));
		
		panel5.add(BorderLayout.SOUTH, scrollDecryptedHexCode);
		
		panel6 = new JPanel(new BorderLayout());
		
		clearText = new JTextArea();
		clearText.setLineWrap(true);
		clearText.setWrapStyleWord(true);
		clearText.setRows(10);
		clearText.setFont(font);
		scrollClearText = new JScrollPane(clearText);
		scrollClearText.setBorder(new TitledBorder("decrypted text"));
		
		JPanel centerInRSA = new JPanel(new FlowLayout());
		
		enc = new JButton("Encrypt");
		enc.setFont(font);
		enc.addActionListener(new ClickButton());
		
		dec = new JButton("Decrypt");
		dec.setFont(font);
		dec.addActionListener(new ClickButton());
		
		clearRSA = new JButton("Clear");
		clearRSA.setFont(font);
		clearRSA.addActionListener(new ClickButton());
		
		centerInRSA.add(enc);
		centerInRSA.add(dec);
		centerInRSA.add(clearRSA);
		
		encText = new JTextArea();
		encText.setLineWrap(true);
		encText.setWrapStyleWord(true);
		encText.setRows(10);
		encText.setFont(font);
		scrollEncText = new JScrollPane(encText);
		scrollEncText.setBorder(new TitledBorder("encrypted text"));
		
		panel6.add(BorderLayout.NORTH, scrollClearText);
		panel6.add(BorderLayout.CENTER, centerInRSA);
		panel6.add(BorderLayout.SOUTH, scrollEncText);
		
		tp.addTab("Binary Texts", panel1);
		tp.addTab("Reverse Texts", panel2);
		tp.addTab("1337 Texts", panel3);
		tp.addTab("MD5 Texts", panel4);
		tp.addTab("Hex Code", panel5);
		tp.addTab("RSA", panel6);
		
		info = new JToolBar("Lines");
		
		decrypt = new JLabel("Decrypt: 0");
		decrypt.setToolTipText(decrypt.getText());
		decrypt.setFont(font);
		
		encrypt = new JLabel("Encrypt: ");
		encrypt.setToolTipText(encrypt.getText());
		encrypt.setFont(font);
		
		info.add(Box.createGlue());
		info.add(decrypt);
		info.add(new JLabel("     "));
		info.add(encrypt);
		
		count = new Thread(this);
		count.start();
		
		c.add(BorderLayout.NORTH, tb);
		c.add(BorderLayout.CENTER, tp);
		c.add(BorderLayout.SOUTH, info);
	}
	
	public void run() {
		run = true;
		
		while(run) {
			if(count.isInterrupted()) {
				count.interrupt();
				run = false;
			}
			
			if(tp.getSelectedIndex() == 0) {
				decrypt.setText("Decrypt: " + asciiCode.getText().length());
				encrypt.setText("Encrypt: " + binCode.getText().length());
			}
			else if(tp.getSelectedIndex() == 1) {
				decrypt.setText("Decrypt: " + forwardText.getText().length());
				encrypt.setText("Encrypt: " + reverseText.getText().length());
			}
			else if(tp.getSelectedIndex() == 2) {
				decrypt.setText("Decrypt: " + normalText.getText().length());
				encrypt.setText("Encrypt: " + leetText.getText().length());
			}
			else if(tp.getSelectedIndex() == 3) {
				decrypt.setText("Decrypt: " + decryptedText.getText().length());
				encrypt.setText("Encrypt: " + encryptedText.getText().length());
			}
			else if(tp.getSelectedIndex() == 4) {
				decrypt.setText("Decrypt: " + decryptedHexCode.getText().length());
				encrypt.setText("Encrypt: " + hexcode.getText().length());
			}
			else if(tp.getSelectedIndex() == 5) {
				decrypt.setText("Decrypt: " + clearText.getText().length());
				encrypt.setText("Encrypt: " + encText.getText().length());
			}
			
			try {
				count.sleep(10);
			}
			catch(InterruptedException e) {
			}
		}
	}
	
	/**
	 * Returns the source of the JButton clearDETexts
	 * 
	 * @return JButton
	 */
	public JButton getClearDETextsSource() {
		return clearDETexts;
	}
	
	/**
	 * Returns the source of the JButton encryptText
	 * 
	 * @return JButton
	 */
	public JButton getEncryptTextSource() {
		return encryptText;
	}
	
	/**
	 * Returns the value of the TextArea encryptedText
	 * 
	 * @return String
	 */
	public String readFromEncryptedText() {
		return encryptedText.getText();
	}
	
	/**
	 * Returns the value of the TextArea decryptedText
	 * 
	 * @return String
	 */
	public String readFromDecryptedText() {
		return decryptedText.getText();
	}
	
	/**
	 * Writs the transfered text in the TextArea encryptedText
	 * 
	 * @param text String
	 */
	public void writeInEncryptedText(String text) {
		encryptedText.setText(text);
	}
	
	/**
	 * Writes the transfered text in the TextArea decryptedText
	 * 
	 * @param text String
	 */
	public void writeInDecryptedText(String text) {
		decryptedText.setText(text);
	}
	
	/**
	 * Gets the source of the JButton clearNLTexts
	 * 
	 * @return JButton
	 */
	public JButton getClearNLTextsSource() {
		return clearNLTexts;
	}
	
	/**
	 * Gets the source of the JButton normalToLeet
	 * 
	 * @return JButton
	 */
	public JButton getNormalToLeetSource() {
		return normalToLeet;
	}
	
	/**
	 * Gets the value of the TextArea leetText
	 * 
	 * @return String
	 */
	public String readFromLeetText() {
		return leetText.getText();
	}
	
	/**
	 * Gets the value of the TextArea normalText
	 * 
	 * @return String
	 */
	public String readFromNormalText() {
		return normalText.getText();
	}
	
	/**
	 * Writes the transfered text in the TextArea leetText
	 * 
	 * @param text String
	 */
	public void writeInLeetText(String text) {
		leetText.setText(text);
	}
	
	/**
	 * Writes the transfered text in the TextArea normalText
	 * 
	 * @param text String
	 */
	public void writeInNormalText(String text) {
		normalText.setText(text);
	}
	
	/**
	 * Gets the source of the JButton clearFRTexts
	 * 
	 * @return JButton
	 */
	public JButton getClearFRTextsSource() {
		return clearFRTexts;
	}
	
	/**
	 * Gets the source of the JButton forwardToReverse
	 * 
	 * @return JButton
	 */
	public JButton getForwardToReverseSource() {
		return forwardToReverse;
	}
	
	/**
	 * Gets the value of the TextArea reverseText
	 * 
	 * @return String
	 */
	public String readFromReverseText() {
		return reverseText.getText();
	}
	
	/**
	 * Gets the value of the TextArea forwardText
	 * 
	 * @return String
	 */
	public String readFromForwardText() {
		return forwardText.getText();
	}
	
	/**
	 * Writes the transfered text in the TextArea reverseText
	 * 
	 * @param text String
	 */
	public void writeInReverseText(String text) {
		reverseText.setText(text);
	}
	
	/**
	 * Writes the transfered text in the TextArea forwardText
	 * 
	 * @param text String
	 */
	public void writeInForwardText(String text) {
		forwardText.setText(text);
	}
	
	/**
	 * Writes the transfered text in the TextArea binCode
	 * 
	 * @param text String
	 */
	public void writeInBinCode(String text) {
		binCode.setText(text);
	}
	
	/**
	 * Writes the transfered text in the TextArea asciiCode
	 * 
	 * @param text
	 */
	public void writeInAsciiCode(String text) {
		asciiCode.setText(text);
	}
	
	/**
	 * Returns the source of binToAscii
	 * 
	 * @return JButton
	 */
	public JButton getBinToAsciiSource() {
		return binToAscii;
	}
	
	/**
	 * Returns the source of asciiToBin
	 * 
	 * @return JButton
	 */
	public JButton getAsciiToBinSource() {
		return asciiToBin;
	}
	
	/**
	 * Returns the source of clearBoth
	 * 
	 * @return JButton
	 */
	public JButton getClearBothSource() {
		return clearBoth;
	}
	
	/**
	 * Gets the value of the TextArea asciiCode
	 * 
	 * @return String
	 */
	public String readFromAsciiCode() {
		return asciiCode.getText();
	}
	
	/**
	 * Gets the value of the TextArea binCode
	 * 
	 * @return String
	 */
	public String readFromBinCode() {
		return binCode.getText();
	}
	
	/**
	 * This method will be called when the program was started.
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		StringManipulator window = new StringManipulator();
		window.setTitle("String Manipulator 1.0");
		window.setSize(600, 630);
		window.setResizable(false);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}