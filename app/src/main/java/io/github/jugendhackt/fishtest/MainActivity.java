package io.github.jugendhackt.fishtest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.stealthcopter.networktools.ARPInfo;
import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected ArrayList<Device> devices = new ArrayList<>();
    protected ArrayList<DeviceWithMAC> devicesWithMAC = new ArrayList<>();
    protected String URL = "http://cherob.eu:3002/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);


        final Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FISH", "Started");
                SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
                    @Override
                    public void onDeviceFound(Device device) {
                        devices.add(device);
                    }

                    @Override
                    public void onFinished(ArrayList<Device> devicesFound) {
                        Log.i("FISH", "Done");
                        for(Device device : devices){
                            DeviceWithMAC deviceWithMAC = new DeviceWithMAC(device, ARPInfo.getMACFromIPAddress(device.ip));
                            devicesWithMAC.add(deviceWithMAC);
                        }
                        for (DeviceWithMAC deviceWithMAC : devicesWithMAC){
                            if (deviceWithMAC.getMAC() != null) {
                                try {
                                    Log.i("FISH", doPostMAC(URL, deviceWithMAC.getMAC()).getString("value"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });

        //ImageView imageview = findViewById(R.id.imageView);       //Zum Überschreiben wenn
        //imageview.setImageResource(R.drawable.ic_wanzepicred);  //ein Echo gefunden ist
    }

    protected JSONObject doPostMAC(String url, String mac) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        httpBuilder.addQueryParameter("mac_address_key", mac);

        Request request = new Request.Builder().url(httpBuilder.build()).build();

        JSONObject response = new JSONObject(client.newCall(request).execute().body().string());

        Log.i("FISH", response.toString());

        return response;
    }
}
