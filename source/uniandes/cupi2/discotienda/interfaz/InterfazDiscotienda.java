/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: InterfazDiscotienda.java,v 1.12 2007/04/13 03:56:39 carl-veg Exp $
 * Universidad de los Andes (Bogoto - Colombia)
 * Departamento de Ingenieroa de Sistemas y Computacion 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n8_discotienda 
 * Autor: Nicolos Lopez - 06/12/2005 
 * Autor: Mario Sonchez - 26/01/2005
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package uniandes.cupi2.discotienda.interfaz;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import uniandes.cupi2.discotienda.mundo.*;

/**
 * Es la clase principal de la interfaz
 */
public class InterfazDiscotienda extends JFrame
{
    // -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * La ruta donde deben guardarse las facturas
     */
    private static final String RUTA_FACTURAS = "./data/facturas";

    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------

    /**
     * Es una referencia a la discotienda
     */
    private Discotienda discotienda;

    /**
     * Es una referencia al disco de la cual se eston mostrando los datos
     */
    private Disco discoSeleccionado;

    // -----------------------------------------------------------------
    // Atributos de la Interfaz
    // -----------------------------------------------------------------

    /**
     * Es el panel con los botones para las extensiones de la aplicacion
     */
    private PanelExtension panelExtension;

    /**
     * Es el panel con la informacion del disco seleccionado
     */
    private PanelDiscos panelDiscos;

    /**
     * Es el panel para presentar las canciones del disco
     */
    private PanelDatosCanciones panelDatosCanciones;

    /**
     * Es el panel donde se muestra una imagen decorativa
     */
    private PanelImagen panelImagen;

    /**
     * Es el panel donde se realizan los pedidos
     */
    private PanelPedido panelPedido;

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Construye la interfaz de la aplicacion
     * @param d es la discotienda que se va a mostrar
     */
    public InterfazDiscotienda( Discotienda d )
    {
        discotienda = d;

        // Panel con la Imagen
        panelImagen = new PanelImagen( );
        add( panelImagen, BorderLayout.NORTH );

        // Panel central con los datos del disco, de las canciones y el boton para cargar un pedido
        JPanel panelCentral = new JPanel( new BorderLayout( ) );
        add( panelCentral, BorderLayout.CENTER );

        panelDiscos = new PanelDiscos( this, discotienda.darDiscos( ) );
        panelCentral.add( panelDiscos, BorderLayout.CENTER );

        panelDatosCanciones = new PanelDatosCanciones( this );
        panelCentral.add( panelDatosCanciones, BorderLayout.EAST );

        ArrayList discos = discotienda.darDiscos( );
        if( discos.size( ) > 0 )
        {
            cambiarDiscoSeleccionado( ( ( String )discos.get( 0 ) ) );
        }

        panelPedido = new PanelPedido( this );
        panelCentral.add( panelPedido, BorderLayout.SOUTH );

        // Panel inferior con los botones para las extensiones del ejercicio
        panelExtension = new PanelExtension( this );
        add( panelExtension, BorderLayout.SOUTH );

        setTitle( "miDiscoTienda" );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        pack( );
    }

    // -----------------------------------------------------------------
    // Motodos
    // -----------------------------------------------------------------

    /**
     * Cambia el disco seleccionado en el panel de detalles del disco
     * @param nombreDisco el nombre del disco a mostrar los detalles
     */
    public void cambiarDiscoSeleccionado( String nombreDisco )
    {
        discoSeleccionado = discotienda.darDisco( nombreDisco );
        panelDiscos.cambiarDisco( discoSeleccionado );
        panelDatosCanciones.cambiarDisco( discoSeleccionado );
    }

    /**
     * Muestra el diologo para agregar un nuevo disco a la discotienda
     */
    public void mostrarDialogoAgregarDisco( )
    {
        DialogoCrearDisco dialogo = new DialogoCrearDisco( this );
        dialogo.setLocationRelativeTo( this );
        dialogo.setVisible( true );
    }

    /**
     * Muestra el diologo para agregar una nueva cancion al disco en el panel de detalles del disco
     */
    public void mostrarDialogoAgregarCancion( )
    {
        DialogoCrearCancion dialogo = new DialogoCrearCancion( this );
        dialogo.setLocationRelativeTo( this );
        dialogo.setVisible( true );
    }

    /**
     * Crea un nuevo disco en la discotienda y actualiza el panel con la lista de discos <br>
     * <b>pre: <b>No debe haber otro disco con el mismo nombre en la discotienda
     * @param nombreDisco El nombreDisco del disco a crear
     * @param artista el artista del nuevo disco
     * @param genero el genero del nuevo disco
     * @param imagen el nombre de la imagen asociada al nuevo disco
     * @return Retorna true si la cancion se pudo agregar. Esto sirve para saber si se debe cerrar el diologo.
     */
    public boolean crearDisco( String nombreDisco, String artista, String genero, String imagen )
    {
        boolean ok = false;
        try
        {
            discotienda.agregarDisco( nombreDisco, artista, genero, imagen );
            panelDiscos.refrescarDiscos( discotienda.darDiscos( ) );
            ok = true;
        }
        catch( ElementoExisteException e )
        {
            JOptionPane.showMessageDialog( this, e.getMessage( ) );
        }

        return ok;
    }

