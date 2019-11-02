package com.sinch.rcssdk.rcs.ncrApi;

import com.sinch.rcssdk.rcs.model.Category;
import com.sinch.rcssdk.rcs.model.Item;
import com.sinch.rcssdk.rcs.model.Store;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


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
                    System.out.println(obj.getString("CategoryName"));
                    categories.add(new Category(obj.getString("CategoryName"))) ;
                }
            }
            return categories;
        }
    }

    public HashMap<String, List<Item>>getAllItems() throws Exception{

        HashMap<String, List<Item>> map = new HashMap<>();
        String token = getToken();
        HttpGet request = new HttpGet("https://api-reg.ncrsilverlab.com/v2/inventory/items");

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
                    JSONObject obj = (JSONObject)resultArray.get(i);
                    String name = obj.getString("Name");
                    String description = obj.getString("Description");
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

    public HashMap<String, List<Item>> showTopTenOrders() throws Exception {
        HashMap<String, List<Item>> topTen = new HashMap<>();
        try {
            Random rand = new Random();
            HashMap<String, List<Item>> allItems = getAllItems();
            String[] keys = (String[]) allItems.keySet().toArray();
            while (topTen.size() <= 10) {
                String key = keys[rand.nextInt(keys.length)];
                topTen.put(key, allItems.get(key));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return topTen;

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



    public static void main(String[] args) {
        NcrApi ncrApi  = new NcrApi();
        try{

            Store store = ncrApi.getStore();
            System.out.println(store.phoneNumber);
            System.out.println(store.storeName);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
