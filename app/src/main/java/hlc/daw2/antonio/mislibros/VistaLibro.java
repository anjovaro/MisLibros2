/*
 * Copyright (c) 2016. Todos los derechos reservados por el desarrollador Antonio J. Vázquez Romero
 */

package hlc.daw2.antonio.mislibros;

import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

//import android.support.v4.app.FragmentManager;

public class VistaLibro extends Fragment {
    TextView titulo;
    TextView autor;
    TextView editorial;
    TextView isbn;
    TextView paginas;
    TextView anio;
    CheckBox ebook;
    CheckBox leido;
    RatingBar nota;
    TextView resumen;
    static final String TAG = "VistaLibro";
    int registro;   // registro a tratar
    DBAdapter db = new DBAdapter(getActivity().getBaseContext());
    boolean grabar, borrar, modificar;

    Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contenedor, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_vista_libro);
        titulo = (TextView) findViewById(R.id.titulo);
        autor = (TextView) findViewById(R.id.autor);
        editorial = (TextView) findViewById(R.id.editorial);
        isbn = (TextView) findViewById(R.id.isbn);
        paginas = (TextView) findViewById(R.id.paginas);
        anio = (TextView) findViewById(R.id.anio);
        ebook = (CheckBox) findViewById(R.id.ebook);
        leido = (CheckBox) findViewById(R.id.leido);
        nota = (RatingBar) findViewById(R.id.nota);
        resumen = (TextView) findViewById(R.id.resumen);
        // recupero los datos del bundle recibido por la actividad
        Bundle extra = getIntent().getExtras();
        registro = (int) extra.getLong("registro");
        // si el número de reistro es 0, hay que añadir un nuevo libro, en caso contrario se pasará
        // a editarlo para modificarlo o borrarlo si se desea.
        if (registro == 0) {  //tengo que añadir un nuevo libro
            Log.d(TAG, "Se crea nuevo libro");
            Toast.makeText(this, "Introduce los datos del nuevo libro,\ndespués pulsa el botón de grabar", Toast.LENGTH_LONG).show();
            grabar = true;
            borrar = false;
            modificar = false;

        } else {   // edito libro existente
            db.openW();
            grabar = false;
            borrar = true;
            modificar = true;
            Cursor c = db.getLibro(registro);
            if (c.moveToFirst()) {
                // hay datos los muestro para visualizar, editar o borrar
                // hay dos formas de hacerlo
                //titulo.setText(c.getString(c.getColumnIndex("titulo")));
                // o titulo.setText(c.getString(1) siendo el número el del campo a recuperar

                // creo el objeto libro y recupero los datos del cursor
                Libro libro = new Libro(c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                        c.getInt(5),c.getInt(6),c.getInt(7),c.getInt(8), c.getFloat(9), c.getString(10));

                // muestro el libro en los respectivos elementos de la interfaz.
                titulo.setText(libro.getTitulo());
                autor.setText(libro.getAutor());
                editorial.setText(libro.getEditorial());
                isbn.setText(libro.getIsbn());
                paginas.setText(String.format(Locale.getDefault(),"%d", libro.getPaginas()));
                anio.setText(String.format(Locale.getDefault(),"%d", libro.getAnio()));
                ebook.setChecked(libro.getEbook() != 0);
                leido.setChecked(libro.getLeido() != 0);
                nota.setRating(libro.getNota());
                resumen.setText(libro.getResumen());

            /*    titulo.setText(c.getString(1)); // or getColumnIndex("titulo");
                autor.setText(c.getString(2));
                editorial.setText(c.getString(3));
                isbn.setText(c.getString(4));
                paginas.setText(String.format(Locale.getDefault(),"%d",c.getInt(5))); // en vez de Integer.toString(c.getInt(5))
                anio.setText(String.format(Locale.getDefault(),"%d",c.getInt(6)));   // en lugar de Integer.toString(c.getInt(6)));
                ebook.setChecked(c.getInt(7) != 0);
                leido.setChecked(c.getInt(8) != 0);
                nota.setRating(c.getFloat(9));
                resumen.setText(c.getString(10));  */
            } else titulo.setText(R.string.noLibro);
            db.close();
        }
        return inflater.inflate(R.layout.activity_vista_libro, contenedor, false);
    }
