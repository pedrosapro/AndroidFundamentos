package pro.pedrosa.orderme.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.Toast
import pro.pedrosa.orderme.R
import pro.pedrosa.orderme.fragments.TablePagerFragment
import pro.pedrosa.orderme.model.Tables

class TablePagerActivity : AppCompatActivity(), TablePagerFragment.OnClickPay{


    companion object {
        val EXTRA_TABLE_INDEX = "EXTRA_TABLE_INDEX"
        private val REQUEST_CODE_DISHES = 1
        private val REQUEST_CODE_PAY = 12


        fun intent(context: Context, tableIndex: Int) : Intent {
            val intent = Intent(context, TablePagerActivity::class.java)
            intent.putExtra(EXTRA_TABLE_INDEX, tableIndex)
            return intent
        }

    }

    var tableIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_pager)

        // Configuramos la Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_launcher)
        setSupportActionBar(toolbar)



        //Parametro de la mesa a mostrar
        tableIndex = intent.getIntExtra(EXTRA_TABLE_INDEX,0)

        // Cargamos el fragmento
        if(fragmentManager.findFragmentById(R.id.fragment_table_pager) == null) {
            val fragment = TablePagerFragment.newInstance(tableIndex)
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_table_pager,fragment)
                    .commit()

        }

        // Click en el boton
        val addButton = findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            //Obtengo la referencia la fragmento y le paso la posicion
            val fragmentPager = fragmentManager.findFragmentById(R.id.fragment_table_pager) as? TablePagerFragment
            if (fragmentPager != null) {
                startActivityForResult(DishesActivity.intent(this,fragmentPager.getPosition()), REQUEST_CODE_DISHES)
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Obtengo la referencia la fragmento y le paso la posicion
        val fragmentPager = fragmentManager.findFragmentById(R.id.fragment_table_pager) as? TablePagerFragment
        fragmentPager?.updateTableFragment()

        // ResullCode, Toast OK - no OK
        if (requestCode == REQUEST_CODE_DISHES) { // Comprobamos que volvemos de SecondActivity
            if (resultCode == Activity.RESULT_OK) {
                // Hacemos lo que tenga sentido sabiendo que nos han devuelto RESULT_OK



            }else if (resultCode == DishesDetailActivity.NO_ADD_MORE) {
                Toast.makeText(this, R.string.order_added, Toast.LENGTH_SHORT).show()

            }
            else if (resultCode == DishesActivity.ERR_DOWNLOAD) {
                // Mostramos un snackbar con el error que me devuelve la actividad
                val msg : String = data?.getStringExtra(DishesActivity.EXTRA_ERR_MSG) ?: "Error"
                Snackbar.make(findViewById(android.R.id.content)
                        , msg , Snackbar.LENGTH_LONG)
                        .show()


            }
        } else if (requestCode == REQUEST_CODE_PAY) {
            if (resultCode == Activity.RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content)
                        , "Ready, waiting for new  order" , Snackbar.LENGTH_LONG)
                        .show()


            }

        }
    }

    override fun onClickPay() {

        val fragmentPager = fragmentManager.findFragmentById(R.id.fragment_table_pager) as? TablePagerFragment
        if (fragmentPager != null) {
            if(Tables[fragmentPager.getPosition()].order.isEmpty()){
                Snackbar.make(findViewById(android.R.id.content)
                        , "No Order. Add dishes" , Snackbar.LENGTH_LONG)
                        .show()

            } else {
                startActivityForResult(PayActivity.intent(this, fragmentPager.getPosition()), REQUEST_CODE_PAY)
            }
        }

    }


}
