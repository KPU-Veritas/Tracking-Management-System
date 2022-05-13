package com.veritas.TMapp

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier
import com.veritas.TMapp.beacon.BeaconScannerApplication
import com.veritas.TMapp.database.AppDatabase
import com.veritas.TMapp.database.DBController
import com.veritas.TMapp.databinding.ActivityMainBinding
import com.veritas.TMapp.fragment.MainFragment
import com.veritas.TMapp.fragment.ContactInfoFragment
import com.veritas.TMapp.fragment.OptionFragment
import com.veritas.TMapp.fragment.PagerAdapter
import com.veritas.TMapp.server.FcmToken
import com.veritas.TMapp.server.ResponseMsg
import com.veritas.TMapp.server.ResponseSigninModel
import com.veritas.TMapp.server.ServerSetting.fcmToken
import com.veritas.TMapp.server.ServerSetting.processedUuid
import com.veritas.TMapp.server.ServerSetting.signApi
import com.veritas.TMapp.sign.SigninActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null
    //매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재선언
    private val binding get() = mBinding!!
    private lateinit var beaconScannerApplication: BeaconScannerApplication
    var db: AppDatabase?= null
    private var dbController: DBController? = null
    private var bluetoothAdapter : BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용하여 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter!=null){
            // Device doesn't support Bluetooth
            if(bluetoothAdapter?.isEnabled==false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        }else{
            Log.d("블루투스","기기가 블루투스를 지원하지 않습니다.")
        }
        db = AppDatabase.getInstance(this)
        dbController = DBController()
        beaconScannerApplication = application as BeaconScannerApplication
        saveFcmToken()
        // Set up a Live Data observer for beacon data
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(beaconScannerApplication.region)
        // observer will be called each time the monitored regionState changes (inside vs. outside region)
        regionViewModel.regionState.observe(this, monitoringObserver)
        // observer will be called each time a new list of beacons is ranged (typically ~1 second in the foreground)
        regionViewModel.rangedBeacons.observe(this, rangingObserver)
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        // 뷰페이저의 페이지뷰를 생성하기 위해 사용되는 어댑터 클래스
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(MainFragment(beaconScannerApplication,beaconManager, db!!, dbController!!), "메인")
        adapter.addFragment(ContactInfoFragment(), "접촉 정보")
        adapter.addFragment(OptionFragment(), "환경 설정")
        binding.afterLoginViewpager.adapter = adapter
        binding.afterLoginTablayout.setupWithViewPager(binding.afterLoginViewpager)
    }
    // 다른 Activiy가 활성화 되었을 때 호출
    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }
    // Activity가 사용자와 상호작용하기 직전에 호출
    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        checkPermissions()
    }
    // 비콘을 모니터링하고 모니터링 결과를 로그로 출력
    // 비콘 감지 다이얼로그 주석 처리
    private val monitoringObserver = Observer<Int> { state ->
        //var dialogTitle = "Beacons detected"
        //var dialogMessage = "didEnterRegionEvent has fired"
        val stateString = "inside"
        /*if (state == MonitorNotifier.OUTSIDE) {
            dialogTitle = "No beacons detected"
            dialogMessage = "didExitRegionEvent has fired"
            stateString = "outside"
        }*/
        Log.d(TAG, "monitoring state changed to : $stateString")

        //showDialogs(dialogTitle,dialogMessage,null)
    }

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "감지된 기기 수: ${beacons.count()}개")
        if (BeaconManager.getInstanceForApplication(this).rangedRegions.isNotEmpty()) {
            //binding.beaconCount.text = "Ranging enabled: ${beacons.count()} beacon(s) detected"
            /*binding.beaconList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                beacons
                    .sortedBy { it.distance }
                    .map { "${it.id1}\nid2: ${it.id2} id3: ${it.id3}  rssi: ${it.rssi}\nest. distance: ${it.distance} m" }.toTypedArray())*/
        }
    }
    // 권한 요청 결과를 가져옴
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in 1 until permissions.size) {
            Log.d(TAG, "권한 요청 결과: ${permissions[i]}: ${grantResults[i]}")
        }
    }


    private fun checkPermissions() {
        // basepermissions are for M and higher
        var permissions = arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        //var permissionRationale ="This app needs both fine location permission and background location permission to detect beacons in the background.  Please grant both now."
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            permissions = arrayOf( Manifest.permission.ACCESS_FINE_LOCATION)
            //permissionRationale ="This app needs fine location permission and nearby devices permission to detect beacons.  Please grant this now."
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Uncomment line below if targeting SDK 31
                permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            }
            //permissionRationale ="This app needs both fine location permission and nearby devices permission to detect beacons.  Please grant both now."
        }
        var allGranted = true
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) allGranted = false
        }
        if (!allGranted) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                /*showDialogs(
                    "This app needs permissions to detect beacons",
                    permissionRationale,
                    requestPermissions(
                        permissions,
                        PERMISSION_REQUEST_FINE_LOCATION
                    )
                )*/
                requestPermissions(
                    permissions,
                    PERMISSION_REQUEST_FINE_LOCATION
                )
            }
            /*else {
                showDialogs(
                    "Functionality limited",
                    "Since location and device permissions have not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location and device discovery permissions to this app.",
                    null
                )
            }*/
        }
        else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        /*showDialogs(
                            "This app needs background location access",
                            "Please grant location access so this app can detect beacons in the background.",
                            requestPermissions(
                                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                                PERMISSION_REQUEST_BACKGROUND_LOCATION
                            )
                        )*/
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                            PERMISSION_REQUEST_BACKGROUND_LOCATION
                        )
                    } /*else {
                        showDialogs(
                            "Functionality limited",
                            "Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.",
                            null
                        )
                    }*/
                }
            }
        }
    }
    /* 다이얼로그 출력 함수 주석 처리
    private fun showDialogs(title:String, msg:String, dismissListener: Unit?){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.setOnDismissListener{dismissListener}
        builder.show()
    }*/

    private fun saveFcmToken(){
        // fcm 토큰 값 받아오기 나중에 서버로 전송하는 방식으로 교체할 예정
        try{
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if(!task.isSuccessful){
                    Log.w("FCM 토큰", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                fcmToken = task.result.toString()
                val data = FcmToken(processedUuid, fcmToken)
                val dialog = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)

                signApi.addFcmToken(data).enqueue(object : Callback<ResponseMsg>{
                    override fun onResponse(call: Call<ResponseMsg>, response: Response<ResponseMsg>) {
                        if(response.code()==200){
                            Log.d("FCM 토큰", response.body()!!.data.toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                        Log.e(TAG, "서버와의 연결에 실패: ${t.message.toString()}")
                    }
                })
            })
        }catch (e:NullPointerException ) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val PERMISSION_REQUEST_BACKGROUND_LOCATION = 0
        const val PERMISSION_REQUEST_FINE_LOCATION = 1
    }
    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
    }
}