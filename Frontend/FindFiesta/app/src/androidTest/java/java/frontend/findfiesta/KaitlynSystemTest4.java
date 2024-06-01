package java.frontend.findfiesta;

import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.frontend.findfiesta.databinding.ActivityDirectmessageBinding;

public class KaitlynSystemTest4 {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<DirectMessageActivity> activityScenarioRule = new ActivityScenarioRule<>(DirectMessageActivity.class);

    @Test
    public void accountBindingTest(){
        // Inflate the layout using ActivityScenario
        ActivityScenario<DirectMessageActivity> activityScenario = activityScenarioRule.getScenario();
        activityScenario.onActivity(activity -> {
            View view = LayoutInflater.from(activity).inflate(R.layout.activity_directmessage, null);

            // Bind views using generated binding class
            ActivityDirectmessageBinding binding = ActivityDirectmessageBinding.bind(view);

        });
    }

}

