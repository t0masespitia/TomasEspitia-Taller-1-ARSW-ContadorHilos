public class Contador implements Runnable {
    private final long inicio;
    private final long fin;
    private volatile long resultado;

    public Contador(long inicio, long fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public static Contador[] crearHilos(int cantidadHilos, long limite) {
        Contador[] hilos = new Contador[cantidadHilos];
        long rangoPorHilo = limite / cantidadHilos;
        long inicioRango = 1L;


        for (int i = 0; i < cantidadHilos; i++) {
            long finRango = (i == cantidadHilos - 1) ? limite : (inicioRango + rangoPorHilo - 1);
            hilos[i] = new Contador(inicioRango, finRango);
            inicioRango = finRango + 1;
        }

        return hilos;
    }

    @Override
    public void run() {
        long sumaLocal = 0L;

        for (long i = inicio; i <= fin; i++) {
            sumaLocal += i;
        }

        resultado = sumaLocal;
    }

    public long getResultado() {
        return resultado;
    }
}
