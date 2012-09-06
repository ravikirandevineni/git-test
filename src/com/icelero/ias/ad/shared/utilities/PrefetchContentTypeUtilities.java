package com.icelero.ias.ad.shared.utilities;

import java.util.ArrayList;
import java.util.List;

public class PrefetchContentTypeUtilities {

	public static final String[] CONTENT_TYPE = new String[] { "IMAGE", "VIDEO", "TEXT" };

	private PrefetchContentTypeUtilities() {
	}

	public static List<String> getContentTypes() {
		return asList(CONTENT_TYPE);
	}

	public static List<String> asList(String[] elements) {
		List<String> list = new ArrayList<String>();
		for (String element : elements) {
			list.add(element);
		}
		return list;
	}

	public static boolean isValidContentType(String type) {
		if (type.equalsIgnoreCase("IMAGE")) {
			return true;
		} else if (type.equalsIgnoreCase("VIDEO")) {
			return true;
		} else if (type.equalsIgnoreCase("TEXT")) {
			return true;
		}
		return false;
	}

}
