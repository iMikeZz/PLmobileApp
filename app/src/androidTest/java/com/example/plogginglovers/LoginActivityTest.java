package com.example.plogginglovers;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityEmailAndPasswordEmpty() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        onView(withText("Email e password estão vazios"))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void loginActivityEmptyPasswordTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("teste@teste.pt"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextEmail), withText("teste@teste.pt"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        onView(withText("Campo Password está vazia"))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void loginActivityEmptyEmailTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("teste"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextPassword), withText("teste"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        onView(withText("Campo E-mail está vazio"))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void loginActivityInvalidEmailTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("testes.teste.pt"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextEmail), withText("testes.teste.pt"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("teste"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextPassword), withText("teste"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        onView(withText("Por favor insira um E-mail válido"))
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }

    @Test
    public void loginActivityValidLoginAndLogoutTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("s1@plogginglovers.test"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextEmail), withText("s1@plogginglovers.test"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextEmail), withText("s1@plogginglovers.test"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("secret"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.editTextPassword), withText("secret"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(pressImeActionButton());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btnLogin), withText("Entrar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.relativeLayout),
                                        1),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction viewGroup = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.drawer_layout),
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0)),
                        0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));


        logout();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Terminou sessão")).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))) .check(matches(isDisplayed()));
    }

    private void logout() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        onView(withId(R.id.design_navigation_view)).perform(swipeUp());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        15),
                        isDisplayed()));
        navigationMenuItemView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
