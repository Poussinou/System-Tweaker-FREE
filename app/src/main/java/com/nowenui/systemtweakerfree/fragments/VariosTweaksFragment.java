package com.nowenui.systemtweakerfree.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class VariosTweaksFragment extends Fragment {
    private CheckBox checkbox24, checkbox25, quickboot;

    public static VariosTweaksFragment newInstance(Bundle bundle) {
        VariosTweaksFragment messagesFragment = new VariosTweaksFragment();

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
        View view = inflater.inflate(R.layout.fragment_variostweaks, parent, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        RootTools.debugMode = false;

        File file = new File("/system/build.prop");

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

        checkbox24 = (CheckBox) view.findViewById(R.id.checkBox24);
        if (text.toString().contains("debug.sf.nobootanimation=1")) {
            checkbox24.setChecked(true);
        } else {
            checkbox24.setChecked(false);
        }
        checkbox24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {

                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/debug.sf.nobootanimation/d' /system/build.prop",
                                                                              "echo \"debug.sf.nobootanimation=1\" >> /system/build.prop",
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
                                                                              "busybox sed -i '/debug.sf.nobootanimation/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
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

        quickboot = (CheckBox) view.findViewById(R.id.quickboot);
        String check16 = "/system/etc/init.d/quick_power";
        if (new File(check16).exists()) {
            quickboot.setChecked(true);
        } else {
            quickboot.setChecked(false);
        }
        quickboot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView,
                                                                              boolean isChecked) {
                                                     if (isChecked) {

                                                         if (RootTools.isBusyboxAvailable()) {
                                                             if (RootTools.isRootAvailable()) {
                                                                 if (RootTools.isAccessGiven()) {
                                                                     Command command1 = new Command(0,
                                                                             "busybox mount -o rw,remount /proc /system",
                                                                             "cp /sdcard/Android/data/com.nowenui.systemtweaker/files/quick_power /system/etc/init.d/",
                                                                             "chmod 777 /system/etc/init.d/quick_power",
                                                                             "dos2unix /system/etc/init.d/quick_power",
                                                                             "sh /system/etc/init.d/quick_power",
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
                                                                             "rm -f /system/etc/init.d/quick_power",
                                                                             "busybox mount -o ro,remount /proc /system");
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


        checkbox25 = (CheckBox) view.findViewById(R.id.checkBox25);
        if (text.toString().contains("persist.adb.notify=0")) {
            checkbox25.setChecked(true);
        } else {
            checkbox25.setChecked(false);
        }
        checkbox25.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView,
                                                                               boolean isChecked) {
                                                      if (isChecked) {

                                                          if (RootTools.isBusyboxAvailable()) {
                                                              if (RootTools.isRootAvailable()) {
                                                                  if (RootTools.isAccessGiven()) {
                                                                      Command command1 = new Command(0,
                                                                              "busybox mount -o rw,remount /proc /system",
                                                                              "busybox sed -i '/persist.adb.notify/d' /system/build.prop",
                                                                              "echo \"persist.adb.notify=0\" >> /system/build.prop",
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
                                                                              "busybox sed -i '/persist.adb.notify=0/d' /system/build.prop",
                                                                              "busybox mount -o ro,remount /proc /system");
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

    }


}