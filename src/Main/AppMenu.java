package Main;

import Models.Persona;
import Models.Domicilio;
import Service.PersonaServiceImpl;
import Service.DomicilioServiceImpl;
import Dao.PersonaDAO;
import Dao.DomicilioDAO;

import java.util.Scanner;
import java.util.List;

public class AppMenu {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        DomicilioServiceImpl domicilioService = new DomicilioServiceImpl(new DomicilioDAO());
        PersonaServiceImpl personaService = new PersonaServiceImpl(new PersonaDAO(), domicilioService);
                
        int opcion = -1;
        do {
            try {
                menu();
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> {
                        try {
                            Persona persona = crearPersona();

                            System.out.print("¿Desea agregar un domicilio? (s/n): ");
                            String tieneDomicilio = sc.nextLine();
                            Domicilio domicilio = null;

                            if (tieneDomicilio.equalsIgnoreCase("s")) {
                                domicilio = crearDomicilio();
                                domicilioService.insertar(domicilio);
                            }

                            persona.setDomicilio(domicilio);

                            personaService.insertar(persona);
                        } catch (Exception e) {
                            System.err.println("Error al insertar persona: " + e.getMessage());
                        }
                    }
                    case 2 -> {
                        try {
                            System.out.print("¿Desea (1) listar todos o (2) buscar por nombre/apellido? Ingrese opción: ");
                            int subopcion = Integer.parseInt(sc.nextLine());

                            List<Persona> personas;

                            if (subopcion == 1) {
                                personas = personaService.getAll();
                            } else if (subopcion == 2) {
                                System.out.print("Ingrese texto a buscar: ");
                                String filtro = sc.nextLine();
                                personas = personaService.buscarPorNombreApellido(filtro);
                            } else {
                                System.out.println("Opción inválida.");
                                break;
                            }

                            for (Persona p : personas) {
                                System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Apellido: " + p.getApellido() + ", DNI: " + p.getDni());
                                if (p.getDomicilio() != null && Boolean.FALSE.equals(p.getDomicilio().isEliminado())) {
                                    System.out.println("   Domicilio: " + p.getDomicilio().getCalle() + " " + p.getDomicilio().getNumero());
                                }
                            }

                        } catch (Exception e) {
                            System.err.println("Error al listar personas: " + e.getMessage());
                        }
                    }

                        case 3 -> {
                        try {
                            System.out.print("ID de la persona a actualizar: ");
                            int id = Integer.parseInt(sc.nextLine());
                            Persona p = personaService.getById(id);
                            if (p != null && Boolean.FALSE.equals(p.isEliminado())) {
                                
                                actualizarPersona(p);

                                // Preguntar si quiere actualizar el domicilio
                                if (p.getDomicilio() != null) {
                                    
                                    if (p.getDomicilio().isEliminado() == true) {
                                        System.out.print("La persona no tiene domicilio. ¿Desea agregar uno? (s/n): ");
                                        String agregarDom = sc.nextLine();
                                        if (agregarDom.equalsIgnoreCase("s")) {
                                            domicilioService.recuperar(p.getDomicilio().getId());
                                            actualizarDomicilio(p.getDomicilio());
                                            domicilioService.actualizar(p.getDomicilio());
                                        } else {
                                            System.out.print("Saliendo...");
                                        }
                                    } else {
                                        System.out.print("¿Desea actualizar el domicilio? (s/n): ");
                                    String actualizarDom = sc.nextLine();
                                    if (actualizarDom.equalsIgnoreCase("s")) {
                                        
                                        actualizarDomicilio(p.getDomicilio());

                                        // actualizar el domicilio
                                        domicilioService.actualizar(p.getDomicilio());
                                        
                                    } else {
                                        System.out.println("si loco es aca");
                                        System.out.print("¿Desea eliminar el domicilio? (s/n): ");
                                        String eliminarDom = sc.nextLine();
                                        if (eliminarDom.equalsIgnoreCase("s")) {
                                            domicilioService.eliminar(p.getDomicilio().getId());
                                            p.setDomicilio(null);
                                        }
                                    }
                                }
                                    
                                    
                                } else {
                                    System.out.print("La persona no tiene domicilio. ¿Desea agregar uno? (s/n): ");
                                    String agregarDom = sc.nextLine();
                                    if (agregarDom.equalsIgnoreCase("s")) {

                                        Domicilio nuevoDom = crearDomicilio();
                                        domicilioService.insertar(nuevoDom);
                                        p.setDomicilio(nuevoDom);
                                    }
                                }

                                personaService.actualizar(p);
                                System.out.println("Persona actualizada.");
                            } else {
                                System.out.println("Persona no encontrada.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error al actualizar persona: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        try {
                            System.out.print("ID de la persona a eliminar: ");
                            int personaId = sc.nextInt();
                            Persona p = personaService.getById(personaId);
                            if (p != null && p.getDomicilio() != null) {
                                int domicilioId = p.getDomicilio().getId();
                                personaService.getDomicilioService().eliminar(domicilioId);
                            } else {
                                System.out.println("No se encontró domicilio para esta persona.");
                            }
                            personaService.eliminar(personaId);
                            System.out.println("Persona eliminada.");
                            sc.nextLine();
                            
                        } catch (Exception e) {
                            System.err.println("Error al eliminar persona: " + e.getMessage());
                        }
                    }                 
                    case 5 -> {
                        try {
                            List<Domicilio> domicilios = domicilioService.getAll();
                            for (Domicilio d : domicilios) {
                                System.out.println("ID: " + d.getId() + ", " + d.getCalle() + " " + d.getNumero());
                            }
                        } catch (Exception e) {
                            System.err.println("Error al obtener domicilios: " + e.getMessage());
                        }
                    }
                 
                    case 0 ->
                        System.out.println("Saliendo...");
                    default ->
                        System.out.println("Opción no válida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
            }
        } while (opcion != 0);
        sc.close();
    }

    public static void menu() {
        System.out.println("\n========= MENÚ =========");
        System.out.println("1. Crear persona");
        System.out.println("2. Listar personas");
        System.out.println("3. Actualizar persona");
        System.out.println("4. Eliminar persona");
        System.out.println("5. Listar domicilios");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");
    }

    public static Persona crearPersona() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("DNI: ");
        String dni = sc.nextLine();

        return new Persona(0, nombre, apellido, dni);
    }

    public static Domicilio crearDomicilio() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Calle: ");
        String calle = sc.nextLine();
        System.out.print("Número: ");
        String numero = sc.nextLine();
        return new Domicilio(0, calle, numero);

    }

    public static void actualizarPersona(Persona p) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nuevo nombre (" + p.getNombre() + "): ");
        String nombre = sc.nextLine();
        if (!nombre.isBlank()) {
            p.setNombre(nombre);
        }
        System.out.print("Nuevo apellido (" + p.getApellido() + "): ");
        String apellido = sc.nextLine();
        if (!apellido.isBlank()) {
            p.setApellido(apellido);
        }

        System.out.print("Nuevo DNI (" + p.getDni() + "): ");
        String dni = sc.nextLine();
        if (!dni.isBlank()) {
            p.setDni(dni);
        }
    }

    public static void actualizarDomicilio(Domicilio dom) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nueva calle (" + dom.getCalle() + "): ");
        String calle = sc.nextLine();
        if (!calle.isBlank()) {
            dom.setCalle(calle);
        }
        System.out.print("Nuevo número (" + dom.getNumero() + "): ");
        String numero = sc.nextLine();
        if (!numero.isBlank()) {
            dom.setNumero(numero);
        }

    }
    
}
