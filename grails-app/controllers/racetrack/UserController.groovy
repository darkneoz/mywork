package racetrack

class UserController {
  def beforeInterceptor = [action:this.&auth, except:['login', 'logout', 'authentication']]
  def auth(){
    if(!session.user) {
        redirect(controller:"user", action:"login")
        return false
    }
    if(!session.user.admin){
        flash.message = "Tsk tsk-admins only"
        redirect(controller:"race", action:"list")
        return false
    }
  }
  def debug(){
    println "DEBUG: ${actionUri} called."
    println "DEBUG: ${params}"
  }

  def scaffold = true
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
  def index = {
    redirect(action:"list", params: params)
  }
  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10,100)
    return [userInstanceList: User.list(params), userInstanceTotal: User.count()]
  }
  def create = {
    def userInstance = new User()
    userInstance.properties = params
    return [userInstance: userInstance]
  }
  def save = {
    def userInstance = new User(params)
    if (userInstance.save(flush: true)){
      flash.message="${message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
      redirect(action: "show", id: userInstance.id)
    } else {
      render(view: "create", model: [userInstance: userInstance])
    }
  }
  def show = {
      def userInstance = User.get(params.id)
      if (!userInstance){
        flash.message="${message(code:'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
        redirect(action: "list")
      } else {
        [userInstance: userInstance]
      }
  }
  def edit = {}
  def update = {}
  def delete = {}
  def login = {}
  def logout = {
    flash.message = "Goodbye ${session.user.login}"
    session.user = null
    redirect(action:"login")
  }
  def authentication = {
      def user =
      User.findByLoginAndPassword(params.login,params.password.encodeAsSHA())
      if(user){
        session.user = user
        flash.message = "Hello ${user.login}!"
        redirect(controller:"race", action:"list")
      } else {
        flash.message = "Sorry, ${params.login}. Please try again."
        redirect(action:"login")
      }

  }
}
