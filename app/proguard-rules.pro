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
-ignorewarnings

-keep class * {
    public private *;
}

-keepclassmembers class * {
        private *;
}

# Models
-keep class demo.app.com.app2.models.** { *; }

#Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

#butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

# For All extenal Libraries
-keep class org.** { *; }
-keep class android.** { *; }
-keep class java.** { *; }
-keep class me.** { *; }
-keep class butterknief.** { *; }
-keep class javax.** { *; }
-keep class io.** { *; }
-keep class io.protostuff.** { *; }
-keep class net.** { *; }
-keep class permissions.** { *; }
-keep class com.google.** { *; }
-keep class com.focus.** { *; }
-keep class com.github.** { *; }
-keep class com.afollestad.** { *; }
-keep class com.android.** { *; }
