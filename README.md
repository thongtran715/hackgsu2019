# java-SinchRCS-Library

# Motivation 

This Java Sinch RCS library is a tool where you can directly interact with Sinch RCS API. It is built on top of core RCS API and enables developers the ability to understand core features of rich messaging that normal SMS is not capable of. We hope with this library , you can find the easy ways to use our service. 

---

# Link
Please visit our RCS Page for further information [Sinch-RCS](https://www.sinch.com/docs/rcs/index.html)

---

# Usage
In order to use this library, you will first need to set up your RCS bot environment. You will need to obtain Agent ID and Token ID from Sinch, Inc, so please visit [Sinch](https://www.sinch.com/) and we can help you set up the bot. 
<br />
Otherwise, feel free to use our sample bot which is already integrating in the library. 

## Configuration 
You will need to tell our library which bot you are using including the Agent ID and Token ID. <br />
Let's create a configuration class which is called YourBotConfiguration and extends it from ChatAgentConfiguration
```$xslt
public class YourBotConfiguration extends AgentConfiguration {

    public YourBotConfiguration(RCSConfigureType type) {
        super(type);
        this.TOKEN = "YOUR TOKEN";
        this.AGENT_ID = "YOUR ID";
    }
}
```
<br/>
Next step is to create an instance from ChatBot class. <br/>

```
      ChatBot chatBot = new ChatBot(new YourBotConfiguration(RCSConfigureType.api));
      // Optional - Setting up your destinated supplier. 
      chatBot.setSupplier(AgentMessage.Supplier.MAAP_SAMSUNG);
```

That is it for the configuration. Now let's jump to how we can use this ChatBot instance to send RCS features message. 
# Core Features 
- [ Text Message ](#textMessage)
- [ Image Message ](#imageMessage)
- [ Video Message ](#videoMessage)
- [ Standalone Rich Card Message](#standaloneMessage)
- [ Carousels Message](#carouselsMessage)
---

<a name="textMessage"></a>
## Text Message

Sending text message is simple. You will just need to have a phone number and the text you want to send 

```$xslt
  chatBot.sendTextMessage("+14047691562", "Hello from Sinch");
```



<a name="imageMessage"></a>
## Image Message

Sending image message requires an URI to host the file and it has mime type of png or jpeg

```$xslt
        try {
            chatBot.sendImage( "+14047691562", "https://s3.amazonaws.com/sketchers-chatbot/Revision_1/Kid/Kids+Boys'+Sport/Boys'+Kinectors+Thermovolt.jpg");
        }catch (FileSizeExceedLimitException e){
            // File size limit the recommendation
            System.out.println(e.getMessage());
        }catch (IOException e){
            // Unable to reach the url host
            System.out.println(e.getMessage());
        }
```

<a name="videoMessage"></a>
## Video Message

<a name="standaloneMessage"></a>
## Standalone Message

<a name="carouselsMessage"></a>
## Carousel Message

---

# Contributors 
