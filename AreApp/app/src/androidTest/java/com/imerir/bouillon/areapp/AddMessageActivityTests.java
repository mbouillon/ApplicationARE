package com.imerir.bouillon.areapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.imerir.bouillon.areapp.Activities.AddMessageActivity;
import com.imerir.bouillon.areapp.Models.Document;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by SireRemy on 30/05/2017.
 *
 * src/androidTest = Est destiné à des tests unitaires impliquant l'instrumentation android.
 *
 * src/test = Est pour un test d'unité pure qui n'implique pas le cadre Android.
 * Vous pouvez exécuter des tests ici sans courir sur un appareil réel ou sur un émulateur.
 */

@RunWith(AndroidJUnit4.class)
public class AddMessageActivityTests {

    @Rule
    public ActivityTestRule<AddMessageActivity> mActivityRule = new ActivityTestRule<>(AddMessageActivity.class);

    @Test
    public void testButton() {
        onView(withId(R.id.etMessage)).perform(typeText("Hello World ! "), closeSoftKeyboard());
        onView(withId(R.id.btnPublierMessage)).perform(click());

        //Assert.assertTrue();


    }

    /**
     * Tests the correctness of the initial text.
     *
    /*
    public void testEmptyView_labelText() {
        // It is good practice to read the string
        // from your resources in order to not break
        // multiple tests when a string changes.
        String expected = AddMessageActivityTests.getString(R.string.help_text);
        String actual = AddMessageActivity.getText().toString();
        assertEquals("mTestEmptyText contains wrong text",
                expected, actual);
    }
    */
}
