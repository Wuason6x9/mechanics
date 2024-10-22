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
    /**
     * The base URL for the Spiget API used to interact with the service endpoints.
     */
    private final static String BASE_URL = "https://api.spiget.org/v2";
    /**
     * URL for retrieving the latest version of a resource.
     * The `{resource}` placeholder should be replaced with the resource ID.
     */
    private final static String LATEST_VERSION_URL = BASE_URL + "/resources/{resource}/versions/latest";
    /**
     * The URL endpoint used for checking the status of the API.
     * This is constructed by appending "/status" to the BASE_URL.
     */
    private final static String STATUS = BASE_URL + "/status";


    /**
     * Retrieves the latest version string for a specified resource ID.
     *
     * @param id The unique identifier of the resource.
     * @return The latest version string of the resource or null if the resource is not found or if an error occurs.
     */
    public static String getLastVersionByResourceId(int id) {
        if(!isOnline()) return null;
        HttpGet httpGet = new HttpGet(LATEST_VERSION_URL.replace("{resource}", String.valueOf(id)));
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() != 200) return null;
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
                return jsonObject.get("name").getAsString().trim();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Checks the online status of the API by sending a GET request to the status URL.
     *
     * @return true if the API is online (HTTP status code 200), false otherwise.
     */
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
