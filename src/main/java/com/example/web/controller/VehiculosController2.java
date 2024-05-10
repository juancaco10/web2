package com.example.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.web.modelo.Vehiculo;
import jakarta.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;

import com.example.web.modelo.Categoria;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.web.dto.VehiculoDTO;

import com.example.web.modelo.Vehiculo;
import com.example.web.repositorio.CategoriaRepository;
import com.example.web.repositorio.VehiculoRepository;

@Controller
@RequestMapping("/vehiculos")
public class VehiculosController2 {

	@Autowired
	VehiculoRepository vehiculoRepository;

	@Autowired
	CategoriaRepository categoriaRepository;

	@GetMapping("/inicio")
	public ModelAndView mostrarPaginaInicial() {
		ModelAndView mv = new ModelAndView("vehiculos/inicio");
		List<Vehiculo> vehiculos = vehiculoRepository.findAll();
		List<Categoria> categorias = categoriaRepository.findAll();
		mv.addObject("vehiculos", vehiculos);
		mv.addObject("categorias", categorias);
		return mv;
	}

	@GetMapping("/insertar")
	public ModelAndView formulario() {
		ModelAndView mv = new ModelAndView("vehiculos/insertar");
		List<Categoria> categorias = categoriaRepository.findAll();
		mv.addObject("categorias", categorias);
		mv.addObject("vehiculoDTO", new VehiculoDTO());
		return mv;
	}

	@PostMapping("/listarPorModelo")
	public ModelAndView listarVehiculosPorModelo(@RequestParam("modelo") String modelo) {
		List<Vehiculo> vehiculos = vehiculoRepository.findVehiculoByModeloLike("%" + modelo + "%");
		if (!vehiculos.isEmpty()) {
			ModelAndView mv = new ModelAndView("vehiculos/resultado_busqueda");
			mv.addObject("vehiculos", vehiculos);
			return mv;
		} else {
			ModelAndView mv = new ModelAndView("vehiculos/listar");
			mv.addObject("error", "Vehículo no encontrado");
			return mv;
		}
	}

	@PostMapping("/listarPorAno")
	public ModelAndView listarVehiculosPorAno(@RequestParam("ano") String ano) {
		List<Vehiculo> vehiculos = vehiculoRepository.findVehiculoByAnoLike("%" + ano + "%");
		if (!vehiculos.isEmpty()) {
			ModelAndView mv = new ModelAndView("vehiculos/resultado_busqueda");
			mv.addObject("vehiculos", vehiculos);
			return mv;
		} else {
			ModelAndView mv = new ModelAndView("vehiculos/listar");
			mv.addObject("error", "Vehículo no encontrado");
			return mv;
		}
	}

	@GetMapping("/detalles/{idVehiculo}")
	public ModelAndView detallesVehiculo(@PathVariable("idVehiculo") int idVehiculo) {
		ModelAndView mv = new ModelAndView("vehiculos/detalles");
		Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(idVehiculo);
		if (vehiculoOpt.isPresent()) {
			mv.addObject("vehiculo", vehiculoOpt.get());
		} else {
			mv.addObject("error", "Vehículo no encontrado");
		}
		return mv;
	}

	@PostMapping("/insertar")
	public ModelAndView guardarVehiculo(@ModelAttribute @Valid VehiculoDTO vehiculoDTO, BindingResult result,
			@RequestParam("file") MultipartFile imagen, @RequestParam("idCategoria") int idCategoria,
			RedirectAttributes msg) {
		ModelAndView mv = new ModelAndView("vehiculos/insertar");
		if (result.hasErrors()) {
			List<Categoria> categorias = categoriaRepository.findAll();
			mv.addObject("categorias", categorias);
			return mv;
		}
		var vehiculo = new Vehiculo();
		BeanUtils.copyProperties(vehiculoDTO, vehiculo);
		try {
			if (!imagen.isEmpty()) {
				byte[] bytes = imagen.getBytes();
				Path ruta = Paths.get("./src/main/resources/static/img/" + imagen.getOriginalFilename());
				Files.write(ruta, bytes);
				vehiculo.setImagen(imagen.getOriginalFilename());
			}
		} catch (IOException e) {
			System.out.println("Error en la imagen");
		}

		Optional<Categoria> categoriaOpt = categoriaRepository.findById(idCategoria);
		if (categoriaOpt.isPresent()) {
			vehiculo.setIdCategoria(categoriaOpt.get());
		} else {
			msg.addFlashAttribute("error", "Categoría no encontrada.");
			return mv;
		}

		vehiculoRepository.save(vehiculo);
		msg.addFlashAttribute("success", "¡Vehículo registrado con éxito!");

		List<Categoria> categorias = categoriaRepository.findAll();
		mv.addObject("categorias", categorias);
		mv.addObject("vehiculoDTO", new VehiculoDTO());
		return mv;
	}

