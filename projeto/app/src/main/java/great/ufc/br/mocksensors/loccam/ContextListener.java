package great.ufc.br.mocksensors.loccam;

public interface ContextListener {
    void onContextReady(String data);
    String getContextKey();
}
