package com.imerir.bouillon.areapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.imerir.bouillon.areapp.Activities.AddMessageActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by SireRemy on 30/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AddMessageActivityTests {

    @Rule
    public ActivityTestRule<AddMessageActivity> mActivityRule
            = new ActivityTestRule<>(AddMessageActivity.class);

    @Test
    public void testButton() {
        onView(withId(R.id.etMessage)).perform(typeText("Jake"), closeSoftKeyboard());
    }

}
