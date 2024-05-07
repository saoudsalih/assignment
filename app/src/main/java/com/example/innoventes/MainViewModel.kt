import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainViewModel : ViewModel() {

    private val _isNextButtonEnabled = MutableLiveData<Boolean>()
    val isNextButtonEnabled: LiveData<Boolean> = _isNextButtonEnabled

    init {
        _isNextButtonEnabled.value = false
    }

    fun validateInputs(birthdate: String, pan: String) {
        // Launch a coroutine in IO context
        viewModelScope.launch(Dispatchers.IO) {
            val isBirthdateValid = isValidDate(birthdate)
            val isPANValid = isValidPAN(pan)

            // Update LiveData in Main thread
            withContext(Dispatchers.Main) {
                _isNextButtonEnabled.value = isBirthdateValid && isPANValid
            }
        }
    }

    private fun isValidDate(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(dateStr)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun isValidPAN(pan: String): Boolean {
        val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher: Matcher = pattern.matcher(pan)
        return matcher.matches()
    }
}
