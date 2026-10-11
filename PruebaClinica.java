import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PruebaClinica {

    // Listas en memoria para administrar los registros durante la ejecución
    private static ArrayList<Paciente> listaPacientes = new ArrayList<>();
    private static ArrayList<Doctor> listaDoctores = new ArrayList<>();
    private static GestorCitas gestorCitas = new GestorCitas();

    public static void main(String[] args) {
        // Precargar datos iniciales (como el ejemplo que tenías al principio)
        cargarDatosIniciales();

        int opcionPrincipal = 0;

        do {
            String menu = "=== SISTEMA DE GESTIÓN - CLÍNICA UDB ===\n"
                    + "1. Gestión de Pacientes\n"
                    + "2. Gestión de Doctores\n"
                    + "3. Gestión de Citas Médicas\n"
                    + "4. Salir\n\n"
                    + "Seleccione una opción:";

            String input = JOptionPane.showInputDialog(null, menu, "Menú Principal - Clínica UDB", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                break; // Si el usuario presiona Cancelar
            }

            try {
                opcionPrincipal = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            switch (opcionPrincipal) {
                case 1:
                    menuPacientes();
                    break;
                case 2:
                    menuDoctores();
                    break;
                case 3:
                    menuCitas();
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Saliendo del sistema. ¡Hasta luego!", "Cierre", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } while (opcionPrincipal != 4);
    }

    private static void cargarDatosIniciales() {
        try {
            Paciente p1 = new Paciente("P001", "Juan Perez", "7777-7777", "juan@gmail.com", 20, "Estudiante", "Activo");
            listaPacientes.add(p1);

            Doctor d1 = new Doctor("D001", "Carlos Lopez", "7777-8888", "carlos@gmail.com", "Medicina General", "Activo");
            listaDoctores.add(d1);

            CitaMedica c1 = new CitaMedica("C001", p1, d1, LocalDate.of(2026, 10, 10), LocalTime.of(9, 0), "Consulta general", "Programada");
            gestorCitas.registrarCita(c1);
        } catch (ValidacionException e) {
            System.out.println("Error al cargar datos iniciales: " + e.getMessage());
        }
    }

    // ==========================================
    // MENÚ DE PACIENTES
    // ==========================================
    public static void menuPacientes() {
        int op = 0;
        do {
            String menu = "--- GESTIÓN DE PACIENTES ---\n"
                    + "1. Registrar Paciente\n"
                    + "2. Consultar Pacientes\n"
                    + "3. Actualizar Paciente\n"
                    + "4. Volver al Menú Principal\n\n"
                    + "Seleccione una opción:";

            String input = JOptionPane.showInputDialog(null, menu, "Gestión de Pacientes", JOptionPane.QUESTION_MESSAGE);
            if (input == null) break;

            try {
                op = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }

            switch (op) {
                case 1:
                    try {
                        String codigo = JOptionPane.showInputDialog("Ingrese Código de Paciente (Ej. P002):");
                        if (codigo == null) break;
                        String nombre = JOptionPane.showInputDialog("Ingrese Nombre Completo:");
                        int edad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese Edad:"));
                        String tel = JOptionPane.showInputDialog("Ingrese Teléfono:");
                        String correo = JOptionPane.showInputDialog("Ingrese Correo Electrónico:");

                        String[] tipos = {"Estudiante", "Docente", "Administrativo", "Visitante"};
                        String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo de paciente:",
                                "Tipo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);

                        String estado = JOptionPane.showInputDialog("Ingrese Estado (Activo / Inactivo):", "Activo");

                        // Utiliza el constructor con validaciones que crearon tus compañeros
                        Paciente nuevoP = new Paciente(codigo, nombre, tel, correo, edad, tipo, estado);
                        listaPacientes.add(nuevoP);
                        JOptionPane.showMessageDialog(null, "¡Paciente registrado exitosamente!");
                    } catch (ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error de validación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Dato inválido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    if (listaPacientes.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay pacientes registrados.");
                    } else {
                        StringBuilder sb = new StringBuilder("--- LISTA DE PACIENTES ---\n");
                        for (Paciente p : listaPacientes) {
                            sb.append("Código: ").append(p.getCodigo()) // Heredado de Persona
                                    .append(" | Nombre: ").append(p.getNombreCompleto())
                                    .append(" | Tipo: ").append(p.getTipoPaciente()).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString());
                    }
                    break;

                case 3:
                    String busqP = JOptionPane.showInputDialog("Ingrese el Código del Paciente a actualizar:");
                    Paciente encontradoP = null;
                    for (Paciente p : listaPacientes) {
                        if (p.getCodigo().equalsIgnoreCase(busqP)) {
                            encontradoP = p;
                            break;
                        }
                    }
                    if (encontradoP != null) {
                        try {
                            String nuevoNombre = JOptionPane.showInputDialog("Nuevo Nombre Completo:", encontradoP.getNombreCompleto());
                            encontradoP.setNombreCompleto(nuevoNombre);
                            JOptionPane.showMessageDialog(null, "Paciente actualizado correctamente.");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Paciente no encontrado.");
                    }
                    break;
            }
        } while (op != 4);
    }

    // ==========================================
    // MENÚ DE DOCTORES
    // ==========================================
    public static void menuDoctores() {
        int op = 0;
        do {
            String menu = "--- GESTIÓN DE DOCTORES ---\n"
                    + "1. Registrar Doctor\n"
                    + "2. Consultar Doctores\n"
                    + "3. Actualizar Doctor\n"
                    + "4. Volver al Menú Principal\n\n"
                    + "Seleccione una opción:";

            String input = JOptionPane.showInputDialog(null, menu, "Gestión de Doctores", JOptionPane.QUESTION_MESSAGE);
            if (input == null) break;

            try {
                op = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }

            switch (op) {
                case 1:
                    try {
                        String codigo = JOptionPane.showInputDialog("Ingrese Código de Doctor (Ej. D002):");
                        if (codigo == null) break;
                        String nombre = JOptionPane.showInputDialog("Ingrese Nombre Completo:");

                        String[] especialidades = {"Medicina General", "Psicología", "Nutrición", "Fisioterapia"};
                        String esp = (String) JOptionPane.showInputDialog(null, "Seleccione Especialidad:",
                                "Especialidad", JOptionPane.QUESTION_MESSAGE, null, especialidades, especialidades[0]);

                        String tel = JOptionPane.showInputDialog("Ingrese Teléfono:");
                        String correo = JOptionPane.showInputDialog("Ingrese Correo Electrónico:");
                        String estado = JOptionPane.showInputDialog("Ingrese Estado (Activo / Inactivo):", "Activo");

                        Doctor nuevoD = new Doctor(codigo, nombre, tel, correo, esp, estado);
                        listaDoctores.add(nuevoD);
                        JOptionPane.showMessageDialog(null, "¡Doctor registrado exitosamente!");
                    } catch (ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error de validación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    if (listaDoctores.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay doctores registrados.");
                    } else {
                        StringBuilder sb = new StringBuilder("--- LISTA DE DOCTORES ---\n");
                        for (Doctor d : listaDoctores) {
                            sb.append("Código: ").append(d.getCodigo()) // Heredado de Persona
                                    .append(" | Nombre: ").append(d.getNombreCompleto())
                                    .append(" | Especialidad: ").append(d.getEspecialidad()).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString());
                    }
                    break;

                case 3:
                    String busqD = JOptionPane.showInputDialog("Ingrese Código del Doctor a actualizar:");
                    Doctor encontradoD = null;
                    for (Doctor d : listaDoctores) {
                        if (d.getCodigo().equalsIgnoreCase(busqD)) {
                            encontradoD = d;
                            break;
                        }
                    }
                    if (encontradoD != null) {
                        try {
                            String nuevoTel = JOptionPane.showInputDialog("Nuevo Teléfono:", encontradoD.getTelefono());
                            encontradoD.setTelefono(nuevoTel);
                            JOptionPane.showMessageDialog(null, "Doctor actualizado correctamente.");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Doctor no encontrado.");
                    }
                    break;
            }
        } while (op != 4);
    }

    // ==========================================
    // MENU DE CITAS MEDICAS
    // ==========================================
    public static void menuCitas() {
        int op = 0;
        do {
            String menu = "--- GESTION DE CITAS MEDICAS ---\n"
                    + "1. Registrar Cita Medica\n"
                    + "2. Consultar Citas\n"
                    + "3. Volver al Menu Principal\n\n"
                    + "Seleccione una opcion:";

            String input = JOptionPane.showInputDialog(null, menu, "Gestión de Citas", JOptionPane.QUESTION_MESSAGE);
            if (input == null) break;

            try {
                op = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }

            switch (op) {
                case 1:
                    try {
                        String codCita = JOptionPane.showInputDialog("Ingrese Codigo de Cita (Ej. C002):");
                        if (codCita == null) break;

                        String codPaciente = JOptionPane.showInputDialog("Ingrese Codigo de Paciente existente:");
                        Paciente pacienteEncontrado = null;
                        for (Paciente p : listaPacientes) {
                            if (p.getCodigo().equalsIgnoreCase(codPaciente)) {
                                pacienteEncontrado = p;
                                break;
                            }
                        }

                        if (pacienteEncontrado == null) {
                            JOptionPane.showMessageDialog(null, "Paciente no encontrado. Registre el paciente primero.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        String codDoctor = JOptionPane.showInputDialog("Ingrese Codigo de Doctor existente:");
                        Doctor doctorEncontrado = null;
                        for (Doctor d : listaDoctores) {
                            if (d.getCodigo().equalsIgnoreCase(codDoctor)) {
                                doctorEncontrado = d;
                                break;
                            }
                        }

                        if (doctorEncontrado == null) {
                            JOptionPane.showMessageDialog(null, "Doctor no encontrado. Registre el doctor primero.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        String fechaStr = JOptionPane.showInputDialog("Ingrese Fecha (AAAA-MM-DD):", "2026-10-10");
                        LocalDate fecha = LocalDate.parse(fechaStr);

                        String horaStr = JOptionPane.showInputDialog("Ingrese Hora (HH:MM):", "09:00");
                        LocalTime hora = LocalTime.parse(horaStr);

                        String motivo = JOptionPane.showInputDialog("Ingrese Motivo de Consulta:");

                        String[] estados = {"Programada", "Atendida", "Cancelada"};
                        String estado = (String) JOptionPane.showInputDialog(null, "Seleccione Estado de Cita:",
                                "Estado", JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);

                        // Crear la cita médica validando con las reglas de negocio
                        CitaMedica nuevaCita = new CitaMedica(codCita, pacienteEncontrado, doctorEncontrado, fecha, hora, motivo, estado);

                        // Registrar usando el gestor de citas (aquí valida si el doctor está activo o si hay traslape)
                        gestorCitas.registrarCita(nuevaCita);
                        JOptionPane.showMessageDialog(null, "¡Cita médica registrada exitosamente!");

                    } catch (ValidacionException e) {
                        // Captura las restricciones de negocio (Doctor inactivo o cruce de horarios)
                        JOptionPane.showMessageDialog(null, "Restriccion de Negocio: " + e.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(null, "Formato de fecha u hora incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ocurrio un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    if (gestorCitas.getCitas().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay citas registradas.");
                    } else {
                        StringBuilder sb = new StringBuilder("--- CITAS MÉDICAS REGISTRADAS ---\n");
                        for (CitaMedica c : gestorCitas.getCitas()) {
                            sb.append("Cita: ").append(c.getCodigoCita())
                                    .append(" | Paciente: ").append(c.getPaciente().getNombreCompleto())
                                    .append(" | Doctor: ").append(c.getDoctor().getNombreCompleto())
                                    .append(" | Fecha: ").append(c.getFecha()) // Metodo correcto getFecha()
                                    .append(" [").append(c.getEstadoCita()).append("]\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString());
                    }
                    break;
            }
        } while (op != 3);
    }
}