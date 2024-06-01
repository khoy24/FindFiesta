package java.frontend.findfiesta;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringEndsWith.endsWith;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.Matchers.hasToString;

import android.os.Looper;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.junit.Rule;
import org.junit.Test;

//@RunWith(AndroidJUnit4ClassRunner.class)
//@LargeTest
public class postTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule(LoginActivity.class);
    //    public ActivityTestRule<CreatePostActivity> activityRule = new ActivityTestRule<>(CreatePostActivity.class);


    // works as long as there are no duplicate posts with the same name
    //and there aren't too many posts that the one it makes is off the screen
    @Test
    public void createPost() {



        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onView(withId(R.id.createPostButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_createpost)).check(matches(isDisplayed()));

        String title = "TestX";
        String location = "Carver hall Iowa State University";
        String notes = "first tests";
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

        onView(withId(R.id.activity_post_add_picture)).check(matches(isDisplayed()));
        onView(withId(R.id.SkipBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onData(is("Lost: TestX")).inAdapterView(withId(R.id.postList)).check(matches(isDisplayed()));

    }

    @Test
    public void editPost() {

        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onData(is("Lost: TestX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        //change details of boxes
        onView(withId(R.id.changeDetailsBtn)).perform(click());
        onView(withId(R.id.changeTitle)).perform(typeText("TestY"), closeSoftKeyboard());
//        onView(withId(R.id.changeLocation)).perform(typeText("Coover hall Iowa State University"), closeSoftKeyboard());
        onView(withId(R.id.changeNotes)).perform(typeText("second tests"), closeSoftKeyboard());
        onView(withId(R.id.cancelChanDtlBtn)).perform(click());


        onView(withId(R.id.changeDetailsBtn)).perform(click());
        onView(withId(R.id.changeTitle)).perform(typeText("TestY"), closeSoftKeyboard());
//        onView(withId(R.id.changeLocation)).perform(typeText("Coover hall Iowa State University"), closeSoftKeyboard());
        onView(withId(R.id.changeNotes)).perform(typeText("second tests"), closeSoftKeyboard());
        onView(withId(R.id.confirmChanDtlBtn)).perform(click());


        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        //check details were set
        onView(withId(R.id.titleDisplay)).check(matches(withText(containsString("TestY"))));
//        onView(withId(R.id.locationDisplay)).check(matches(withText(startsWith("Coover hall Iowa State University"))));
        onView(withId(R.id.notesDisplay)).check(matches(withText(containsString("second tests"))));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onData(is("Lost: TestY")).inAdapterView(withId(R.id.postList)).check(matches(isDisplayed()));

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

    }

    @Test
    public void createComment() {
        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.commentBtn)).perform(click());
//      cancel the comment
        onView(withId(R.id.cancelCmtBtn)).perform(click());
        onView(withId(R.id.commentBtn)).perform(click());
        onView(withId(R.id.commentText)).perform(typeText("test TextX"), closeSoftKeyboard());
        onView(withId(R.id.confirmCmtBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: test TextX")).inAdapterView(withId(R.id.commentList)).check(matches(isDisplayed()));
    }

    @Test
    public void editComment() {

        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: test TextX")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.backBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onData(is("chrisMoseley: test TextX")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_view_comment)).check(matches(isDisplayed()));

        onView(withId(R.id.editComment)).perform(click());
        onView(withId(R.id.commentEdit)).perform(typeText("this is the new text"), closeSoftKeyboard());
        onView(withId(R.id.confirmBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_view_comment)).check(matches(isDisplayed()));

        onView(withId(R.id.backBtn)).perform(click());

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: this is the new text")).inAdapterView(withId(R.id.commentList)).check(matches(isDisplayed()));

    }

    @Test
    public void deletePost() {

        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onData(is("Lost: TestY")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.deletePostBtn)).perform(click());
        onView(withId(R.id.cancelChanDtlBtn)).perform(click());
        onView(withId(R.id.deletePostBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.confirmChanDtlBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        onView(withId(R.id.postList)).check(matches(not(withListContainsString("Lost: TestY"))));
//        onData(is("Lost: TestY")).inAdapterView(withId(R.id.postList)).check((ViewAssertion)equalTo(false));
//        onData(is("Lost: TestY")).inAdapterView(withId(R.id.postList)).check(matches(not(isDisplayed())));
//        onData(is("Lost: TestY")).inAdapterView(withId(R.id.postList)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteComment() {
        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: this is the new text")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_view_comment)).check(matches(isDisplayed()));

        onView(withId(R.id.deleteComment)).perform(click());
        onView(withId(R.id.cancelBtn)).perform(click());

        onView(withId(R.id.deleteComment)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.confirmBtn)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.commentList)).check(matches(not(withListContainsString("chrisMoseley: this is the new text"))));
    }

    @Test
    public void viewPostUser2() {
        //testing view with other users
        String testEmail2 = "khoyme@outlook.com";
        String testPassword2 = "password1";
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail2), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword2), closeSoftKeyboard());
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.changeDetailsBtn)).check(matches(not(isDisplayed())));
        onView(withId(R.id.deletePostBtn)).check(matches(not((isDisplayed()))));

        onData(is("chrisMoseley: test TextX")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.editComment)).check(matches(not(isDisplayed())));
        onView(withId(R.id.deleteComment)).check(matches(not((isDisplayed()))));

    }
    @Test
    public void notifications() {
        String testEmail2 = "chrisrmoseley@outlook.com";
        String testPassword2 = "password";
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail2), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword2), closeSoftKeyboard());
        onView(withId(R.id.loginButton2)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onView(withId(R.id.findUsersButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_userlist)).check(matches(isDisplayed()));

        onView(withId(R.id.searchUserText)).perform(typeText("chrisMoseley"), closeSoftKeyboard());
        onView(withId(R.id.findUserButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_otheruser)).check(matches(isDisplayed()));

        onView(withId(R.id.followUserButton)).perform(click());
        onView(withId(R.id.unfollowButton)).perform(click());
        onView(withId(R.id.followUserButton)).perform(click());
        onView(withId(R.id.unfollowButton)).perform(click());
    }

    @Test
    public void viewCommentNavbar() {
        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());


        //for main
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onData(is("Lost: test")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: comment")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));





        //for createpost
        onData(is("Lost: test")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: comment")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.createPostButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_createpost)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));



        //for account
        onData(is("Lost: test")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: comment")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.accountButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));



        //for maps
        onData(is("Lost: test")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: comment")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

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



        //for messages
        onData(is("Lost: test")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onData(is("chrisMoseley: comment")).inAdapterView(withId(R.id.commentList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.messagesButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }


    @Test
    public void viewPostNavBar() {
        String testEmail = "chrisrmoseley@outlook.com";
        String testPassword = "password";
        // Type in testString and send request
        onView(withId(R.id.editTextTextEmailAddress))
                .perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText(testPassword), closeSoftKeyboard());
//       click login button
        onView(withId(R.id.loginButton2)).perform(click());


        //for main
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));





        //for createpost
        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.createPostButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_createpost)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));



        //for account
        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.accountButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_account)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));



        //for maps
        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

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



        //for messages
        onData(is("Lost: testPostX")).inAdapterView(withId(R.id.postList)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_post_view)).check(matches(isDisplayed()));

        onView(withId(R.id.messagesButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_messages)).check(matches(isDisplayed()));

        onView(withId(R.id.homeButton)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {

        }

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }

    public static Matcher<View> withListContainsString(final String searchString) {
        return new BoundedMatcher<View, ListView>(ListView.class) {
            @Override
            public void describeTo(Description description) {
//                description.appendText("ListView with item containing string: " + searchString);
            }

            @Override
            protected boolean matchesSafely(ListView listView) {
                ListAdapter adapter = listView.getAdapter();
                if (adapter == null) {
                    return false;
                }

                for (int i = 0; i < adapter.getCount(); i++) {
                    Object item = adapter.getItem(i);
                    if (item != null && item.toString().contains(searchString)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
