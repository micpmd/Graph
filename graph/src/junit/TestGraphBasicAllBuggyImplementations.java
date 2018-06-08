package junit;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


public class TestGraphBasicAllBuggyImplementations
{
    // Run with your code in graph.impl
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestYourCode{
        @BeforeClass
        public static void init() {
            VERSION.version = 0;
        }
    }
    
    // run with code in graph.broken1 package
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestBuggy1{
        @BeforeClass
        public static void init() {
            VERSION.version = 1;
        }
    }
    
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestBuggy2 {
        @BeforeClass
        public static void init() {
            VERSION.version = 2;
        }
    }
    
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestBuggy3 {
        @BeforeClass
        public static void init() {
            VERSION.version = 3;
        }
    }
    
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestBuggy4{
        @BeforeClass
        public static void init() {
            VERSION.version = 4;
        }
    }
    
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestBuggy5{
        @BeforeClass
        public static void init() {
            VERSION.version = 5;
        }
    }

    // run with Spacco's solution
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
      TestGraphBasic.class
    })
    public static class TestSpacco{
        @BeforeClass
        public static void init() {
            VERSION.version = -1;
        }
    }

}
