package es.instituto.diario

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader
import java.text.SimpleDateFormat


@Root(name = "entradas")
class DiarioModel: ViewModel() {
    //métodos estáticos de la clase
    companion object{
        const val filename="AGENDA"
        var extension: Extension = Extension.cvs
        const val separator=";"
    }
    //atributos
    @field:ElementList(inline = true , name = "entrada")
    private var elementos= mutableListOf<Entrada>()
    var item_selected: Entrada? =null
    val elements get()=elementos
    //método para almacenar los datos en formato CSV
    public fun save(context: Context, extensionsave: Extension): Void?{
        val file: File = File(context.getFilesDir(), filename + "." + extensionsave)
        var fw= FileWriter(/* file = */ file)
        var bw= BufferedWriter(fw)
        when(extensionsave){
            Extension.cvs -> {
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                elementos.forEach{
                    bw.write(" ${formatter.format(it.fecha)}${DiarioModel.separator}${it.texto}")
                    bw.newLine()
                }
            }
            Extension.json -> {
                val jsonString = Json.encodeToString(this.elementos.toTypedArray())//toTypedArray())
                bw.write(jsonString)
                bw.flush()
            }
            Extension.xml -> {
                val serializer = Persister()
                serializer.write(this,bw)

            }
        }
        bw.flush()
        bw.close()
        fw.close()

       return null
    }

    //nétodo para cargar los datos a partir fichero CVS
    public fun load(context: Context, extesionload: Extension): Void?{
        val file: File = File(context.getFilesDir(), filename + "." + extesionload)
        //if(file.exists() && !file.isDirectory){
        var fr= InputStreamReader(file.inputStream())//FileReader(/* file = */ file)
        var br= BufferedReader(fr)
        var stringBuider = StringBuilder()
        when(extesionload){
            Extension.cvs -> {
                this.elementos=br.lineSequence().map {
                    val (fecha,texto)=it.split(DiarioModel.separator, ignoreCase = false,limit=2)
                    val formatter = SimpleDateFormat("dd-MM-yyyy")

                    Entrada(formatter.parse(fecha),texto)
                }.toMutableList()
            }
            Extension.json -> {

                if (file.exists() && !file.isDirectory) {
                    context.openFileInput(
                        filename + "." + extesionload
                    ).bufferedReader().useLines { lines ->
                        lines.forEach { stringBuider.append(it) }
                    }
                }
                var cadena = stringBuider.toString()
                val entrada = Json.decodeFromString<Array<Entrada>>(cadena)
                this.elementos.clear()
                this.elementos.addAll(entrada)

            }
            Extension.xml -> {
                val serializer = Persister()
                if (file.exists() && !file.isDirectory) {
                    file.inputStream().bufferedReader().use { br ->
                        val localXml = serializer.read(DiarioModel::class.java, br)
                        this.elementos.clear()
                        this.elementos.addAll(localXml.elementos)
                    }
                }
            }
        }
        br.close()
        fr.close()

        // }
        return null
    }

    public fun add(entrada:Entrada): Void?{
        elementos.add(entrada)
        return null
    }
}