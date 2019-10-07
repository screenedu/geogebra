package org.geogebra.common.jre.plugin;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.App;
import org.geogebra.common.plugin.Event;
import org.geogebra.common.plugin.ScriptManager;
import org.geogebra.common.plugin.script.JsScript;
import org.geogebra.common.util.debug.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class ScriptManagerJre extends ScriptManager {

    /**
     * @param app application
     */
    public ScriptManagerJre(App app) {
        super(app);
    }

    @Override
    public void ggbOnInit() {
        try {
            // call only if libraryJavaScript is not the default (ie do nothing)
            if (!app.getKernel().getLibraryJavaScript()
                    .equals(Kernel.defaultLibraryJavaScript)) {
                evalJavaScript("ggbOnInit();");
            }
        } catch (Exception e) {
            Log.debug("Error calling ggbOnInit(): " + e.getMessage());
        }
    }

    @Override
    protected void callClientListeners(List<JsScript> listeners, Event evt) {
        if (listeners.isEmpty()) {
            return;
        }

        ArrayList<String> args = new ArrayList<>();
        args.add(evt.type.getName());
        if (evt.targets != null) {
            for (GeoElement geo : evt.targets) {
                args.add(geo.getLabelSimple());
            }
        } else if (evt.target != null) {
            args.add(evt.target.getLabelSimple());
        } else {
            args.add("");
        }
        if (evt.argument != null) {
            args.add(evt.argument);
        }

        for (JsScript listener : listeners) {
            evalJavaScript(listener.getText(), args.toArray(new String[0]));
        }
    }

    @Override
    protected void callListener(String jsFunction, String arg0, String arg1) {
        if (arg0 == null) {
            evalJavaScript(jsFunction, new String[] {});
        } else if (arg1 == null) {
            evalJavaScript(jsFunction, new String[] {arg0});
        } else {
            evalJavaScript(jsFunction, new String[] {arg0, arg1});
        }
    }

    private void evalJavaScript(String jsFunction, String[] args) {
        evalJavaScript(createJavascriptFunction(jsFunction, args));
    }

    protected abstract void evalJavaScript(String jsFunction);

    private String createJavascriptFunction(String jsFunction, Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(jsFunction);
        sb.append("(");
        for (int i = 0; i < args.length; i++) {
            sb.append('"');
            sb.append(args[i].toString());
            sb.append('"');
            if (i < args.length - 1) {
                sb.append(",");
            }
        }
        sb.append(");");
        return sb.toString();
    }
}
