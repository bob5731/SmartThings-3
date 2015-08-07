/* **DISCLAIMER**
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * Without limitation of the foregoing, Contributors/Regents expressly does not warrant that:
 * 1. the software will meet your requirements or expectations;
 * 2. the software or the software content will be free of bugs, errors, viruses or other defects;
 * 3. any results, output, or data provided through or generated by the software will be accurate, up-to-date, complete or reliable;
 * 4. the software will be compatible with third party software;
 * 5. any errors in the software will be corrected.
 * The user assumes all responsibility for selecting the software and for the results obtained from the use of the software. The user shall bear the entire risk as to the quality and the performance of the software.
 */ 
 
 /**
 *  Use a trigger to activate camera to take pictures
 *
 *  Copyright RBoy
 *  Redistribution of any changes or code is not allowed without permission
 *
 */
definition(
    name: "Trigger to take Pictures",
    namespace: "rboy",
    author: "RBoy",
    description: "Use a trigger (motion sensor, door sensor etc) to activate a camera to take pictures",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Categories/cameras.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Categories/cameras@2x.png"
)

/**
 *  Author: RBoy
 *  Change log:
 *  2015-1-30 - Initial code
 */
preferences {
	section("Choose Camera(s)") {
		input "cameras", "capability.imageCapture", multiple: true
	}

	section("Choose Motion Sensor(s)") {
		input "motionSensors", "capability.motionSensor", multiple: true, required: false
	}
    
	section("Choose Contact Sensor(s)") {
		input "contactSensors", "capability.contactSensor", multiple: true, required: false
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	unschedule() // clear any pending timers
	initialize()
}

def initialize() {
    subscribe(motionSensors, "motion.active", motionHandler)
    subscribe(contactSensors, "contact.open", contactHandler)
}

def motionHandler(evt) {
	log.debug "Active motion detected, activating cameras"
    
    for(camera in cameras){
    	log.info "$evt.displayName motion detected, activating camera $camera to take pictures"
		camera.take()
    }
}

def contactHandler(evt) {
	log.debug "Open door detected, activating cameras"
    
    for(camera in cameras){
    	log.info "$evt.displayName open detected, activating camera $camera to take pictures"
		camera.take()
    }
}