package hlc.daw2.antonio.mislibros;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Antonio on 28/01/2016 at 12:52.
 */
  class AdaptadorLista extends CursorAdapter {
    //private final Activity actividad;
    //private final ArrayList<Libro> libros;
     public  AdaptadorLista(Context contexto, Cursor cursor, int flags) {
        super(contexto, cursor, 0);
    }

    /**
     * el método newView se usa para inflar una nueva vista y devolverla,
     * no se enlaza ningún dato a la vista en este punto.
     * @param context el contexto del adaptador
     * @param cursor es el cursor con los datos
     * @param parent es el elemento padre
     * @return devuelve la vista que se integrará en el ListView
     */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.listview_element, parent, false);
        }

    /**
     * El método bindView se usa para enlazar todos los datos a una vista dada
     * tal como establecer el texto de un TextView.
     * @param view es la vista a enlazar de la cual sacamos los elementos a enlazar
     * @param context se usa para expresar el contexto
     * @param cursor del que obtenemos los datos
     */

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // busco los campos para llenar en la plantilla de inflado
            TextView titulo =(TextView) view.findViewById(R.id.tvTitulo);
            TextView autor=(TextView)view.findViewById(R.id.tvAutor);
            ImageView imagen = (ImageView) view.findViewById(R.id.ivLibro);
            RatingBar nota = (RatingBar) view.findViewById(R.id.rbNota);
            TextView id = (TextView) view.findViewById(R.id.tvID);

            // desactivo los focusable para que el listview funcione correctamente
            titulo.setFocusable(false);
            titulo.setFocusableInTouchMode(true);
            autor.setFocusable(false);
            autor.setFocusableInTouchMode(true);
            nota.setFocusable(false);
            nota.setFocusableInTouchMode(true);

            // extraigo las propiedades del cursor
            String tit = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
            String aut = cursor.getString(cursor.getColumnIndexOrThrow("autor"));
            Float not = cursor.getFloat(cursor.getColumnIndexOrThrow("nota"));
            int rowID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

            // relleno los campos con las propiedades extraídas
            titulo.setText(tit); // el título
            autor.setText(aut);  // el autor
            nota.setRating(not); // la nota para el RatingBar
            id.setText(Integer.toString(rowID));   // ID pero oculto para que no se vea

            // relleno la imagen aleatoriamente
            switch (Math.round((float)Math.random()*3)){
                case 0:
                    imagen.setImageResource(R.drawable.libro1);
                    break;
                case 1:
                    imagen.setImageResource(R.drawable.libro2);
                    break;
                default:
                    imagen.setImageResource(R.drawable.libro3);
                    break;
            }
        }
    }