public boolean onPrepareOptionsMenu(Menu menu){
    menu.findItem(R.id.action_grabar).setVisible(grabar);
    menu.findItem(R.id.action_borrar).setVisible(borrar);
    menu.findItem(R.id.action_actualizar).setVisible(modificar);
    return true;
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vista_libro, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // accedo a la base de datos en modo escritura para hacer la operación elegida
        db.openW();
        switch (item.getItemId()) {
            case R.id.action_grabar:
                Log.d(TAG, "Añadiendo registro");
                if (titulo.getText().toString().trim().length() > 0 &&
                        autor.getText().toString().trim().length() > 0 ) {
                    Libro libro= new Libro(titulo.getText().toString(),autor.getText().toString(),
                            editorial.getText().toString(), isbn.getText().toString(),
                            Integer.parseInt(paginas.getText().toString()), Integer.parseInt(anio.getText().toString()),
                            (ebook.isChecked() ? 1 : 0), (leido.isChecked() ? 1: 0),
                            nota.getRating(), resumen.getText().toString());
                    db.insertaLibro(libro);
                  /*  db.insertaLibro(titulo.getText().toString(), autor.getText().toString(),
                            editorial.getText().toString(), isbn.getText().toString(),
                            Integer.parseInt(paginas.getText().toString()),
                            Integer.parseInt(anio.getText().toString()),
                            (ebook.isChecked() ? 1 : 0),
                            (leido.isChecked() ? 1 : 0),
                            nota.getRating(), resumen.getText().toString()); */
                    Toast.makeText(this, "Insertado libro " + titulo.getText().toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "No se ha podido insertar libro sin título y/o autor", Toast.LENGTH_LONG).show();
                }
                finish();
                break;
            case R.id.action_actualizar:
                String mensaje = "¿Quieres actualizar el libro "+"<strong>"+
                        titulo.getText().toString()+"</strong>?";
                crearDialogoModal("Confirma operación", mensaje, "actualizar").show();
                Log.d(TAG, "Actualizando libro");
                break;
            case R.id.action_borrar:
                crearDialogoModal("Confirma operación", "¿Quieres borrar el libro "+"<strong>"+
                                titulo.getText().toString()+"</strong>?<br>Operación irreversible", "borrar").show();
                Log.d(TAG, "Borrando libro");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Con esta función creamos un diálogo modal para confirmar la operación a realizar
     * @param titulo título de la ventana
     * @param mensaje mensaje que aparecerá en la ventana
     * @param operacion indicará si la opración a realizar es modificación o borrado de registro
     * @return la ventana modal
     */
    private AlertDialog crearDialogoModal(String titulo, String mensaje, final String operacion) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(Html.fromHtml(mensaje)); // permite formatear el código con html
        // OnClickListener para Cancelar
        DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Operación cancelada", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        };
        // OnClickListener para Aceptar la operación
        DialogInterface.OnClickListener listenerOK = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realizarOperación(operacion);
            }
        };
        //asignamos los botones positivo y negativo a sus listeners
        alertDialogBuilder.setPositiveButton("Aceptar", listenerOK);
        alertDialogBuilder.setNegativeButton("Cancelar", listenerCancelar);
        return alertDialogBuilder.create();
    }

    /**
     * realiza la operación especificada como parámetro
     * @param operacion será actualizar o borrar un registro/libro
     */
    public void realizarOperación(String operacion) {
        if (operacion.equals("actualizar")) {
            db.actualizaLibro(registro, titulo.getText().toString(), autor.getText().toString(),
                    editorial.getText().toString(), isbn.getText().toString(),
                    Integer.parseInt(paginas.getText().toString()),
                    Integer.parseInt(anio.getText().toString()),
                    (ebook.isChecked() ? 1 : 0), (leido.isChecked() ? 1 : 0),
                    nota.getRating(), resumen.getText().toString());
            Toast.makeText(this, "Libro "+titulo.getText().toString()+" actualizado.", Toast.LENGTH_LONG).show();
        } else if (operacion.equals("borrar")) {
            db.borraLibro(registro);
            Toast.makeText(this, "Eliminado el libro: \n" + titulo.getText().toString(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void onStop() {
        super.onStop();
        db.close();
    }
}
