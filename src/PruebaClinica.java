import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PruebaClinica {

    private static PacienteDAO pacienteDAO = new PacienteDAO();
    private static DoctorDAO doctorDAO = new DoctorDAO();
    private static CitaMedicaDAO citaMedicaDAO = new CitaMedicaDAO();

    public static void main(String[] args) {
        // Verificar conexión con MySQL al iniciar
        if (ConexionBD.probarConexion()) {
            cargarDatosInicialesSiVacio();
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar a la base de datos MySQL (localhost:3306/clinica_medica).\n"
                    + "Verifique que MySQL esté iniciado y el script clinica_medica.sql haya sido ejecutado.\n\n"
                    + "Las funciones que requieran la base de datos mostrarán aviso si no hay conexión.",
                    "Aviso de Conexión MySQL",
                    JOptionPane.WARNING_MESSAGE
            );
        }

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
                break; // Si el usuario presiona Cancelar o cierra la ventana
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

    /**
     * Si la base de datos está vacía, precarga los registros iniciales de prueba.
     */
    private static void cargarDatosInicialesSiVacio() {
        try {
            if (pacienteDAO.listarTodos().isEmpty()) {
                Paciente p1 = new Paciente("P001", "Juan Perez", "7777-7777", "juan@gmail.com", 20, "Estudiante", "Activo");
                pacienteDAO.insertar(p1);

                Doctor d1 = new Doctor("D001", "Carlos Lopez", "7777-8888", "carlos@gmail.com", "Medicina General", "Activo");
                doctorDAO.insertar(d1);

                CitaMedica c1 = new CitaMedica("C001", p1, d1, LocalDate.of(2026, 10, 10), LocalTime.of(9, 0), "Consulta general", "Programada");
                citaMedicaDAO.registrarCita(c1);
            }
        } catch (SQLException | ValidacionException e) {
            System.err.println("Nota sobre precarga inicial: " + e.getMessage());
        }
    }

    // ==========================================
    // MENÚ DE PACIENTES
    // ==========================================
    public static void menuPacientes() {
        int op = 0;
        do {
            String menu = "--- GESTIÓN DE PACIENTES (MySQL) ---\n"
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
                        if (codigo == null || codigo.isBlank()) break;

                        if (pacienteDAO.existe(codigo.trim())) {
                            JOptionPane.showMessageDialog(null, "Ya existe un paciente con el código " + codigo, "Código Duplicado", JOptionPane.WARNING_MESSAGE);
                            break;
                        }

                        String nombre = JOptionPane.showInputDialog("Ingrese Nombre Completo:");
                        if (nombre == null) break;

                        String edadStr = JOptionPane.showInputDialog("Ingrese Edad:");
                        if (edadStr == null) break;
                        int edad = Integer.parseInt(edadStr);

                        String tel = JOptionPane.showInputDialog("Ingrese Teléfono:");
                        if (tel == null) break;

                        String correo = JOptionPane.showInputDialog("Ingrese Correo Electrónico:");
                        if (correo == null) break;

                        String[] tipos = {"Estudiante", "Docente", "Administrativo", "Visitante"};
                        String tipo = (String) JOptionPane.showInputDialog(null, "Seleccione el tipo de paciente:",
                                "Tipo", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
                        if (tipo == null) break;

                        String estado = JOptionPane.showInputDialog("Ingrese Estado (Activo / Inactivo):", "Activo");
                        if (estado == null) break;

                        Paciente nuevoP = new Paciente(codigo.trim(), nombre.trim(), tel.trim(), correo.trim(), edad, tipo, estado.trim());
                        pacienteDAO.insertar(nuevoP);
                        JOptionPane.showMessageDialog(null, "¡Paciente registrado exitosamente en MySQL!");

                    } catch (ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error de validación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error en la Base de Datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "La edad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    try {
                        List<Paciente> listaPacientes = pacienteDAO.listarTodos();
                        if (listaPacientes.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No hay pacientes registrados en la base de datos.");
                        } else {
                            StringBuilder sb = new StringBuilder("--- LISTA DE PACIENTES ---\n");
                            for (Paciente p : listaPacientes) {
                                sb.append("Código: ").append(p.getCodigo())
                                        .append(" | Nombre: ").append(p.getNombreCompleto())
                                        .append(" | Edad: ").append(p.getEdad())
                                        .append(" | Tel: ").append(p.getTelefono())
                                        .append(" | Tipo: ").append(p.getTipoPaciente())
                                        .append(" [").append(p.getEstadoPaciente()).append("]\n");
                            }
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                    } catch (SQLException | ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error al consultar la base de datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3:
                    try {
                        String busqP = JOptionPane.showInputDialog("Ingrese el Código del Paciente a actualizar:");
                        if (busqP == null || busqP.isBlank()) break;

                        Paciente encontradoP = pacienteDAO.buscarPorCodigo(busqP.trim());
                        if (encontradoP != null) {
                            String nuevoNombre = JOptionPane.showInputDialog("Nuevo Nombre Completo:", encontradoP.getNombreCompleto());
                            if (nuevoNombre != null && !nuevoNombre.isBlank()) {
                                encontradoP.setNombreCompleto(nuevoNombre.trim());
                            }

                            String nuevoTel = JOptionPane.showInputDialog("Nuevo Teléfono:", encontradoP.getTelefono());
                            if (nuevoTel != null && !nuevoTel.isBlank()) {
                                encontradoP.setTelefono(nuevoTel.trim());
                            }

                            String nuevoCorreo = JOptionPane.showInputDialog("Nuevo Correo:", encontradoP.getCorreoElectronico());
                            if (nuevoCorreo != null && !nuevoCorreo.isBlank()) {
                                encontradoP.setCorreoElectronico(nuevoCorreo.trim());
                            }

                            String nuevoEstado = JOptionPane.showInputDialog("Nuevo Estado (Activo / Inactivo):", encontradoP.getEstadoPaciente());
                            if (nuevoEstado != null && !nuevoEstado.isBlank()) {
                                encontradoP.setEstadoPaciente(nuevoEstado.trim());
                            }

                            pacienteDAO.actualizar(encontradoP);
                            JOptionPane.showMessageDialog(null, "Paciente actualizado correctamente en MySQL.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Paciente no encontrado con código: " + busqP);
                        }
                    } catch (SQLException | ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar en la base de datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
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
            String menu = "--- GESTIÓN DE DOCTORES (MySQL) ---\n"
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
                        if (codigo == null || codigo.isBlank()) break;

                        if (doctorDAO.existe(codigo.trim())) {
                            JOptionPane.showMessageDialog(null, "Ya existe un doctor con el código " + codigo, "Código Duplicado", JOptionPane.WARNING_MESSAGE);
                            break;
                        }

                        String nombre = JOptionPane.showInputDialog("Ingrese Nombre Completo:");
                        if (nombre == null) break;

                        String[] especialidades = {"Medicina General", "Psicología", "Nutrición", "Fisioterapia"};
                        String esp = (String) JOptionPane.showInputDialog(null, "Seleccione Especialidad:",
                                "Especialidad", JOptionPane.QUESTION_MESSAGE, null, especialidades, especialidades[0]);
                        if (esp == null) break;

                        String tel = JOptionPane.showInputDialog("Ingrese Teléfono:");
                        if (tel == null) break;

                        String correo = JOptionPane.showInputDialog("Ingrese Correo Electrónico:");
                        if (correo == null) break;

                        String estado = JOptionPane.showInputDialog("Ingrese Estado (Activo / Inactivo):", "Activo");
                        if (estado == null) break;

                        Doctor nuevoD = new Doctor(codigo.trim(), nombre.trim(), tel.trim(), correo.trim(), esp, estado.trim());
                        doctorDAO.insertar(nuevoD);
                        JOptionPane.showMessageDialog(null, "¡Doctor registrado exitosamente en MySQL!");

                    } catch (ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error de validación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error en la Base de Datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    try {
                        List<Doctor> listaDoctores = doctorDAO.listarTodos();
                        if (listaDoctores.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No hay doctores registrados en la base de datos.");
                        } else {
                            StringBuilder sb = new StringBuilder("--- LISTA DE DOCTORES ---\n");
                            for (Doctor d : listaDoctores) {
                                sb.append("Código: ").append(d.getCodigo())
                                        .append(" | Nombre: ").append(d.getNombreCompleto())
                                        .append(" | Especialidad: ").append(d.getEspecialidad())
                                        .append(" | Tel: ").append(d.getTelefono())
                                        .append(" [").append(d.getEstadoDoctor()).append("]\n");
                            }
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                    } catch (SQLException | ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error al consultar la base de datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3:
                    try {
                        String busqD = JOptionPane.showInputDialog("Ingrese Código del Doctor a actualizar:");
                        if (busqD == null || busqD.isBlank()) break;

                        Doctor encontradoD = doctorDAO.buscarPorCodigo(busqD.trim());
                        if (encontradoD != null) {
                            String nuevoTel = JOptionPane.showInputDialog("Nuevo Teléfono:", encontradoD.getTelefono());
                            if (nuevoTel != null && !nuevoTel.isBlank()) {
                                encontradoD.setTelefono(nuevoTel.trim());
                            }

                            String nuevoCorreo = JOptionPane.showInputDialog("Nuevo Correo:", encontradoD.getCorreoElectronico());
                            if (nuevoCorreo != null && !nuevoCorreo.isBlank()) {
                                encontradoD.setCorreoElectronico(nuevoCorreo.trim());
                            }

                            String nuevoEstado = JOptionPane.showInputDialog("Nuevo Estado (Activo / Inactivo):", encontradoD.getEstadoDoctor());
                            if (nuevoEstado != null && !nuevoEstado.isBlank()) {
                                encontradoD.setEstadoDoctor(nuevoEstado.trim());
                            }

                            doctorDAO.actualizar(encontradoD);
                            JOptionPane.showMessageDialog(null, "Doctor actualizado correctamente en MySQL.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Doctor no encontrado con código: " + busqD);
                        }
                    } catch (SQLException | ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar en la base de datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (op != 4);
    }

    // ==========================================
    // MENÚ DE CITAS MÉDICAS
    // ==========================================
    public static void menuCitas() {
        int op = 0;
        do {
            String menu = "--- GESTIÓN DE CITAS MÉDICAS (MySQL) ---\n"
                    + "1. Registrar Cita Médica\n"
                    + "2. Consultar Citas\n"
                    + "3. Volver al Menú Principal\n\n"
                    + "Seleccione una opción:";

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
                        String codCita = JOptionPane.showInputDialog("Ingrese Código de Cita (Ej. C002):");
                        if (codCita == null || codCita.isBlank()) break;

                        if (citaMedicaDAO.existe(codCita.trim())) {
                            JOptionPane.showMessageDialog(null, "Ya existe una cita con el código " + codCita, "Código Duplicado", JOptionPane.WARNING_MESSAGE);
                            break;
                        }

                        String codPaciente = JOptionPane.showInputDialog("Ingrese Código de Paciente existente:");
                        if (codPaciente == null || codPaciente.isBlank()) break;

                        Paciente pacienteEncontrado = pacienteDAO.buscarPorCodigo(codPaciente.trim());
                        if (pacienteEncontrado == null) {
                            JOptionPane.showMessageDialog(null, "Paciente no encontrado. Registre el paciente primero en MySQL.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        String codDoctor = JOptionPane.showInputDialog("Ingrese Código de Doctor existente:");
                        if (codDoctor == null || codDoctor.isBlank()) break;

                        Doctor doctorEncontrado = doctorDAO.buscarPorCodigo(codDoctor.trim());
                        if (doctorEncontrado == null) {
                            JOptionPane.showMessageDialog(null, "Doctor no encontrado. Registre el doctor primero en MySQL.", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        String fechaStr = JOptionPane.showInputDialog("Ingrese Fecha (AAAA-MM-DD):", LocalDate.now().toString());
                        if (fechaStr == null) break;
                        LocalDate fecha = LocalDate.parse(fechaStr.trim());

                        String horaStr = JOptionPane.showInputDialog("Ingrese Hora (HH:MM):", "09:00");
                        if (horaStr == null) break;
                        LocalTime hora = LocalTime.parse(horaStr.trim());

                        String motivo = JOptionPane.showInputDialog("Ingrese Motivo de Consulta:");
                        if (motivo == null || motivo.isBlank()) break;

                        String[] estados = {"Programada", "Atendida", "Cancelada"};
                        String estado = (String) JOptionPane.showInputDialog(null, "Seleccione Estado de Cita:",
                                "Estado", JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
                        if (estado == null) break;

                        CitaMedica nuevaCita = new CitaMedica(codCita.trim(), pacienteEncontrado, doctorEncontrado, fecha, hora, motivo.trim(), estado);

                        // Registra en MySQL validando que el doctor esté activo y no exista traslape de horario
                        citaMedicaDAO.registrarCita(nuevaCita);
                        JOptionPane.showMessageDialog(null, "¡Cita médica registrada exitosamente en MySQL!");

                    } catch (ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Restricción de Negocio: " + e.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(null, "Formato de fecha u hora incorrecto. Use AAAA-MM-DD y HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error en la Base de Datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ocurrió un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    try {
                        List<CitaMedica> citas = citaMedicaDAO.listarTodas();
                        if (citas.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "No hay citas registradas en la base de datos.");
                        } else {
                            StringBuilder sb = new StringBuilder("--- CITAS MÉDICAS REGISTRADAS ---\n");
                            for (CitaMedica c : citas) {
                                sb.append("Cita: ").append(c.getCodigoCita())
                                        .append(" | Paciente: ").append(c.getPaciente().getNombreCompleto())
                                        .append(" | Doctor: ").append(c.getDoctor().getNombreCompleto())
                                        .append(" | Fecha: ").append(c.getFecha())
                                        .append(" | Hora: ").append(c.getHora())
                                        .append(" [").append(c.getEstadoCita()).append("]\n");
                            }
                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                    } catch (SQLException | ValidacionException e) {
                        JOptionPane.showMessageDialog(null, "Error al consultar las citas de la base de datos: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (op != 3);
    }
}