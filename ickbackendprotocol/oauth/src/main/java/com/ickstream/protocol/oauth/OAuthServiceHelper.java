package com.ickstream.protocol.oauth;

public class OAuthServiceHelper {
    public static String getDeviceOptimizedStylesheetHeader() {
        StringBuilder sb = new StringBuilder();
        String styleSheetUrl = System.getProperty("ickstream-core-stylesheet-url", "https://api.ickstream.com/ickstream-cloud-core");
        sb.append("<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>\n" +
                "<!--[if !IE]>-->\n" +
                "<link media=\"only screen and (max-device-width: 480px)\" href=\"").append(styleSheetUrl).append("/small-device.css\" type= \"text/css\" rel=\"stylesheet\">\n" +
                "<!--<![endif]-->\n" +
                "<link media=\"screen and (min-device-width: 481px)\" href=\"").append(styleSheetUrl).append("/large-device.css\" type=\"text/css\" rel=\"stylesheet\">\n");
        return sb.toString();
    }
}
