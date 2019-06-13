package lite.android.xiangwushuo.com.sr.app.router

import android.content.Intent
import android.os.Bundle
import lite.android.xiangwushuo.com.sr.BaseActivity
import lite.android.xiangwushuo.com.sr.app.MyApp
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile
import java.io.File
import java.util.*
import java.util.jar.JarEntry


object RouteInitial {

    private val pathMap : LinkedHashMap<String , Class<in BaseActivity>> = LinkedHashMap()

    fun init(){
        val classSet = getClassesFromPackage(MyApp.INSTANCE.packageName, true)
        for(className in classSet!!.iterator()){
//            val classLoader = BaseActivity::class.java.classLoader
//            var  clazz = Class.forName(className)
//            if(clazz.isInstance(BaseActivity::class.java)){
//                val activityClass = Class.forName(className , false , classLoader)
////                val activityClazz = clazz as BaseActivity::class.java
//                for(annotation in clazz.annotations){
//                    if(annotation is RoutePath){
////                        pathMap.put(annotation.path , activityClass as Class<in BaseActivity>)
//                        Log.e("RouteInitial" , "MDataBindActivity route path is ${annotation.path}")
//                    }
//                }
//            }
        }
    }

    fun routeByPath(path : String){
        if(pathMap.isEmpty()){
            init()
        }
        val appCxt = MyApp.INSTANCE
        val clazz = pathMap[path]
        appCxt.startActivity(Intent(appCxt , clazz).apply {
            this.putExtra("extraData" , Bundle().apply {
                this.putInt("index" , 1)

            })
        })

    }


    private fun  getClassesFromPackage( packageName:String,  isRecursion:Boolean) :Set<String>?{
        var  classNames :Set<String>?= null;
        val loader:ClassLoader = Thread.currentThread().contextClassLoader;
        val packagePath = packageName.replace(".", "/");

        val url = loader.getResource("$packagePath/");
        if (url != null) {
            val protocol = url.protocol;
            if (protocol.equals("file")) {
                classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
            } else if (protocol.equals("jar")) {
                var jarFile :JarFile ?= null;
                try {
                    jarFile =  (url.openConnection() as JarURLConnection).getJarFile()
                } catch (e : Exception ) {
                    e.printStackTrace();
                }

                if (jarFile != null) {
                    getClassNameFromJar(jarFile.entries(), packageName, isRecursion);
                }
            }
        } else {
            /*从所有的jar包中查找包名*/
            classNames = getClassNameFromJars((loader as URLClassLoader).getURLs(), packageName, isRecursion);
        }

        return classNames;
    }


    private fun getClassNameFromJars(urls: Array<URL>, packageName: String, isRecursion: Boolean): Set<String> {
        val classNames = HashSet<String>()

        for (i in urls.indices) {
            val classPath = urls[i].getPath()

            //不必搜索classes文件�?
            if (classPath.endsWith("classes/")) {
                continue
            }

            var jarFile: JarFile? = null
            try {
                jarFile = JarFile(classPath.substring(classPath.indexOf("/")))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (jarFile != null) {
                classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion))
            }
        }

        return classNames
    }

    private fun getClassNameFromJar(jarEntries: Enumeration<JarEntry>, packageName: String, isRecursion: Boolean): Set<String> {
        val classNames = HashSet<String>()

        while (jarEntries.hasMoreElements()) {
            val jarEntry = jarEntries.nextElement()
            if (!jarEntry.isDirectory()) {
                var entryName = jarEntry.getName().replace("/", "")
                if (entryName.endsWith(".class") && !entryName.contains("$") && entryName.startsWith(packageName)) {
                    entryName = entryName.replace(".class", "")
                    if (isRecursion) {
                        classNames.add(entryName)
                    } else if (!entryName.replace("$packageName.", "").contains("")) {
                        classNames.add(entryName)
                    }
                }
            }
        }

        return classNames
    }


    private fun getClassNameFromDir(filePath: String, packageName: String, isRecursion: Boolean): Set<String> {
        val className = HashSet<String>()
        val file = File(filePath)
        val files = file.listFiles()
        for (childFile in files) {
            if (childFile.isDirectory()) {
                if (isRecursion) {
                    className.addAll(getClassNameFromDir(childFile.getPath(), packageName + "" + childFile.getName(), isRecursion))
                }
            } else {
                val fileName = childFile.getName()
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    className.add(packageName + "" + fileName.replace(".class", ""))
                }
            }
        }

        return className
    }


}