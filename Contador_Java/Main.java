import java.util.Scanner;

public class Main {
    private static final long LIMITE = 500_000_000L;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Ingrese la cantidad de hilos: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Debe ingresar un numero entero.");
                return;
            }

            int cantidadHilos = scanner.nextInt();

            if (cantidadHilos <= 0) {
                System.out.println("La cantidad de hilos debe ser mayor que 0.");
                return;
            }

            if (cantidadHilos > LIMITE) {
                System.out.println("La cantidad de hilos no puede superar " + LIMITE);
                return;
            }

            Contador[] hilos = Contador.crearHilos(cantidadHilos, LIMITE);
            Thread[] workers = new Thread[cantidadHilos];

            for (int i = 0; i < cantidadHilos; i++) {
                workers[i] = new Thread(hilos[i]);
            }

            long inicioTiempo = System.nanoTime();

            for (Thread worker : workers) {
                worker.start();
            }

            for (Thread worker : workers) {
                try {
                    worker.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("La ejecucion fue interrumpida.");
                    return;
                }
            }

            long sumaTotal = 0L;
            for (Contador hilo : hilos) {
                sumaTotal += hilo.getResultado();
            }

            long finTiempo = System.nanoTime();
            double tiempoTotalMs = (finTiempo - inicioTiempo) / 1_000_000.0;

            System.out.println("Cantidad de hilos utilizada: " + cantidadHilos);
            System.out.println("Resultado acumulado: " + sumaTotal);
            System.out.printf("Tiempo total de ejecucion: %.2f ms%n", tiempoTotalMs);
        }
    }
}
