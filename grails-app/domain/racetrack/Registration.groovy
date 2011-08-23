package racetrack

class Registration {

	Date dateCreated
    static constraints = {
	race()
	runner()
	paid()
	dateCreated()
    }

    static belongsTo =  [race:Race, runner:Runner]
    Boolean paid

}
