package com.sun.tools.xjc.addon.krasa;

import java.math.BigDecimal;
import java.math.BigInteger;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vojtech Krasa
 */
public class UtilsTest {

    @Test
    public void testIsNumber() throws Exception {
        Assert.assertFalse(Utils.isNumber(String.class));
        Assert.assertFalse(Utils.isNumber(IllegalStateException.class));
        Assert.assertTrue(Utils.isNumber(BigDecimal.class));
    }

    @Test
    public void integerToIntTest() {
        assertEquals(12, Utils.toInt(Integer.valueOf(12)) );
        assertEquals(-3, Utils.toInt(Integer.valueOf(-3)) );
    }

    @Test
    public void bigIntegerToIntTest() {
        assertEquals(12, Utils.toInt(BigInteger.valueOf(12)) );
        assertEquals(-3, Utils.toInt(BigInteger.valueOf(-3)) );
    }

    @Test
    public void longToIntTest() {
        Utils.toInt(Long.valueOf(7L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptStringToIntTest() {
        Utils.toInt("7");
    }

    public static class A {
        public static float PUBLIC_VALUE = 1.23f;
        public static String PRIVATE_VALUE = "private";

        public float aPublic = PUBLIC_VALUE;
        private String aPrivate = PRIVATE_VALUE;
    }

    public static class B extends A {
    }

    public static class C {
        B b = new B();
    }

    public static class D {
        C c = new C();
    }

    @Test
    public void shouldGetAPrivateFieldFromClass() {
        assertNotNull(Utils.getSimpleField("aPrivate", A.class));
    }

    @Test
    public void shouldGetAPublicFieldFromClass() {
        assertNotNull(Utils.getSimpleField("aPublic", A.class));
    }

    @Test
    public void shouldGetAPrivateFieldFromSuperClass() {
        assertNotNull(Utils.getSimpleField("aPrivate", B.class));
    }

    @Test
    public void shouldGetAPublicFieldFromSuperClass() {
        assertNotNull(Utils.getSimpleField("aPublic", B.class));
    }

    @Test
    public void shoudGetFieldValue() {
        A a = new A();
        B b = new B();

        assertEquals(A.PRIVATE_VALUE, Utils.getFieldValue("aPrivate", a));
        assertEquals(A.PUBLIC_VALUE, Utils.getFieldValue("aPublic", a));
        assertEquals(A.PRIVATE_VALUE, Utils.getFieldValue("aPrivate", b));
        assertEquals(A.PUBLIC_VALUE, Utils.getFieldValue("aPublic", b));
    }

    @Test
    public void shouldGetCompositePathFieldValue() {
        C c = new C();
        assertEquals(A.PRIVATE_VALUE, Utils.getFieldValue("b.aPrivate", c));
        assertEquals(A.PUBLIC_VALUE, Utils.getFieldValue("b.aPublic", c));

        D d = new D();
        assertEquals(A.PRIVATE_VALUE, Utils.getFieldValue("c.b.aPrivate", d));
        assertEquals(A.PUBLIC_VALUE, Utils.getFieldValue("c.b.aPublic", d));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotFindAnInexistentField() {
        Utils.getFieldValue("inexistent", new B());
    }

    @Test
    public void shouldTestIsMin() {
        assertTrue(Utils.isMin("-9223372036854775808"));
        assertTrue(Utils.isMin("-2147483648"));
        assertFalse(Utils.isMin("-2147483649"));
    }

    @Test
    public void shouldTestIsMax() {
        assertTrue(Utils.isMax("9223372036854775807"));
        assertTrue(Utils.isMax("2147483647"));
        assertFalse(Utils.isMax("2147483648"));
    }

    public static class TypeTest {
        private String stringField;
        private int intField;
        private Long longField;
        private BigInteger bigIntegerField;
    }

    @Test
    public void isFieldTypeNameNumber() {
        assertTrue(Utils.isFieldTypeNameNumber(BigDecimal.class.getSimpleName()));
        assertTrue(Utils.isFieldTypeNameNumber(Long.class.getSimpleName()));
        assertTrue(Utils.isFieldTypeNameNumber(Short.class.getSimpleName()));
        assertTrue(Utils.isFieldTypeNameNumber(Integer.class.getSimpleName()));

        assertFalse(Utils.isFieldTypeNameNumber(String.class.getSimpleName()));
        assertFalse(Utils.isFieldTypeNameNumber(Boolean.class.getSimpleName()));
    }

    @Test
    public void isFieldTypeFullNameNumber() {
        assertTrue(Utils.isFieldTypeFullNameNumber(BigDecimal.class.getCanonicalName()));
        assertTrue(Utils.isFieldTypeFullNameNumber(Long.class.getCanonicalName()));
        assertTrue(Utils.isFieldTypeFullNameNumber(Short.class.getCanonicalName()));
        assertTrue(Utils.isFieldTypeFullNameNumber(Integer.class.getCanonicalName()));

        assertFalse(Utils.isFieldTypeFullNameNumber(String.class.getCanonicalName()));
        assertFalse(Utils.isFieldTypeFullNameNumber(Boolean.class.getCanonicalName()));
    }
}
