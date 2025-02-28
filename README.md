# Riders Attack

## César Carios & Jhonatan Homsany

### Información general y consideraciones

Este proyecto ha sido desarrollado para la asignatura Lenguajes de Programación en el semestre 2-2024. Está desarrollado completamente en Java y entre los archivos se encuentra un makefile para poder compilar el proyecto fácilmente. El comando _make_ es principalmente utilizado en distribuciones de Linux, así que aunque el código fuente compila y se ejecuta sin problemas en Windows, se recomienda ejecutarlo en alguna distribución de Linux (como Ubuntu) para poder usar el comando _make_. También funciona en Windows Subsystem for Linux (WSL).

### Pasos para la ejecución del programa

Se asume que el usuario está utilizando una distribución de Linux para ejecutar el código fuente, entonces los pasos a seguir son los siguientes:

1. Abrir la consola de comandos en el directorio donde ha sido descargado el código fuente.
2. Para compilar el código fuente escribir el comando _make all_. El programa estará compilado y las clases estarán disponibles para su uso.
3. Como el programa funciona con un archivo de entrada, el siguiene paso es escribir en la consola _java RidersAttack In1.txt_ donde el archivo .txt puede ser reemplazado por cualquier otro caso de prueba disponible en el .zip de este proyecto u otro caso de prueba creado por el usuario.
4. Luego de haber terminado de usar el programa se puede usar el comando _make clean_ para remover todos los archivos .class generados para la ejecución del mismo.

### Procesos, recursos críticos y condiciones de sincronización

**Procesos**: Los procesos que intervienen son los Usuarios y los Riders.

**Recursos Críticos**: Los Riders son los recursos críticos, pues un usuario puede pedir el mismo rider al mismo tiempo, si eso ocurre habría un problema de concurrencia. Eso es lo que se quiere solucionar con el Despachador que funge como monitor.

**Condiciones de Sincronización**: Las acciones que deben ser sincronizadas para que no haya problemas de concurrencia deben ser:

-Búsqueda de Riders: Que cada usuario pueda encontrar un Rider cercano y que más de un usuario no pida el mismo Rider.

-Liberación de Riders: Luego de haber llevado a un Usuario a su destino, el Rider debe de quedar libre para que otro Usuario pueda utilizarlo.

-Asignación de Riders: Asegurar la asignación del Rider más cercano al usuario en cualquier momento del programa, esto incluye el hecho de que un Usuario puede cambiar de Rider si encuentra otro que está más cerca de él en comparación con el que ya se le había asignado ateriormente.

### Clases

_Riders.java_

Esta clase representa a los conductores (riders) que ofrecen servicios de transporte. Cada rider tiene atributos como la aplicación con la que trabaja (app), el tipo de servicio que ofrece (service), el tiempo de llegada (arrivalTime), y si está disponible o no (isAvailable).

**Atributos:**

id: Identificador único del rider.

app: La aplicación con la que trabaja el rider ("bipbip", "ridery", "yummy").

service: El tipo de servicio que ofrece ("motorcycle" o "car").

arrivalTime: Tiempo de llegada del rider (generado aleatoriamente entre 1 y 30 segundos).

isAvailable: Indica si el rider está disponible para tomar un servicio.

randomIntGenerator: Objeto Random para generar valores aleatorios.

**Métodos:**

Constructor Riders(String app, String[] service, int id): Inicializa un rider con la aplicación, el tipo de servicio (elegido aleatoriamente entre "motorcycle" y "car"), y un tiempo de llegada aleatorio. También establece el rider como disponible (isAvailable = true).

**Getters:**

getApp(): Devuelve la aplicación con la que trabaja el rider.

getService(): Devuelve el tipo de servicio que ofrece.

getArrivalTime(): Devuelve el tiempo de llegada.

getID(): Devuelve el identificador del rider.

isAvailable(): Devuelve si el rider está disponible.

**Setter:**

setAvailability(boolean availability): Cambia la disponibilidad del rider.

_DespachadorRiders.java_

