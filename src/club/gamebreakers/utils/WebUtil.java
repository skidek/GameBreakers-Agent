package club.gamebreakers.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

public class WebUtil {

    public static Tuple<String, Integer> get(String url) {
        if (!url.startsWith("http")) url = "http://" + url;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:69.0) Gecko/20100101 Firefox/69.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            int code = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while((line = br.readLine()) != null) response.append(line);
            }

            return new Tuple<>(response.toString(), code);
        } catch (Exception ex) {
            if (
                    !(ex instanceof SocketTimeoutException)
                            && !(ex instanceof ConnectException)
                            && !(ex instanceof NoRouteToHostException)) ex.printStackTrace();
        }
        return null;
    }

    public static Tuple<String, Integer> postBody(String url, String body) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                dos.writeBytes(body);
                dos.flush();
            }

            int code = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while((line = br.readLine()) != null) response.append(line);
            }

            return new Tuple<>(response.toString(), code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Tuple<String, Integer> post(String url, Map<String, String> params) {
        if (!url.startsWith("http")) url = "http://" + url;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:71.0) Gecko/20100101 Firefox/71.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            StringBuilder parameters = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
            }
            String requestParams = parameters.toString().trim();
            connection.setDoOutput(true);
            try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
                dos.writeBytes(requestParams);
                dos.flush();
            }

            int code = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while((line = br.readLine()) != null) response.append(line);
            }

            return new Tuple<>(response.toString(), code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String encode(String input) {
        final char[] charArray = input.toCharArray();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length; ++i) {
            if (charArray[i] <= '\u007f') {
                sb.append(charArray[i]);
            }
            else {
                sb.append(String.format("\\u%04x", (int)charArray[i]));
            }
        }
        return sb.toString();
    }

}
