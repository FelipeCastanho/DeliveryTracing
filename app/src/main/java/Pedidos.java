import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jennifer on 13/10/2017.
 */

public class Pedidos {
    public String empleadoId;
    public String empresaId;
    public String estado;
    public String nombrePedido;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();


    public Pedidos() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Pedidos(String empleadoId, String empresaId, String estado, String nombrePedido) {
        this.empleadoId = empleadoId;
        this.empresaId = empresaId;
        this.estado = estado;
        this.nombrePedido = nombrePedido;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("empleadoId", empleadoId);
        result.put("empresaId", empresaId);
        result.put("estado", estado);
        result.put("nombrePedido", nombrePedido);
        result.put("starCount", starCount);
        result.put("stars", stars);
        return result;
    }

}
