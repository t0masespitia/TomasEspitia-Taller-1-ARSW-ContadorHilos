package main

import (
	"fmt"
	"sync"
	"time"
)

const LIMITE int64 = 500_000_000

func crearRangos(cantidadHilos int, limite int64) [][2]int64 {
	rangos := make([][2]int64, cantidadHilos)
	rangoPorHilo := limite / int64(cantidadHilos)
	inicioRango := int64(1)

	for i := 0; i < cantidadHilos; i++ {
		finRango := inicioRango + rangoPorHilo - 1
		if i == cantidadHilos-1 {
			finRango = limite
		}

		rangos[i] = [2]int64{inicioRango, finRango}
		inicioRango = finRango + 1
	}

	return rangos
}

func contar(inicio int64, fin int64, resultados []int64, indice int, wg *sync.WaitGroup) {
	defer wg.Done()

	var sumaLocal int64
	for i := inicio; i <= fin; i++ {
		sumaLocal += i
	}

	resultados[indice] = sumaLocal
}

func main() {
	var cantidadHilos int

	fmt.Print("Ingrese la cantidad de hilos: ")
	if _, err := fmt.Scan(&cantidadHilos); err != nil {
		fmt.Println("Debe ingresar un numero entero.")
		return
	}

	if cantidadHilos <= 0 {
		fmt.Println("La cantidad de hilos debe ser mayor que 0.")
		return
	}

	if int64(cantidadHilos) > LIMITE {
		fmt.Println("La cantidad de hilos no puede superar", LIMITE)
		return
	}

	rangos := crearRangos(cantidadHilos, LIMITE)
	resultados := make([]int64, cantidadHilos)
	var wg sync.WaitGroup
	wg.Add(cantidadHilos)

	inicioTiempo := time.Now()

	for i, rango := range rangos {
		go contar(rango[0], rango[1], resultados, i, &wg)
	}

	wg.Wait()

	var sumaTotal int64
	for _, resultado := range resultados {
		sumaTotal += resultado
	}

	tiempoTotalMs := time.Since(inicioTiempo).Seconds() * 1000

	fmt.Println("Cantidad de hilos utilizada:", cantidadHilos)
	fmt.Println("Resultado acumulado:", sumaTotal)
	fmt.Printf("Tiempo total de ejecucion: %.2f ms\n", tiempoTotalMs)
}
