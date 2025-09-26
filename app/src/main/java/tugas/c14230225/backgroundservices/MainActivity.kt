package tugas.c14230225.backgroundservices

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.jar.Manifest
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    private lateinit var _textStatus : TextView
    private lateinit var _btnStart : Button

    companion object{
        const val ACTION_SERVICE_STOPPED_JUMP = "nit2x.paba.backgroundservice.ACTION_SERVICE_STOPPED_JUMP"
        const val REQUEST_CODE_PERMISSIONS = 101
    }
    private val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayOf(
            android.Manifest.permission.ACTIVITY_RECOGNITION,
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            android.Manifest.permission.ACTIVITY_RECOGNITION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            var allPermissionsGranted = true
            for (result in grantResults){
                if (result != android.content.pm.PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted = false
                    break
                }
            }

            if (allPermissionsGranted){
                val serviceIntent = Intent(this, TrackerService::class.java)
                startService(serviceIntent)
                finish()
            } else {
                _textStatus.text = "Izin diperlukan untuk menjalankan layanan"
            }
        }
    }

    private fun handleIntent(intent: Intent){
        if (intent.action == ACTION_SERVICE_STOPPED_JUMP){
            _textStatus.text = "Background services sudah berhenti karena Anda melompat."
        } else {

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            handleIntent(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        _textStatus = findViewById<TextView>(R.id.txtStatus)
//        _btnStart = findViewById<Button>(R.id.btnStart)

        _btnStart.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest,
                REQUEST_CODE_PERMISSIONS
            )
        }

        handleIntent(intent)
    }
}