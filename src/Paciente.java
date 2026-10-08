public class Paciente extends Persona {

    //DATOS PROPIOS DEL PACIENTE
    private int edad;
    private String tipoPaciente;
    private String estadoPaciente;

    //CONSTRUCTOR
    public Paciente(String codigo, String nombreCompleto, String telefono, String correoElectronico, int edad, String tipoPaciente, String estadoPaciente) throws ValidacionException {
        super(codigo, nombreCompleto, telefono, correoElectronico);

        if (edad <= 0) {
            throw new ValidacionException("La edad debe ser mayor que 0.");
        }

        if (!tipoPaciente.equals("Estudiante") &&
                !tipoPaciente.equals("Docente") &&
                !tipoPaciente.equals("Administrativo") &&
                !tipoPaciente.equals("Visitante")) {

            throw new ValidacionException("El tipo de paciente no es válido.");
        }

        if (!estadoPaciente.equals("Activo") &&
                !estadoPaciente.equals("Inactivo")) {

            throw new ValidacionException("El estado del paciente no es válido.");
        }

        this.edad = edad;
        this.tipoPaciente = tipoPaciente;
        this.estadoPaciente = estadoPaciente;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) throws ValidacionException {

        if (edad <= 0) {
            throw new ValidacionException("La edad debe de ser mayor que 0.");
        }

        this.edad = edad;
    }

    public String getTipoPaciente() {
        return tipoPaciente;
    }

    public void setTipoPaciente(String tipoPaciente) throws ValidacionException {

        if (!tipoPaciente.equals("Estudiante") &&
                !tipoPaciente.equals("Docente") &&
                !tipoPaciente.equals("Administrativo") &&
                !tipoPaciente.equals("Visitante")) {

            throw new ValidacionException("El tipo de paciente no es válido");
        }

        this.tipoPaciente = tipoPaciente;
    }

    public String getEstadoPaciente() {
        return estadoPaciente;
    }

    public void setEstadoPaciente(String estadoPaciente) throws ValidacionException {

        if (!estadoPaciente.equals("Activo") &&
                !estadoPaciente.equals("Inactivo")) {

            throw new ValidacionException("El estado del paciente no es válido.");
        }

        this.estadoPaciente = estadoPaciente;
    }
}