Esta clase actúa como un monitor que gestiona la asignación de riders a los usuarios. Es responsable de encontrar el mejor rider disponible para un usuario en función del tiempo de llegada y la aplicación preferida.

**Atributos:**

listOfRiders: Un arreglo de riders disponibles.

totalClients: Número total de usuarios que solicitarán servicios.

arrivedClients: Contador de usuarios que han completado su viaje.

**Métodos:**

Constructor DespachadorRiders(Riders[] riders, int totalClients): Inicializa el despachador con una lista de riders y el número total de usuarios.

getBestRider(String userApp, String userService, int travelTime): Busca el mejor rider disponible para un usuario en función del tipo de servicio solicitado y el tiempo de llegada. Si hay varios riders con el mismo tiempo de llegada, se prioriza el que trabaja con la misma aplicación que el usuario. Este método es sincronizado para evitar condiciones de carrera cuando varios usuarios intentan asignarse un rider al mismo tiempo.

clientArrived(): Incrementa el contador de usuarios que han completado su viaje. Si todos los usuarios han llegado a su destino, finaliza el programa.

_User.java_

Esta clase representa a los usuarios que solicitan servicios de transporte. Cada usuario es un hilo que simula la solicitud de un servicio, la espera de un rider y el viaje.

**Atributos:**

id: Identificador único del usuario.

app: La aplicación desde la cual el usuario solicita el servicio.

service: El tipo de servicio que necesita (por ejemplo, "motorcycle" o "car").

travelTime: Tiempo de viaje (generado aleatoriamente entre 1 y 50 segundos).

randomIntGenerator: Objeto Random para generar valores aleatorios.

monitor: Referencia al DespachadorRiders para asignar riders.

assignedRider: El rider asignado al usuario.

**Métodos:**

Constructor User(int id, String[] app, String[] service, DespachadorRiders ridersMonitor): Inicializa un usuario con un identificador, una aplicación aleatoria, un tipo de servicio aleatorio y un tiempo de viaje aleatorio.

run(): Método principal del hilo. Simula la solicitud de un servicio y la espera de un rider. Llama al método selectRider() para asignar un rider al usuario.

selectRider(): Busca el mejor rider disponible usando el DespachadorRiders. Si el rider asignado cambia durante la espera (porque aparece un rider con un tiempo de llegada menor), se reasigna el rider.

travel(): Simula el viaje del usuario con el rider asignado. Una vez que el viaje termina, el rider se marca como disponible nuevamente y se notifica al despachador que el usuario ha llegado.

_RidersAttack_

Esta es la clase principal del programa, el main. Se encarga de leer el archivo de entrada, inicializar los riders y usuarios, y lanzar la simulación.

**Métodos:**

main(String[] args): Lee el archivo de entrada (pasado como argumento) para obtener el número de usuarios y la cantidad de riders por aplicación. Crea un arreglo de riders y los inicializa según las cantidades especificadas, crea un DespachadorRiders para gestionar la asignación de riders y crea un arreglo de usuarios y lanza sus hilos para simular la solicitud de servicios. Finalmente, interrumpe los hilos de los usuarios cuando la simulación termina.

_Ejecución_

**Inicio:** 

RidersAttack lee el archivo de entrada y crea los riders y usuarios. Los riders se inicializan con atributos aleatorios (aplicación, servicio, tiempo de llegada). Por otra parte, los usuarios se inicializan con atributos aleatorios (aplicación, servicio, tiempo de viaje).

**Simulación:**

Cada usuario (hilo) solicita un servicio llamando al DespachadorRiders para obtener el mejor rider disponible. El DespachadorRiders busca el rider más cercano y lo asigna al usuario. Durante la espera, si aparece un rider más cercano, el usuario puede cambiar de rider. Una vez que el viaje comienza, el usuario simula el viaje y notifica al despachador cuando llega a su destino.

**Finalización:**

Cuando todos los usuarios han completado sus viajes, el programa termina.

### Casos de prueba

Se incluyen seis casos de prueba con la finalidad de probar al máximo las características del programa. Se asume que las entradas siempre serán correctas: la cantidad de usuarios y riders será mayor que cero.