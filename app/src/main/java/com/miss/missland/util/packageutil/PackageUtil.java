package com.miss.missland.util.packageutil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PackageUtil {

    /**
     *      跳转到其他 APP
     * @param context
     * @param packageName   目标包名
     * @param activityName  目标Activity
     */
    public static void jump(Context context,String packageName,String activityName,String value) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        /**知道要跳转应用的包命与目标Activity*/
        ComponentName componentName = new ComponentName(packageName, packageName + "." + activityName);
        intent.setComponent(componentName);
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
