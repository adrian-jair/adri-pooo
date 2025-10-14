package pe.edu.upeu.asistencia.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.modelo.Asistencia;
import pe.edu.upeu.asistencia.modelo.Evento;
import pe.edu.upeu.asistencia.repositorio.AsistenciaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsistenciaServicioImp extends AsistenciaRepository implements AsistenciaServicioI{

    @Override
    public List<Asistencia> findAll() {
        if(asistencias.isEmpty()){
            return super.findAll();
        }
        return asistencias;
    }
    @Override
    public List<Asistencia> buscarXCodEvent(String codEvent){
        findAll();
        List<Asistencia> filtrados = asistencias.stream()
                .filter(e -> e.getCodigoEvento().equals(codEvent))
                .collect(Collectors.toList());
        return filtrados;
    }
    @Override
    public void save(Asistencia asistencia) {
        asistencias.add(asistencia);
    }
}
