import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

class Dispositivo {
    private String id;
    private String tipo;
    private String marca;
    private String modelo;
    private String estado;
    private String usuarioActual;

    public Dispositivo(String id, String tipo, String marca, String modelo) {
        this.id = id;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = "disponible";
        this.usuarioActual = "";
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getEstado() {
        return estado;
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setUsuarioActual(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Tipo: " + tipo + " | Marca: " + marca + 
               " | Modelo: " + modelo + " | Estado: " + estado +
               (usuarioActual.isEmpty() ? "" : " | Usuario: " + usuarioActual);
    }
}

class SistemaInventario {
    private Queue<Dispositivo> dispositivosDisponibles;
    private Queue<Dispositivo> dispositivosPrestados;
    private Queue<Dispositivo> dispositivosNoDisponibles;

    public SistemaInventario() {
        this.dispositivosDisponibles = new LinkedList<>();
        this.dispositivosPrestados = new LinkedList<>();
        this.dispositivosNoDisponibles = new LinkedList<>();
    }

    // Agregar nuevo dispositivo (se agrega a la cola de disponibles)
    public void agregarDispositivo(Dispositivo dispositivo) {
        dispositivosDisponibles.offer(dispositivo);
        System.out.println("Dispositivo agregado exitosamente a la cola de disponibles.");
    }

    // Prestar el primer dispositivo disponible
    public void prestarPrimerDisponible(String usuario) {
        if (dispositivosDisponibles.isEmpty()) {
            System.out.println("No hay dispositivos disponibles para prestar.");
            return;
        }
        Dispositivo dispositivo = dispositivosDisponibles.poll(); // con el poll saco el primer dispositivo de la cola  
        dispositivo.setEstado("prestado");
        dispositivo.setUsuarioActual(usuario);
        dispositivosPrestados.offer(dispositivo); // con el offer agrego el dispositivo a la cola de prestados  
        System.out.println("Se ha prestado el dispositivo: " + dispositivo);
    }

    // Devolver un dispositivo prestado
    public void devolverDispositivo(String id) {
        Queue<Dispositivo> colaTemp = new LinkedList<>();
        Dispositivo devuelto = null;
        
        // Buscar en la cola de prestados para validar si el dispositivo existe en la cola de prestados 
        while (!dispositivosPrestados.isEmpty()) {
            Dispositivo d = dispositivosPrestados.poll();
            if (d.getId().equals(id)) {
                devuelto = d;
            } else {
                colaTemp.offer(d);
            }
        }
        
        // Restaurar la cola de prestados - Tengo también que saber si el dispositivo existe en la cola de prestados   
        while (!colaTemp.isEmpty()) {
            dispositivosPrestados.offer(colaTemp.poll());
        }

        if (devuelto != null) {
            devuelto.setEstado("disponible");
            devuelto.setUsuarioActual("");
            dispositivosDisponibles.offer(devuelto);
            System.out.println("Dispositivo devuelto exitosamente.");
        } else {
            System.out.println("Dispositivo no encontrado en la lista de prestados.");
        }
    }

    // Mostrar todos los dispositivos
    public void mostrarInventario() {
        System.out.println("\n=== DISPOSITIVOS DISPONIBLES ===");
        mostrarCola(dispositivosDisponibles);
        
        System.out.println("\n=== DISPOSITIVOS PRESTADOS ===");
        mostrarCola(dispositivosPrestados);
        
        System.out.println("\n=== DISPOSITIVOS NO DISPONIBLES ===");
        mostrarCola(dispositivosNoDisponibles);
    }

    private void mostrarCola(Queue<Dispositivo> cola) {
        if (cola.isEmpty()) {
            System.out.println("(vacío)");
            return;
        }
        for (Dispositivo d : cola) {
            System.out.println(d);
        }
    }

    public void modificarDispositivo(String id, String nuevoTipo, String nuevaMarca, String nuevoModelo) {
        Dispositivo disp = null;
        
        // Buscar en dispositivos disponibles
        for (Dispositivo d : dispositivosDisponibles) {
            if (d.getId().equals(id)) {
                disp = d;
                break;
            }
        }
        
        if (disp != null) {
            // Modificar los datos del dispositivo
            disp.setTipo(nuevoTipo);
            disp.setMarca(nuevaMarca);
            disp.setModelo(nuevoModelo);
            System.out.println("Dispositivo modificado exitosamente.");
            System.out.println("Nuevos datos: " + disp);
        } else {
            System.out.println("Dispositivo no encontrado en el inventario.");
        }
    }
}

public class DepartamentoSistemas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaInventario sistema = new SistemaInventario();
        
        while (true) {
            System.out.println("\n=== SISTEMA DE INVENTARIO ===");
            System.out.println("1. Agregar nuevo dispositivo");
            System.out.println("2. Mostrar inventario");
            System.out.println("3. Prestar dispositivo");
            System.out.println("4. Devolver dispositivo");
            System.out.println("5. Modificar dispositivo");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // debemos limpiar el buffer
            
            switch (opcion) {
                case 1:
                    System.out.print("ID del dispositivo: ");
                    String id = scanner.nextLine();
                    System.out.print("Tipo de dispositivo: ");
                    String tipo = scanner.nextLine();
                    System.out.print("Marca: ");
                    String marca = scanner.nextLine();
                    System.out.print("Modelo: ");
                    String modelo = scanner.nextLine();
                    
                    sistema.agregarDispositivo(new Dispositivo(id, tipo, marca, modelo));
                    break;
                    
                case 2:
                    sistema.mostrarInventario();
                    break;
                    
                case 3:
                    System.out.print("Nombre del usuario: ");
                    String usuario = scanner.nextLine();
                    sistema.prestarPrimerDisponible(usuario);
                    break;
                    
                case 4:
                    System.out.print("ID del dispositivo a devolver: ");
                    String idDevolver = scanner.nextLine();
                    sistema.devolverDispositivo(idDevolver);
                    break;
                    
                case 5:
                    System.out.print("ID del dispositivo a modificar: ");
                    String idMod = scanner.nextLine();
                    System.out.print("Nuevo tipo de dispositivo: ");
                    String nuevoTipo = scanner.nextLine();
                    System.out.print("Nueva marca: ");
                    String nuevaMarca = scanner.nextLine();
                    System.out.print("Nuevo modelo: ");
                    String nuevoModelo = scanner.nextLine();
                    
                    sistema.modificarDispositivo(idMod, nuevoTipo, nuevaMarca, nuevoModelo);
                    break;
                    
                case 6:
                    System.out.println("Chao Chao");
                    scanner.close();
                    System.exit(0);
                    
                default:
                    System.out.println("Opción no válida, por favor digite una nueva opción");
            }
        }
    }
}
