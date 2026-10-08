import java.time.LocalDate;
import java.time.LocalTime;

public class CitaMedica {

    //DATOS PROPIOS DE LA CITA
    private String codigoCita;
    private Paciente paciente;
    private Doctor doctor;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoConsulta;
    private String estadoCita;

    //CONSTRUCTOR
    public CitaMedica(String codigoCita, Paciente paciente, Doctor doctor, LocalDate fecha, LocalTime hora, String motivoConsulta, String estadoCita) throws ValidacionException {

        if (codigoCita == null || codigoCita.isBlank()) {
            throw new ValidacionException("El codigo de la cita no puede estar vacio.");
        }
        if (paciente == null) {
            throw new ValidacionException("La cita debe de tener un paciente.");
        }
        if (doctor == null) {
            throw new ValidacionException("La cita debe de tener un doctor.");
        }
        if (fecha == null) {
            throw new ValidacionException("La fecha de la cita es obligatoria.");
        }

        if (hora == null) {
            throw new ValidacionException("La hora de la cita es obligatoria.");
        }

        if (motivoConsulta == null || motivoConsulta.isBlank()) {
            throw new ValidacionException("El motivo de consulta no puede estar vacio.");
        }

        if (!estadoCita.equals("Programada") &&
                !estadoCita.equals("Atendida") &&
                !estadoCita.equals("Cancelada")) {

            throw new ValidacionException("El estado de la cita no es valido.");
        }

        this.codigoCita = codigoCita;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.motivoConsulta = motivoConsulta;
        this.estadoCita = estadoCita;
    }

    public String getCodigoCita() {
        return codigoCita;
    }

    public void setCodigoCita(String codigoCita) throws ValidacionException {

        if (codigoCita == null || codigoCita.isBlank()) {
            throw new ValidacionException("El codigo de la cita no puede estar vacio.");
        }

        this.codigoCita = codigoCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) throws ValidacionException {

        if (paciente == null) {
            throw new ValidacionException("La cita debe de tener un paciente.");
        }

        this.paciente = paciente;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) throws ValidacionException {

        if (doctor == null) {
            throw new ValidacionException("La cita debe de tener un doctor.");
        }

        this.doctor = doctor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) throws ValidacionException {

        if (fecha == null) {
            throw new ValidacionException("La fecha de la cita es obligatoria.");
        }

        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) throws ValidacionException {

        if (hora == null) {
            throw new ValidacionException("La hora de la cita es obligatoria.");
        }

        this.hora = hora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) throws ValidacionException {

        if (motivoConsulta == null || motivoConsulta.isBlank()) {
            throw new ValidacionException("El motivo de consulta no puede estar vacío.");
        }

        this.motivoConsulta = motivoConsulta;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) throws ValidacionException {

        if (!estadoCita.equals("Programada") &&
                !estadoCita.equals("Atendida") &&
                !estadoCita.equals("Cancelada")) {

            throw new ValidacionException("El estado de la cita no es válido.");
        }
        this.estadoCita = estadoCita;
    }
}
