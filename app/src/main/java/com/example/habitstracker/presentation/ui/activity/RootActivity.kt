package com.example.habitstracker.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.example.habitstracker.R
import com.example.habitstracker.databinding.TestBinding
import com.google.android.material.navigation.NavigationView

class RootActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: TestBinding
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.FragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            navController.navigate(R.id.habitsFragment)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val nav = supportFragmentManager.findFragmentById(R.id.FragmentContainerView) as NavHostFragment
        val navController = nav.navController
        when (item.itemId) {
            R.id.main -> navController.navigate(R.id.habitsFragment)
            R.id.infoFragment -> navController.navigate(R.id.infoFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}