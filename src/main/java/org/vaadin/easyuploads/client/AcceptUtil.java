package org.vaadin.easyuploads.client;

import java.util.ArrayList;
import java.util.List;

public class AcceptUtil {

    public static boolean accepted(String fileName, String fileType,
            List<String> accepted) {
        if (accepted.isEmpty()) {
            return true; // no limit
        }
        boolean matchesFileEnding = false;
        boolean matchesMimeType = false;
        boolean hasFileEndingLimiter = false;
        boolean hasMimeTypeLimiter = false;

        for (String limiter : accepted) {
            if (limiter.startsWith(".")) {
                hasFileEndingLimiter = true;
                if (fileName.toLowerCase().endsWith(limiter)) {
                    matchesFileEnding = true;
                    if (matchesMimeType) {
                        break;
                    }
                }
            } else if (limiter.contains("/")) {
                hasMimeTypeLimiter = true;
                if (fileType.equalsIgnoreCase(limiter)) {
                    matchesMimeType = true;
                    if (matchesFileEnding) {
                        break;
                    }
                } else if (limiter.contains("/*") && fileType.toLowerCase()
                        .startsWith(limiter.toLowerCase().substring(0,
                                limiter.indexOf("*")))) {
                    matchesMimeType = true;
                    if (matchesFileEnding) {
                        break;
                    }
                }
            }
        }

        if (!hasFileEndingLimiter) {
            matchesFileEnding = true;
        }
        if (!hasMimeTypeLimiter) {
            matchesMimeType = true;
        }

        return matchesFileEnding && matchesMimeType;
    }

    public static List<String> unpack(String accept) {
        List<String> accepted = new ArrayList<String>();
        if (accept != null) {
            String[] tokens = accept.split(",");
            for (String token : tokens) {
                String trimmed = token.trim();
                if (!accepted.contains(trimmed)) {
                    accepted.add(trimmed);
                }
            }
        }
        return accepted;
    }
}
