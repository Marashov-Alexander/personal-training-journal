package ru.ok.technopolis.training.personal
//
//import android.os.Bundle
//import androidx.appcompat.app.ActionBar
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.ui.AppBarConfiguration
//
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import kotlinx.android.synthetic.main.activity_main.*
//import ru.ok.technopolis.training.personal.activities.BaseActivity

//class MainActivity : BaseActivity() {
//    lateinit var toolbar: ActionBar
//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.profileFragment -> {
//
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val navHostFragment =
//                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        findViewById<BottomNavigationView>(R.id.nav_view)
//                .setupWithNavController(navController)

//        toolbar = supportActionBar!!
//        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
//    bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//        router?.showLoginPage()
//        finish()
//    }

//    override fun getActivityLayoutId() = R.layout.activity_main

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val navView: BottomNavigationView = nav_view
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
////                R.id.navigationFragment,
////                R.id.statisticsFragment,
////                R.id.navigation_home,
////                R.id.messagesFragment,
//                R.id.calendarFragment))
//        print("nuu")
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }
//}