	@GetMapping("/listar")
	public ModelAndView listarVehiculos() {
		ModelAndView mv = new ModelAndView("vehiculos/listar");
		List<Vehiculo> vehiculos = vehiculoRepository.findAll();
		mv.addObject("vehiculos", vehiculos);
		return mv;
	}

	@GetMapping("/imagen/{imagen}")
	@ResponseBody
	public byte[] mostrarImagen(@PathVariable("imagen") String imagen) throws IOException {
		String filePath = Paths.get("src", "main", "resources", "static", "img", imagen).toString();
		File archivo = new File(filePath);
		if (archivo.exists()) {
			return Files.readAllBytes(archivo.toPath());
		}
		return null;
	}
	
	@GetMapping("/editar/{idVehiculo}")
	public ModelAndView mostrarFormularioEdicion(@PathVariable("idVehiculo") int idVehiculo) {
	    ModelAndView mv = new ModelAndView("vehiculos/editar");
	    Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(idVehiculo);
	    if (vehiculoOpt.isPresent()) {
	        Vehiculo vehiculo = vehiculoOpt.get();
	        VehiculoDTO vehiculoDTO = new VehiculoDTO();
	        BeanUtils.copyProperties(vehiculo, vehiculoDTO);
	        mv.addObject("vehiculoDTO", vehiculoDTO);
	        mv.addObject("idVehiculo", idVehiculo);

	        // Recuperar la información anterior del vehículo
	        String infoAnterior = "Marca anterior: " + vehiculo.getMarca() +
	                              ", Modelo anterior: " + vehiculo.getModelo() +
	                              ", Año anterior: " + vehiculo.getAno() +
	                              ", Color anterior: " + vehiculo.getColor() +
	                              ", Matrícula anterior: " + vehiculo.getMatricula();

	        mv.addObject("infoAnterior", infoAnterior);

	        return mv;
	    } else {
	        mv.addObject("error", "¡Vehículo no encontrado!");
	        return mv;
	    }
	}

	@PostMapping("/editar/{idVehiculo}")
	public String editarVehiculo(@PathVariable("idVehiculo") int idVehiculo,
	                             @RequestParam("imagen") MultipartFile imagen,
	                             @ModelAttribute @Valid VehiculoDTO vehiculoDTO,
	                             BindingResult result, RedirectAttributes msg) {
	    Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(idVehiculo);
	    if (vehiculoOpt.isPresent()) {
	        Vehiculo vehiculo = vehiculoOpt.get();
	        vehiculo.setMarca(vehiculoDTO.getMarca());
	        vehiculo.setModelo(vehiculoDTO.getModelo());
	        vehiculo.setAno(vehiculoDTO.getAno());
	        vehiculo.setColor(vehiculoDTO.getColor());
	        vehiculo.setMatricula(vehiculoDTO.getMatricula());

	        if (!imagen.isEmpty()) {
	            try {
	                String nombreImagen = UUID.randomUUID().toString() + "."
	                        + FilenameUtils.getExtension(imagen.getOriginalFilename());
	                Files.copy(imagen.getInputStream(),
	                        Paths.get("src/main/resources/static/img").resolve(nombreImagen));
	                vehiculo.setImagen(nombreImagen);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	        vehiculoRepository.save(vehiculo);
	        msg.addFlashAttribute("success", "¡Vehículo actualizado con éxito!");
	    } else {
	        msg.addFlashAttribute("error", "¡Vehículo no encontrado!");
	    }
	    return "redirect:/vehiculos/listar";
	}





	@GetMapping("/filtrarPorCategoria")
	public ModelAndView filtrarVehiculosPorCategoria(@RequestParam("idCategoria") int idCategoria) {
	    List<Vehiculo> vehiculos = vehiculoRepository.findVehiculoByIdCategoria_IdCategoria(idCategoria);
	    ModelAndView mv = new ModelAndView("vehiculos/filtrarPorCategoria");
	    mv.addObject("vehiculos", vehiculos);
	    return mv;
	}


	@PostMapping("/eliminar/{id}")
	public String eliminarVehiculo(@PathVariable(value = "id") int id) {
		Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findById(id);
		if (vehiculoOpt.isPresent()) {
			vehiculoRepository.delete(vehiculoOpt.get());
		}
		return "redirect:/vehiculos/listar";
	}
}
