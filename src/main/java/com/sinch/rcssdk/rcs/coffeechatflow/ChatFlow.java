package com.sinch.rcssdk.rcs.coffeechatflow;

import com.sinch.rcssdk.rcs.chatflow.ChatBot;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.configuration.RcsAgentConfiguration;
import com.sinch.rcssdk.rcs.message.component.action.DialPhoneNumber;
import com.sinch.rcssdk.rcs.message.component.action.RequestLocationPush;
import com.sinch.rcssdk.rcs.message.component.action.ShowLocation;
import com.sinch.rcssdk.rcs.message.component.postback.PostBack;
import com.sinch.rcssdk.rcs.message.component.richcard.FileInfo;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardContent;
import com.sinch.rcssdk.rcs.message.component.richcard.RichCardMedia;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedAction;
import com.sinch.rcssdk.rcs.message.component.suggestions.SuggestedReply;
import com.sinch.rcssdk.rcs.message.enums.HeightType;
import com.sinch.rcssdk.rcs.message.enums.OrientationType;
import com.sinch.rcssdk.rcs.message.enums.ThumbnailAlignmentType;
import com.sinch.rcssdk.rcs.message.messagetype.AgentMessage;
import com.sinch.rcssdk.rcs.model.Category;
import com.sinch.rcssdk.rcs.model.Item;
import com.sinch.rcssdk.rcs.model.Store;
import com.sinch.rcssdk.rcs.model.UserLocation;
import com.sinch.rcssdk.rcs.ncrApi.NcrApi;
import com.sinch.rcssdk.rcs.util.Pair;
import com.sinch.rcssdk.rcs.util.Util;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatFlow {

    private String CATEGORY_ID = "categories_id_";
    private String ADD_TO_CART = "add_to_cart_";
    private ChatBot chatBot;
    private NcrApi ncrApi;
    private List<Category> categories;
    private Store store;
    private HashMap<String, List<Item>> map;
    private List<Item> carts;

    public ChatFlow() {
        chatBot = new ChatBot(new RcsAgentConfiguration(RCSConfigureType.api));
        chatBot.setSupplier(AgentMessage.Supplier.GOOGLE);
        ncrApi = new NcrApi();

        try {
            categories = ncrApi.getAllCategories();
            map = ncrApi.getAllItems();
            store = ncrApi.getStore();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        carts = new ArrayList<>();
    }


    public void handlePostback(String postback, String phoneNumber){
        List<Pair<String, String>> suggestions = new ArrayList<>();
        List<SuggestedAction> actions = new ArrayList<>();
        if (postback.equalsIgnoreCase("show_categories")){
            try {
                int size = categories.size();
                while (size > 12){
                    categories.remove(0) ;
                    size--;
                }
                for (int i = 0 ; i < categories.size(); ++i){
                    suggestions.add(new Pair<>(categories.get(i).name, CATEGORY_ID + categories.get(i).name));
                }
                this.chatBot.setSuggestions(suggestions, null);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            chatBot.sendTextMessage(phoneNumber, "Here is some of our product category that you can browse") ;
        }
        else if (postback.contains(CATEGORY_ID)){
            String productName = postback.substring(CATEGORY_ID.length()) ;
            List<Item> items = map.get(productName);
            try {
                carouselHelper(phoneNumber, items, null, null);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else if (postback.equalsIgnoreCase("finished_shopping")){
            suggestions.add(new Pair<>("To the restaurant", "to_restaurant"));
            suggestions.add(new Pair<>("Deliver to my house", "deliver_my_house"));
            try {
                chatBot.setSuggestions(suggestions, null);
                chatBot.sendTextMessage(phoneNumber, "Great! Thanks for shopping with us. Now, please tell us you want to come to our restaurant, or we deliver for you");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else if (postback.equalsIgnoreCase("deliver_my_house")){

            SuggestedAction findClosestStore = new SuggestedAction("Locate me", new PostBack("closest_store"));
            RequestLocationPush pushLocation = new RequestLocationPush();
            findClosestStore.setAction(pushLocation);
            actions.add(findClosestStore);
            try {
                this.chatBot.setSuggestions(null, actions);
                chatBot.sendTextMessage(phoneNumber, "Okay, Let's first locate your location");
            }catch (Exception e){}

        }else if (postback.equalsIgnoreCase("to_restaurant")){

        }else if (postback.contains(ADD_TO_CART)){
            String productId = postback.substring(ADD_TO_CART.length());
            Item item  = findItem(productId);
            carts.add(item);
            chatBot.sendTextMessage(phoneNumber, "This item is added");
            suggestions.add(new Pair<>("Show cart", "show_cart"));
            suggestions.add(new Pair<>("Show categories", "show_categories"));
            try {
                this.chatBot.setSuggestions(suggestions, null);
                this.chatBot.sendTextMessage(phoneNumber, "May I help you anything else?");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (postback.equals("show_cart")){
            try{
                suggestions.add(new Pair<>("Show cart", "show_cart"));
            suggestions.add(new Pair<>("Show categories", "show_categories"));
            this.chatBot.setSuggestions(suggestions, null);
            carouselHelper(phoneNumber, carts, null, null);
            }catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        this.chatBot.clearSuggestionsChip();
    }

    private Item findItem(String productId){
        for (String productName: map.keySet()){
            List<Item> items = map.get(productName);
            for (Item item : items)
                if (item.itemId.equalsIgnoreCase(productId))return item;
        }
        return null;
    }
    public void handleTextMessage(String text, String phoneNumber){
        List<Pair<String, String>> suggestions = new ArrayList<>();
        List<SuggestedAction> actions = new ArrayList<>();
        if (text.equalsIgnoreCase("hello")) {
            try {
                suggestions.add(new Pair<>("Show categories", "show_categories"));
                suggestions.add(new Pair<>("Show order status", "show_order_status"));
                SuggestedAction callUsAction = new SuggestedAction("Call Us Now", new PostBack(store.phoneNumber));
                DialPhoneNumber dialPhoneNumber = new DialPhoneNumber();
                dialPhoneNumber.setPhone_number("14084294121");
                callUsAction.setAction(dialPhoneNumber);
                actions.add(callUsAction);
                SuggestedAction findClosestStore = new SuggestedAction("Direction", new PostBack("closest_store"));
                ShowLocation showLocation = new ShowLocation();
                showLocation.setLabel(store.storeName);
                showLocation.setLatitude(46);
                showLocation.setLongitude(-95);
                findClosestStore.setAction(showLocation);
                actions.add(findClosestStore);
                this.chatBot.setSuggestions(suggestions, actions);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            chatBot.sendTextMessage(phoneNumber, "Hello, May I help you today?");

            this.chatBot.clearSuggestionsChip();
        }
    }


    private void carouselHelper(String phoneNumber, List<Item> products, List<Pair<String, String>> suggestions, List<SuggestedAction> actions) throws  Exception
    {
        List<RichCardContent> richCardContents = new ArrayList<>();
        while (products.size() > 10){
            products.remove(0);
        }
        for (Item product  : products) {
            RichCardContent richCardContent = new RichCardContent();
            richCardContent.setTitle(product.title);
            richCardContent.setDescription(product.description);
            RichCardMedia media = new RichCardMedia();
            long imageSize = Util.getFileSize(product.imageUrl);
            media.setFile(new FileInfo(FileInfo.Mime_type.IMAGE_PNG, imageSize, "product.jpg", product.imageUrl));
            media.setThumbnail(new FileInfo(FileInfo.Mime_type.IMAGE_PNG, imageSize, "thumbnail.jpg", product.imageUrl));
            media.setHeight(HeightType.MEDIUM);
            richCardContent.setMedia(media);
            richCardContent.addSuggestion(new SuggestedReply("Add this item", new PostBack(ADD_TO_CART + product.itemId)));
            richCardContents.add(richCardContent);
        }


        // Sorting the description descending
        Collections.sort(richCardContents, new Comparator<RichCardContent>() {
            @Override
            public int compare(RichCardContent o1, RichCardContent o2) {
                return o2.getDescription().compareTo(o1.getDescription());
            }
        });
        this.chatBot.setSuggestions(suggestions, actions);
        if (richCardContents.size() < 2)
            chatBot.sendRichCard(phoneNumber, richCardContents.get(0), OrientationType.VERTICAL, ThumbnailAlignmentType.LEFT);
        else
        chatBot.sendCarousel(phoneNumber, richCardContents);
    }


    public void processMessageWithLocation(String phoneNumber, JSONObject obj) {
        Double latitude = obj.getDouble("latitude");
        Double longitude = obj.getDouble("longitude");
        UserLocation userLocation = new UserLocation(latitude.toString(), longitude.toString());
        try {

            String address = Util.fetchLocation(userLocation.toString());
            chatBot.sendTextMessage(phoneNumber, "We have found your location");
            chatBot.sendTextMessage(phoneNumber, address);
            chatBot.sendTextMessage(phoneNumber, "Your order will come shortly");
        } catch (Exception e) {
            System.out.println("Unable to find location");
        }

        this.chatBot.clearSuggestionsChip();
    }


    public void sendBooksItems(String phoneNumber){

    }

    public void sendMessage(String text, String phoneNumber){
        this.chatBot.sendTextMessage(phoneNumber, text);
    }

}
