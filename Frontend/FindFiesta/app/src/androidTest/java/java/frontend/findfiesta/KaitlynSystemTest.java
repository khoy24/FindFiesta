package java.frontend.findfiesta;


import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.frontend.findfiesta.databinding.ActivityAccountBinding;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class KaitlynSystemTest {


    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<AccountActivity> activityScenarioRule = new ActivityScenarioRule<>(AccountActivity.class);


//    binding test 1 (accounts)

    @Test
    public void accountBindingTest(){
        // Inflate the layout using ActivityScenario
        ActivityScenario<AccountActivity> activityScenario = activityScenarioRule.getScenario();
        activityScenario.onActivity(activity -> {
            View view = LayoutInflater.from(activity).inflate(R.layout.activity_account, null);

            // Bind views using generated binding class
            ActivityAccountBinding binding = ActivityAccountBinding.bind(view);

        });
    }

//    account settings tests

    /**
     * tests the users ability to change their theme
     */
    @Test
    public void changeThemeSetting(){

//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
//        click settings button
        onView(withId(R.id.settingsButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//      check it switches to settings page
        onView(withId(R.id.activity_settings)).check(matches(isDisplayed()));

//        check the change theme button
        onView(withId(R.id.changeThemeButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check that the screen changes to change theme page
        onView(withId(R.id.activity_changetheme)).check(matches(isDisplayed()));
//        click green theme button
        onView(withId(R.id.greenThemeButton)).perform(click());
//        check if the users theme
        int themeID = Actor.getCurrentTheme();
//        onView(withId(R.id.activity_changetheme)).check();
//      check that the actors current theme changed to green
        assertEquals(Actor.getCurrentTheme(), 2131820634);

//        click the pink theme button
        onView(withId(R.id.pinkThemeButton)).perform(click());
//        check the actors current theme is pink
        assertEquals(Actor.getCurrentTheme(), 2131820666);

//        click the main theme button
        onView(withId(R.id.mainThemeButton)).perform(click());
//        check the actors current theme is main
        assertEquals(Actor.getCurrentTheme(), 2131820633);

//        check the back button
        onView(withId(R.id.backButton11)).perform(click());
//        check the screen changed back to settings
        onView(withId(R.id.activity_settings)).check(matches(isDisplayed()));

    }
    //
    @Test
    public void changePassword(){

//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
//        click settings button
        onView(withId(R.id.settingsButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//      check it switches to settings page
        onView(withId(R.id.activity_settings)).check(matches(isDisplayed()));

        String oldPassword = Actor.getCurrentPassword();
        String newPassword = "NewPassword1";
//      type out the new password
        onView(withId(R.id.changePassword)).perform(typeText(newPassword), closeSoftKeyboard());
        onView(withId(R.id.changePassword)).perform(clearText());
        onView(withId(R.id.changePassword)).perform(typeText("NewPassword1"), closeSoftKeyboard());
//        click change password button
        onView(withId(R.id.changePasswordButton)).perform(click());
//        check that the actors new current password equals the new password
        assertEquals(Actor.getCurrentPassword(), newPassword);

//        change the password back to the old password
//        delete textView text
        onView(withId(R.id.changePassword)).perform(clearText());
        //      type out the new password
        onView(withId(R.id.changePassword)).perform(typeText(oldPassword), closeSoftKeyboard());
//        click change password button
        onView(withId(R.id.changePasswordButton)).perform(click());
//        check that the actors new current password equals the new password
        assertEquals(Actor.getCurrentPassword(), oldPassword);



    }


    @Test
    public void changeUsername(){

//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
//        click settings button
        onView(withId(R.id.settingsButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//      check it switches to settings page
        onView(withId(R.id.activity_settings)).check(matches(isDisplayed()));

        String oldUsername = Actor.getCurrentUsername();
        String newUsername = "newUsername";
//      type out the new password
        onView(withId(R.id.changeUsername)).perform(typeText(newUsername), closeSoftKeyboard());
//        click change username button
        onView(withId(R.id.usernameChangeButton)).perform(click());
//        check that the actors new current username equals the new password
        assertEquals(Actor.getCurrentUsername(), newUsername);

//        check the back button
        onView(withId(R.id.backButton6)).perform(click());
//        check it goes back to the account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
        //        check the account page has the new username
        onView(withId(R.id.GreetingText)).check(matches(withText(endsWith(newUsername))));
//       go back to settings page
        onView(withId(R.id.settingsButton)).perform(click());

//        change the username back to the old username
//        delete textView text
        onView(withId(R.id.changeUsername)).perform(clearText());
        //      type out the new username
        onView(withId(R.id.changeUsername)).perform(typeText(oldUsername), closeSoftKeyboard());
//        click change username button
        onView(withId(R.id.usernameChangeButton)).perform(click());
//        check that the actors new current username equals the new username
        assertEquals(Actor.getCurrentUsername(), oldUsername);

    }

    @Test
    public void logOut(){
//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }


        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
//        click settings button
        onView(withId(R.id.settingsButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//      check it switches to settings page
        onView(withId(R.id.activity_settings)).check(matches(isDisplayed()));

//        clicks the logout button
        onView(withId(R.id.logoutButton)).perform(click());

//        check the login screen displays and current user is null
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));
        assertEquals(Actor.getCurrentUsername(), null);

    }


//    account tests

    @Test
    public void myPosts() {

//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";
//        String resultString = "olleh";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

//        check it goes to myposts page
        onView(withId(R.id.myPostsButton)).perform(click());
        onView(withId(R.id.activity_myposts)).check(matches(isDisplayed()));
//        check back button
        onView(withId(R.id.backButton4)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
    }

    @Test
    public void followers() {
//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

//        click followers button
        onView(withId(R.id.userListButton)).perform(click());
//        check it goes to following page
        onView(withId(R.id.activity_myfollowers)).check(matches(isDisplayed()));
//        check chrisMoseley follows khoyme
//        onView(withId(R.id.myFollowingList)).check()
        onData(hasToString(containsString("chrisMoseley")))
                .inAdapterView(withId(R.id.myFollowersList))
                .check(matches(isDisplayed()));

        //        test back button
        onView(withId(R.id.backButton5)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.createPostButton)).perform(click());
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.homeButton)).perform(click());
        onView(withId(R.id.accountButton)).perform(click());

        onView(withId(R.id.userListButton)).perform(click());
        onData(hasToString(containsString("chrisMoseley")))
                .inAdapterView(withId(R.id.myFollowersList))
                .perform(click());
//        screen isnt' supposed to change yet
//        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));

    }

    @Test
    public void following() {
        //        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.accountButton)).perform(click());
//        check it switches to account page
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

//        click following button, make sure it goes to following page
        onView(withId(R.id.followingBtn)).perform(click());
        onView(withId(R.id.activity_myfollowing)).check(matches(isDisplayed()));

        onData(hasToString(containsString("hehe")))
                .inAdapterView(withId(R.id.myFollowingList))
                .check(matches(isDisplayed()));

///     check recommended followers pops up
        onView(withId(R.id.recFollowList)).check(matches(isDisplayed()));
        // Click on the first item in the list
        onData(anything())
                .inAdapterView(withId(R.id.recFollowList))
                .atPosition(0)
                .perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton3)).perform(click());
        onView(withId(R.id.backButton)).perform(click());
//        onView(withId(R.id.accountButton));
        onView(withId(R.id.followingBtn)).perform(click());

//        not test the content of rec followers right now as its somewhat randomized
//        test back button
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

        onView(withId(R.id.followingBtn)).perform(click());
        onView(withId(R.id.activity_myfollowing)).check(matches(isDisplayed()));
        onData(hasToString(containsString("hehe")))
                .inAdapterView(withId(R.id.myFollowingList))
                .perform(click());
//        screen actually isn't supposed to change
//        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));
    }

    //    camerainstrumentation tests
    @Test
    public void validateCamera() {

//        login go to page
//
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText("khoyme@outlook.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("password1"), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.editProfilePicButton)).perform(click());

        // Create a bitmap we can use for our simulated camera image
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
//        intending(toPackage("com.android.camera2")).respondWith(result);

        // Verify that the camera intent is sent with the correct action and package
//        intending(allOf(hasAction(MediaStore.ACTION_IMAGE_CAPTURE), toPackage("com.android.camera")));

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
//        onView(withId(R.id.CameraBtn2)).perform(click());

//

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
//        intended(toPackage("com.android.camera2"));


//        Set the captured photo in the ImageView
//        intentsRule.getActivity().runOnUiThread(() -> intentsRule.getActivity().profilePic.setImageDrawable(Drawable.createFromPath("drawable/account_icon.png")));

        // Verify that the intent contains the simulated camera image data
//        intending(hasExtra("data", icon));

        // ... additional test steps and validation ...

//        take photo?
    }

    @Test
    public void validateCameraRoll() {

//        login go to page
//
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText("khoyme@outlook.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("password1"), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.editProfilePicButton)).perform(click());

        // Create a bitmap we can use for our simulated camera image
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
//        intending(toPackage("com.android.camera2")).respondWith(result);

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
        onView(withId(R.id.selectBtn)).perform(click());

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
//        intended(toPackage("com.android.camera2"));

        // ... additional test steps and validation ...

    }

    @Test
    public void validateDeletePic() {

//        login go to page
//
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText("khoyme@outlook.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("password1"), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.editProfilePicButton)).perform(click());

        // Create a bitmap we can use for our simulated camera image
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
//        intending(toPackage("com.android.camera")).respondWith(result);

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
        onView(withId(R.id.deletePicButton)).perform(click());

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
//        intended(toPackage("com.android.camera2"));

        // ... additional test steps and validation ...

    }

    //    actor tests (unit)
    @Test
    public void actormethodtests(){

        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";


        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
//        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));

//        if succeeded it should change views and the text on main should say Hello + username
        // Verify that activity switches to main
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

//        check all actor methods
        TestCase.assertEquals(Actor.getCurrentUsername(), "khoyme");
        TestCase.assertEquals(Actor.getCurrentTheme(), 2131820633);
        TestCase.assertEquals(Actor.getEnabled().booleanValue(), true);
        TestCase.assertEquals(Actor.getCurrentEmail(), "khoyme@outlook.com");
        TestCase.assertEquals(Actor.getCurrentId(), "133");
        TestCase.assertEquals(Actor.getCurrentPassword(), "password1");
        Actor.getInstance().setUserType(0);
        TestCase.assertEquals(Actor.getUserType(), 0);

    }

//    guest tests

    @Test
    public void guestAccountAccess(){

//        click login as guest button
        onView(withId(R.id.guestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that activity switches to main
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        // Verify that the text on main says "Hello" followed by guest
        onView(withId(R.id.welcomeMessage)).check(matches(withText(startsWith("Hello Guest"))));

//        Check all button sequences from main to see if it sends to createAccount (other than maps)
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

//        back to main by going back to login
        onView(withId(R.id.toLoginButton)).perform(click());
//        click login as guest button
        onView(withId(R.id.guestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        check the createPost button
        onView(withId(R.id.createPostButton)).perform(click());
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

//        back to main by going back to login
        onView(withId(R.id.toLoginButton)).perform(click());
//        click login as guest button
        onView(withId(R.id.guestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        check messages
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

//        back to main by going back to login
        onView(withId(R.id.toLoginButton)).perform(click());
//        click login as guest button
        onView(withId(R.id.guestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        check map (should work)
        onView(withId(R.id.mapButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

//        check other user page and all buttons
        onView(withId(R.id.findUsersButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check page changed
        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
//        search name
        onView(withId(R.id.searchUserText)).perform(typeText("khoyme"), closeSoftKeyboard());
        onView(withId(R.id.findUserButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check screen switched
        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));

//        check followers list works
        onView(withId(R.id.otherUsersFollowersButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onData(hasToString(containsString("chrisMoseley")))
                .inAdapterView(withId(R.id.followersList))
                .check(matches(isDisplayed()));

        onView(withId(R.id.removeFollowerButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check goes to create account
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

//        re-login as guest and try follow button
        onView(withId(R.id.toLoginButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        click login as guest button
        onView(withId(R.id.guestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.findUsersButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check page changed
        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
//        search name
        onView(withId(R.id.searchUserText)).perform(typeText("khoyme"), closeSoftKeyboard());
        onView(withId(R.id.findUserButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check follow button goes to create account page
        onView(withId(R.id.followUserButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

//        re-login as guest and check unfollow button
        onView(withId(R.id.toLoginButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        click login as guest button
        onView(withId(R.id.guestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.findUsersButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check page changed
        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
//        search name
        onView(withId(R.id.searchUserText)).perform(typeText("khoyme"), closeSoftKeyboard());
        onView(withId(R.id.findUserButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check follow button goes to create account page
        onView(withId(R.id.unfollowButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

        onView(withId(R.id.toLoginButton)).perform(click());
        onView(withId(R.id.guestButton)).perform(click());
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

        onView(withId(R.id.toLoginButton)).perform(click());
        onView(withId(R.id.guestButton)).perform(click());
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.createPostButton)).perform(click());
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

        onView(withId(R.id.toLoginButton)).perform(click());
        onView(withId(R.id.guestButton)).perform(click());
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.activity_createaccount)).check(matches(isDisplayed()));

        onView(withId(R.id.toLoginButton)).perform(click());
        onView(withId(R.id.guestButton)).perform(click());
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onView(withId(R.id.homeButton)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

    }

//    login tests

    /**
     * Start the server and run this test
     */
    @Test
    public void loginEmail(){
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

//        check all texts are visible
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.textView2)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextTextEmailAddress)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextTextPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton2)).check(matches(isDisplayed()));
        onView(withId(R.id.createAccountButton)).check(matches(isDisplayed()));
        onView(withId(R.id.guestButton)).check(matches(isDisplayed()));

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
//        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));

//        if succeeded it should change views and the text on main should say Hello + username
        // Verify that activity switches to main
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        // Verify that the text on main says "Hello" followed by the username
        onView(withId(R.id.welcomeMessage)).check(matches(withText(startsWith("Hello "))));
    }
    //
    @Test
    public void loginFail(){
        String testEmailString = "khoyme@iastate.edu";
        String testPasswordString = "pass";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        // Verify that activity does not switch to main when the account does not exist
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));

    }

    @Test
    public void loginToCreateAccount(){

        onView(withId(R.id.createAccountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that activity switches to create account (textView3 is the Create Account text appearance)
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

//        try creating an account with a password without a number
        onView(withId(R.id.userName)).perform(typeText("eeeeee"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("eeeee"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("eeee"), closeSoftKeyboard());


    }

    @Test
    public void LoginCreateLogin(){
//      from login screen click create account
        onView(withId(R.id.createAccountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that activity switches to create account (textView3 is the Create Account text appearance)
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

//        then once on create account page go back to the login screen
        onView(withId(R.id.toLoginButton)).perform(click());
        // Verify that activity switches to login page
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));


    }

    /**
     * tests creating an account (texts & buttons) shouldn't be able to log in since
     * they cannot verify their account
     */
    @Test
    public void createAccount(){

        onView(withId(R.id.createAccountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that activity switches to create account (textView3 is the Create Account text appearance)
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));


        String testString = "testing3";
//      type in info
        onView(withId(R.id.userName)).perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(testString), closeSoftKeyboard());

//        click create account
        onView(withId(R.id.CreateAccountButton2)).perform(click());
        Actor.getInstance().setUserEnabled(true);
//        makeEnabled("testing2", "testing2");
        onView(withId(R.id.toLoginButton)).perform(click());

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        it shouldn't let them login despite creating an account since they are not verified so it should stay on login
//        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));

//        now they should be able to log in
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.deleteAccountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        check back to login page
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));

//        try creating user with space in name
        onView(withId(R.id.createAccountButton)).perform(click());
//        test empty fields
        onView(withId(R.id.CreateAccountButton2)).perform(click());

        onView(withId(R.id.userName)).perform(typeText("space name"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("emailtest"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("passwordtest1"), closeSoftKeyboard());
        onView(withId(R.id.CreateAccountButton2)).perform(click());

//        try creating user with no fields
        onView(withId(R.id.userName)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.CreateAccountButton2)).perform(click());


    }

//    maps tests


    @Test
    public void maps(){

//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";
//        String resultString = "olleh";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//            check it goes to the maps page
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));

//            check all the nav bar buttons
        onView(withId(R.id.homeButton)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));

        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.createPostButton)).perform(click());
        onView(withId(R.id.activity_createpost)).check(matches(isDisplayed()));

        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
        onView(withId(R.id.mapButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.mapsearch)).perform(click());
        onView(withId(R.id.mapsearch)).perform(typeSearchViewText("australia"), closeSoftKeyboard());

//            check the search
        Espresso.onView(ViewMatchers.withId(R.id.mapsearch))
                .perform(ViewActions.typeText("Your Location"), closeSoftKeyboard());

        // Simulate submitting the search query
//            onView(isAssignableFrom(SearchView.class))
//                    .perform(pressImeActionButton());
//
//            // Assert that the intent is sent to search for the location
//            Intents.intended(IntentMatchers.hasAction(Intent.ACTION_SEARCH));
        typeSearchViewText("Australia");
    }

    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,false);
            }
        };
    }

//    messages test


    @Test
    public void messages(){

        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));
//        check goes to messages screen
        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));
        onView(withId(R.id.textView6)).check(matches(isDisplayed()));

//        check the public chat websocket
        onView(withId(R.id.publicChat2Button)).perform(click());
        onView(withId(R.id.activity_publicchat2)).check(matches(isDisplayed()));
//       check send message
        onView(withId(R.id.msgText)).perform(typeText("hello!"), closeSoftKeyboard());
//        send message button
        onView(withId(R.id.sendMsgBtn)).perform(click());
//        now check textview ends with hello!
        onView(withId(R.id.msgChat)).check(matches(withText(endsWith("hello!"))));
        //        check the back button
        onView(withId(R.id.backBtn90)).perform(click());
        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));

//        create a new message
        onView(withId(R.id.newMessageButton)).perform(click());
//        check the screen changes
        onView(withId(R.id.activity_newmessage)).check(matches(isDisplayed()));
//        try an account that doesn't exist, a toast pops up but no error so screen just stays
        onView(withId(R.id.userSearch)).perform(typeText("eeee"));
        onView(withId(R.id.activity_newmessage)).check(matches(isDisplayed()));
//        empty text
        onView(withId(R.id.userSearch)).perform(clearText());
//        try an account that exists
        onView(withId(R.id.userSearch)).perform(typeText("chrisMoseley"));
//        create chat button
        onView(withId(R.id.findUserButton2)).perform(click());
//        back button
        onView(withId(R.id.backButton20)).perform(click());
        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));

//      check the messages list contains a message with user after creating it
        onData(hasToString(containsString("chrisMoseley")))
                .inAdapterView(withId(R.id.messagesList))
                .check(matches(isDisplayed()));

//        check all button sequences
        onView(withId(R.id.mapButton)).perform(click());
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.homeButton)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onView(withId(R.id.messagesButton)).perform(click());
        onView(withId(R.id.createPostButton)).perform(click());
        onView(withId(R.id.activity_createpost)).check(matches(isDisplayed()));
        onView(withId(R.id.messagesButton)).perform(click());

//        check going into the messages
        onView(withId(R.id.messagesList)).check(matches(isDisplayed()));
//        onData(withText("hehe")).perform(click());
        onData(hasToString(containsString("hehe")))
                .inAdapterView(withId(R.id.messagesList))
                .perform(click());

        onView(withId(R.id.activity_directmessage)).check(matches(isDisplayed()));
        onView(withId(R.id.senderText)).check(matches(withText(endsWith("hehe"))));
//        type message hello, check it displays
        onView(withId(R.id.msgToSend)).perform(typeText("hello there!"), closeSoftKeyboard());
//        send button
        onView(withId(R.id.buttonToSend)).perform(click());
//        check the message pops up in the text view
        onView(withId(R.id.directMessageView)).check(matches(withText(endsWith("hello there!"))));
//        check back button
        onView(withId(R.id.backToMsgButton)).perform(click());
        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));

    }

//    other user tests


    @Test
    public void findUser(){

//        login
        String testEmailString = "khoyme@outlook.com";
        String testPasswordString = "password1";
//        String resultString = "olleh";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
//        click find users button
        onView(withId(R.id.findUsersButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request

//        check if it takes you to find users page
        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
//        search for user chrisMoseley
        onView(withId(R.id.searchUserText)).perform(typeText("chrisMoseley"), closeSoftKeyboard());

        onView(withId(R.id.findUserButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        should take you to the other users page
        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));
//        check the other username displayed is chrisMoseley
        onView(withId(R.id.otherUsernameText)).check(matches(withText(startsWith("chrisMoseley"))));
        onView(withId(R.id.OtherProfilePicture)).check(matches(isDisplayed()));

//        check followers
        onView(withId(R.id.otherUsersFollowersButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onData(hasToString(containsString("defaultpic")))
                .inAdapterView(withId(R.id.followersList))
                .check(matches(isDisplayed()));
        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(1000000000);
//        } catch (InterruptedException e) {
//        }

////        check unfollow
        onView(withId(R.id.unfollowButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        check followers
        onView(withId(R.id.otherUsersFollowersButton)).perform(click());
//        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        onData(not(hasToString(containsString("khoyme"))))
//                .inAdapterView(withId(R.id.followersList))
//                .check(matches(isDisplayed()));
//        follow again
        onView(withId(R.id.followUserButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.otherUsersFollowersButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onData(hasToString(containsString("defaultpic")))
                .inAdapterView(withId(R.id.followersList))
                .check(matches(isDisplayed()));
//
//
//        check remove follower
        onView(withId(R.id.removeFollowerButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        check back buttons

        onView(withId(R.id.backButton3)).perform(click());
        onView(withId(R.id.userListBackButton)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onView(withId(R.id.accountButton)).perform(click());
//        click followers button
        onView(withId(R.id.userListButton)).perform(click());
//        onData(not(hasToString(containsString("chrisMoseley"))))
//                .inAdapterView(withId(R.id.myFollowersList))
//                .check(matches(isDisplayed()));
        onView(withId(R.id.backButton5)).perform(click());
//        logout
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.logoutButton)).perform(click());
//
//        login with chrisMoseley to follow khoyme so other tests aren't messed up, tests follow button
        //        login
        testEmailString = "chrisrmoseley@outlook.com";
        testPasswordString = "password";

        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmailString), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPasswordString), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.findUsersButton)).perform(click());
        onView(withId(R.id.searchUserText)).perform(typeText("khoyme"), closeSoftKeyboard());
        onView(withId(R.id.findUserButton)).perform(click());
        onView(withId(R.id.followUserButton)).perform(click());

    }

//    profile pic test

    @Test
    public void profilePic(){


        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText("khoyme@outlook.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("password1"), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
//        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));

//        if succeeded it should change views and the text on main should say Hello + username
        // Verify that activity switches to main
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
        onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));
        onView(withId(R.id.editProfilePicButton)).check(matches(isDisplayed()));
        onView(withId(R.id.GreetingText)).check(matches(isDisplayed()));
        onView(withId(R.id.myPostsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.followingBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.userListButton)).check(matches(isDisplayed()));
        onView(withId(R.id.settingsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.linearlayoutaccount)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView5)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView4)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView3)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.accountButton)).check(matches(isDisplayed()));
        onView(withId(R.id.homeButton)).check(matches(isDisplayed()));
        onView(withId(R.id.mapButton)).check(matches(isDisplayed()));
        onView(withId(R.id.messagesButton)).check(matches(isDisplayed()));
        onView(withId(R.id.createPostButton)).check(matches(isDisplayed()));

        onView(withId(R.id.editProfilePicButton)).perform(click());
//        check all are displayed
        onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));
        onView(withId(R.id.selectBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.CameraBtn2)).check(matches(isDisplayed()));
        onView(withId(R.id.deletePicButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backbutton20)).check(matches(isDisplayed()));

        onView(withId(R.id.backbutton20)).perform(click());
        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

        onView(withId(R.id.editProfilePicButton)).perform(click());
//        select from camera roll
        onView(withId(R.id.selectBtn)).perform(click());

    }

//    reporting tests

//
//    @Test
//    public void reportPostAndUser(){
//        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("khoyme@outlook.com"), closeSoftKeyboard());
//        onView(withId(R.id.editTextTextPassword)).perform(typeText("password"), closeSoftKeyboard());
//        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.loginButton2)).perform(click());
////        login and create post, request to be a mod
//        String title = "admintestpost";
//        String location = "falalaliland";
//        String notes = "n/a";
////
//        onView(withId(R.id.title)).perform(typeText(title), closeSoftKeyboard());
//        onView(withId(R.id.location)).perform(typeText(location), closeSoftKeyboard());
//        onView(withId(R.id.note)).perform(typeText(notes), closeSoftKeyboard());
//        onView(withId(R.id.submit)).perform(click());
//        onView(withId(R.id.lostCB)).perform(click());
//        onView(withId(R.id.foundCB)).perform(click());
//        onView(withId(R.id.submit)).perform(click());
//        onView(withId(R.id.foundCB)).perform(click());
//        onView(withId(R.id.submit)).perform(click());
//
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//
//        }
//
//        onView(withId(R.id.activity_post_add_picture)).check(matches(isDisplayed()));
//        onView(withId(R.id.SkipBtn)).perform(click());
//
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//
//        }
//
//        //apply to be mod then logout
//        onView(withId(R.id.accountButton)).perform(click());
//        onView(withId(R.id.modApplyButton)).perform(click());
//        onView(withId(R.id.settingsButton)).perform(click());
//        onView(withId(R.id.logoutButton)).perform(click());
//
////        login as testing5 to report that post
//
//        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("testing5"), closeSoftKeyboard());
//        onView(withId(R.id.editTextTextPassword)).perform(typeText("testing5"), closeSoftKeyboard());
//        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        //       click login button
//        onView(withId(R.id.loginButton2)).perform(click());
//        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//
//        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
//        //        click find users button
//        onView(withId(R.id.findUsersButton)).perform(click());
//        // Put thread to sleep to allow volley to handle the request
//
////        check if it takes you to find users page
//        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
////        search for user chrisMoseley
//        onView(withId(R.id.searchUserText)).perform(typeText("teehee"), closeSoftKeyboard());
//
//        onView(withId(R.id.findUserButton)).perform(click());
//        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
////        should take you to the other users page
//        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));
////        check the other username displayed is chrisMoseley
//        onView(withId(R.id.otherUsernameText)).check(matches(withText(startsWith("teehee"))));
//
//        onView(withId(R.id.reportUserButton)).perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.activity_reportcomment)).check(matches(isDisplayed()));
//        onView(withId(R.id.textView8)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.reportComments)).perform(typeText("they are evil"), closeSoftKeyboard());
//        onView(withId(R.id.sendCommentButton)).perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//
////        goes back to main
//        onView(withId(R.id.backButton70)).perform(click());
//
//        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//
//        onData(hasToString(containsString("admintestpost")))
//                .inAdapterView(withId(R.id.postList))
//                .perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.reportPostButton)).perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.reportComments)).perform(typeText("i hope it stays lost"), closeSoftKeyboard());
//        onView(withId(R.id.sendCommentButton)).perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.backButton70)).perform(click());
//
//
//
//
//    }

//    user types tests

//
//    @Test
//    public void admin(){
//
////        user logs in applys to be a mod
//
////        admin logs in
//
//        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("findfiesta309@gmail.com"), closeSoftKeyboard());
//        onView(withId(R.id.editTextTextPassword)).perform(typeText("findfiestarules"), closeSoftKeyboard());
//        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.loginButton2)).perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//        onView(withId(R.id.accountButton)).perform(click());
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//
//        onView(withId(R.id.reportsButton)).check(matches(isDisplayed()));
//        onView(withId(R.id.modRequestsButton)).check(matches(isDisplayed()));
//
////        tests reports
//        onView(withId(R.id.reportsButton)).perform(click());
//        onView(withId(R.id.activity_reports)).check(matches(isDisplayed()));
//
////        test clicking on a report
//        onData(anything())
//                .inAdapterView(withId(R.id.reportsList))
//                .atPosition(0)
//                .perform(click());
//        onView(withId(R.id.activity_viewreports)).check(matches(isDisplayed()));
//        onView(withId(R.id.reportedUserText)).check(matches(isDisplayed()));
//
//
////        back button
//        onView(withId(R.id.backButton90)).perform(click());
//        onView(withId(R.id.activity_reports)).check(matches(isDisplayed()));
//
////        back to account
//        onView(withId(R.id.backButton50)).perform(click());
//        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
//
//
////        test mod requests
//        onView(withId(R.id.modRequestsButton)).perform(click());
//        onView(withId(R.id.activity_modrequests)).check(matches(isDisplayed()));
//
////        back to account
//        onView(withId(R.id.backbutton60)).perform(click());
////        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));
//
//    }


    @Test
    public void adminTestReport(){

        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("testing4"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("testing4"), closeSoftKeyboard());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.loginButton2)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS * 2);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.createPostButton)).perform(click());
//        login and create post, request to be a mod
        String title = "admintestpost";
        String location = "falalaliland";
        String notes = "n/a";
//
        onView(withId(R.id.title)).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.location)).perform(typeText(location), closeSoftKeyboard());
        onView(withId(R.id.note)).perform(typeText(notes), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
        onView(withId(R.id.lostCB)).perform(click());
        onView(withId(R.id.foundCB)).perform(click());
        onView(withId(R.id.submit)).perform(click());
        onView(withId(R.id.foundCB)).perform(click());
        onView(withId(R.id.submit)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.SkipBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        //apply to be mod then logout
        onView(withId(R.id.accountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS * 2);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.modApplyButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS * 2);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.settingsButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.logoutButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//

//        login as testing5 to report that post

        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("testing5"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("testing5"), closeSoftKeyboard());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //       click login button
        onView(withId(R.id.loginButton2)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        //        click find users button
        onView(withId(R.id.findUsersButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request

//        check if it takes you to find users page
        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
//        search for user chrisMoseley
        onView(withId(R.id.searchUserText)).perform(typeText("testing4"), closeSoftKeyboard());

        onView(withId(R.id.findUserButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        should take you to the other users page
        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));
//        check the other username displayed is chrisMoseley
        onView(withId(R.id.otherUsernameText)).check(matches(withText(startsWith("testing4"))));

        onView(withId(R.id.reportUserButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activity_reportcomment)).check(matches(isDisplayed()));
        onView(withId(R.id.textView8)).check(matches(isDisplayed()));

        onView(withId(R.id.reportComments)).perform(typeText("they are evil"), closeSoftKeyboard());
        onView(withId(R.id.sendCommentButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        goes back to main
        onView(withId(R.id.backButton70)).perform(click());

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //        click find users button
        onView(withId(R.id.findUsersButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request

//        check if it takes you to find users page
        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));
//        search for user chrisMoseley
        onView(withId(R.id.searchUserText)).perform(typeText("testing4"), closeSoftKeyboard());

        onView(withId(R.id.findUserButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        should take you to the other users page
        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));
//        check the other username displayed is chrisMoseley
        onView(withId(R.id.otherUsernameText)).check(matches(withText(startsWith("testing4"))));

        onView(withId(R.id.reportUserButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activity_reportcomment)).check(matches(isDisplayed()));
        onView(withId(R.id.textView8)).check(matches(isDisplayed()));

        onView(withId(R.id.reportComments)).perform(typeText("they are evil"), closeSoftKeyboard());
        onView(withId(R.id.sendCommentButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        goes back to main
        onView(withId(R.id.backButton70)).perform(click());

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        doesn't work, too far down
        onData(is("Lost: admintestpost"))
                .inAdapterView(withId(R.id.postList))
                .perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.reportPostButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.reportComments)).perform(typeText("i hope it stays lost"), closeSoftKeyboard());
        onView(withId(R.id.sendCommentButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.backButton70)).perform(click());

//        logout
        onView(withId(R.id.accountButton)).perform(click());
        onView(withId(R.id.settingsButton)).perform(click());
        onView(withId(R.id.logoutButton)).perform(click());

//        login as admin and view reports
//        disable account of first one

//        admin logs in

        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("findfiesta309@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("findfiestarules"), closeSoftKeyboard());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.loginButton2)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.accountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.reportsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.modRequestsButton)).check(matches(isDisplayed()));

//        approve khoyme then decline to keep khoyme at 0 for testing
//        or make another account stay at 0 (login and apply)

//        tests reports
        onView(withId(R.id.reportsButton)).perform(click());
        onView(withId(R.id.activity_reports)).check(matches(isDisplayed()));

//        test clicking on a report
        onData(anything())
                .inAdapterView(withId(R.id.reportsList))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.activity_viewreports)).check(matches(isDisplayed()));
        onView(withId(R.id.reportedUserText)).check(matches(isDisplayed()));

//        submit moderator notes
        onView(withId(R.id.modNotes)).perform(typeText("these are mod notes"));
        onView(withId(R.id.submitModNotesButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

//        view the profile of the post
        onView(withId(R.id.viewReportedAccountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        go back
        onView(withId(R.id.backButton3)).perform(click());
//        disable account
        onView(withId(R.id.disableAccountButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.backButton90)).perform(click());
//        test clicking on a report
        onData(anything())
                .inAdapterView(withId(R.id.reportsList))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.activity_viewreports)).check(matches(isDisplayed()));
        onView(withId(R.id.reportedUserText)).check(matches(isDisplayed()));

//        submit moderator notes
        onView(withId(R.id.modNotes)).perform(typeText("these are mod notes"), closeSoftKeyboard());
        onView(withId(R.id.submitModNotesButton)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.deleteReportButton)).perform(click());
        onView(withId(R.id.backButton90)).perform(click());

//        test clicking on a report with a post
        onData(anything())
                .inAdapterView(withId(R.id.reportsList))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.activity_viewreports)).check(matches(isDisplayed()));
        onView(withId(R.id.reportedUserText)).check(matches(isDisplayed()));

        onView(withId(R.id.viewReportedPostButton)).perform(click());
        onView(withId(R.id.backButton100)).perform(click());

        onView(withId(R.id.deleteReportedPostButton)).perform(click());
        onView(withId(R.id.backButton90)).perform(click());
        onView(withId(R.id.backButton50)).perform(click());
//        view completed reports
        onView(withId(R.id.completedReportsButton)).perform(click());
//        on reports page
//        click on first completed report
        onData(anything())
                .inAdapterView(withId(R.id.reportsList))
                .atPosition(0)
                .perform(click());
//        delete the completed report
        onView(withId(R.id.deleteCompletedReportButton)).perform(click());
//      go back
        onView(withId(R.id.backButton101)).perform(click());
        onView(withId(R.id.backButton50)).perform(click());


//        mod requests
        onView(withId(R.id.modRequestsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.modRequestsButton)).perform(click());

//        test clicking on a mod request
        onData(anything())
                .inAdapterView(withId(R.id.modApplicationsList))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.approveModButton)).perform(click());
        onView(withId(R.id.declineModButton)).perform(click());
        onView(withId(R.id.backButton3)).perform(click());
        onView(withId(R.id.backbutton60)).perform(click());



    }

    @Test
    public void mod(){


//        login, has mod permissions. check reports is there, mods isn't.

    }


}
