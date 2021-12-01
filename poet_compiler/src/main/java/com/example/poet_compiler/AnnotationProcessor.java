package com.example.poet_compiler;

import com.example.poet_annotation.Route;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/***
 *
 */
@AutoService(Process.class)
public class AnnotationProcessor extends AbstractProcessor {

    Filer filer;

    /***
     * 该方法主要用于一些初始化的操作，通过该方法的参数ProcessingEnvironment可以获取一些列有用的工具类。
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        filer = processingEnv.getFiler();
        super.init(processingEnv);
    }

    /***
     * 返回此注释 Processor 支持的最新的源版本，该方法可以通过注解@SupportedSourceVersion指定。
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    /***
     * 返回此 Processor 支持的注释类型的名称。结果元素可能是某一受支持注释类型的规范（完全限定）名称。
     * 它也可能是 ” name.” 形式的名称，表示所有以 ” name.” 开头的规范名称的注释类型集合。
     * 最后，自身表示所有注释类型的集合，包括空集。注意，Processor 不应声明 “*”，除非它实际处理了所有文件；
     * 声明不必要的注释可能导致在某些环境中的性能下降。
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Route.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//      原理，就是去找所有继承了activity文件，并且是被Router注解了的文件
//      第一种方式：字符串的拼接
//      第二中方式：用javaopet规范来生成文件

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Route.class);


        Map<String, String> map = new HashMap<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
//            activity全名
            String activityName = typeElement.getQualifiedName().toString();
            String key = typeElement.getAnnotation(Route.class).value();
            map.put(key, activityName);

//            Route注解的类，生成java文件
//            第一种方式：字符串拼接
            generatedClassOne(map);


        }
        return false;
    }

    private void generatedClassOne(Map<String, String> map) {
        if (map.size() > 0) {
            String name = "ActivityUtil" + System.currentTimeMillis();
            Writer writer = null;
            try {
                JavaFileObject sourceFile = filer.createSourceFile("com.maniu.router" + name);
                writer = sourceFile.openWriter();
//              字符串拼接
                StringBuffer stringBuffer = new StringBuffer();
//                stringBuffer.append("package com.maniu.router")
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}