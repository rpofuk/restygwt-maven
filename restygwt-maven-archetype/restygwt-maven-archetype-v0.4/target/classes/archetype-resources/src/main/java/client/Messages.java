#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	C:/Users/robert.pofuk/Desktop/NOVI LMS/Eclipse/git/restygwt-maven-plugin/gwt-mave-plugin-restygwt/${artifactId}/src/main/resources/${package}/client/Messages.properties'.
 */
public interface Messages extends com.google.gwt.i18n.client.Messages {
  
  /**
   * Translated "Enter your name".
   * 
   * @return translated "Enter your name"
   */
  @DefaultMessage("Enter your name")
  @Key("nameField")
  String nameField();

  /**
   * Translated "Send".
   * 
   * @return translated "Send"
   */
  @DefaultMessage("Send")
  @Key("sendButton")
  String sendButton();
}
