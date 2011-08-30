package racetrack

import grails.test.*
import org.codehaus.groovy.grails.plugins.codecs.*

class UserControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
        String.metaClass.encodeAsBase64 = {->
            Base64Codec.encode(delegate)
        }
        String.metaClass.encodeAsSHA = {->
            SHACodec.encode(delegate)
        }
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndex(){
        controller.index()
        assertEquals "list", controller.redirectArgs["action"]
    }

    void testShow(){
      def darkneoz1 = new User(login:"darkneoz1")
      def suziq = new User(login:"suziq")
      def admin = new User(login:"admin")
      mockDomain(User, [darkneoz1, suziq, admin])
      controller.params.id = 3
      def map = controller.show()
      assertEquals "admin", map.userInstance.login
    }

    void testAuthenticate(){
      def jdoe = new User(login:"jdoe", password:"12345".encodeAsSHA())
      mockDomain(User, [jdoe])
      controller.params.login = "jdoe"
      controller.params.password = "12345"
      controller.authentication()
      assertNotNull controller.session.user
      assertEquals "jdoe", controller.session.user.login
      controller.params.password = "foo"
      controller.authentication()
      assertTrue controller.flash.message.startsWith("Sorry, jdoe")
    }
}
