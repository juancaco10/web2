package com.example.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.web.dto.CategoriaDTO;
import com.example.web.modelo.Categoria;
import com.example.web.modelo.Vehiculo;
import com.example.web.repositorio.CategoriaRepository;
import com.example.web.repositorio.VehiculoRepository;

@Controller
public class CategoriaController {

	@Autowired
	private VehiculoRepository vehiculoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/categorias/insertar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("categoria", new CategoriaDTO());
        return "categorias/insertar";
    }

    @PostMapping("/categorias/insertar")
    public String agregarCategoria(@ModelAttribute("categoria") CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoriaRepository.save(categoria);
        return "redirect:/categorias/insertar";
    }

        @GetMapping("/categorias/listar")
    public ModelAndView listarCategorias() {
        ModelAndView mv = new ModelAndView("categorias/listar");
        List<Categoria> categorias = categoriaRepository.findAll();
        mv.addObject("categorias", categorias);
        return mv;
    }

    @GetMapping("/categorias/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        model.addAttribute("categoria", categoria);
        return "categorias/editar";
    }

    @PostMapping("/categorias/editar/{id}")
    public String editarCategoria(@PathVariable("id") int id, @ModelAttribute("categoria") CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        categoria.setNombre(categoriaDTO.getNombre());
        categoriaRepository.save(categoria);
        return "redirect:/categorias/listar";
    }

    
    @PostMapping("/categorias/eliminar")
    public String eliminarCategoria(@RequestParam("id") int id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            List<Vehiculo> vehiculos = categoria.getVehiculos();
            if (!vehiculos.isEmpty()) {
               
                vehiculos.forEach(vehiculo -> vehiculoRepository.deleteById(vehiculo.getIdVehiculo()));
            }
            
            categoriaRepository.deleteById(id);
        }
        return "redirect:/categorias/listar";
    }





}
