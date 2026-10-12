import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CitaMedicaDAO {

    public boolean registrarCita(CitaMedica nuevaCita) throws SQLException, ValidacionException {
        // Regla 1: El doctor debe estar Activo
        if (!nuevaCita.getDoctor().getEstadoDoctor().equalsIgnoreCase("Activo")) {
            throw new ValidacionException("No se puede registrar la cita porque el doctor está inactivo.");
        }

        // Regla 2: Comprobar traslape en la base de datos
        String checkSql = "SELECT 1 FROM Citas_Medicas WHERE codigo_doctor = ? AND fecha_cita = ? AND hora_cita = ? AND estado_cita = 'Programada'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            checkPs.setString(1, nuevaCita.getDoctor().getCodigo());
            checkPs.setDate(2, Date.valueOf(nuevaCita.getFecha()));
            checkPs.setTime(3, Time.valueOf(nuevaCita.getHora()));

            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    throw new ValidacionException("El doctor ya tiene una cita programada para esa fecha y esa hora.");
                }
            }
        }

        // Insertar en la base de datos
        String insertSql = "INSERT INTO Citas_Medicas (codigo_cita, codigo_paciente, codigo_doctor, fecha_cita, hora_cita, motivo_consulta, estado_cita) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            ps.setString(1, nuevaCita.getCodigoCita());
            ps.setString(2, nuevaCita.getPaciente().getCodigo());
            ps.setString(3, nuevaCita.getDoctor().getCodigo());
            ps.setDate(4, Date.valueOf(nuevaCita.getFecha()));
            ps.setTime(5, Time.valueOf(nuevaCita.getHora()));
            ps.setString(6, nuevaCita.getMotivoConsulta());
            ps.setString(7, nuevaCita.getEstadoCita());

            return ps.executeUpdate() > 0;
        }
    }

    public List<CitaMedica> listarTodas() throws SQLException, ValidacionException {
        List<CitaMedica> lista = new ArrayList<>();
        String sql = "SELECT c.codigo_cita, c.fecha_cita, c.hora_cita, c.motivo_consulta, c.estado_cita, "
                   + "p.codigo_paciente, p.nombre_completo AS pac_nombre, p.telefono AS pac_tel, p.correo_electronico AS pac_correo, p.edad AS pac_edad, p.tipo_paciente, p.estado_paciente, "
                   + "d.codigo_doctor, d.nombre_completo AS doc_nombre, d.telefono AS doc_tel, d.correo_electronico AS doc_correo, d.especialidad, d.estado_doctor "
                   + "FROM Citas_Medicas c "
                   + "INNER JOIN Pacientes p ON c.codigo_paciente = p.codigo_paciente "
                   + "INNER JOIN Doctores d ON c.codigo_doctor = d.codigo_doctor "
                   + "ORDER BY c.fecha_cita, c.hora_cita";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente pac = new Paciente(
                        rs.getString("codigo_paciente"),
                        rs.getString("pac_nombre"),
                        rs.getString("pac_tel"),
                        rs.getString("pac_correo"),
                        rs.getInt("pac_edad"),
                        rs.getString("tipo_paciente"),
                        rs.getString("estado_paciente")
                );

                Doctor doc = new Doctor(
                        rs.getString("codigo_doctor"),
                        rs.getString("doc_nombre"),
                        rs.getString("doc_tel"),
                        rs.getString("doc_correo"),
                        rs.getString("especialidad"),
                        rs.getString("estado_doctor")
                );

                LocalDate fecha = rs.getDate("fecha_cita").toLocalDate();
                LocalTime hora = rs.getTime("hora_cita").toLocalTime();

                CitaMedica cita = new CitaMedica(
                        rs.getString("codigo_cita"),
                        pac,
                        doc,
                        fecha,
                        hora,
                        rs.getString("motivo_consulta"),
                        rs.getString("estado_cita")
                );

                lista.add(cita);
            }
        }
        return lista;
    }

    public boolean existe(String codigo) throws SQLException {
        String sql = "SELECT 1 FROM Citas_Medicas WHERE codigo_cita = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
