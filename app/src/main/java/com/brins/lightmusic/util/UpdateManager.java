package com.brins.lightmusic.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.brins.baselib.utils.ToastUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;

/**
 * Created by lipeilin
 * on 2021/3/10
 */
public class UpdateManager {
    public static final String TAG = "UpdateManager";
    private static final String LAST_CHECK_TIME = "app_update_last_check_time";
    private Activity mActivity;

    public UpdateManager(Activity activity) {
        this.mActivity = activity;
    }


    private UpgradeInfo loadUpgradeInfo() {
        return Beta.getUpgradeInfo();
    }

    public void checkUpgrade() {

        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                Log.d(TAG,"upgradeStateListener upgrade fail");
            }

            @Override
            public void onUpgradeSuccess(boolean b) {
                Log.d(TAG,"upgradeStateListener upgrade success");

            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                ToastUtils.show("没有更新", Toast.LENGTH_SHORT);
            }

            @Override
            public void onUpgrading(boolean b) {
                ToastUtils.show("正在更新", Toast.LENGTH_SHORT);
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Log.d(TAG,"upgradeStateListener download apk file success");

            }
        };
        Beta.checkUpgrade();
    }
}
