package icu.uiyzzi.hdmiswitcher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HdmiSwitcher";
    private static final boolean DEBUG = true;

    private static final String OPEN_PACKAGE_NAME = "com.softwinner.awlivetv";
    private static final String OPEN_ACTIVITY_NAME = "com.softwinner.awlivetv.MainActivity";
    private static final String TV_DATA_NAME = "input_source";
    private static final String MANUAL_SET_SOURCE = "manual_set_source";

    // 信号源列表（来自逆向代码）
    private static final String[][] INPUT_SOURCES = {
        {"HDMI1", "hdmi"},
        {"HDMI2", "hdmi"},
        {"HDMI3", "hdmi"},
        {"CVBS1", "cvbs"},
        {"CVBS2", "cvbs"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 直接执行 HDMI1 切换，退出当前 Activity
        switchToHdmi1();
        finish();
    }

    /**
     * 切换到 HDMI1 输入源
     * 核心逻辑来源于逆向代码
     */
    private void switchToHdmi1() {
        if (DEBUG) {
            Log.w(TAG, "switchToHdmi1 init");
        }

        // 查找 HDMI1 输入源
        boolean foundHdmi = findAndOpenTv(INPUT_SOURCES[0]);  // ["HDMI1", "hdmi"]

        if (!foundHdmi) {
            Toast.makeText(this, R.string.hdmi_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 查找 TV 输入设备并启动目标 Activity
     * @param inputSource 输入源数组 [显示名, 参数]
     * @return 是否找到匹配的输入源
     */
    private boolean findAndOpenTv(String[] inputSource) {
        String displayName = inputSource[0];  // 如 "HDMI1"
        String param = inputSource[1];        // 如 "hdmi"

        // 获取 TvInputManager 服务
        TvInputManager tvInputManager = (TvInputManager) getSystemService("tv_input");

        if (tvInputManager == null) {
            Log.e(TAG, "TvInputManager is null");
            return false;
        }

        // 遍历所有 TV 输入设备
        List<TvInputInfo> tvInputList = tvInputManager.getTvInputList();
        if (tvInputList == null) {
            Log.e(TAG, "TvInputList is null");
            return false;
        }

        boolean found = false;
        for (TvInputInfo tvInputInfo : tvInputList) {
            if (DEBUG) {
                Log.w(TAG, "Checking input: " + tvInputInfo.getId());
            }

            // 检查输入源的 label 和 id 是否包含目标参数
            CharSequence label = tvInputInfo.loadLabel(this);
            String labelStr = label != null ? label.toString().toLowerCase(Locale.ROOT) : "";
            String idStr = tvInputInfo.getId().toLowerCase(Locale.ROOT);

            // 检查是否匹配参数 (如 "hdmi" 或 "cvbs")
            if (labelStr.contains(param) || idStr.contains(param)) {
                found = true;
                if (DEBUG) {
                    Log.w(TAG, "Found matching input: " + tvInputInfo.getId());
                }
                break;
            }
        }

        if (!found) {
            if (DEBUG) {
                Log.w(TAG, "No matching input source found for: " + param);
            }
            return false;
        }

        if (DEBUG) {
            Log.w(TAG, "openTv check success, displayName: " + displayName);
        }

        // 创建 Intent 启动 TV 应用
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(OPEN_PACKAGE_NAME, OPEN_ACTIVITY_NAME);
        intent.setComponent(componentName);

        // 传递参数：使用显示名称 "HDMI1"
        intent.putExtra(TV_DATA_NAME, displayName);
        intent.putExtra(MANUAL_SET_SOURCE, true);

        // 设置 flags
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
            overridePendingTransition(0, 0);
            if (DEBUG) {
                Log.w(TAG, "Successfully started TV activity");
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "Activity not found: " + OPEN_ACTIVITY_NAME);
            Toast.makeText(this, R.string.tv_app_not_found, Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
