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

#------------------------------------------通用区域----------------------------------------------------

#代码混淆压缩比，在0和7之间，默认为5，一般不需要改
-optimizationpasses 5
#混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
#指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses
#指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
# 混淆时记录日志，混淆后就会生成映射文件
#包含有类名->混淆后类名的映射关系
#然后使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt
# 不优化输入的类文件
-dontoptimize
#不做预校验，preverify是proguard的4个步骤之一
#Android不需要preverify，去掉这一步可加快混淆速度
-dontpreverify
# 混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn dalvik.**
# 匿名类
-keepattributes EnclosingMethod
# 忽略警告
-ignorewarnings
# 把混淆类中的方法名也混淆了
#-useuniqueclassmembernames
# 优化时允许访问并修改有修饰符的类和类的成员
#-allowaccessmodification
# 将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
# 保留行号
-keepattributes SourceFile,LineNumberTable


#---------------------默认保留-------------------------
#基础保留
-keep class * extends android.app.Activity
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.preference.Preference
-keep class * extends android.view.View {
    <init>(...);
}

#序列化
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#WebView
-keepclassmembers class * extends android.webkit.WebView {*;}
-keepclassmembers class * extends android.webkit.WebViewClient {*;}
-keepclassmembers class * extends android.webkit.WebChromeClient {*;}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 方法名中含有“JNI”字符、或带有 native 关键词的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除
-keepclasseswithmembers class * { native <methods>; }
-keepclasseswithmembers class * {
    ... *JNI*(...);
}
-keepclasseswithmembernames class * {
	... *JRI*(...);
}
-keep class **JNI* {*;}

# keep annotated by NotProguard
-keep @com.proguard.annotation.NotProguard class * {*;}
-keep class * {
    @com.proguard.annotation <fields>;
    @android.webkit.JavascriptInterface <fields>;
}
-keepclassmembers class * {
    @com.proguard.annotation <fields>;
    @android.webkit.JavascriptInterface <fields>;
}

# 保护注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keep class com.busap.myvideo.livenew.nearby.widget.menu.base.anotation.**{*;}
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
# 避免混淆泛型
-keepattributes Signature
# 避免混淆反射
-keepattributes EnclosingMethod
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 不混淆内部类
-keepattributes InnerClasses

# 不混淆枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆Bean对象
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * {
    public <init> (org.json.JSONObject);
}

# R文件的静态成员
-keepclassmembers class **.R$* {
    public static <fields>;
}

#kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

#-------------------------------------------项目定义区-------------------------------------------------
-keep class tv.newtv.minilive.data.bean.** { *; }

# icntvplayer
-dontwarn tv.icntv.icntvplayersdk.**
-keep class tv.icntv.icntvplayersdk.** { *; }

-dontwarn com.google.common.**
-keep class com.google.common.**{*;}

#rxjava
-dontwarn com.squareup.**
-keep class com.squareup.** {*;}

-dontwarn com.google.auto.**
-keep class com.google.auto.**{*;}

#adsdk
-dontwarn tv.icntv.**
-keep class tv.icntv.**{*;}

#amlogic
-dontwarn com.droidlogic.**
-keep class com.droidlogic.**{*;}

#service
-dontwarn tv.newtv.logservice.**
-keep class tv.newtv.logservice.**{*;}

#vds
-dontwarn tv.icntv.vds
-keep class tv.icntv.**{*;}

#retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#okhttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-dontwarn okhttp3.**
-keep class okio.**{*;}
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# gson
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }

# NewTvSdk
-dontwarn tv.newtv.ottsdk.**
-keep class tv.newtv.ottsdk.**{*;}

# POI
-dontwarn org.apache.poi.**
-keep class org.apache.poi.**{*;}
-dontwarn javax.**
-keep class javax.**{*;}
-dontwarn com.fasterxml.**
-keep class com.fasterxml.**{*;}