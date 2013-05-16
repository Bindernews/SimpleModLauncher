package com.github.modlauncher.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class HelpTextField extends JPasswordField implements FocusListener{
	private static final long serialVersionUID = 1L;
	
	public static char PASSWORD_ECHO = '\u25CF';
	private char echoChar = (char)0;
	private String helpText;
	private boolean helpShowing = false;
	
	public HelpTextField() {
		super();
		init("");
	}
	
	public HelpTextField(int columns, String help) {
		super(columns);
		init(help);
	}
	
	public HelpTextField(String help) {
		super();
		init(help);
	}
	
	private void init(String help) {
		helpText = help;
		addFocusListener(this);
		onFocusLost();
	}
	
	public String getHelpText() {
		return helpText;
	}
	
	public void setHelpText(String help) {
		helpText = help;
	}
	
	@Override
	public void setEchoChar(char c) {
		echoChar = c;
	}
	
	@Override
	public char getEchoChar() {
		return echoChar;
	}
	
	@Override
	public String getText() {
		if (helpShowing)
			return "";
		else
			return new String(getPassword()); 
	}
	
	@Override
	public void setText(String text) {
		onFocusGained();
		super.setText(text);
		if (!hasFocus()) {
			onFocusLost();
		}
	}
	
	private void onFocusLost() {
		if (getText().length() == 0) {
			super.setForeground(Color.GRAY);
			super.setText(helpText);
			helpShowing = true;
			echoChar = super.getEchoChar();
			super.setEchoChar((char)0);
		}
	}
	
	private void onFocusGained() {
		if (helpShowing) {
			super.setForeground(Color.BLACK);
			super.setText("");
			helpShowing = false;
			super.setEchoChar(echoChar);
		}
	}
	
	public void focusGained(FocusEvent e) {
		onFocusGained();
	}
	
	public void focusLost(FocusEvent e) {
		onFocusLost();
	}
}
