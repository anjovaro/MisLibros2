package hlc.daw2.antonio.mislibros;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Antonio on 26/01/2016 at 12:16.
 */

public class DBAdapter {
    private static final String KEY_ROWID = "_id";              // campo identificador de fila
    private static final String KEY_TITULO = "titulo";          // título del libro
    private static final String KEY_AUTOR = "autor";            // autor del libro
    private static final String KEY_EDITORIAL = "editorial";    // editorial del libro
    private static final String KEY_ISBN = "isbn";              // isbn del libro
    private static final String KEY_PAGINAS = "paginas";        // número de páginas
    private static final String KEY_AÑO = "anio";                // año de edición
    private static final String KEY_EBOOK = "ebook";            // formato eBook
    private static final String KEY_LEIDO = "leido";            // lo he leido ya
    private static final String KEY_RATING = "nota";            // valor para la ratingbar
    private static final String KEY_RESUMEN = "resumen";        // resumen o comentario sobre el libro
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MisLibros";
    private static final String DATABASE_TABLE = "libros";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
                    "create table libros (_id integer primary key autoincrement, "
                    + "titulo text not null, autor text not null, editorial text, isbn text,"
                    + " paginas integer, anio integer, ebook integer,"
                    + "leido integer, nota real, resumen text);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Actualizando base de datos de la version " + oldVersion + " a la "
                    + newVersion + ", lo cual destruirá todos los datos existentes");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---abre la base de datos en modo escritura---
    public DBAdapter openW() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---abre la base de datos en modo lectura---
    public DBAdapter openR() throws SQLException{
        db = DBHelper.getReadableDatabase();
        return this;
    }

    //---cierra la base de datos---
    public void close()
    {
        DBHelper.close();
    }

    //---inserta un libro en la base de datos---
    public long insertaLibro(String titulo, String autor, String editorial, String isbn,
                              int paginas, int anio, int ebook, int leido, float nota, String resumen)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITULO, titulo);
        initialValues.put(KEY_AUTOR, autor);
        initialValues.put(KEY_EDITORIAL, editorial);
        initialValues.put(KEY_ISBN, isbn);
        initialValues.put(KEY_PAGINAS, paginas);
        initialValues.put(KEY_AÑO, anio);
        initialValues.put(KEY_EBOOK, ebook);
        initialValues.put(KEY_LEIDO, leido);
        initialValues.put(KEY_RATING, nota);
        initialValues.put(KEY_RESUMEN, resumen);
        Log.d("DBAdapter", "Insertando libro en DBAdapter");
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Inserta libro con el objeto, de momento es una sobrecarga para ver si funciona
     * @param libro es el objeto libro que quiero añadir a la base de datos
     * @return un long con la operación de inservión realizada
     */
    public long insertaLibro(Libro libro){
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put(KEY_TITULO,libro.getTitulo());
        valoresIniciales.put(KEY_AUTOR, libro.getAutor());
        valoresIniciales.put(KEY_EDITORIAL, libro.getEditorial());
        valoresIniciales.put(KEY_ISBN, libro.getIsbn());
        valoresIniciales.put(KEY_PAGINAS, libro.getPaginas());
        valoresIniciales.put(KEY_AÑO, libro.getAnio());
        valoresIniciales.put(KEY_EBOOK, libro.getEbook());
        valoresIniciales.put(KEY_LEIDO, libro.getLeido());
        valoresIniciales.put(KEY_RATING, libro.getNota());
        valoresIniciales.put(KEY_RESUMEN, libro.getResumen());

        return db.insert(DATABASE_TABLE, null, valoresIniciales);
    }
    //---borra un libro en particular---
    public boolean borraLibro(int rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Recupera el número de registros de la base de datos
      * @return el número de registros de la base de datos.
     */
    public int getCount(){
        Cursor c = recuperaTodosLibros();
        return c.getCount();
    }
    //---recupera todos los libros---
    public Cursor recuperaTodosLibros()
    {
        Log.d(TAG,"Recuperando registros en DBAdapter");
       // String[] campos = {"_id", "titulo","autor", "editorial", "isbn", "paginas",
       //     "anio", "ebook", "leido", "nota", "resumen"};
        //return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_AUTOR, KEY_EDITORIAL, KEY_ISBN, KEY_PAGINAS,
        //               KEY_AÑO, KEY_EBOOK, KEY_LEIDO, KEY_RATING, KEY_RESUMEN}, null, null, null, null, null);
        return db.rawQuery("SELECT * FROM libros", null );
    }

    //---recupera un libro en particular---
    public Cursor getLibro(int rowId) throws SQLException
    {
        //Cursor mCursor =
        //        db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_AUTOR, KEY_EDITORIAL, KEY_ISBN, KEY_PAGINAS,
        //                        KEY_AÑO, KEY_EBOOK, KEY_LEIDO, KEY_RATING, KEY_RESUMEN}, KEY_ROWID + "=" + rowId, null,
        //                null, null, null, null);
        Cursor mCursor = db.rawQuery("SELECT * FROM libros WHERE "+KEY_ROWID+"="+rowId,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---actualiza un libro---
    public boolean actualizaLibro(long rowId, String titulo, String autor, String editorial, String isbn,
                                  int paginas, int anio, int ebook, int leido, float nota, String resumen)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITULO, titulo);
        args.put(KEY_AUTOR, autor);
        args.put(KEY_EDITORIAL, editorial);
        args.put(KEY_ISBN, isbn);
        args.put(KEY_PAGINAS, paginas);
        args.put(KEY_AÑO, anio);
        args.put(KEY_EBOOK, ebook);
        args.put(KEY_LEIDO, leido);
        args.put(KEY_RATING, nota);
        args.put(KEY_RESUMEN, resumen);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
