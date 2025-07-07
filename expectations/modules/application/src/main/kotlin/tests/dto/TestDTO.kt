
import kotlinx.serialization.Serializable
import com.streview.application.usecases.*
import kotlinx.datetime.LocalDate

@Serializable
data class EncounterRequest(val userID:String, val encounters: Map<String, List<String>>): InputPort



@Serializable
data class EncounterResponse(val result: Boolean): OutputPort

