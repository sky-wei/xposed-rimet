# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Serializable
-keepclasseswithmembernames class * implements java.io.Serializable {*;}

-keep class tk.anysoft.xposed.lark.Main {*;}
-keep class tk.anysoft.xposed.lark.StringFog

-keep class rx.** {*;}
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class com.squareup.** {*;}
-keep class org.apache.** {*;}
-keep class com.google.gson.** {*;}
-keep class android.support.** {*;}
-keep class retrofit2.** {*;}
-keep class com.google.** {*;}


-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn com.amap.**

-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keep   class com.amap.api.services.**{*;}

-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}