/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: PanelExtension.java,v 1.1 2006/08/10 20:04:35 da-romer Exp $
 * Universidad de los Andes (Bogoto - Colombia)
 * Departamento de Ingenieroa de Sistemas y Computacion 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n8_discotienda 
 * Autor: Nicolos Lopez - 06/12/2005 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package uniandes.cupi2.discotienda.interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Es el panel donde se encuentran los botones para las extensiones
 */
public class PanelExtension extends JPanel implements ActionListener
{
    // -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * Comando para la opcion 1
     */
    private static final String OPCION_1 = "Opcion1";

    /**
     * Comando para la opcion 2
     */
    private static final String OPCION_2 = "Opcion2";

    /**
     * Comando para la opcion 3
     */
    private static final String OPCION_3 = "Opcion3";

    /**
     * Comando para la opcion 4
     */
    private static final String OPCION_4 = "Opcion4";

    /**
     * Comando para la opcion 5
     */
    private static final String OPCION_5 = "Opcion5";

    /**
     * Comando para la opcion 6
     */
    private static final String OPCION_6 = "Opcion6";

    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------

    /**
     * Es una referencia a la clase principal de la interfaz
     */
    private InterfazDiscotienda principal;

    // -----------------------------------------------------------------
    // Atributos de la Interfaz
    // -----------------------------------------------------------------

    /**
     * Es el boton para ejecutar la extension 1
     */
    public JButton botonOpcion1;

    /**
     * Es el boton para ejecutar la extension 1
     */
    public JButton botonOpcion2;

    /**
     * Es el boton para ejecutar la extension 3
     */
    public JButton botonOpcion3;

    /**
     * Es el boton para ejecutar la extension 4
     */
    public JButton botonOpcion4;

    /**
     * Es el boton para ejecutar la extension 5
     */
    public JButton botonOpcion5;

    /**
     * Es el boton para ejecutar la extension 6
     */
    public JButton botonOpcion6;

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------
    /**
     * Construye el panel
     * @param ventanaPrincipal es una referencia a la clase principal de la interfaz
     */
    public PanelExtension( InterfazDiscotienda ventanaPrincipal )
    {
        principal = ventanaPrincipal;

        botonOpcion1 = new JButton( "Opcion 1" );
        botonOpcion1.setActionCommand( OPCION_1 );
        botonOpcion1.addActionListener( this );
        add( botonOpcion1 );

        botonOpcion2 = new JButton( "Opcion 2" );
        botonOpcion2.setActionCommand( OPCION_2 );
        botonOpcion2.addActionListener( this );
        add( botonOpcion2 );

        botonOpcion3 = new JButton( "Opcion 3" );
        botonOpcion3.setActionCommand( OPCION_3 );
        botonOpcion3.addActionListener( this );
        add( botonOpcion3 );

        botonOpcion4 = new JButton( "Opcion 4" );
        botonOpcion4.setActionCommand( OPCION_4 );
        botonOpcion4.addActionListener( this );
        add( botonOpcion4 );

        botonOpcion5 = new JButton( "Opcion 5" );
        botonOpcion5.setActionCommand( OPCION_5 );
        botonOpcion5.addActionListener( this );
        add( botonOpcion5 );

        botonOpcion6 = new JButton( "Opcion 6" );
        botonOpcion6.setActionCommand( OPCION_6 );
        botonOpcion6.addActionListener( this );
        add( botonOpcion6 );
    }

    // -----------------------------------------------------------------
    // Motodos
    // -----------------------------------------------------------------

    /**
     * Ejecuta las acciones asociadas a los eventos
     * @param evento es el evento del click sobre un boton
     */
    public void actionPerformed( ActionEvent evento )
    {
        String comando = evento.getActionCommand( );

        if( OPCION_1.equals( comando ) )
        {
            principal.reqFuncOpcion1( );
        }
        else if( OPCION_2.equals( comando ) )
        {
         try 
          {
		principal.reqFuncOpcion2( );
		} 
         catch (Exception e)
         {
			e.printStackTrace();
		}
        }
        else if( OPCION_3.equals( comando ) )
        {
            principal.reqFuncOpcion3( );
        }
        else if( OPCION_4.equals( comando ) )
        {
            principal.reqFuncOpcion4( );
        }
        else if( OPCION_5.equals( comando ) )
        {
            principal.reqFuncOpcion5( );
        }
        else if( OPCION_6.equals( comando ) )
        {
            principal.reqFuncOpcion6( );
        }
    }
}