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

/*
 * @test
 * @bug 6962585
 * @summary inner classes should their return proper modifiers
 * @run main TestInnerClasses
 */

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
