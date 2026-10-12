import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public boolean insertar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO Pacientes (codigo_paciente, nombre_completo, edad, telefono, correo_electronico, tipo_paciente, estado_paciente) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getCodigo());
            ps.setString(2, paciente.getNombreCompleto());
            ps.setInt(3, paciente.getEdad());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, paciente.getCorreoElectronico());
            ps.setString(6, paciente.getTipoPaciente());
            ps.setString(7, paciente.getEstadoPaciente());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Paciente> listarTodos() throws SQLException, ValidacionException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT codigo_paciente, nombre_completo, edad, telefono, correo_electronico, tipo_paciente, estado_paciente FROM Pacientes ORDER BY codigo_paciente";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente p = new Paciente(
                        rs.getString("codigo_paciente"),
                        rs.getString("nombre_completo"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getInt("edad"),
                        rs.getString("tipo_paciente"),
                        rs.getString("estado_paciente")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public Paciente buscarPorCodigo(String codigo) throws SQLException, ValidacionException {
        String sql = "SELECT codigo_paciente, nombre_completo, edad, telefono, correo_electronico, tipo_paciente, estado_paciente FROM Pacientes WHERE codigo_paciente = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getString("codigo_paciente"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getInt("edad"),
                            rs.getString("tipo_paciente"),
                            rs.getString("estado_paciente")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE Pacientes SET nombre_completo = ?, edad = ?, telefono = ?, correo_electronico = ?, tipo_paciente = ?, estado_paciente = ? WHERE codigo_paciente = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNombreCompleto());
            ps.setInt(2, paciente.getEdad());
            ps.setString(3, paciente.getTelefono());
            ps.setString(4, paciente.getCorreoElectronico());
            ps.setString(5, paciente.getTipoPaciente());
            ps.setString(6, paciente.getEstadoPaciente());
            ps.setString(7, paciente.getCodigo());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean existe(String codigo) throws SQLException {
        String sql = "SELECT 1 FROM Pacientes WHERE codigo_paciente = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
