package hlc.daw2.antonio.mislibros;
@SuppressWarnings("WeakerAccess")
/**
 * Created by Antonio on 28/01/2016.
 * creo el objeto libro
 * @param titulo es el título del libro
 * @param autor es el autor
 * @param editorial es la editorial
 * @param isbn es el I.S.B.N. del libro
 * .... son autoexplicativas
 */
 public class Libro {
    //private int    _id;          // campo identificador de fila
    private String titulo;       // título del libro
    private String autor;        // autor del libro
    private String editorial;    // editorial del libro
    private String isbn;         // isbn del libro
    private int    paginas;      // número de páginas
    private int    anio;         // año de edición
    private int    ebook = 0;    // formato eBook
    private int    leido = 0;    // lo he leido ya
    private float  nota = 0;     // valor para la ratingbar
    private String resumen;      // resumen o comentario sobre el libro

     public Libro(String titulo, String autor, String editorial, String isbn,
                 int paginas, int anio, int ebook, int leido, float nota, String resumen){
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.isbn = isbn;
        this.paginas = paginas;
        this.anio = anio;
        this.ebook = ebook;
        this.leido = leido;
        this.nota = nota;
        this.resumen = resumen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getEbook() {
        return ebook;
    }

    public void setEbook(int ebook) {
        this.ebook = ebook;
    }

    public int getLeido() {
        return leido;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
}
