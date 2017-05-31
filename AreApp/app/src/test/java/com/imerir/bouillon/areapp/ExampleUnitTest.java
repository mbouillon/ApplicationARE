package com.imerir.bouillon.areapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 2 tests très simples qui vérifient 2 objets pour des incohérences (2 numéros dans notre cas)
     * @throws Exception
     */
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * La première méthode a été exécutée et la seconde obtient une erreur depuis 5! = (2 + 2).
     * Vous pouvez également configurer une exception prévue pour le test en utilisant un paramètre attendu:
     * @throws Exception
     */
    @Test
    public void addition_isNotCorrect() throws Exception {
        assertEquals("Numbers isn't equals!", 5, 2 + 2);
    }

    @Test(expected = NullPointerException.class)
    public void nullStringTest() {
        String str = null;
        assertTrue(str.isEmpty());
    }

}