package iot.exemplo.middleware.testandoloccam;

import java.util.List;

public interface ContextListener {

    void onContextReady(String data);
    String getContextKey();
}
