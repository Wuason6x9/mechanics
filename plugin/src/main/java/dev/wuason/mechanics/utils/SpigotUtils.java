package dev.wuason.mechanics.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SpigotUtils {
    private final static String BASE_URL = "https://api.spiget.org/v2";
    private final static String LATEST_VERSION_URL = BASE_URL + "/resources/{resource}/versions/latest";
    private final static String STATUS = BASE_URL + "/status";


    public static String getLastVersionByResourceId(int id) {
        if(!isOnline()) return null;
        HttpGet httpGet = new HttpGet(LATEST_VERSION_URL.replace("{resource}", String.valueOf(id)));
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() != 200) return null;
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
                return jsonObject.get("name").getAsString();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isOnline() {
        HttpGet httpGet = new HttpGet(STATUS);
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
                httpClient.close();
                return response.getStatusLine().getStatusCode() == 200;
            }
            catch (IOException e) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

}
