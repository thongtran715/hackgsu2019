package com.sinch.rcssdk.rcs.message.messagetype;


import com.sinch.rcssdk.rcs.message.Utils.UtilsToString;
import com.sinch.rcssdk.rcs.message.enums.MessageType;

public abstract class Message {
    private MessageType type;

    public Message() {
        type = null;
    }

    public Message(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return this.type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public abstract void generateFallback();

    @Override
    public String toString() {
//        try {
//            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException c) {
//            c.printStackTrace();
//        }
//        return null;
        return UtilsToString.convertString(this);
    }
}
