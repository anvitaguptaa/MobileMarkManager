package ui.lectures.android.mvvm

//import androidx.navigation.findNavController
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import ui.lectures.android.mvvm.viewmodel.CoursesViewModel

//Move to another fragment
// should be blank
//Go to main actvity and drag nav hosyt fragmebt to component tree
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.navigation)
        navController.navigate(R.id.homeActivity)
    }
}

