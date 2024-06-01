package java.frontend.findfiesta;

import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.frontend.findfiesta.databinding.ActivityAccountBinding;
import java.frontend.findfiesta.databinding.ActivityEditprofilepicBinding;

public class KaitlynSystemTest5 {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<EditProfilePicActivity> activityScenarioRule = new ActivityScenarioRule<>(EditProfilePicActivity.class);

    @Test
    public void accountBindingTest(){
        // Inflate the layout using ActivityScenario
        ActivityScenario<EditProfilePicActivity> activityScenario = activityScenarioRule.getScenario();
        activityScenario.onActivity(activity -> {
            View view = LayoutInflater.from(activity).inflate(R.layout.activity_editprofilepic, null);

            // Bind views using generated binding class
            ActivityEditprofilepicBinding binding = ActivityEditprofilepicBinding.bind(view);

        });
    }

}
