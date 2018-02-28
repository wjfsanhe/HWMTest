package framework.com.hwmtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.qiyi.hwmessenger.IHWMessenger;
import com.qiyi.hwmessenger.IHWMessengerCallback;
import com.qiyi.hwmessenger.IHWControllerClient;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "HWMessenger" ;
    static final String EXT = "[TEST] " ;
    IHWMessenger mHWMessenger ;
    TestCallback mCallback;

    private IHWMessenger getBinderProxy(){
        IHWMessenger iHWM = null;
        try {
            Class clazz = getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder ibinder = (IBinder) method.invoke(null, "HWMessenger");
            iHWM = IHWMessenger.Stub.asInterface(ibinder);
        } catch (Exception e){
            e.printStackTrace();
        }
        return iHWM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mCallback = new TestCallback();
        mHWMessenger = getBinderProxy();
        try {
            mHWMessenger.registerCallback(mCallback) ;

        IHWControllerClient client = mHWMessenger.createHWControllerClient();
        if (client == null) {
            Log.d(TAG,EXT + "binder is null ");
            return ;
        }
        String result = client.readSysfs(new String("/sys/class/graphics/fb0/mod"));
        Log.d(TAG,EXT + "result: " + result);
        result = client.readSysfs(new String("/sys/class/graphics/fb0/modes"));
        Log.d(TAG,EXT + "result: " + result);

        result = client.writeSysfs(new String("/sys/class/leds/red/brightness"), new String("0"));
        Log.d(TAG,EXT + ": led brightness : " + result);
        result = client.writeSysfs(new String("/sys/class/leds/red/brightness"), new String("1"));
        Log.d(TAG,EXT + ": led brightness : " + result);


        result= client.getProperty(new String("sys.hw.test"));
        Log.d(TAG,EXT + ": getProp [sys.hw.test] : " + result);

        result= client.setProperty(new String("sys.hw.test"), new String("debug"));
        Log.d(TAG,EXT + ": getProp [sys.hw.test] : " + result);
        result= client.setProperty(new String("sys.hw.test"), new String("normal"));
        Log.d(TAG,EXT + ": getProp [sys.hw.test] : " + result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    private class TestCallback extends IHWMessengerCallback.Stub
    {
        public void onKey(String deviceName, int keyCode, int value, int flags) throws android.os.RemoteException
        {
            Log.d(TAG,EXT + "device[" + deviceName + "]" + "----" + keyCode + ", " + value + ", " + flags );
        }
        @Override
        public IBinder asBinder() {
            return this;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
