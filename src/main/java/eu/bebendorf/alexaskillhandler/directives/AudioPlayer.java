package eu.bebendorf.alexaskillhandler.directives;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;

public class AudioPlayer {
    @AllArgsConstructor
    public static class Play implements SkillDirective {
        private PlayBehavior playBehavior;
        private AudioStream stream;
        public JsonObject toJson() {
            JsonObject object = new JsonObject();
            object.addProperty("type","AudioPlayer.Play");
            object.addProperty("playBehavior", playBehavior.toString());
            JsonObject audioItem = new JsonObject();
            audioItem.add("stream",stream.toJson());
            object.add("audioItem",audioItem);
            return object;
        }
    }
    public enum PlayBehavior {
        ENQUEUE,
        REPLACE_ALL
    }
    @AllArgsConstructor
    public static class AudioStream {
        private String token;
        private String url;
        private int offsetInMilliseconds;
        public JsonObject toJson(){
            JsonObject object = new JsonObject();
            object.addProperty("token",token);
            object.addProperty("url",url);
            object.addProperty("offsetInMilliseconds",offsetInMilliseconds);
            return object;
        }
    }
}
