package racetrack

import grails.test.*

class UserTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {

    }
    void testSimpleConstraints(){
        mockForConstraintsTests(User)
        def user = new User(
        login:"someone",
        password:"blah",
        role:"SuperUser"
        )
        assertFalse user.validate()
        assertEquals "inList", user.errors["role"]
    }
    void testUniqueConstraint(){
        def darkneoz = new User(login:"darkneoz")
        def admin  =new User(login:"admin")
        mockDomain(User, [darkneoz, admin])
        def badUser = new User(login:"darkneoz")
        badUser.save()
        assertEquals 2, User.count()
        assertEquals "unique", badUser.errors["login"]
        def goodUser = new User(login:"good", password:"password", role:"user")
        goodUser.save()
        assertEquals 3, User.count()
        assertNotNull User.findByLoginAndPassword("good", "password".encodeAsSHA())
    }
}
