package jc.tienda_libros.repositorio;

import jc.tienda_libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILibroRepositorio extends JpaRepository<Libro, Integer> {

}
