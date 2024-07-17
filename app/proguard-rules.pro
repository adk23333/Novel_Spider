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
-keep class net.sf.** {*;}

-dontwarn org.apache.**
-dontwarn org.openxmlformats.schemas.**
-dontwarn org.etsi.**
-dontwarn org.w3.**
-dontwarn com.microsoft.schemas.**
-dontwarn com.graphbuilder.**
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontnote org.apache.**
-dontnote org.openxmlformats.schemas.**
-dontnote org.etsi.**
-dontnote org.w3.**
-dontnote com.microsoft.schemas.**
-dontnote com.graphbuilder.**

-keeppackagenames org.apache.poi.ss.formula.function

-keep class org.apache.poi.** {*;}
-keep class org.apache.poi.ooxml.** {*;}
-keep class org.apache.xmlbeans.** {*;}
-keep class com.fasterxml.** {*;}
-keep class com.microsoft.schemas.** {*;}
-keep class org.openxmlformats.** {*;}
-keep class org.openxmlformats.schemas.** {*;}
-keep class schemaorg_apache_xmlbeans.** {*;}
-keep class org.apache.commons.compress.archivers.zip.** {*;}
-keep class org.apache.logging.log4j.** {*;}
-keepattributes *Annotation*, EnclosingMethod, Signature
-keepnames class org.apache.logging.log4j.** {*;}

