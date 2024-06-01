package java.frontend.findfiesta;

import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.frontend.findfiesta.databinding.ActivityChangethemeBinding;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class KaitlynSystemTest1 {


    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<ChangeThemeActivity> activityScenarioRule = new ActivityScenarioRule<>(ChangeThemeActivity.class);


//    binding test 1 (accounts)

    @Test
    public void accountBindingTest(){
        // Inflate the layout using ActivityScenario
        ActivityScenario<ChangeThemeActivity> activityScenario = activityScenarioRule.getScenario();
        activityScenario.onActivity(activity -> {
            View view = LayoutInflater.from(activity).inflate(R.layout.activity_changetheme, null);

            // Bind views using generated binding class
            ActivityChangethemeBinding binding = ActivityChangethemeBinding.bind(view);

        });
    }
}
