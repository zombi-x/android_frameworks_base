package org.meerkats.katkiss;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import android.view.IWindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionHandler {
    protected ArrayList<String> mActions;
    protected Context mContext;
    static final String TAG = "ActionHandler";

    public ActionHandler(Context context, ArrayList<String> actions) {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        mContext = context;
        mActions = actions;
    }

    public ActionHandler(Context context, String actions) {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        mContext = context;
        mActions = new ArrayList<String>();
        mActions.addAll(Arrays.asList(actions.split("\\|")));
    }

    public ActionHandler(Context context) {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        mContext = context;
    }


    public void addAction(String action) {
      if (mActions == null)              
          mActions = new ArrayList<String>();
      mActions.add(action);
    }
    
    /**
     * Set the actions to perform.
     * 
     * @param actions
     */
    public void setActions(List<String> actions) {
        if (actions == null) {
            mActions = null;
        } else {
            mActions = new ArrayList<String>();
            mActions.addAll(actions);
        }
    }

    public List<String> getActions() { return mActions; }

/*    private void killProcess() {
        if (mContext
                .checkCallingOrSelfPermission(android.Manifest.permission.FORCE_STOP_PACKAGES) == PackageManager.PERMISSION_GRANTED) {
            ActivityManager am = (ActivityManager) mContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> task = am.getRunningTasks(1, 0, null);
            String packageName = task.get(0).baseActivity.getPackageName();

            //Check that we're not killing the launcher 
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            if (packageName.equals(resolveInfo.activityInfo.packageName)) {
                postActionEventHandled(false);
                return;
            }

            am.forceStopPackage(task.get(0).baseActivity.getPackageName());
            postActionEventHandled(true);
        } else {
            Log.d("ActionHandler", "Caller cannot kill processes, aborting");
            postActionEventHandled(false);
        }
    }
*/

    private void screenOff() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        pm.goToSleep(SystemClock.uptimeMillis());
    }
/*
    private void startAssistActivity() {
        IWindowManager mWm = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
        boolean isKeyguardShowing = false;
        try {
            isKeyguardShowing = mWm.isKeyguardLocked();
        } catch (RemoteException e) {

        }

        if (isKeyguardShowing) {
            try {
                mWm.showAssistant();
            } catch (RemoteException e) {
            }
        } else {
            Intent intent = ((SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE))
                    .getAssistIntent(mContext, true, UserHandle.USER_CURRENT);
            if (intent == null)
                return;

            try {
                ActivityManagerNative.getDefault().dismissKeyguardOnNextActivity();
            } catch (RemoteException e) {
                // too bad, so sad...
            }

            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivityAsUser(intent, new UserHandle(UserHandle.USER_CURRENT));
            } catch (ActivityNotFoundException e) {
                Slog.w(TAG, "Activity not found for " + intent.getAction());
            }
        }
    }
*/
/*    private void showPowerMenu() {
        new PowerMenu(mContext);
        postActionEventHandled(true);
    }
*/

    /**
     * This method is called after an action is performed. This is useful for subclasses to
     * override, such as the one in the lock screen. As you need to unlock the device after
     * performing an action.
     * 
     * @param actionWasPerformed
     */
    protected boolean postActionEventHandled(boolean actionWasPerformed) {
        return actionWasPerformed;
    }

    /**
     * This the the fall over method that is called if this base class cannot process an action. You
     * do not need to manually call {@link postActionEventHandled}
     * 
     * @param action
     * @return
     */
    public boolean handleAction(String action){ return false;}
}
