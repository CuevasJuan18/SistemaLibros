package jc.tienda_libros.vista;

import jc.tienda_libros.modelo.Libro;
import jc.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField nombreLibroTextField;
    private JTextField nombreAutorTextField;
    private JTextField numPrecioTextField;
    private JTextField cantidadLibrosTextField;
    private JTextField cantidadPaginasTextField;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tableModel;


    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarVista();
        agregarButton.addActionListener(e -> agregarLibro());
        modificarButton.addActionListener(e -> modificarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> eliminarLibro());
    }

    private void eliminarLibro() {
        if (this.idTexto.getText().equals("")){
            mostrarMensaje("Debe selecionar un registro para poder eliminar");
        }else{
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = nombreLibroTextField.getText();
            var autor = nombreAutorTextField.getText();
            var precio = Double.parseDouble(numPrecioTextField.getText());
            var cantidad = Integer.parseInt(cantidadLibrosTextField.getText());
            var paginas = Integer.parseInt(cantidadPaginasTextField.getText());
            var libro = new Libro(idLibro, nombreLibro, autor, paginas,
                    precio, cantidad);
            libroServicio.eliminarLibro(libro);
            mostrarMensaje("Se elimino el libro " + nombreLibro);
            limpiarFormulario();
            listarLibros();
        }
    }

    private void modificarLibro() {
            if (this.idTexto.getText().equals("")){
                mostrarMensaje("Debe selecionar un registro");
            }else {
                if (nombreLibroTextField.getText().equals("")){
                    mostrarMensaje("Proporciona el nombre del libro");
                    nombreLibroTextField.requestFocusInWindow();
                    return;
                }
                int idLibro = Integer.parseInt(idTexto.getText());
                var nombreLibro = nombreLibroTextField.getText();
                var autor = nombreAutorTextField.getText();
                var precio = Double.parseDouble(numPrecioTextField.getText());
                var cantidad = Integer.parseInt(cantidadLibrosTextField.getText());
                var paginas = Integer.parseInt(cantidadPaginasTextField.getText());
                var libro = new Libro(idLibro, nombreLibro, autor, paginas,
                        precio, cantidad);
                libroServicio.guardarLibro(libro);
                mostrarMensaje("Se modifico el libro!");
                limpiarFormulario();
                listarLibros();
            }
        }

    public void iniciarVista(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension pantalla = toolkit.getScreenSize();
        int x = (pantalla.width - getWidth() / 2);
        int y = (pantalla.height - getHeight() / 2);
        setLocation(x, y);

    }
    private void agregarLibro(){
        if (nombreLibroTextField.getText().equals("")) {
            mostrarMensaje("Proporcione el Nombre del libro");
            nombreLibroTextField.requestFocusInWindow();
            return;
        }
        var nombreLibro = nombreLibroTextField.getText();
        var autor = nombreAutorTextField.getText();
        var precio = Double.parseDouble(numPrecioTextField.getText());
        var cantidad = Integer.parseInt(cantidadLibrosTextField.getText());
        var paginas = Integer.parseInt(cantidadPaginasTextField.getText());
        var libro = new Libro();
        libro.setNombreLibro(nombreLibro);
        libro.setAutor(autor);
        libro.setPrecio(precio);
        libro.setCantidad(cantidad);
        libro.setNumPaginas(paginas);
        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("Se agrego el libro!");
        limpiarFormulario();
        listarLibros();
    }
    private void limpiarFormulario(){
        nombreLibroTextField.setText("");
        nombreAutorTextField.setText("");
        numPrecioTextField.setText("");
        cantidadLibrosTextField.setText("");
        cantidadPaginasTextField.setText("");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tableModel = new DefaultTableModel(0, 5){
            @Override
            public boolean isCellEditable(int row, int collum){
                return false;
            }
        };

        String[] cabecero = {"Id", "Nombre", "Autor", "Precio", "Numero de paginas", "Cantidad"};
        tableModel.setColumnIdentifiers(cabecero);
        this.tablaLibros = new JTable(tableModel);
        listarLibros();
    }
    public void listarLibros(){
        tableModel.setRowCount(0);
        var libros = libroServicio.listarLibros();
        libros.forEach(libro -> {
            Object[] renglonLibro = {
                     libro.getIdLibro(),
                     libro.getNombreLibro(),
                     libro.getAutor(),
                     libro.getPrecio(),
                     libro.getNumPaginas(),
                     libro.getCantidad()
            };
            this.tableModel.addRow(renglonLibro);
        });
    }
    public void cargarLibroSeleccionado(){
        var renglon = tablaLibros.getSelectedRow();
        if (renglon != -1){
            String idLibro = tablaLibros.getModel().getValueAt(renglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro =
                    tablaLibros.getModel().getValueAt(renglon, 1).toString();
            nombreLibroTextField.setText(nombreLibro);
            String autor =
                    tablaLibros.getModel().getValueAt(renglon, 2).toString();
            nombreAutorTextField.setText(autor);
            String precio =
                    tablaLibros.getModel().getValueAt(renglon, 3).toString();
            numPrecioTextField.setText(precio);
            String cantidad =
                    tablaLibros.getModel().getValueAt(renglon, 4).toString();
            cantidadLibrosTextField.setText(cantidad);
            String paginas =
                    tablaLibros.getModel().getValueAt(renglon, 5).toString();
            cantidadPaginasTextField.setText(paginas);
        }
    }
}
