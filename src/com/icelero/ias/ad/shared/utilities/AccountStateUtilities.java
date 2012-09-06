package com.icelero.ias.ad.shared.utilities;

import java.util.ArrayList;
import java.util.List;

public class AccountStateUtilities {

	public static final String[] STATE = new String[] { "ACTIVE", "SUSPENDED", "BLACKLISTED" };

	private AccountStateUtilities() {
	}

	public static List<String> getState() {
		return asList(STATE);
	}

	public static List<String> asList(String[] elements) {
		List<String> list = new ArrayList<String>();
		for (String element : elements) {
			list.add(element);
		}
		return list;
	}

	public static boolean isValidState(String state) {
		if (state.equalsIgnoreCase("ACTIVE")) {
			return true;
		} else if (state.equalsIgnoreCase("SUSPENDED")) {
			return true;
		} else if (state.equalsIgnoreCase("BLACKLISTED")) {
			return true;
		}
		return false;
	}

}
