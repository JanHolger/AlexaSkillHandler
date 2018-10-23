package eu.bebendorf.alexaskillhandler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class SkillRequest {

    private String version;
    private Session session;
    private Request request;

    public SkillRequest(String json){
        this(new JsonParser().parse(json).getAsJsonObject());
    }

    public SkillRequest(JsonObject object){
        version = object.get("version").getAsString();
        session = new Session(object.get("session").getAsJsonObject());
        request = new Request(object.get("request").getAsJsonObject());
    }

    @Getter
    public static class Session {
        private boolean newSession;
        private String sessionId;
        public Session(JsonObject object){
            newSession = object.get("new").getAsBoolean();
            sessionId = object.get("sessionId").getAsString();
        }
    }

    @Getter
    public static class Request {
        private RequestType type;
        private Intent intent = null;
        public Request(JsonObject object){
            type = RequestType.valueOf(object.get("type").getAsString());
            if(type.equals(RequestType.IntentRequest))
                intent = new Intent(object.get("intent").getAsJsonObject());
        }
    }

    @Getter
    public static class Intent {
        private String name;
        private String confirmationStatus;
        private Slot[] slots;
        public Intent(JsonObject object){
            name = object.get("name").getAsString();
            confirmationStatus = object.get("confirmationStatus").getAsString();
            List<Slot> slots = new ArrayList<>();
            if(object.has("slots"))
                for(Map.Entry<String,JsonElement> entry : object.get("slots").getAsJsonObject().entrySet())
                    slots.add(new Slot(entry.getValue().getAsJsonObject()));
            this.slots = slots.toArray(new Slot[0]);
        }
        public Slot getSlot(String name){
            for(Slot slot : slots)
                if(slot.getName().equals(name))
                    return slot;
            return null;
        }
        public boolean verifySlot(String name){
            Slot slot = getSlot(name);
            return !(slot==null||slot.getValue()==null);
        }
    }

    @Getter
    public static class Slot {
        private String name;
        private String value = "?";
        public Slot(JsonObject object){
            name = object.get("name").getAsString();
            if(object.has("value"))
                value = object.get("value").getAsString();
            if(value.equals("?"))
                value = null;
        }
        public int asInt(){
            return Integer.parseInt(value);
        }
    }

    public enum RequestType {
        LaunchRequest,
        CanFulfillIntentRequest,
        IntentRequest,
        SessionEndedRequest
    }

}
