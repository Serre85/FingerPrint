package com.hslee.fingerprint

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.hslee.fingerprint.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var captureFlag = true

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                authenticateToEncrypt()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        biometricPrompt = setBiometricPrompt()
        promptInfo = setPromptInfo()

        binding.btnFinger.setOnClickListener {
            authenticateToEncrypt()
        }

        //캡처 on/off
        binding.btnCapture.setOnClickListener {
            if (captureFlag) {
                binding.btnCapture.text = "capture_off"
                screenCaptureDisable()
                captureFlag = false
            } else {
                binding.btnCapture.text = "capture_on"
                screenCaptureEnable()
                captureFlag = true
            }
        }
    }

    private fun authenticateToEncrypt() = with(binding) {
        var status: String
        val biometricManager = BiometricManager.from(this@MainActivity)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> status = "지문 인식을 사용할 수 있습니다."
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> status = "지문 인식을 지원 하지 않습니다."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> status = "지문 인식을 사용할 수 없습니다."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                status = "지문이 등록 되어 있지 않습니다."
                val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                dialogBuilder
                    .setTitle("알림")
                    .setMessage("지문 등록이 필요 합니다. 설정 화면으로 이동 하시겠습니까?")
                    .setPositiveButton("확인") { dialog, which -> goBiometricSettings() }
                    .setNegativeButton("취소") { dialog, which -> dialog.cancel() }
                dialogBuilder.show()
            }

            else -> status = "실패"

        }
        binding.tvStatus.text = status

        goAuthenticate()
    }

    /*
        * 생체 인식 인증 실행
    * */
    private fun goAuthenticate() {
        promptInfo.let {
            biometricPrompt.authenticate(it)
        }
    }

    /*
        * 지문 등록 화면으로 이동
    * */
    fun goBiometricSettings() {
        val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
        }
        activityResult.launch(intent)
    }

    private fun setPromptInfo(): BiometricPrompt.PromptInfo {
        val promptBuilder: BiometricPrompt.PromptInfo.Builder = BiometricPrompt.PromptInfo.Builder()
        promptBuilder.setTitle("지문 인식")
//        promptBuilder.setSubtitle("지문 인식을 해주세요.")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            promptBuilder.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        }
        promptInfo = promptBuilder.build()

        return promptInfo
    }

    private fun setBiometricPrompt(): BiometricPrompt {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    binding.tvStatus.text = "지문 인식 에러 : $errString"
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    binding.tvStatus.text = "지문 인식 성공"
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.tvStatus.text = "지문 인식 실패"
                }
            })

        return biometricPrompt
    }

    fun screenCaptureEnable() {
        //캡쳐 on
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun screenCaptureDisable() {
        //캡쳐 off
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}