package ru.ok.technopolis.training.personal.activities

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_base_fragment.*
import ru.ok.technopolis.training.personal.db.AppDatabase
import ru.ok.technopolis.training.personal.lifecycle.Router
import ru.ok.technopolis.training.personal.utils.logger.Logger

abstract class BaseActivity : AppCompatActivity() {

    var router: Router? = null
        private set

    var database: AppDatabase? = null
        private set

    protected val taskContainer: CompositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        configureNav()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        Logger.d(this, "onCreate")

        router = Router(this)
        database = AppDatabase.getInstance(applicationContext)

        setContentView(getActivityLayoutId())
    }

    override fun onDestroy() {
        super.onDestroy()
        taskContainer.dispose()
    }

    private fun configureNav() {
        configureNav(isBottomNavVisible())
    }

    fun configureNav(visibility: Boolean) {
        if (visibility) {
            nav_view?.visibility = View.VISIBLE
        } else {
            nav_view?.visibility = View.GONE
        }
    }

    protected abstract fun getActivityLayoutId(): Int

    protected open fun isBottomNavVisible(): Boolean = true
}