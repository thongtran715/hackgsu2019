package com.sinch.rcssdk.rcs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinch.rcssdk.rcs.chatflow.RCSConfigureType;
import com.sinch.rcssdk.rcs.configuration.RcsAgentConfiguration;
import com.sinch.rcssdk.rcs.message.component.agentevent.AgentComposingEvent;
import com.sinch.rcssdk.rcs.message.component.agentevent.AgentEventSup;
import com.sinch.rcssdk.rcs.message.component.agentevent.AgentReadEvent;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

public class Util {
  public static long getFileSize(String urlFile) throws IOException {

        URL url = new URL(urlFile);
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    public static void agentReadEvent(String uuid, String from) {
        AgentReadEvent agentReadEvent = new AgentReadEvent();
        AgentEventSup agentEventSup = new AgentEventSup();
        RcsAgentConfiguration post = new RcsAgentConfiguration(RCSConfigureType.event);
        agentReadEvent.setMessage_id(uuid);
        agentEventSup.setEvent_id(UUID.randomUUID().toString());
        agentEventSup.setTo(from);
        agentEventSup.setEvent(agentReadEvent);
        String payloadRead = agentEventSup.toString();
        post.post(payloadRead);
    }

        public static String fetchLocation(String payload) throws Exception {
        String result = "";
        String url = "http://3.89.184.69:4041/api/address";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(payload));
        CloseableHttpResponse response = httpClient.execute(request);
        try {
            result = EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
        JSONObject jsonObject = new JSONObject(result);
        return (String) jsonObject.get("address");
    }

    public static JSONObject getIntent(String payload) throws Exception {
        String result;
        String url = "http://0.0.0.0:4041/api/diagflow/intent";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(payload));
        CloseableHttpResponse response = client.execute(request);
        try {
            result = EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject;
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        map.put("text", "I want some books");
        map.put("phone", "14047691562");
        try {
            String j = mapper.writeValueAsString(map);
            System.out.println(getIntent(j));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

        public static void agentComposing(String phoneNumber) {
        AgentEventSup agentEventSup = new AgentEventSup();
        RcsAgentConfiguration post = new RcsAgentConfiguration(RCSConfigureType.event);
        agentEventSup.setEvent(new AgentComposingEvent());
        agentEventSup.setEvent_id(UUID.randomUUID().toString());
        agentEventSup.setTo(phoneNumber);
        System.out.println(agentEventSup.toString());
        post.post(agentEventSup.toString());
    }
}
