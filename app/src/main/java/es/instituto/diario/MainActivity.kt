package es.instituto.diario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import es.instituto.diario.databinding.ActivityMainBinding



import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val viewModel: DiarioModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(es.instituto.diario.R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            es.instituto.diario.R.id.nuevo-> {
                this.viewModel.item_selected=null
                val fm: FragmentManager = supportFragmentManager
                fm.commit {
                    replace(R.id.fragmentContainerView, EntradaFragment.newInstance())
                    addToBackStack("replacement")
                }
                true
            }
            es.instituto.diario.R.id.todos -> {
                replaceListFragment()

                true
            }
            es.instituto.diario.R.id.guardar->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Seleccionar formato")
                builder.setCancelable(false)
                builder.setItems(R.array.formatos_array) { dialog, which ->
                    when (which) {
                        0 -> {
                            //se guardan los datos
                            this.viewModel.save(applicationContext,Extension.cvs)

                        }
                        1 -> {
                            this.viewModel.save(applicationContext,Extension.json)
                        }
                        2 -> {
                            this.viewModel.save(applicationContext,Extension.xml)
                        }
                    }

                    }

                val alertDialog = builder.create()
                alertDialog.show()

                true
            }
            es.instituto.diario.R.id.cargar->{

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Cargar diario de formato")
                builder.setItems(R.array.formatos_array) { dialog, which ->
                    when (which) {
                        0 -> {
                            this.viewModel.load(applicationContext,Extension.cvs)
                        }
                        1 -> {
                            this.viewModel.load(applicationContext,Extension.json)
                        }
                        2 -> {
                            this.viewModel.load(applicationContext,Extension.xml)
                        }
                    }

                }
                val alertDialog = builder.create()
                alertDialog.show()
                true
            }
            android.R.id.home->{
                supportFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun replaceListFragment() {
        val fm: FragmentManager = supportFragmentManager
        fm.commit {
            replace(R.id.fragmentContainerView, ListadoFragment.newInstance())
            addToBackStack("replacement")
        }
    }
}