    /**
     * Crea una nueva cancion en el disco que se muestra en los detalles de disco en la discotienda <br>
     * <b>pre: <b>No debe haber otra cancion con el mismo nombre en el disco
     * @param nombre el nombre de la nueva cancion
     * @param minutos el nomero de minutos de duracion de la cancion
     * @param segundos el nomero de segundos de duracion de la cancion
     * @param precio el precio de la cancion
     * @param tamano el tamaoo en MB de la cancion
     * @param calidad la calidad de la cancion en KBps
     * @return Retorna true si la cancion se pudo agregar. Esto sirve para saber si se debe cerrar el diologo.
     */
    public boolean crearCancion( String nombre, int minutos, int segundos, double precio, double tamano, int calidad )
    {
        boolean ok = false;

        if( discoSeleccionado != null )
        {
            try
            {
                discotienda.agregarCancionADisco( discoSeleccionado.darNombreDisco( ), nombre, minutos, segundos, precio, tamano, calidad );
                discoSeleccionado = discotienda.darDisco( discoSeleccionado.darNombreDisco( ) );
                panelDiscos.cambiarDisco( discoSeleccionado );
                ok = true;
            }
            catch( ElementoExisteException e )
            {
                JOptionPane.showMessageDialog( this, e.getMessage( ) );
            }
        }

        return ok;
    }

    /**
     * Vende una cancion a una persona
     * @param disco el disco al que pertenece la cancion que se va a vender - disco != null
     * @param cancion la cancion que se va a vender - cancion != null
     */
    public void venderCancion( Disco disco, Cancion cancion )
    {
        String email = JOptionPane.showInputDialog( this, "Indique el email del comprador", "Email", JOptionPane.QUESTION_MESSAGE );
        if( email != null )
        {
            if( discotienda.validarEmail( email ) )
            {
                try
                {
                    String archivoFactura = discotienda.venderCancion( disco, cancion, email, RUTA_FACTURAS );
                    JOptionPane.showMessageDialog( this, "La factura se guardo en el archivo: " + archivoFactura, "Factura Guardada", JOptionPane.INFORMATION_MESSAGE );
                }
                catch( IOException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presento un problema guardando el archivo de la factura:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( this, "El email indicado no es volido", "Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /**
     * Este motodo se encarga de cargar la informacion de un pedido
     */
    public void cargarPedido( )
    {
        JFileChooser fc = new JFileChooser( "./data" );
        fc.setDialogTitle( "Pedido" );
        int resultado = fc.showOpenDialog( this );
        if( resultado == JFileChooser.APPROVE_OPTION )
        {
            File archivo = fc.getSelectedFile( );
            if( archivo != null )
            {
                try
                {
                    String archivoFactura = discotienda.venderListaCanciones( archivo, RUTA_FACTURAS );
                    JOptionPane.showMessageDialog( this, "La factura se guardo en el archivo: " + archivoFactura, "Factura Guardada", JOptionPane.INFORMATION_MESSAGE );
                }
                catch( FileNotFoundException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presento un problema leyendo el archivo:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
                catch( IOException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presento un problema leyendo el archivo:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
                catch( ArchivoVentaException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presento un problema debido al formato del archivo:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    /**
     * Este motodo se encarga de salvar la informacion de la discotienda, justo antes de cerrar la aplicacion
     */
    public void dispose( )
    {
        try
        {
            discotienda.salvarDiscotienda( );
            super.dispose( );
        }
        catch( Exception e )
        {
            setVisible( true );
            int respuesta = JOptionPane.showConfirmDialog( this, "Problemas salvando la informacion de la discotienda:\n" + e.getMessage( ) + "\noQuiere cerrar el programa sin salvar?", "Error", JOptionPane.YES_NO_OPTION );
            if( respuesta == JOptionPane.YES_OPTION )
            {
                super.dispose( );
            }
        }
    }

    // -----------------------------------------------------------------
    // Puntos de Extension
    // -----------------------------------------------------------------

    /**
     * Ejecuta el punto de extension 1
     */
    public void reqFuncOpcion1( )
    {
        String resultado = discotienda.metodo1( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    /**
     * Ejecuta el punto de extension 2
     * @throws Exception 
     */
    public void reqFuncOpcion2( ) throws Exception
    {
        String resultado = discotienda.metodo2( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }
    /**
     * Ejecuta el punto de extension 3
     */
    public void reqFuncOpcion3( )
    {
        String resultado = discotienda.metodo3( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    /**
     * Ejecuta el punto de extension 4
     */
    public void reqFuncOpcion4( )
    {
        String resultado = discotienda.metodo4( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    /**
     * Ejecuta el punto de extension 5
     */
    public void reqFuncOpcion5( )
    {
        String resultado = discotienda.metodo5( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    /**
     * Ejecuta el punto de extension 6
     */
    public void reqFuncOpcion6( )
    {
        String resultado = discotienda.metodo6( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    // -----------------------------------------------------------------
    // Programa principal
    // -----------------------------------------------------------------

    /**
     * Ejecuta la aplicacion
     * @param args son parometros de ejecucion de la aplicacion. No se usan en este programa
     */
    public static void main( String[] args )
    {
        Discotienda discotienda = null;
        try
        {
            discotienda = new Discotienda( "./data/discotienda.discos" );
        }
        catch( PersistenciaException e )
        {
            e.printStackTrace( );
            System.exit( 1 );
        }
        InterfazDiscotienda id = new InterfazDiscotienda( discotienda );
        id.setVisible( true );
    }
}
