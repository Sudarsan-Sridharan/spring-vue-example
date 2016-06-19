package info.kitak.vue;

import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class VueRenderer {
    private NashornScriptEngine nashornScriptEngine;

    public VueRenderer() {
        NashornScriptEngine nashornScriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        try {
            nashornScriptEngine.eval(read("static/event-loop.js"));
            nashornScriptEngine.eval(read("static/nashorn-polyfill.js"));
            nashornScriptEngine.eval(read("static/server.js"));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        this.nashornScriptEngine = nashornScriptEngine;
    }

    public String renderCommentBox(List<Comment> comments) {
        try {
            Object html = nashornScriptEngine.invokeFunction("renderServer", comments);
            return String.valueOf(html);
        }
        catch (Exception e) {
            throw new IllegalStateException("failed to render vue component", e);
        }

    }

    private Reader read(String path) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        return new InputStreamReader(in);
    }
}
