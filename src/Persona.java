public class Persona {

    //DATOS COMUNES DE PACIENTE Y DOCTOR
    private String codigo;
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;


    //CONSTRUCTOR
    public Persona(String codigo, String nombreCompleto, String telefono, String correoElectronico) throws ValidacionException {

        if (codigo == null || codigo.isBlank()) {
            throw new ValidacionException("El código no puede estar vacío.");
        }

        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new ValidacionException("El nombre no puede estar vacío.");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new ValidacionException("El teléfono no puede estar vacío.");
        }

        if (correoElectronico == null || correoElectronico.isBlank()) {
            throw new ValidacionException("El correo electrónico no puede estar vacío.");
        }

        this.codigo = codigo;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}
