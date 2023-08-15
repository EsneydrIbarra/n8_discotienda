/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: Discotienda.java,v 1.15 2007/04/10 12:24:14 p-marque Exp $
 * Universidad de los Andes (Bogote - Colombia)
 * Departamento de Ingenierea de Sistemas y Computacien 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n8_discotienda 
 * Autor: Nicoles Lepez - 06/12/2005 
 * Autor: Mario Senchez - 26/01/2005
 * Autor: Jorge Villalobos - 29/07/2006
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package uniandes.cupi2.discotienda.mundo;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Es la clase que representa la tienda virtual con sus discos. <br>
 * <b>inv: </b> <br>
 * discos != null <br>
 * No hay dos discos con el mismo nombre
 * 
 */
public class Discotienda
{
    // -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * Nombre del archivo de registro de errores del programa
     */
    private static final String LOG_FILE = "./data/error.log";

    /**
     * La lista de discos de la discotienda
     */
    private ArrayList discos;

    /**
     * Es el nombre del archivo de donde se cargan y salvan los discos
     */
    private String archivoDiscotienda;

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Construye una nueva discotienda. <br>
     * Si el archivo indicado no existe, entonces la discotienda se crea vacea y su estado se guardare en el archivo indicado.<br>
     * Si el archivo existe, entonces de el se saca la informacien de los discos y canciones.
     * @param nombreArchivoDiscotienda es el nombre del archivo que contiene los datos de la discotienda - nombreArchivoDiscotienda != null
     * @throws PersistenciaException Se lanza esta excepcien si se encuentran problemas cargando los datos del archivo
     */
    public Discotienda( String nombreArchivoDiscotienda ) throws PersistenciaException
    {
        archivoDiscotienda = nombreArchivoDiscotienda;
        File archivo = new File( archivoDiscotienda );
        if( archivo.exists( ) )
        {
            // El archivo existe: se debe recuperar de alle el estado del modelo del mundo
            try
            {
                ObjectInputStream ois = new ObjectInputStream( new FileInputStream( archivo ) );
                discos = ( ArrayList )ois.readObject( );
                ois.close( );
            }
            catch( Exception e )
            {
                // Se atrapan en este bloque todos los tipos de excepcien
                registrarError( e );
                throw new PersistenciaException( "Error fatal: imposible restaurar el estado del programa (" + e.getMessage( ) + ")" );
            }
        }
        else
        {
            // El archivo no existe: es la primera vez que se ejecuta el programa
            discos = new ArrayList( );
        }
        verificarInvariante( );
    }

