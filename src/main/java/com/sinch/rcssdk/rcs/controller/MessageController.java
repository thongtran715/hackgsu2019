package com.sinch.rcssdk.rcs.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.rcssdk.rcs.coffeechatflow.ChatFlow;
import com.sinch.rcssdk.rcs.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private ChatFlow chatFlow;

    @PostMapping(path = "/mo")
    public ResponseEntity handleMOMessage(@Valid @RequestBody Map<String, Object> payload) {

        JSONObject json = new JSONObject(payload);

        if (json.has("message_id")) {
            String type = (String) json.get("type");
            if (type.contains("status_report")) {
                return new ResponseEntity(payload, HttpStatus.OK);
            }

            Util.agentReadEvent(json.getString("message_id"), json.getString("from"));
            String phoneNumber = json.getString("from");

            if (json.get("message") instanceof JSONObject) {
                JSONObject obj = (JSONObject) json.get("message");
                if (obj.getString("type").equalsIgnoreCase("suggestion_response")) {
                    String postBack = (String) obj.get("postback_data");
                    if (postBack == null) {
                        postBack = obj.getString("text");
                    }
                    chatFlow.handlePostback(postBack, phoneNumber);
                } else if (obj.getString("type").equalsIgnoreCase("location")) {
                    chatFlow.processMessageWithLocation(phoneNumber, obj);
                } else if (obj.getString("type").equalsIgnoreCase("text")) {
                    String text = obj.getString("text").toLowerCase().trim();
                    chatFlow.handleTextMessage(text, phoneNumber);
                    HashMap<String, String> map = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    map.put("text", text);
                    map.put("phone", phoneNumber);
                    try {
                        String j = mapper.writeValueAsString(map);
                        JSONObject jsonObject = Util.getIntent(j);
                        if (jsonObject.getString("intent").isEmpty()) {
//                            chatFlow.sendSingle(jsonObject.getString("fullfill"), contact);
                            System.out.println(jsonObject.getString("fullfill"));
                        }
                    } catch (JsonGenerationException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ResponseEntity(payload, HttpStatus.OK);
        } else {
            return new ResponseEntity(payload, HttpStatus.BAD_GATEWAY);
        }
    }


    @PostMapping(path = "/nlp")
    public ResponseEntity receiveMessageFromDiagFlow(HttpEntity<String> httpEntity) {
            String reqObject = httpEntity.getBody();
        JSONObject obj = new JSONObject(reqObject);
        System.out.println(obj);
        JSONObject queryResult = (JSONObject) obj.get("queryResult");

        if (queryResult.has("intent")) {

            JSONObject intentJson = (JSONObject) queryResult.get("intent");

            String intent = intentJson.getString("displayName");

            JSONObject parameters = (JSONObject) queryResult.get("parameters");

            JSONArray ja = (JSONArray) queryResult.get("outputContexts");

            JSONObject contexts = (JSONObject) ja.get(0);

            String fullfill = queryResult.getString("fulfillmentText");

            String phoneNumber = ((JSONObject) contexts.get("parameters")).getString("phoneNumber");

            if (parameters.has("number"))
                this.chatFlow.sendMessage(fullfill, phoneNumber);
            else if (intent.equalsIgnoreCase("Brewed Coffee")){
            }
        }
        return new ResponseEntity("Created ", HttpStatus.ACCEPTED);

    }
}
