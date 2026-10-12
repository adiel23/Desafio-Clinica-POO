import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public boolean insertar(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO Doctores (codigo_doctor, nombre_completo, especialidad, telefono, correo_electronico, estado_doctor) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctor.getCodigo());
            ps.setString(2, doctor.getNombreCompleto());
            ps.setString(3, doctor.getEspecialidad());
            ps.setString(4, doctor.getTelefono());
            ps.setString(5, doctor.getCorreoElectronico());
            ps.setString(6, doctor.getEstadoDoctor());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Doctor> listarTodos() throws SQLException, ValidacionException {
        List<Doctor> lista = new ArrayList<>();
        String sql = "SELECT codigo_doctor, nombre_completo, especialidad, telefono, correo_electronico, estado_doctor FROM Doctores ORDER BY codigo_doctor";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getString("codigo_doctor"),
                        rs.getString("nombre_completo"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("especialidad"),
                        rs.getString("estado_doctor")
                );
                lista.add(d);
            }
        }
        return lista;
    }

    public Doctor buscarPorCodigo(String codigo) throws SQLException, ValidacionException {
        String sql = "SELECT codigo_doctor, nombre_completo, especialidad, telefono, correo_electronico, estado_doctor FROM Doctores WHERE codigo_doctor = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Doctor(
                            rs.getString("codigo_doctor"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("especialidad"),
                            rs.getString("estado_doctor")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(Doctor doctor) throws SQLException {
        String sql = "UPDATE Doctores SET nombre_completo = ?, especialidad = ?, telefono = ?, correo_electronico = ?, estado_doctor = ? WHERE codigo_doctor = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctor.getNombreCompleto());
            ps.setString(2, doctor.getEspecialidad());
            ps.setString(3, doctor.getTelefono());
            ps.setString(4, doctor.getCorreoElectronico());
            ps.setString(5, doctor.getEstadoDoctor());
            ps.setString(6, doctor.getCodigo());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean existe(String codigo) throws SQLException {
        String sql = "SELECT 1 FROM Doctores WHERE codigo_doctor = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
