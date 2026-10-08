import java.time.LocalDate;
import java.time.LocalTime;

public class PruebaClinica {

    public static void main(String[] args) {

        try {

            // CREAR PACIENTE
            Paciente paciente = new Paciente(
                    "P001",
                    "Juan Perez",
                    "7777-7777",
                    "juan@gmail.com",
                    20,
                    "Estudiante",
                    "Activo"
            );

            // CREAR DOCTOR
            Doctor doctor = new Doctor(
                    "D001",
                    "Carlos Lopez",
                    "7777-8888",
                    "carlos@gmail.com",
                    "Medicina General",
                    "Activo"
            );

            // CREAR GESTOR DE CITAS
            GestorCitas gestor = new GestorCitas();

            // CREAR PRIMERA CITA
            CitaMedica cita1 = new CitaMedica(
                    "C001",
                    paciente,
                    doctor,
                    LocalDate.of(2026, 10, 10),
                    LocalTime.of(9, 0),
                    "Consulta general",
                    "Programada"
            );

            // REGISTRAR PRIMERA CITA
            gestor.registrarCita(cita1);

            System.out.println("Primera cita registrada correctamente.");

            //CONSULTAR CITAS
            for (CitaMedica cita : gestor.getCitas()) {
                System.out.println("Cita registrada:" + cita.getCodigoCita());
            }

            // CREAR SEGUNDA CITA
            CitaMedica cita2 = new CitaMedica(
                    "C002",
                    paciente,
                    doctor,
                    LocalDate.of(2026, 10, 10),
                    LocalTime.of(9, 0),
                    "Consulta de seguimiento",
                    "Programada"
            );

            // INTENTAR REGISTRAR SEGUNDA CITA
            gestor.registrarCita(cita2);

            System.out.println("Segunda cita registrada correctamente.");

        } catch (ValidacionException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}