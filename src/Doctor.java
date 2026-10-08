public class Doctor extends Persona {

    //DATOS PROPIOS DEL DOCTOR
    private String especialidad;
    private String estadoDoctor;

    //CONSTRUCTOR
    public Doctor(String codigo, String nombreCompleto, String telefono, String correoElectronico, String especialidad, String estadoDoctor) throws ValidacionException {
        super(codigo, nombreCompleto, telefono, correoElectronico);

        if (!especialidad.equals("Medicina General") &&
                !especialidad.equals("Psicología") &&
                !especialidad.equals("Nutrición") &&
                !especialidad.equals("Fisioterapia")) {

            throw new ValidacionException("La especialidad del doctor no es válida.");
        }
        if (!estadoDoctor.equals("Activo") &&
                !estadoDoctor.equals("Inactivo")) {

            throw new ValidacionException("El estado del doctor no es válido.");
        }

        this.especialidad = especialidad;
        this.estadoDoctor = estadoDoctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) throws ValidacionException {

        if (!especialidad.equals("Medicina General") &&
                !especialidad.equals("Psicología") &&
                !especialidad.equals("Nutrición") &&
                !especialidad.equals("Fisioterapia")) {

            throw new ValidacionException("La especialidad del doctor no es válida.");
        }

        this.especialidad = especialidad;
    }

    public String getEstadoDoctor() {
        return estadoDoctor;
    }

    public void setEstadoDoctor(String estadoDoctor) throws ValidacionException {

        if (!estadoDoctor.equals("Activo") &&
                !estadoDoctor.equals("Inactivo")) {

            throw new ValidacionException("El estado del doctor no es válido.");
        }
        this.estadoDoctor = estadoDoctor;
    }
}
