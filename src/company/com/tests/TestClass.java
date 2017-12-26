package company.com.tests;

import company.com.annotations.Before;
import company.com.annotations.Expected;
import company.com.annotations.SetUp;
import company.com.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestClass {
    HashMap<String, List<String>> testMap;

    @SetUp
    public void setUp() {
        System.out.println("SetUp");
    }


    @Before
    public void before() {
        System.out.println("Before");
    }

   /* @Before
    public void before1(){

    }*/

    @Test
    @Expected(exception = RuntimeException.class)
    public void toPass1() {
        throw new RuntimeException("what happened");
    }


    @Test(isEnebled = false)
    public void toSkip() {
    }

    @Expected(exception = NullPointerException.class)
    @Test
    public void toFail1() {
        throw new RuntimeException("sadf");
    }

    @Test
    @Expected(exception = IndexOutOfBoundsException.class)
    public void toFail2() {
        throw new RuntimeException();
    }

    @Test
    public void toFail3() {
        throw new RuntimeException();
    }

}
