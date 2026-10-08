import java.util.ArrayList;
import java.util.List;

public class GestorCitas {

    //LISTAS DE CITAS REGISTRADAS
    private List<CitaMedica> citas;

    //CONSTRUCTOR
    public GestorCitas() {
        citas = new ArrayList<>();
    }

    //REGISTRAR CITA
    public void registrarCita(CitaMedica nuevaCita) throws ValidacionException {

        if (!nuevaCita.getDoctor().getEstadoDoctor().equals("Activo")) {
            throw new ValidacionException("No se puede registrar la cita porque el doctor está inactivo.");
        }

        for (CitaMedica cita : citas) {

            if (cita.getDoctor().getCodigo().equals(nuevaCita.getDoctor().getCodigo())
                    && cita.getFecha().equals(nuevaCita.getFecha())
                    && cita.getHora().equals(nuevaCita.getHora())
                    && cita.getEstadoCita().equals("Programada")) {

                throw new ValidacionException("El doctor ya tiene una cita programada para esa fecha y esa hora.");
            }
        }
        citas.add(nuevaCita);
    }
    //CONSULTAR CITAS

    public List<CitaMedica> getCitas() {
        return citas;
    }
}
