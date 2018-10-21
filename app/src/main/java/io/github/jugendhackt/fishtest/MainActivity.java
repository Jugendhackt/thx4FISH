package io.github.jugendhackt.fishtest;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.stealthcopter.networktools.ARPInfo;
import com.stealthcopter.networktools.PortScan;
import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.net.util.SubnetUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;

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
                devices = new ArrayList<>();
                devicesWithMAC = new ArrayList<>();
                toggleTextView();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.INVISIBLE);
                        imageView.setImageResource(R.drawable.ic_wanzepicgreen);
                    }
                });
                Log.i("FISH", "Started");
                SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
                    @Override
                    public void onDeviceFound(Device device) {
                        Log.i("FISH", device.toString());
                    }

                    @Override
                    public void onFinished(ArrayList<Device> devicesFound) {
                        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                        Integer ip = dhcpInfo.ipAddress;
                        Integer mask = dhcpInfo.netmask;
                        String ipStr = String.format("%d.%d.%d.%d", ip & 0xff, ip >> 8 & 0xff, ip >> 16 & 0xff, ip >> 24 & 0xff);
                        String maskStr = String.format("%d.%d.%d.%d", mask & 0xff, mask >> 8 & 0xff, mask >> 16 & 0xff, mask >> 24 & 0xff);
                        SubnetUtils subnetUtils = new SubnetUtils(ipStr, maskStr);
                        String[] ips = subnetUtils.getInfo().getAllAddresses();
                        ArrayList<IPAndMAC> ipAndMACS = new ArrayList<>();
                        for (int i = 0; i < ips.length; i++) {
                            final String ipAddr = ips[i];
                            String mac = ARPInfo.getMACFromIPAddress(ipAddr);
                            if (mac != null){
                                IPAndMAC ipAndMAC = new IPAndMAC(ipAddr, mac);
                                ipAndMACS.add(ipAndMAC);
                            }
                        }
                        for (IPAndMAC ipAndMAC : ipAndMACS){
                            try {
                                String response = doPostMAC(URL, ipAndMAC.getMAC()).getString("value");
                                if (!response.equals("unknown")){
                                    Log.i("FISH", response);
                                    if (response.toLowerCase().contains("amazon")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ImageView imageView = findViewById(R.id.imageView);
                                                imageView.setImageResource(R.drawable.ic_wanzepicred);
                                                imageView.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        toggleTextView();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView imageView = findViewById(R.id.imageView);
                                imageView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });



            }
        });
    }

    protected JSONObject doPostMAC(String url, String mac) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        httpBuilder.addQueryParameter("mac_address_key", mac);

        Request request = new Request.Builder().url(httpBuilder.build()).build();

        JSONObject response = new JSONObject(client.newCall(request).execute().body().string());

        //Log.i("FISH", response.toString());

        return response;
    }

    protected void toggleTextView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = findViewById(R.id.textView);

                textView.setVisibility((textView.getVisibility() == View.VISIBLE)? View.INVISIBLE : View.VISIBLE);
            }
        });
    }
}
