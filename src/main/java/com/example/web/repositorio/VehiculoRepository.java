package com.example.web.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.web.modelo.Categoria;
import com.example.web.modelo.Vehiculo;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    
    List<Vehiculo> findVehiculoByModeloLike(String modelo);
    
    List<Vehiculo> findVehiculoByAnoLike(String ano);
    
    List<Vehiculo> findVehiculoByIdCategoria_IdCategoria(int idCategoria);

}
