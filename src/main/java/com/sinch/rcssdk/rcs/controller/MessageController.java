package com.sinch.rcssdk.rcs.controller;

import com.sinch.rcssdk.rcs.coffeechatflow.ChatFlow;
import com.sinch.rcssdk.rcs.util.Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "api/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
                }
            }
            return new ResponseEntity(payload, HttpStatus.OK);
        } else {
            return new ResponseEntity(payload, HttpStatus.BAD_GATEWAY);
        }
    }
}