    // -----------------------------------------------------------------
    // Metodos
    // -----------------------------------------------------------------
 
	
	 /**
     * Retorna un disco de la discotienda dado su nombre.
     * @param nombreDisco el nombre del disco a buscar - nombreDisco != null
     * @return 
     */
    public Disco darDisco( String nombreDisco )
    {
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco d = ( Disco )discos.get( i );
            if( d.equals( nombreDisco ) )
                return d;
        }
        return null;
    }

    /**
     * Retorna un disco de la discotienda dado un nombre, un artista y una Cancion
     * @param nombreDisco El nombre del disco donde deberea estar la Cancion - nombreDisco != null
     * @param nombreArtista El nombre del artista del disco - nombreArtista != null
     * @param nombreCancion El nombre de la Cancion buscada - nombreCancion != null
     * @return Retorna el disco en el que se encuentra la Cancion buscada. Si no se encuentra retorna null.
     */
    private Disco darDisco( String nombreDisco, String nombreArtista, String nombreCancion )
    {
        Disco discoBuscado = darDisco( nombreDisco );
        if( discoBuscado != null && discoBuscado.darArtista( ).equalsIgnoreCase( nombreArtista ) )
            return ( discoBuscado.darCancion( nombreCancion ) != null ) ? discoBuscado : null;
        else
            return null;
    }

    /**
     * Agrega un nuevo disco a la discotienda <br>
     * @param nombreDiscoD el nombre del disco - nombreDiscoC != null
     * @param artistaD el artista del nuevo disco - artistaD != null
     * @param generoD el genero del nuevo disco - generoD != null
     * @param imagenD el nombre del archivo imagen del disco que debe estar en ./data/imagenes - imagenD != null
     * @throws ElementoExisteException Esta excepcien se lanza si ya existe un disco con el mismo nombre
     */
    public void agregarDisco( String nombreDiscoD, String artistaD, String generoD, String imagenD ) throws ElementoExisteException
    {
        if( darDisco( nombreDiscoD ) != null )
            throw new ElementoExisteException( "El disco " + nombreDiscoD + " ya existe en miDiscoTienda" );

        discos.add( new Disco( nombreDiscoD, artistaD, generoD, imagenD ) );
        verificarInvariante( );
    }

    /**
     * Agrega una nueva canc al disco
     * @param nombreDisco el nombre del disco para adicionar la Cancion - hay un disco con ese nombre en la discotienda
     * @param nombreC el nombre de la c a crear - nombreC != null, nombreC != ""
     * @param minutosC el  de minutos de  de la Cancion - minutosC >= 0
     * @param segundosC el nero de segundos de  de la Cancion - 0 <= segundosC < 60, minutosC + segundosC > 0
     * @param precioC el precio de la c - precioC > 0
     * @param tamanoC el tamao en Mb de l- tamanoC > 0
     * @param calidadC la calidad de la cancin en Kbps - calidadC > 0
     * @throws ElementoExisteException Esta excepcin se lanza si el ya existe otra canci en el disco con el mismo nombre
     */
    public void agregarCancionADisco( String nombreDisco, String nombreC, int minutosC, int segundosC, double precioC, double tamanoC, int calidadC ) throws ElementoExisteException
    {
        Disco d = darDisco( nombreDisco );
        d.agregarCancion( new Cancion( nombreC, minutosC, segundosC, precioC, tamanoC, calidadC, 0 ) );
        verificarInvariante( );
    }

    /**
     * Registra la venta de unque se va a vender - disco != null
     * @param cancion  cual se va a vender una unida la canc - email != null, email es un email velido (usuario@dominio.ext)
     * @param rutaFacturagenerarse la factura - rutaFactura != null
     * @return Retorna el nom la factura
     * @throws IOException Se genera estasi hay problemas salvando el archivo con la factura
     */
    public String venderCancion( Disco disco, Cancion cancion, String email, String rutaFactura ) throws IOException
    {
        cancion.vender( );

        // Genera el nombre para la factura
        int posArroba1 = email.indexOf( "@" );
        String login = email.substring( 0, posArroba1 );
        String strTiempo = Long.toString( System.currentTimeMillis( ) );
        String nombreArchivo = login + "_" + strTiempo + ".fac";

        // Guarda el archivo de la factura
        File directorioFacturas = new File( rutaFactura );
        if( !directorioFacturas.exists( ) )
            directorioFacturas.mkdirs( );
        File archivoFactura = new File( directorioFacturas, nombreArchivo );
        PrintWriter out = new PrintWriter( archivoFactura );

        Date fecha = new Date( );
        out.println( "miDiscoTienda - FACTURA" );
        out.println( "Fecha:            " + fecha.toString( ) );
        out.println( "Email:            " + email );

        out.println( "Cancion:          " + cancion.darNombre( ) + " - " + disco.darArtista( ) );
        out.println( "                  " + disco.darNombreDisco( ) );
        out.println( "No de Canciones:  1" );
        DecimalFormat df = new DecimalFormat( "$0.00" );
        out.println( "Valor Total:      " + df.format( cancion.darPrecio( ) ) );
        out.close( );

        return nombreArchivo;
    }

    /**
     * Retorna un vector con los nombres de los discos
     * @return Vector con los nombres de los discos
     */
    public ArrayList darDiscos( )
    {
        ArrayList nombresDiscos = new ArrayList( );
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco d = ( Disco )discos.get( i );
            nombresDiscos.add( d.darNombreDisco( ) );
        }
        return nombresDiscos;
    }

    /**
     * Actualiza la informacien sobre las canciones vendidas a partir de la informacien sobre un pedido y genera una factura.<br>
     * El archivo debe tener una lenea en la cual se encuentra el email de la persona que hizo el pedido y luego debe haber una lenea por cada Cancion solicitada. <br>
     * Cada lenea tiene el siguiente formato: <nombre disco>#<nombre artista>#<nombre Cancion>
     * @param archivoPedido el archivo que tiene la informacien del pedido - archivoPedido != null
     * @param rutaFactura el directorio donde debe generarse la factura - rutaFactura != null
     * @return El archivo en el que se guarde la factura
     * @throws FileNotFoundException Se lanza esta excepcien si el archivo del pedido no existe
     * @throws IOException Se lanza esta excepcien si hay problemas escribiendo el archivo de la factura
     * @throws ArchivoVentaException Se lanza esta excepcien si el archivo no cumple con el formato esperado
     */
    public String venderListaCanciones( File archivoPedido, String rutaFactura ) throws FileNotFoundException, IOException, ArchivoVentaException
    {
        // Abre el archivo con el pedido. Si no existe, el constructor del flujo lanza la excepcien FileNotFoundException
        BufferedReader lector = new BufferedReader( new FileReader( archivoPedido ) );

        String email = null;
        try
        {
            // Lee la primera lenea del archivo (la direccien electrenica) y le suprime los posibles espacios
            email = lector.readLine( );
        }
        catch( IOException e )
        {
            // Hubo un error tratando de leer la primera lenea del archivo
            throw new ArchivoVentaException( e.getMessage( ), 0 );
        }

        // Hace las verificaciones iniciales.
        if( email == null )
            throw new ArchivoVentaException( "El archivo este vaceo", 0 );
        if( !validarEmail( email ) )
            throw new ArchivoVentaException( "El email indicado no es velido", 0 );

        String pedido = null;
        try
        {
            // Intenta leer del archivo la primera Cancion que se quiere vender
            pedido = lector.readLine( );
        }
        catch( IOException e )
        {
            // Hubo un error tratando de leer la primera Cancion del pedido
            throw new ArchivoVentaException( e.getMessage( ), 0 );
        }

        if( pedido == null )
            throw new ArchivoVentaException( "Debe haber por lo menos una Cancion en el pedido", 0 );

        // Inicializa las estructuras de datos necesarias para generar luego la factura
        ArrayList discosFactura = new ArrayList( );
        ArrayList cancionesFactura = new ArrayList( );
        ArrayList cancionesNoEncontradas = new ArrayList( );
        int cancionesVendidas = 0;

        // Utiliza el patren de recorrido de archivos secuenciales
        while( pedido != null )
        {
            // Separa los tres elementos del pedido (disco, artista y Cancion) verificando que el formato pedido se cumpla
            int p1 = pedido.indexOf( "#" );
            if( p1 != -1 )
            {
                // Encontre el primer separador
                String resto1 = pedido.substring( p1 + 1 );
                int p2 = resto1.indexOf( "#" );
                if( p2 != -1 )
                {
                    // Encontre el segundo separador
                    String nombreDisco = pedido.substring( 0, p1 );
                    String nombreArtista = resto1.substring( 0, p2 );
                    String nombreCancion = resto1.substring( p2 + 1 );

                    Disco discoBuscado = darDisco( nombreDisco, nombreArtista, nombreCancion );
                    if( discoBuscado != null )
                    {
                        Cancion cancionPedida = discoBuscado.darCancion( nombreCancion );
                        cancionPedida.vender( );
                        discosFactura.add( discoBuscado );
                        cancionesFactura.add( cancionPedida );
                        cancionesVendidas++;
                    }
                    else
                        // La Cancion no existe en la discotienda
                        cancionesNoEncontradas.add( pedido );
                }
                else
                    // El formato es invelido: no aparece el primer separador
                    cancionesNoEncontradas.add( pedido );
            }
            else
                // El formato es invelido: no aparece el segundo separador
                cancionesNoEncontradas.add( pedido );

            try
            {
                // Lee la siguiente lenea del archivo
                pedido = lector.readLine( );
            }
            catch( IOException e )
            {
                // Hubo un error tratando de leer la siguiente Cancion del pedido
                generarFactura( discosFactura, cancionesFactura, cancionesNoEncontradas, email, rutaFactura );
                throw new ArchivoVentaException( e.getMessage( ), cancionesVendidas );
            }
        }

        // Cierra el flujo de entrada
        lector.close( );

        // Genera la factura
        return generarFactura( discosFactura, cancionesFactura, cancionesNoEncontradas, email, rutaFactura );
    }

    /**
     * Genera la factura de la venta de un conjunto de discos, en un archivo nuevo.
     * @param discos los discos a los que pertenecen las canciones que se van a vender - discos != null
     * @param canciones las canciones que se van a vender - canciones != null, por cada cancion, en el paremetro 'discos' se encuentra el disco correspondiente en la misma
     *        posicien
     * @param noEncontradas vector con las leneas del pedido que no pudieron ser procesadas porque la Cancion no existe
     * @param email el email de la persona a la cual se le vendieron las canciones - email != null, email es un email velido (usuario@dominio.ext)
     * @param rutaFactura el directorio donde debe generarse la factura - rutaFactura != null
     * @return Retorna el nombre del archivo en el que se genere la factura
     * @throws IOException Se genera esta excepcien si hay problemas salvando el archivo
     */
    private String generarFactura( ArrayList discos, ArrayList canciones, ArrayList noEncontradas, String email, String rutaFactura ) throws IOException
    {
        // Genera el nombre para la factura
        int posArroba1 = email.indexOf( "@" );
        String login = email.substring( 0, posArroba1 );
        String strTiempo = Long.toString( System.currentTimeMillis( ) );
        String nombreArchivo = login + "_" + strTiempo + ".fac";

        // Guarda el archivo de la factura
        File directorioFacturas = new File( rutaFactura );
        if( !directorioFacturas.exists( ) )
            directorioFacturas.mkdirs( );

        File archivoFactura = new File( directorioFacturas, nombreArchivo );
        PrintWriter out = new PrintWriter( archivoFactura );
        Date fecha = new Date( );
        out.println( "miDiscoTienda - FACTURA" );
        out.println( "Fecha:            " + fecha.toString( ) );
        out.println( "Email:            " + email );

        double valorTotal = 0;
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco disco = ( Disco )discos.get( i );
            Cancion cancion = ( Cancion )canciones.get( i );
            out.println( "Cancion:          " + cancion.darNombre( ) + " - " + disco.darArtista( ) );
            out.println( "                  " + disco.darNombreDisco( ) );
            valorTotal += cancion.darPrecio( );
        }
        DecimalFormat df = new DecimalFormat( "$0.00" );
        out.println( "No de Canciones:  " + canciones.size( ) );
        out.println( "Valor Total:      " + df.format( valorTotal ) );

        if( noEncontradas.size( ) > 0 )
        {
            out.println( "\nCanciones no encontradas:" );
            for( int i = 0; i < noEncontradas.size( ); i++ )
            {
                out.println( noEncontradas.get( i ) );
            }
        }
        out.close( );

        return nombreArchivo;
    }

    /**
     * Indica si una direccien de correo cumple con el formato esperado.<br>
     * El formato esperado es <login>@<dominio>.<br>
     * El dominio tiene que estar compuesto de por lo menos dos partes separadas por un punto: <parte1>.<parte2>
     * @param email la direccien de email que se quiere verificar - email != null
     * @return Retorna true si el email cumple con el formato especificado
     */
    public boolean validarEmail( String email )
    {
        boolean resultado = true;
        int posArroba1 = email.indexOf( "@" );
        if( posArroba1 == -1 )
        {
            resultado = false;
        }
        else
        {
            String dominio = email.substring( posArroba1 + 1 );
            resultado = dominio.indexOf( "." ) != -1 && ! ( dominio.substring( dominio.indexOf( "." ) + 1 ).equals( "" ) );
        }
        return resultado;
    }

    // -----------------------------------------------------------------
    // Persistencia
    // -----------------------------------------------------------------

    /**
     * Salva la discotienda en un archivo binario
     * @throws PersistenciaException Se lanza esta excepcien si hay problemas guardando los archivos
     */
    public void salvarDiscotienda( ) throws PersistenciaException
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( archivoDiscotienda ) );
            oos.writeObject( discos );
            oos.close( );
        }
        catch( IOException e )
        {
            registrarError( e );
            throw new PersistenciaException( "Error al salvar: " + e.getMessage( ) );
        }
    }

    /**
     * Registra en el archivo de log del programa toda la informacien referente a una excepcien, ocurrida durante el proceso de persistencia
     * @param excepcion es la excepcien que contiene la informacien del error
     */
    public void registrarError( Exception excepcion )
    {
        try
        {
            FileWriter out = new FileWriter( LOG_FILE, true );
            PrintWriter log = new PrintWriter( out );
            log.println( "---------------------------------------------------" );
            log.println( "Discotienda.java :" + new Date( ).toString( ) );
            log.println( "----------------------------------------------------" );
            excepcion.printStackTrace( log );
            log.close( );
            out.close( );
        }
        catch( IOException e )
        {
            excepcion.printStackTrace( );
            e.printStackTrace( );
        }
    }
    /**
     * Verifica <br>
     * discos <br>
     * No hay dos discos con el mismo nombre
     */
    private void verificarInvariante( )
    {
        assert discos != null : "La lista de discos es null";
        assert !buscarDiscosConElMismoNombre( ) : "Hay dos discos con el mismo nombre";
    }
    /**
     *Igualar nombres 
     * @return 
     * */
    private boolean buscarDiscosConElMismoNombre( )
    {
        for( int i = 0; i < discos.size( ); i++ )
        {
         Disco d1 = ( Disco )discos.get( i );
            for( int j = i + 1; j < discos.size( ); j++ )
            {
              Disco d2 = ( Disco )discos.get( j );
              if( d1.equals( d2.darNombreDisco( ) ) )
                return true;
         }
      }
       return false;
    }
    // ---------------------------------------------------------------------------------
    // Trabajo by Esneyder y Esteban
    // ----------------------------------------------------------------------------
    /**
     * Generar un informe de discos en la discoteca
     * @throws FileNotFoundExceptions - Cuando no existe la ruta especifica del archivo a leer o escribir
     */
    
    public void generarInformeDiscos() throws FileNotFoundException
    {
    	// Crear el archivo con la clase FILE
    	File archivo = new File("./data/reporteDiscos.txt");
    	
    	// Crear la pluma para escribir el archivo
    	PrintWriter pluma = new PrintWriter(archivo);
    	
    	// Escribir con la pluma en el archivo
    	pluma.println("Reporte de discos");
    	pluma.println("------------------");
    	// Escribir un reporte de discos con nombre, artista y geenero
    	for(int i = 0; i < discos.size(); i++)
    	{
    		//Extraer información de cada disco
    		Disco miDisco = (Disco)discos.get(i);	
    		//Escribir con la pluma la información
    		pluma.println(
    					  "Nombre: " + miDisco.darNombreDisco() +
    					  "Artista: " + miDisco.darArtista() + 
    					  "Género: " + miDisco.darGenero()
    					  );   				
    	}
    	pluma.close();
    }
    public void generarInformeGeneroPopRock() throws Exception
   {    	
   	// Crear la pluma para escribir el archivo y crear archivo 2
    	File archivo2 = new File("./data/discosCostosos.txt");
    	PrintWriter pluma = new PrintWriter(archivo2);

    	boolean estado = false;
    for(int i = 0; i < discos.size(); i++)
    {
    	Disco miDisco = (Disco)discos.get(i);
    	if(miDisco.darGenero().equals("Pop") || miDisco.darGenero().equals("Rock"))
    	{
    		if(
    				miDisco.darPrecioDisco() <= 1000.0
    		   )
    	{  				
        pluma.println(
        		   	 " Nombre: " + miDisco.darNombreDisco() +
      			     "  Artista: " + miDisco.darArtista() + 
      				 "  Género: " + miDisco.darGenero() + 
      			     "  Precio: " + miDisco.darPrecioDisco()
      			     );
        estado=true;
    			}
    		}
    	}
    	
    	// Cerrar la plumma
    	pluma.close();
    	
    	
    	//punto b 1/2
    	if(estado==false){
    		throw new Exception("no hay discos de genero pop ni rock que valgan mas de mil pesos");	
    	}
    	{
    		archivo2.delete();
    	}
    }
    
    // -----------------------------------------------------------------
    // Puntos de extension
    // ----------------------------------------------------------------- 
    
    /**
     *punto de eextension 1
     * @return respuesta 1
     */
    public String metodo1( )
    {
        try {
        generarInformeDiscos();
        	return "Reporte generado satisfactoriamente";
        }
        catch (Exception e) {
      	return "Error fatal :( " + e.getMessage();
   }
}
    
    /**
     *punto de extension 2
     * @return respuesta 2
     */
    public String metodo2() throws Exception
    {
    	try {
			generarInformeGeneroPopRock();
			return "reporte generado";
		} 
    	catch (Exception e)
    	{
    		return e.getMessage();
    	}
    }
    /**
     *punto de extension 3
     * @return respuesta 3
     */
    public String metodo3( )
    {
        return "Hola...";
    }

    /**
     *punto de extension 4
     * @return respuesta 4
     */
    public String metodo4( )
    {
        return "El trabajo...";
    }

    /**
     *punto de extension 5
     * @return respuesta 5
     */
    public String metodo5( )
    {
        return "Estuvo muy...";
    }

    /**
     *punto de extension 6
     * @return respuesta 6
     */
    public String metodo6( )
    {
        return "Dificiiil :,(";

    }
}
