package pe.edu.upeu.asistencia.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.dto.ComboBoxOption;
import pe.edu.upeu.asistencia.modelo.Evento;
import pe.edu.upeu.asistencia.repositorio.EventoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventoServiceImp extends EventoRepository implements EventoServicioI {
    @Override
    public void save(Evento evento) {
        eventos.add(evento);
    }

    @Override
    public List<Evento> findAll() {
        if(eventos.size()==0){
            return super.findAll();
        }
        return eventos;
    }

    @Override
    public Evento update(Evento evento, int index) {
        return eventos.set(index, evento);
    }

    @Override
    public void delete(int index) {
        eventos.remove(index);
    }

    @Override
    public Evento findById(int index) {
        return eventos.get(index);
    }

    @Override
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(Evento cate : eventos) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getCodigoEvento()));
            cb.setValue(cate.getNombreEvento());
            listar.add(cb);
        }
        return listar;
    }
}
