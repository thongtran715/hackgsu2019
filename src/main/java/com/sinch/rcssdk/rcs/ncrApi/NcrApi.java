package com.sinch.rcssdk.rcs.ncrApi;

import com.sinch.rcssdk.rcs.model.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NcrApi {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

  private String getToken() throws Exception {

        HttpGet request = new HttpGet("https://api-reg.ncrsilverlab.com/v2/oauth2/token");

        // add request headers
        request.addHeader("client_id", "gsu_552517");
        request.addHeader("client_secret", "9DC0AA50-347C-42DA-BFDE-D2627B897F4F");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(result);
                return object.getJSONObject("Result").getString("AccessToken");
            }
        }
        return null;
    }

    public List<Category> getAllCategories() throws Exception{

      List<Category> categories = new ArrayList<>();

      String token = getToken();
        HttpGet request = new HttpGet("https://api-reg.ncrsilverlab.com/v2/inventory/itemcategories");

        // add request headers
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(result);
                JSONArray resultArray = object.getJSONArray("Result");
                for (int i = 0; i < resultArray.length(); ++i){
                    Object pr = resultArray.get(i);
                    if (pr.equals(null)) continue;
                    JSONObject obj = (JSONObject)pr;
                    categories.add(new Category(obj.getString("CategoryName"))) ;
                }
            }
            return categories;
        }
    }

    public HashMap<String, List<Item>>getAllItems() throws Exception{

        HashMap<String, List<Item>> map = new HashMap<>();
        String token = getToken();
        HttpGet request = new HttpGet("https://api-reg.ncrsilverlab.com/v2/inventory/items?limit=10000");

        // add request headers
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(result);
                JSONArray resultArray = object.getJSONArray("Result");

                for (int i = 0; i < resultArray.length(); ++i){

                    if (resultArray.get(i).equals(null)) continue;
                    JSONObject obj = (JSONObject)resultArray.get(i);

                    String name = obj.getString("Name");
                    String description = "";
                    if (!obj.get("Description").equals(null))
                        description = obj.getString("Description");
                    String cost = String.valueOf(obj.getDouble("Cost"));
                    String category = obj.getString("ItemCategoryName");
                    String itemId = String.valueOf(obj.getInt("ItemMasterId"));
                    if (!map.containsKey(category))
                        map.put(category, new ArrayList<>());
                    map.get(category).add(new Item(name, description, cost, "https://rcs-barcelona-demo.s3.amazonaws.com/rcs-coffee/Caramel+Pumpkin+Spice+Latte+.jpg", category, itemId));

                }
            }
        }
        return map;
    }

    public Store getStore() throws Exception{
      String token = this.getToken();

      HttpGet request = new HttpGet("https://api-reg.ncrsilverlab.com/v2/stores");
        // add request headers
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(result);
                JSONArray resultArray = object.getJSONArray("Result");
                JSONObject ob = resultArray.getJSONObject(0);
                String storeName = ob.getString("StoreName");
                String address = ob.getString("Address1");
                String city = ob.getString("City");
                String phoneNumber = ob.getString("PhoneNumber");
                Store store = new Store(storeName, address, phoneNumber);
                return store;
            }
        }
        return null;
    }


    public boolean makeOrder(String payload) throws Exception {
      String token = this.getToken();

          HttpPost request = new HttpPost("https://api-reg.ncrsilverlab.com/v2/orders?store_number=1");
        // add request headers
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer " + token);
        request.setEntity(new StringEntity(payload));
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println(entity);
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        NcrApi ncrApi  = new NcrApi();
        try{
            Orders orders = new Orders();
            String payload = orders.toString();
            System.out.println(payload);
            ncrApi.makeOrder(payload);

        }
        catch (Exception e){
            System.out.println("Some thing wrong");
        }

    }

}
