package com.sinch.rcssdk.chatbot;

import com.sinch.rcssdk.chatflow.ChatBot;
import com.sinch.rcssdk.chatflow.RCSConfigureType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.management.Agent;

public class ChatBotTest {
    private ChatBot chatBot;

    @Test
    public void name() {
    }

    @Before
    public void setUp() throws Exception {
        chatBot = new ChatBot(new AgentConfig(RCSConfigureType.api));
    }

    @After
    public void tearDown() throws Exception {

    }

}
