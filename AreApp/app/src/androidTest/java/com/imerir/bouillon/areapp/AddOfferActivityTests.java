package com.imerir.bouillon.areapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.imerir.bouillon.areapp.Activities.AddOfferActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by SireRemy on 30/05/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AddOfferActivityTests {

    @Rule
    public ActivityTestRule<AddOfferActivity> mActivityRule = new ActivityTestRule<>(AddOfferActivity.class);

    @Test
    public void testChamps() {
        onView(withId(R.id.NomOffre)).perform(typeText("DEV ANDROID"), closeSoftKeyboard());
        onView(withId(R.id.Duree)).perform(typeText("10 mois"), closeSoftKeyboard());
        onView(withId(R.id.NomEntreprise)).perform(typeText("GOOGLE"), closeSoftKeyboard());
        onView(withId(R.id.Lieu)).perform(typeText("Perpignan"), closeSoftKeyboard());
        onView(withId(R.id.NomContact)).perform(typeText("Hello World ! "), closeSoftKeyboard());
        onView(withId(R.id.MailContact)).perform(typeText("Hello World ! "), closeSoftKeyboard());
        onView(withId(R.id.PhoneContact)).perform(typeText("Hello World ! "), closeSoftKeyboard());
        onView(withId(R.id.NomEntreprise)).perform(typeText("Hello World ! "), closeSoftKeyboard());
        onView(withId(R.id.Detail)).perform(typeText("Hello World ! "), closeSoftKeyboard());
        onView(withId(R.id.DetailsResponsables)).perform(typeText("Hello World ! "), closeSoftKeyboard());

        onView(withId(R.id.AjouterOffre)).perform(click());

        //.....????
    }
}
