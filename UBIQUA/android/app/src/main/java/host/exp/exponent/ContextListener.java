package host.exp.exponent;

public interface ContextListener {
    void onContextReady(String data);
    String getContextKey();
}