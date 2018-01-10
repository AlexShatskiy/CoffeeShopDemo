package com.sh.coffeeshop.controller.command.provider;

/**
 * command names in XML file
 * @author Shatskiy Alex
 * @version 1.0
 */
public enum CommandTagName {

	COMMANDS, COMMAND, NAME, REALIZATION;

	public static CommandTagName getElementTagName(String element) {
		switch (element) {
		case "commands":
			return COMMANDS;
		case "command":
			return COMMAND;
		case "name":
			return NAME;
		case "realization":
			return REALIZATION;
		default:    throw new RuntimeException(element);
		}
	}
}
