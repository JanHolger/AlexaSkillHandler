package eu.bebendorf.alexaskillhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.bebendorf.alexaskillhandler.directives.AudioPlayer;
import eu.bebendorf.alexaskillhandler.directives.SkillDirective;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SkillResponse {

    private OutputSpeech speech = null;
    private Card card = null;
    private List<SkillDirective> directives = new ArrayList<>();
    private boolean shouldEndSession = true;

    public static SkillResponseBuilder builder(){
        return new SkillResponseBuilder();
    }

    public void addDirective(SkillDirective directive){
        directives.add(directive);
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("version","1.0");
        object.add("sessionAttributes",new JsonObject());
        JsonObject responseObject = new JsonObject();
        if(speech!=null)
            responseObject.add("outputSpeech",speech.toJson());
        if(card!=null)
            responseObject.add("card",card.toJson());
        if(!shouldEndSession)
            responseObject.addProperty("shouldEndSession",false);
        if(directives.size()>0){
            JsonArray ja = new JsonArray();
            for(SkillDirective sd : directives)
                ja.add(sd.toJson());
            responseObject.add("directives",ja);
        }
        object.add("response",responseObject);
        return object;
    }

    public String toString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(toJson());
    }

    @AllArgsConstructor
    @Data
    public static class OutputSpeech {
        private String text = "";
        public JsonObject toJson(){
            JsonObject object = new JsonObject();
            object.addProperty("type","PlainText");
            object.addProperty("text",text);
            return object;
        }
    }

    @AllArgsConstructor
    @Data
    public static class Card {
        private String title = "";
        private String content = "";
        public JsonObject toJson(){
            JsonObject object = new JsonObject();
            object.addProperty("type","Simple");
            object.addProperty("title",title);
            object.addProperty("content",content);
            return object;
        }
    }

    public static class SkillResponseBuilder {
        private SkillResponse response = new SkillResponse();
        public SkillResponseBuilder speech(String text){
            response.setSpeech(new OutputSpeech(text));
            return this;
        }
        public SkillResponseBuilder end(boolean shouldEndSession){
            response.setShouldEndSession(shouldEndSession);
            return this;
        }
        public SkillResponseBuilder directive(SkillDirective directive){
            response.addDirective(directive);
            return this;
        }
        public SkillResponseBuilder audioPlayDirective(AudioPlayer.PlayBehavior behavior, AudioPlayer.AudioStream stream){
            return directive(new AudioPlayer.Play(behavior,stream));
        }
        public SkillResponse build() {
            return response;
        }
    }

}
