
import java.lang.reflect.*;

public class TestInnerClasses {
    public void testAnonymousClass() throws IllegalAccessException {
        int classMod = this.getClass().getModifiers();
        Constructor<?>[] con = this.getClass().getDeclaredConstructors();
        int conMod = con[0].getModifiers();
        if (classMod == 0 && conMod == 0)
            return;
        else
            throw new IllegalAccessException("anonymous class/constructor " +
                "should have default modifiers: " + classMod + " " + conMod);
    }
    void testLocalClass() throws IllegalAccessException {
        LocalClass loc = this.new LocalClass();
        int classMod = loc.getClass().getModifiers();
        Constructor<?>[] con = loc.getClass().getDeclaredConstructors();
        int conMod = con[0].getModifiers();
        if (classMod == 0 && conMod == 0) 
            return;
        else    
            throw new IllegalAccessException("local class/constructor " +
                "should match modifiers from declaration: " + classMod + " " + conMod);
    }
    class LocalClass {}
    public static void main(String[] args) throws IllegalAccessException {
        TestInnerClasses anon = new TestInnerClasses() {
            public void inner() {
                System.out.println("anonymous inner method");
            }
        };
        anon.testAnonymousClass();
        TestInnerClasses local = new TestInnerClasses();
        local.testLocalClass();
    }    
}
