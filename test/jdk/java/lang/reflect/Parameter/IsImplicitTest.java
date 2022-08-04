/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */


/**
 * @test
 * @bug 8023087
 * @run main IsImplicitTest
 * @summary the isImplicit function should return true for parameters with the mandated modifier (JLS18 13.1 #12)
 */

import java.lang.reflect.*;
import java.util.Arrays;

public class IsImplicitTest {
    public static int errors = 0;
    public static void main(String[] args) throws Exception {

        errors += testingInnerConstructor() ? 0 : 1;
        System.out.println();
        errors += testAnon() ? 0 : 1;
        System.out.println();
        errors += testColorEnums() ? 0 : 1;
        System.out.println();
        errors += testCompactConstructor() ? 0 : 1;
        
        if (errors > 0)
            throw new RuntimeException("failed " + errors + " tests");
    }

    public static boolean testingInnerConstructor() {
        Constructor<?>[] cons = NestedClass.class.getDeclaredConstructors();
        System.out.println(cons.length);
        for (Constructor<?> c : cons) {
            Parameter[] params = c.getParameters();
            for (Parameter p : params) {
                System.out.println(p.toString() + ": is implicit: " + p.isImplicit());
                if (!p.isImplicit())
                    return false;
            }
        }
        return true;
    }   
    public static boolean testAnon() throws Exception {
        CreateAnonClass outerClass = new CreateAnonClass();
        Foo anonObj = outerClass.createAnonInstance();

        Class<?> anonymousClassType = Class.forName("CreateAnonClass$1");

        Constructor<?>[] cons = anonymousClassType.getDeclaredConstructors();
        for (Constructor<?> c : cons) {
            System.out.println(c.toString());
            Parameter[] params = c.getParameters();
            for (Parameter p : params) {
                System.out.println("    " + p.toString() + ": is implicit: " + p.isImplicit());
                if (!p.isImplicit())
                    return false;
            }
        }
        return true;     
    }

    public static boolean testColorEnums() throws Exception {
        Class<?> enum_class = ColorEnums.class;
        Method[] methods = enum_class.getMethods();
        Method valOfmethod = null;
        for (Method m : methods) {
            if (m.getName() == "valueOf") {
                valOfmethod = m;
            }
        }
        if (valOfmethod != null) {
            Parameter[] params = valOfmethod.getParameters();
            for (Parameter p : params) {
                System.out.println(p.getName() + " is implicit: " + p.isImplicit());
                if (!p.isImplicit())
                    return false;
            }
        }
        else {
            return false;
        }
        return true;
    }

    public static boolean testCompactConstructor() throws Exception{
        RecordClass rc_instance = new RecordClass(2, 5);
        Class<?> rc = rc_instance.getClass();
        Class<?>[] paramTypes = Arrays.stream(rc.getRecordComponents())
            .map(RecordComponent::getType).toArray(Class<?>[]::new);
        Constructor<?> con = rc.getDeclaredConstructor(paramTypes);
        Parameter[] params = con.getParameters();
        for (Parameter p : params) {
            System.out.println(p.getName() + " is implicit: " + p.isImplicit());
            if (!p.isImplicit())
                return false;
        }
        return true;
    }

    class NestedClass {
        private int time;
        private String name;
        public NestedClass(int time) {
            this.time = time;
            this.name = null;
        }
        public NestedClass(String name) {
            this.name = name;
            this.time = 0;
        }
    }


}

class Foo {
    String name = "orginalFoo";
}

class CreateAnonClass {
    IsImplicitTest ti = new IsImplicitTest();

    public Foo createAnonInstance() {
        Foo anonInstance = new Foo() {
            String new_name = "anonFoo";
            int age = 5;
        };
        return anonInstance;
    }
}

enum ColorEnums {
    RED, ORANGE, YELLOW;
}

record RecordClass(int num, int denom) {
    private static int gcd(int a, int b) {
        if (b == 0) return Math.abs(a);
        else return gcd(b, a % b);
    }
    RecordClass(int num, int denom) {
        int gcd = gcd(num, denom);
        num    /= gcd;
        denom  /= gcd;
        this.num   = num;
        this.denom = denom;
    }
}

