import matplotlib.pyplot as plt

# 1. PEGA AQUÍ LAS LISTAS QUE TE ARROJÓ SCALA EN LA CONSOLA
loss_clasificacion = [1.0371916103595906, 0.8892437094603101, 0.866203591954148, 0.8597180458716372, 0.856679856159765, 0.8557007568144392, 0.8555525923151555, 0.8555270169475461, 0.8555260231039171, 0.8555259731842708, 0.8555259662609936] # Reemplazar con tus valores
loss_regresion = [0.49999748927922333, 0.436759981611607, 0.31605871451812534, 0.3137551766030895, 0.3122770070808429, 0.3121787321778962, 0.3121480124157871, 0.3121480084787035, 0.3121480084201449, 0.312148008419911] # Reemplazar con tus valores

# Crear la figura con dos subgráficos
plt.figure(figsize=(12, 5))

# Gráfico 1: Clasificación (Regresión Logística)
plt.subplot(1, 2, 1)
plt.plot(loss_clasificacion, marker='o', linestyle='-', color='b', linewidth=2)
plt.title('Pérdidas de Entrenamiento - Clasificación\n(Regresión Logística Multiclase)', fontsize=12)
plt.xlabel('Iteraciones', fontsize=10)
plt.ylabel('Pérdida (Objective)', fontsize=10)
plt.grid(True, linestyle='--', alpha=0.7)

# Gráfico 2: Regresión (Regresión Lineal)
plt.subplot(1, 2, 2)
plt.plot(loss_regresion, marker='s', linestyle='-', color='r', linewidth=2)
plt.title('Pérdidas de Entrenamiento - Regresión\n(Regresión Lineal)', fontsize=12)
plt.xlabel('Iteraciones', fontsize=10)
plt.ylabel('Pérdida (Objective)', fontsize=10)
plt.grid(True, linestyle='--', alpha=0.7)

# Ajustar layout y guardar la imagen de alta calidad para el informe
plt.tight_layout()
plt.savefig('graficos_perdidas_modelos.png', dpi=300)
print("Los gráficos se han generado correctamente en 'graficos_perdidas_modelos.png'")