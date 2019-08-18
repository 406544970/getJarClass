import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
    /**
     * 这些默认方法不打印
     */
    private static String DEFAULT_METHOD = "waitequalsnotifynotifyAlltoStringhashCodegetClass";

    public static void main(String[] args) throws Exception {
        String FileName = "D:\\java\\IDEA WorkSpace\\authority\\lib\\" +
                "ToolJar.jar";
        if (!FileName.isEmpty())
            getJarName(FileName);
    }

    public static void getJarName(String jarFile) throws Exception {

        try{
            //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
            File f = new File(jarFile);
            URL url1 = f.toURI().toURL();
            URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1},Thread.currentThread().getContextClassLoader());

            //通过jarFile和JarEntry得到所有的类
            JarFile jar = new JarFile(jarFile);
            //返回zip文件条目的枚举
            Enumeration<JarEntry> enumFiles = jar.entries();
            JarEntry entry;

            //测试此枚举是否包含更多的元素
            while(enumFiles.hasMoreElements()){
                entry = (JarEntry)enumFiles.nextElement();
                if(entry.getName().indexOf("META-INF")<0){
                    String classFullName = entry.getName();
                    if(!classFullName.endsWith(".class")){
                        classFullName = classFullName.substring(0,classFullName.length()-1);
                    } else{
                        //去掉后缀.class
                        String className = classFullName.substring(0,classFullName.length()-6).replace("/", ".");
                        Class<?> myclass = myClassLoader.loadClass(className);
                        //打印类名
                        System.out.println("*****************************");
                        System.out.println("全类名:" + className);

                        //根据class对象获得属性
                        Field[] fields = myclass.getDeclaredFields();
                        for(Field f1 : fields){
                            //打印每个属性的类型
                            System.out.println("~~~~~~~~~~~~~~~属性类型：" + f1.getType());
                            //打印每个属性的名字
                            System.out.println("~~~~~~~~~~~~~~~属性名称：" + f1.getName());
                        }

                        //得到类中包含的属性
                        Method[] methods = myclass.getMethods();
                        for (Method method : methods) {
                            String methodName = method.getName();
                            System.out.println("方法名称:" + methodName);
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            for (Class<?> clas : parameterTypes) {
                                // String parameterName = clas.getName();
                                String parameterName = clas.getSimpleName();
                                System.out.println("参数类型:" + parameterName);
                            }
                            System.out.println("==========================");
                        }
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
