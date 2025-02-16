# Riders-Attack
Proyecto #3 para la materia de Lenguajes de Programación, semestre 2024-2
1/2

**Procesos**: De manera preliminar diremos que los procesos que intervienen son los usuarios y los riders.

**Recursos Críticos**: Los riders son los recursos críticos, pues un usuario puede pedir el mismo rider al mismo tiempo y eso es un problema de concurrencia. 

**Condiciones de Sincronización**: Las acciones que deben ser sincronizadas para que no haya problemas de concurrencia deben ser:

Búsqueda de Riders
Liberación de Riders
Asignación de Riders