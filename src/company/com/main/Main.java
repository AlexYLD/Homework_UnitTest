package company.com.main;

import company.com.annotations.*;
import company.com.tests.TestClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Method[] declaredMethods = TestClass.class.getDeclaredMethods();
        TestClass testClassInstance = TestClass.class.newInstance();
        HashMap<TestStatus, List<String>> results = new HashMap<>();
        results.put(TestStatus.PASSED, new ArrayList<>());
        results.put(TestStatus.FAILED, new ArrayList<>());
        results.put(TestStatus.SKIPPED, new ArrayList<>());

        Method setUp = null;
        Method before = null;
        Method after = null;
        Method destroy = null;

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(SetUp.class)) {
                if (setUp != null) {
                    throw new RuntimeException("Illegal envirement");
                }
                setUp = method;

            }
            if (method.isAnnotationPresent(Before.class)) {
                if (before != null) {
                    throw new RuntimeException("Illegal envirement");
                }
                before = method;
            }
            if (method.isAnnotationPresent(After.class)) {
                if (after != null) {
                    throw new RuntimeException("Illegal envirement");
                }
                after = method;

            }
            if (method.isAnnotationPresent(Destroy.class)) {
                if (destroy != null) {
                    throw new RuntimeException("Illegal envirement");
                }
                destroy = method;
            }
        }


        if (setUp != null) setUp.invoke(testClassInstance);

        for (Method method : declaredMethods) {
            try {
                if (method.isAnnotationPresent(Test.class) && method.getAnnotation(Test.class).isEnebled()) {
                    if (before != null) before.invoke(testClassInstance);
                    method.invoke(testClassInstance);
                    results.get(TestStatus.PASSED).add(method.getName());
                } else if (method.isAnnotationPresent(Test.class) && !method.getAnnotation(Test.class).isEnebled()) {
                    results.get(TestStatus.SKIPPED).add(method.getName());
                    continue;
                }
            } catch (InvocationTargetException e) {
                if (method.isAnnotationPresent(Expected.class) &&
                        method.getAnnotation(Expected.class).exception().equals(e.getCause().getClass())) {
                    results.get(TestStatus.PASSED).add(method.getName());
                } else {
                    results.get(TestStatus.FAILED).add(method.getName());
                }

            }
            if (after != null) after.invoke(testClassInstance);
        }
        if (destroy != null) destroy.invoke(testClassInstance);
        System.out.println(results);
    }


}
