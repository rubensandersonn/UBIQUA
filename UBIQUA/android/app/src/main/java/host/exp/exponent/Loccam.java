package host.exp.exponent;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.loccamlib.LoccamListener;
import br.ufc.great.loccamlib.LoccamManager;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IClientReaction;
import br.ufc.great.syssu.base.interfaces.ISysSUService;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;

import javax.annotation.Nonnull;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;


public class Loccam extends ReactContextBaseJavaModule implements LoccamListener {
    protected static Loccam instance;
    private Context context;
    private ArrayList<ContextListener> listeners;
    private LoccamManager loccamManager;

    private ReactApplicationContext reactContext;


    public Loccam (ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @ReactMethod
    public void initLoccam(List<String> listaSacs){

        for (String string : listaSacs){
            loccamManager.init(string);
        }
    }

    @ReactMethod
    public void connect(Context context, String appId){
        this.context = context;

        loccamManager = new LoccamManager(context, appId);
        loccamManager.connect(this);
    }

    @ReactMethod
    public void disconnect(){
        if(loccamManager != null){
            loccamManager.disconnect();
        }
    }

    @ReactMethod
    public void registerListener(final ContextListener listener){
        listeners.add(listener);

    }

    @ReactMethod
    public void unregisterListener(ContextListener listener){
        if(listeners.contains(listener)){
            listeners.remove(listener);
            loccamManager.finish(listener.getContextKey());
        }
    }



    @Override
    public void onServiceConnected(ISysSUService iSysSUService) {

        Log.d("LOCCAM","conectou");

        for(final ContextListener listener:listeners) {
            loccamManager.init(listener.getContextKey());
            loccamManager.getAsync(new IClientReaction.Stub() {

                public void react(Tuple tuple) throws RemoteException {
                    listener.onContextReady(tuple.getField(2).getValue().toString());
                }
            }, "put", ContextKeys.PROXIMITY, null);
        }

    }

    @Override
    public void onServiceDisconnected() {

        Log.d("LOCCAM","Desconectou");

    }

    @Override
    public void onLoccamException(Exception e) {

        Log.d("LOCCAM","Exceção");

    }



    @Nonnull
    @Override
    public String getName() {
        return "Loccam";
    }
}
