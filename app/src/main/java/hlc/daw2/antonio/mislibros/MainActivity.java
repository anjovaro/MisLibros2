package hlc.daw2.antonio.mislibros;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final String TAG = "MainActivity";
    //private Cursor c;
    ListView lvItems;
    private DBAdapter db = new DBAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(MainActivity.this,VistaLibro.class);
                intencion.putExtra("registro",0);
                // lanzo la actividad de alta de registros
                startActivity(intencion);
            }
        });
        inicializaDatos();
        actualizarVista();
        lvItems.setOnItemClickListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        actualizarVista();
    }
    /**
     * inicializamos los datos de la base de datos si está vacía con dos regisatroa de prueba
     */
    public void inicializaDatos(){
        db.openW();
        long id;
        try {
            if (db.getCount()==0 ) { //hay que añadir registros poque esta vacía
                Libro libro = new Libro("Android Desarrollo de Aplicaciones","Wei-Meng Lee","Anaya Multimedia",
                        "978-84-415-3197-0",591,2012,1,0,4.5f,"Muy buen libro para aprender a partir de la versión 4. Usa Android Studio aunque con referencias a Eclipse");
                id = db.insertaLibro(libro);
            //    db.insertaLibro("Android 4 Desarrollo de Aplicaciones","Wei-Meng Lee", "Anaya Multimedia", "978-84-415-3197-0",591,2012,1,0,4.5f,"Buen libro para aprender");
                Libro otro = new Libro("Android Guía de Desarrollo de Aplicaciones","Sébastien Pérochon","Ediciones ENI",
                        "978-2-7460-7343-2",408,2012,0,1,3.2f,"No es demasiado bueno, no sirve para el autoaprendizaje.");
             //   id = db.insertaLibro("Android Guia de desarrollo de Aplicaciones","Sébastien Pérochon", "Ediciones eni", "978-2-7460-7343-2", 408, 2012,0,1,3.2f,"No es demasiado bueno");
                id =db.insertaLibro(otro);
                Log.d(TAG,"Insertados dos libros en la BBDD con id="+Long.toString(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
    /**
     * método para responder a la pulsación de los elementos del ListView
     * @param adapterView es el elemento padre, o sea el ListView
     * @param view  vista sobre la que se ha pulsado
     * @param posicion posición de la vista en el adaptador
     * @param l identificador de la fila del elemento pulsado
     */
    public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
        Log.d(TAG, "dentro de onClick del ListView");
        // creo una intención para lanzar la vista de todos los campos del libro
        Intent intencion = new Intent(MainActivity.this, VistaLibro.class);
        // le paso la posición que en principio debe coincidir con el registro ya que no están ordenados
        //view. ("tvTitulo");
        Bundle b = new Bundle();
        b.putLong("registro",l);
        //intencion.putExtra("registro", posicion);
        intencion.putExtras(b);
        startActivity(intencion);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Método para actualizar la vista del ListView después de dar un alta en la DB
     * es algo temporal que se solucionará con un startActivityForResult
     */
    public void actualizarVista() {
        lvItems = (ListView) findViewById(R.id.lvLibros);
        // apertura en modo lectura
        db.openR();
        Cursor c = db.recuperaTodosLibros(); // recupero los libros
        if (c.moveToFirst()) {
            // establezco el adaptador del cursor usando el cursor del último paso
            AdaptadorLista adaptador = new AdaptadorLista(this, c, 0);
            // relaciono el adaptador del cursor al ListView
            lvItems.setAdapter(adaptador);
        } else {
            lvItems.removeAllViewsInLayout();
            Log.d(TAG, "Registros no recuperados en MainActivity");
            Toast.makeText(this,"Base de datos vacía.\nNada que mostrar",Toast.LENGTH_LONG).show();
        }
        db.close();
    }
    /**
     * Método para responder a las opciones del menú elegidas
     * @param item opción del menú
     * @return booleano de retorno
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            actualizarVista();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Copia la base de datos como un flujo de kb en kb
     * @param inputStream   ficehro de entrada
     * @param outputStream fichero de salida
     * @throws IOException exceoción en caso de error
     */
  /*  public void CopyDB(InputStream inputStream,
                       OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }*/
    /**
     * Muestra los libros en un Toast
     //* @param c cursor con los datos
     */
  /*  public void MuestraLibros(Cursor c)
    {
        Toast.makeText(this,
                "id: "        + c.getInt(0)     + "\n" +
                "Título: "    + c.getString(1)  + "\n" +
                "Autor:  "    + c.getString(2)  + "\n" +
                "Editorial: " + c.getString(3)  + "\n" +
                "I.S.B.N: "   + c.getString(4)  + "\n" +
                "Páginas: "   + c.getInt(5)     + "\n" +
                "Año: "       + c.getInt(6)     + "\n" +
                "eBook: "     + c.getInt(7)     + "\n" +
                "Leido: "     + c.getInt(8)     + "\n" +
                "Nota: "      + c.getFloat(9)   + "\n" +
                "Resumen: "   + c.getString(10)
                ,Toast.LENGTH_LONG).show();
    }*/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("¿Quieres salir de la aplicación?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        alert.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }
}
