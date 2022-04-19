import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.Serializable

@Serializable
class DataStorage(val dataList: List<Int>, val nextPlayer: Int, val isGame: Boolean)
const val GAMES = "Games"
suspend fun writeData(id: String, dataStorage: DataStorage) {
    Firebase.firestore.collection(GAMES).document(id).set(DataStorage.serializer(), dataStorage)
}
suspend fun readData(id: String): DataStorage {
    return Firebase.firestore.collection(GAMES).document(id).get().data()
}
fun getDataFlow(id: String): Flow<DataStorage> {
    return Firebase.firestore.collection(GAMES).document(id).snapshots.mapNotNull { it.takeIf { it.exists }?.data() }
}
