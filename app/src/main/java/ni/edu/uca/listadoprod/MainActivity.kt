package ni.edu.uca.listadoprod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import ni.edu.uca.listadoprod.dataadapter.ProductoAdapter
import ni.edu.uca.listadoprod.dataclass.Producto
import ni.edu.uca.listadoprod.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var listaProd = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciar()
    }

    /*Función para limpiar los editText*/
    private fun limpiar() {
        with(binding) {
            etNombre.setText("")
            etCantidad.setText("")
            etPrecio.setText("")
        }
    }

    /*Función para agregar un registro*/
    private fun agregarProd() {
        with(binding) {
            try {
                val nombre: String = etNombre.text.toString()
                val cantidad: Double = etCantidad.text.toString().toDouble()
                val precio: Double = etPrecio.text.toString().toDouble()
                val t = cantidad.toString().toDouble()*precio.toString().toDouble()
                val total = t.toString().toDouble()
                val prod = Producto(nombre, cantidad, precio, total)
                listaProd.add(prod)
            } catch (ex: Exception) {
                Toast.makeText(
                    this@MainActivity, "Error : ${ex.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
            rcvLista.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvLista.adapter = ProductoAdapter(listaProd,
                { producto -> onItemSelected(producto) },
                { position -> onDeleteItem(position) },
                { position -> onUpdateItem(position) })
            limpiar()
        }
    }

    /*Función para el uso de toEditable*/
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    /*Función para editar un registro*/
    fun onUpdateItem(position: Int) {
        with(binding) {
            val nombre: String = etNombre.text.toString()
            val cantidad: Double = etCantidad.text.toString().toDouble()
            val precio: Double = etPrecio.text.toString().toDouble()
            val t = cantidad.toString().toDouble()*precio.toString().toDouble()
            val total = t.toString().toDouble()
            val prod = Producto(nombre, cantidad, precio, total)
            listaProd.set(position, prod)
            rcvLista.adapter?.notifyItemRemoved(position)
        }

    }

    /*Función para seleccionar un registro y se añada a los editText*/
    fun onItemSelected(producto: Producto) {
        with(binding) {
            etNombre.text = producto.nombre.toString().toEditable()
            etCantidad.text = producto.precio.toString().toEditable()
            etPrecio.text = producto.cantidad.toString().toEditable()
        }
    }

    /*Función para eliminar un registro con su alerta*/
    fun onDeleteItem(position: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("¿Desea realmente eliminar el registro?")
            .setCancelable(false)
            .setPositiveButton("Si") { dialog, id ->
                with(binding) {
                    listaProd.removeAt(position)
                    rcvLista.adapter?.notifyItemRemoved(position)
                }
            }.setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    /*Función para definir las funciones que ocuparan los botones creados*/
    private fun iniciar() {
        binding.btnAgregar.setOnClickListener {
            agregarProd()
        }
        binding.btnLimpiar.setOnClickListener {
            with(binding) {
                etNombre.setText("")
                etCantidad.setText("")
                etPrecio.setText("")
            }
        }
    }
}

