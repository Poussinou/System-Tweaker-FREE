package com.nowenui.systemtweakerfree.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.nowenui.systemtweakerfree.R;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class InternetTweaksFragment extends Fragment {

    private CheckBox checkbox10, checkbox11, checkbox12, checkbox13, checkbox14, checkbox15, tcpboot, openvpnsupport;
    private SwitchCompat switch1;


    public static InternetTweaksFragment newInstance(Bundle bundle) {
        InternetTweaksFragment messagesFragment = new InternetTweaksFragment();

        if (bundle != null) {
            messagesFragment.setArguments(bundle);
        }

        return messagesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                new AlertDialog.Builder(this.getContext())
                        .setTitle(R.string.reboot)
                        .setMessage(R.string.rebootactionbar)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
                                    proc.waitFor();
                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), "ROOT NEEDED!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.warning)
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        RootTools.debugMode = false;

        final View view = inflater.inflate(R.layout.fragment_internettweaks, parent, false);

        File file = new File("/proc/sys/net/ipv4/tcp_congestion_control");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
        }

        File file2 = new File("/system/build.prop");

        StringBuilder text2 = new StringBuilder();

        try {
            BufferedReader br2 = new BufferedReader(new FileReader(file2));
            String line2;

            while ((line2 = br2.readLine()) != null) {
                text2.append(line2);
                text2.append('\n');
            }
            br2.close();
        } catch (IOException e) {
        }

        checkbox10 = (CheckBox) view.findViewById(R.id.checkBox10);
        if (text2.toString().contains("net.tcp.buffersize.default=4096,87380,256960,4096,16384,256960")
                && text2.toString().contains("net.tcp.buffersize.wifi=4096,87380,256960,4096,16384,256960")
                && text2.toString().contains("net.tcp.buffersize.umts=4096,87380,256960,4096,16384,256960")
                && text2.toString().contains("net.tcp.buffersize.gprs=4096,87380,256960,4096,16384,256960")
                && text2.toString().contains("net.tcp.buffersize.edge=4096,87380,256960,4096,16384,256960")
                && text2.toString().contains("net.tcp.buffersize.hspa=6144,87380,524288,6144,163 84,262144")
                && text2.toString().contains("net.tcp.buffersize.lte=524288,1048576,2097152,5242 88,1048576,2097152")
                && text2.toString().contains("net.tcp.buffersize.hsdpa=6144,87380,1048576,6144,8 7380,1048576")
                && text2.toString().contains("net.tcp.buffersize.evdo_b=6144,87380,1048576,6144, 87380,1048576")) {
            checkbox10.setChecked(true);
        } else {
            checkbox10.setChecked(false);
        }
        checkbox10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {

                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/net.tcp.buffersize.default/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.wifi/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.umts/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.gprs/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.edge/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.hspa/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.lte/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.hsdpa/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.evdo_b/d' /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.default=4096,87380,256960,4096,16384,256960\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.wifi=4096,87380,256960,4096,16384,256960\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.umts=4096,87380,256960,4096,16384,256960\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.gprs=4096,87380,256960,4096,16384,256960\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.edge=4096,87380,256960,4096,16384,256960\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.hspa=6144,87380,524288,6144,163 84,262144\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.lte=524288,1048576,2097152,5242 88,1048576,2097152\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.hsdpa=6144,87380,1048576,6144,8 7380,1048576\" >> /system/build.prop",
                                                                              "echo \"net.tcp.buffersize.evdo_b=6144,87380,1048576,6144, 87380,1048576\" >> /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }


                                                      } else {
                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/net.tcp.buffersize.default/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.wifi/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.umts/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.gprs/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.edge/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.hspa/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.lte/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.hsdpa/d' /system/build.prop",
                                                                              "busybox sed -i '/net.tcp.buffersize.evdo_b/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system"
                                                                      );
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }
                                                      }

                                                  }
                                              }

        );

        checkbox11 = (CheckBox) view.findViewById(R.id.checkBox11);
        String check6 = "/system/etc/init.d/05InternetTweak";
        if (new File(check6).exists()) {
            checkbox11.setChecked(true);
        } else {
            checkbox11.setChecked(false);
        }
        checkbox11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {
                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "cp /sdcard/Android/data/com.nowenui.systemtweaker/files/05InternetTweak /system/etc/init.d/",
                                                                              "chmod 777 /system/etc/init.d/05InternetTweak",
                                                                              "dos2unix /system/etc/init.d/05InternetTweak",
                                                                              "sh /system/etc/init.d/05InternetTweak",
                                                                              "busybox mount -o ro,remount /proc /system");
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }


                                                      } else {
                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "rm -f /system/etc/init.d/05InternetTweak",
                                                                              "busybox mount -o ro,remount /proc /system"
                                                                      );
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }
                                                      }

                                                  }
                                              }

        );

        checkbox12 = (CheckBox) view.findViewById(R.id.checkBox12);
        if (text2.toString().contains("net.dns1=8.8.8.8")
                && text2.toString().contains("net.dns2=8.8.4.4")
                && text2.toString().contains("net.rmnet0.dns1=8.8.8.8")
                && text2.toString().contains("net.rmnet0.dns2=8.8.4.4")
                && text2.toString().contains("net.ppp0.dns1=8.8.8.8")
                && text2.toString().contains("net.ppp0.dns2=8.8.4.4")
                && text2.toString().contains("net.wlan0.dns1=8.8.8.8")
                && text2.toString().contains("net.wlan0.dns2=8.8.4.4")
                && text2.toString().contains("net.eth0.dns1=8.8.8.8")
                && text2.toString().contains("net.eth0.dns2=8.8.4.4")
                && text2.toString().contains("net.gprs.dns1=8.8.8.8")
                && text2.toString().contains("net.gprs.dns2=8.8.4.4")) {
            checkbox12.setChecked(true);
        } else {
            checkbox12.setChecked(false);
        }
        checkbox12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {

                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/net.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.rmnet0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.rmnet0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.ppp0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.ppp0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.wlan0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.wlan0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.eth0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.eth0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.gprs.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.gprs.dns2/d' /system/build.prop",
                                                                              "echo \"net.dns1=8.8.8.8\" >> /system/build.prop",
                                                                              "echo \"net.dns2=8.8.4.4\" >> /system/build.prop",
                                                                              "echo \"net.rmnet0.dns1=8.8.8.8\" >> /system/build.prop",
                                                                              "echo \"net.rmnet0.dns2=8.8.4.4\" >> /system/build.prop",
                                                                              "echo \"net.ppp0.dns1=8.8.8.8\" >> /system/build.prop",
                                                                              "echo \"net.ppp0.dns2=8.8.4.4\" >> /system/build.prop",
                                                                              "echo \"net.wlan0.dns1=8.8.8.8\" >> /system/build.prop",
                                                                              "echo \"net.wlan0.dns2=8.8.4.4\" >> /system/build.prop",
                                                                              "echo \"net.eth0.dns1=8.8.8.8\" >> /system/build.prop",
                                                                              "echo \"net.eth0.dns2=8.8.4.4\" >> /system/build.prop",
                                                                              "echo \"net.gprs.dns1=8.8.8.8\" >> /system/build.prop",
                                                                              "echo \"net.gprs.dns2=8.8.4.4\" >> /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }


                                                      } else {
                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/net.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.rmnet0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.rmnet0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.ppp0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.ppp0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.wlan0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.wlan0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.eth0.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.eth0.dns2/d' /system/build.prop",
                                                                              "busybox sed -i '/net.gprs.dns1/d' /system/build.prop",
                                                                              "busybox sed -i '/net.gprs.dns2/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system"
                                                                      );
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }
                                                      }

                                                  }
                                              }

        );

        checkbox13 = (CheckBox) view.findViewById(R.id.checkBox13);
        if (text2.toString().contains("ro.config.hw_fast_dormancy=1")) {
            checkbox13.setChecked(true);
        } else {
            checkbox13.setChecked(false);
        }
        checkbox13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {

                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/ro.config.hw_fast_dormancy/d' /system/build.prop",
                                                                              "echo \"ro.config.hw_fast_dormancy=1\" >> /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }


                                                      } else {
                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/ro.config.hw_fast_dormancy/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system"
                                                                      );
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }
                                                      }

                                                  }
                                              }

        );

        checkbox14 = (CheckBox) view.findViewById(R.id.checkBox14);
        if (text2.toString().contains("persist.telephony.support.ipv6=1")) {
            checkbox14.setChecked(true);
        } else {
            checkbox14.setChecked(false);
        }
        checkbox14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {
                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/persist.telephony.support.ipv6/d' /system/build.prop",
                                                                              "echo \"persist.telephony.support.ipv6=1\" >> /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }


                                                      } else {
                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/persist.telephony.support.ipv6/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system"
                                                                      );
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }
                                                      }

                                                  }
                                              }

        );

        checkbox15 = (CheckBox) view.findViewById(R.id.checkBox15);
        if (text2.toString().contains("persist.telephony.support.ipv4=1")) {
            checkbox15.setChecked(true);
        } else {
            checkbox15.setChecked(false);
        }
        checkbox15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {

                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/persist.telephony.support.ipv4/d' /system/build.prop",
                                                                              "echo \"persist.telephony.support.ipv4=1\" >> /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }


                                                      } else {
                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/persist.telephony.support.ipv4/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system"
                                                                      );
                                                                      try {
                                                                          RootTools.getShell(true).add(command1);
                                                                          Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                      } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                          ex.printStackTrace();
                                                                          Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }

                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                              }
                                                          } else {
                                                              Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                              RootTools.offerBusyBox(getActivity());
                                                          }
                                                      }

                                                  }
                                              }

        );

        openvpnsupport = (CheckBox) view.findViewById(R.id.openvpnsupport);
        String check7 = "/system/xbin/openvpn";
        if (new File(check7).exists()) {
            openvpnsupport.setChecked(true);
        } else {
            openvpnsupport.setChecked(false);
        }
        openvpnsupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                      @Override
                                                      public void onCheckedChanged(CompoundButton buttonView,
                                                                                   boolean isChecked) {
                                                          if (isChecked) {

                                                              if (RootTools.isBusyboxAvailable()) {
                                                                  if (RootTools.isRootAvailable()) {
                                                                      if (RootTools.isAccessGiven()) {
                                                                          Command command1 = new Command(0,
                                                                                  "busybox mount -o rw,remount /proc /system",
                                                                                  "mkdir /system/xbin/bb",
                                                                                  "chmod 755 /system/xbin/bb/",
                                                                                  "cp /sdcard/Android/data/com.nowenui.systemtweaker/files/openvpn /system/xbin/",
                                                                                  "chmod 755 /system/xbin/openvpn",
                                                                                  "cp /sdcard/Android/data/com.nowenui.systemtweaker/files/placeholder /system/xbin/bb/",
                                                                                  "chmod 755 /system/xbin/bb/placeholder",
                                                                                  "busybox mount -o ro,remount /proc /system");
                                                                          try {
                                                                              RootTools.getShell(true).add(command1);
                                                                              Toast.makeText(getActivity(), R.string.tweakenabled, Toast.LENGTH_SHORT).show();
                                                                          } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                              ex.printStackTrace();
                                                                              Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      } else {
                                                                          Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                      }

                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }
                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                                  RootTools.offerBusyBox(getActivity());
                                                              }


                                                          } else {
                                                              if (RootTools.isBusyboxAvailable()) {
                                                                  if (RootTools.isRootAvailable()) {
                                                                      if (RootTools.isAccessGiven()) {
                                                                          Command command1 = new Command(0,
                                                                                  "busybox mount -o rw,remount /proc /system",
                                                                                  "rm -f /system/xbin/openvpn",
                                                                                  "rm -f /system/xbin/bb/placeholder",
                                                                                  "busybox mount -o ro,remount /proc /system"
                                                                          );
                                                                          try {
                                                                              RootTools.getShell(true).add(command1);
                                                                              Toast.makeText(getActivity(), R.string.tweakdisabled, Toast.LENGTH_SHORT).show();
                                                                          } catch (IOException | RootDeniedException | TimeoutException ex) {
                                                                              ex.printStackTrace();
                                                                              Toast.makeText(getActivity(), R.string.errordev, Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      } else {
                                                                          Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                      }

                                                                  } else {
                                                                      Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                                                  }
                                                              } else {
                                                                  Toast.makeText(getActivity(), R.string.errobusybox, Toast.LENGTH_SHORT).show();
                                                                  RootTools.offerBusyBox(getActivity());
                                                              }
                                                          }

                                                      }
                                                  }

        );

        return view;
    }


}