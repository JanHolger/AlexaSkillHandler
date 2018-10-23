package eu.bebendorf.alexaskillhandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkillPipeline {

    private List<SkillHandler> handlerList = new ArrayList<>();

    public String pipe(String request){
        SkillRequest skillRequest = new SkillRequest(request);
        SkillResponse skillResponse = null;
        for(SkillHandler handler : handlerList){
            skillResponse = handler.handle(skillRequest);
            if(skillResponse!=null)
                break;
        }
        if(skillResponse==null)
            skillResponse = SkillResponse.builder().build();
        return skillResponse.toString();
    }

    public void addHandler(SkillHandler skillHandler){
        handlerList.add(skillHandler);
    }

    private static void saveBody(String string){
        try {
            FileWriter fw = new FileWriter(new File("bodies/"+ UUID.randomUUID().toString()+".json"));
            fw.write(string);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
